package com.example.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.activity.MainActivity;
import com.example.adapter.DownloadAdapter;
import com.example.base.C;
import com.example.dbdao.DBHelper;
import com.example.dbdao.NanoHTTPD;
import com.example.fragment.DownloadManagerFragment;
import com.example.fragment.LocalMusicFragment;
import com.example.utis.Downlnfos;
import com.example.utis.MusicInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/***
 * 2013/5/25
 * 
 * @author wwj 音乐播放服务
 */
@SuppressLint("NewApi")
public class DownloadMusicService extends Service {

	private MusicInfo mp3Infos; // 存放Mp3Info对象的集合
	private HashMap<String, Downlnfos> map = new HashMap<String, Downlnfos>();
	private ArrayList<Downlnfos> arrayList = new ArrayList<Downlnfos>();
	private MyReceiver myReceiver; // 自定义广播接收器
	private int currentTime; // 当前播放进度
	private int duration; // 播放长度
	private Context context;
	private int cou = 0;
	private DBHelper dbHelper;
	// 服务要发送的一些Actioncom.wwj.media.MUSIC_SERVICE
	public static final String DOWNLOAD_MUSIC = "com.wwj.action.DOWNLOAD_MUSIC"; // 下载音乐
	public static final String DOWNLOAD_LRC = "com.wwj.action.DOWNLOAD_LRC"; // 下载歌词
	public static final String DOWNLOAD_DELE = "com.wwj.action.DOWNLOAD_DELE"; // 删除音乐
	public static final String DOWNLOAD_CURRENT = "com.wwj.action.DOWNLOAD_CURRENT"; // 删除音乐
	public static final String DOWNLOAD_INFO = "com.wwj.action.DOWNLOAD_INFO"; // 发送下载歌曲

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("service", "service created");

		myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(DOWNLOAD_MUSIC);
		filter.addAction(DOWNLOAD_LRC);
		filter.addAction(DOWNLOAD_LRC);
		registerReceiver(myReceiver, filter);

		dbHelper = MainActivity.dbHelper;
		if (dbHelper == null) {
			dbHelper = new DBHelper(getApplicationContext());
		}

	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

	}

	@Override
	public void onDestroy() {

		unregisterReceiver(myReceiver);

		// handler.removeCallbacks(mRunnable);
	}

	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int control = intent.getIntExtra("control", -1);
			switch (control) {

			}

			String action = intent.getAction();

			if (action.equals(DOWNLOAD_MUSIC)) {
				Log.e("a", "來了服務DOWNLOAD_MUSIC");
				MusicInfo infozz = (MusicInfo) intent.getExtras().get(
						"musicinfo");
				mp3Infos = infozz;
				File f = new File(C.MP3SRC, infozz.getId() + ".mp3");
				if (!f.exists()) {
					AsyncHttpsetMusic(mp3Infos);
				} else {
					Toast.makeText(getApplicationContext(), "已经下载到本地。",
							Toast.LENGTH_SHORT).show();
				}
			}

		}
	}

	public void AsyncHttpsetMusic(final MusicInfo ins) {
		final MusicInfo in = ins;
		final String url = in.getSrc();

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				cou = 0;
				Downlnfos downlnfos = new Downlnfos(in.getId(), in.getTitle(),
						in.getSrc(), null, null);
				map.put(in.getId(), downlnfos);
				C.logshow("插入下载数据库：" + "id:" + in.getId() + "title:"
						+ in.getTitle());
				C.logshow("插入下载数据库：");
				dbHelper.insertDowninfo(downlnfos);
				Toast.makeText(getApplicationContext(),
						"开始下载:" + ins.getTitle(), Toast.LENGTH_SHORT).show();
				DownloadManagerFragment.gengxin();
				// arrayList.add(downlnfos);

				// dbHelper = new DBHelper(getApplicationContext());

			}

			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				// TODO Auto-generated method stub
				super.onProgress(bytesWritten, totalSize);

				int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
				Downlnfos downlnfos = map.get(in.getId());
				currentTime = bytesWritten;
				duration = totalSize;
				if (count > cou) {
					cou = cou + 10;
					downlnfos.setCout(currentTime + "");
					downlnfos.setLeng(duration + "");
					dbHelper.updateDowninfo(downlnfos);
					DownloadManagerFragment.gengxin();
				}
				if (currentTime == duration) {
					downlnfos.setCout(currentTime + "");
					downlnfos.setLeng(duration + "");
					dbHelper.updateDowninfo(downlnfos);
					DownloadManagerFragment.gengxin();
				}
				//
				// if(cou!=count){
				// cou=count;
				// Intent intent = new Intent();
				// intent.setAction(DOWNLOAD_CURRENT);
				// intent.putExtra("currentTime", currentTime);
				// intent.putExtra("duration", duration);
				// intent.putExtra("musicinfo", ins);
				// sendBroadcast(intent); // 给PlayerActivity发送广
				// // C.logshow("发送了广播"+"cou"+cou+"count"+count);
				// }
				// Log.e("下载>>>>>", bytesWritten + " / " + totalSize);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {

				if (statusCode == 200) {
					Log.e("a", "成功了噢");
					Downlnfos downlnfos = map.get(in.getId());
					dbHelper.delMusicinfo(downlnfos);
					DownloadManagerFragment.gengxin();
					saveBitmap(in.getId() + ".mp3", responseBody, in);
				}

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				Log.e("a", "线程关闭");
				cou = 0;
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				error.printStackTrace();
			}

		});

	}

	/**
	 * 保存方法
	 * 
	 * @return
	 */
	public Bitmap saveBitmap(String picName, byte[] buffer, MusicInfo info) {
		MusicInfo musicInfo = info;
		File f = new File(C.MP3SRC, picName);
		
		
		if (!f.exists()) {
			try {
				FileOutputStream out = new FileOutputStream(f);
				out.write(buffer);
				out.flush();
				// dbHelper = new DBHelper(getApplicationContext());
				out.close();
				C.logshow("保存成功");
				musicInfo.setSDsrc(C.MP3SRC + picName);
				Toast.makeText(getApplicationContext(),
						"下载成功:" + musicInfo.getTitle(), Toast.LENGTH_SHORT)
						.show();
//				if(LocalMusicFragment.n!=null){
//					LocalMusicFragment.n.stop();
//					File wwwroot = new File(
//							C.MP3SRC);
//					LocalMusicFragment.n = new NanoHTTPD(8089, wwwroot);
//				}
				dbHelper.insertMusicinfo(musicInfo);
				PlayerService.udpmp3info();
				LocalMusicFragment.gengxin();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			C.logshow("已经保存");

		}
		return null;

	}

}
