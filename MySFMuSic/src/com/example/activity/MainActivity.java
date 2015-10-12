package com.example.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.cybergarage.upnp.Device;

import com.example.adapter.FragmentAdapter;
import com.example.base.C;
import com.example.base.Deiviceutis;
import com.example.base.DeviceDao;
import com.example.base.MManager.MessageType;
import com.example.dbdao.DBHelper;
import com.example.dlna.DLNAContainer;
import com.example.dlna.DLNAService;
import com.example.fragment.LocalMusicFragment;

import com.example.mysfmusic.R;
import com.example.service.AppConstant;
import com.example.service.DownloadMusicService;
import com.example.service.PlayerService;
import com.example.utis.MusicInfo;
import com.ldw.music.lrc.LyricAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.YuvImage;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends FragmentActivity implements OnClickListener {
	/**
	 * 本地音乐
	 */
	public static final int TAB_LOCAL = 0;

	/**
	 * 网络音乐
	 */
	public static final int TAB_NETWORK = 1;
	/**
	 * 最近播放
	 */
	public static final int TAB_RECENTLY = 2;
	
	/**
	 * 下载管理
	 */
	public static final int TAB_DOWNLOAD = 3;
	private ViewPager viewPager;
	private RadioButton rb1, rb2, rb3, rb4;
	private ImageView imageView;
	private static TextView tv_mian_1;

	private static TextView tv_mian_2;

	private static TextView tv_mian_3;
	private int offset = 0;
	private static SeekBar seekBar;
	private int progres = 0;// 拖动的位置
	public HomeReceiver homeReceiver;
	int imgcout;// 当前播放的音乐
	private int currentTime;// 当前播放的时间
	private static int a;
	private int maxTime;// 最大时间
	private String MusicName = "";// 当前播放音乐的名字
	public static ImageView img_1, img_2;
	public static DBHelper dbHelper;
	public static List<Device> mDevices;
	public static  Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			MessageType mt = MessageType.values()[msg.what];
			switch (mt) {
			case MSG_PALY_MUSICLENGTH:
				a = (Integer) msg.obj;
				Deiviceutis.length=a;
				Log.e("MainActivity", "到这来了MSGMSG_PALY_MUSICLENGTH" + a);
				seekBar.setMax((int) a);
				seekBar.setEnabled(true);
				img_2.setBackgroundResource(R.drawable.stop);
				udptvleng((a * 1000));
				break;
			case MSG_PALY_TRUE:
				MusicInfo ms = (MusicInfo) msg.obj;
				udpInfo(ms);
				Log.e("MainActivity", "到这来了MSG_PALY_TRUE" + ms.toString());
				break;
			case MSG_PALY_POSITIO:
				if(Deiviceutis.mPlaying){
					int posi = (Integer) msg.obj;	
					seekBar.setProgress((int) posi);
					udptvcount(posi * 1000);
					}
				
				break;
			case MSG_PALY_PAUSE:
				img_2.setBackgroundResource(R.drawable.play);
				break;
			case MSG_PALY_GOON:
				img_2.setBackgroundResource(R.drawable.stop);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		addListener();
		startDLNAService();		//开启search命令
		mDevices = DLNAContainer.getInstance().getDevices();
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

		 	builder.setTitle("警告: ")
		 	.setMessage("你确定要退出该程序?")
		 	.setIcon(android.R.drawable.ic_dialog_info)
			.setCancelable(false)
		 	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		 		public void onClick(DialogInterface dialog, int id) {
		 			dialog.dismiss();
		 			finish();
	           }
		 	})
		 	.setNegativeButton("No", new DialogInterface.OnClickListener() {
		 		public void onClick(DialogInterface dialog, int id) {
		 			dialog.dismiss();
	           }
		 	});
		 	builder.show();
		}

		return super.onKeyDown(keyCode, event);
    }
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("a", "(int) Deiviceutis.length"+(int) Deiviceutis.length);
	
		if(C.isdevies){
				C.dao.setHandler(mHandler);	
				seekBar.setMax((int) Deiviceutis.length);
				seekBar.setEnabled(true);
		}else{
			C.dao.setHandler(new Handler());
			setReceiver();
		}
		ArrayList<MusicInfo> queryMusicinfo = dbHelper.queryMusicinfo();
		if(C.isMyPlay||Deiviceutis.mPlaying){
			img_2.setBackgroundResource(R.drawable.stop);
			if(Deiviceutis.mPlaying){
				tv_mian_1.setText(Deiviceutis.info.getTitle() + "(" + Deiviceutis.info.getAuthor() + ")");
			}
		}else{
			img_2.setBackgroundResource(R.drawable.play);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("a", "onPause");
		// unregisterReceiver(homeReceiver);
		if (homeReceiver != null) {
			unregisterReceiver(homeReceiver);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopService(new Intent(this, PlayerService.class));
		stopService(new Intent(this, DownloadMusicService.class));
		stopDLNAService();
		C.logshow("停止了服务");
	}

	public void initView() {
		C.setfile();
		dbHelper = new DBHelper(MainActivity.this);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setOffscreenPageLimit(2);
		rb1 = (RadioButton) findViewById(R.id.rb1);
		rb2 = (RadioButton) findViewById(R.id.rb2);
		rb3 = (RadioButton) findViewById(R.id.rb3);
		rb4 = (RadioButton) findViewById(R.id.rb4);

		tv_mian_1 = (TextView) findViewById(R.id.tv_player_title);
		tv_mian_2 = (TextView) findViewById(R.id.tv_player_currentPosition);
		tv_mian_3 = (TextView) findViewById(R.id.tv_player_duration);
		seekBar = (SeekBar) findViewById(R.id.pb_player_progress);
		seekBar.setEnabled(false);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if(C.isdevies){
					String targetPosition = Deiviceutis.secToTime(progres);
					C.dao.seek(targetPosition);
				}else{
					Intent intent = new Intent();
				    intent.setAction(PlayerService.CTL_ACTION);
				    intent.putExtra("MSG", AppConstant.PlayerMsg.PROGRESS_CHANGE);
				    intent.putExtra("progress", progres);
				    sendBroadcast(intent);
				}
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					progres = progress;

				}
			}
		});

		img_1 = (ImageView) findViewById(R.id.ibtn_player_albumart);
		img_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(MainActivity.this,
						PlayActivity.class);
				startActivity(intent);

			}
		});
		img_2 = (ImageView) findViewById(R.id.ibtn_player_control);
		img_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(C.isdevies){
					if(Deiviceutis.mPlaying){
						C.dao.pause();
					}else{
						C.dao.goon();
					}
				}else{
					if (C.isMyPlay) {
						Intent intent = new Intent();
						intent.setAction(PlayerService.CTL_ACTION);
						intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
						sendBroadcast(intent);
					} else {
						Intent intent = new Intent();
						intent.setAction(PlayerService.CTL_ACTION);
						intent.putExtra("MSG", AppConstant.PlayerMsg.CONTINUE_MSG);
						sendBroadcast(intent);
					}
				}
			}
		});

		rb1.setOnClickListener(this);
		rb2.setOnClickListener(this);
		rb3.setOnClickListener(this);
		rb4.setOnClickListener(this);
		startService(new Intent(this, DownloadMusicService.class)); // 下载音乐的
		startService(new Intent(this, PlayerService.class));
		FragmentAdapter fragmentAdapter = new FragmentAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(fragmentAdapter);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.rb1:
			viewPager.setCurrentItem(TAB_LOCAL);
			break;
		case R.id.rb2:
			viewPager.setCurrentItem(TAB_NETWORK);
			break;
		case R.id.rb3:
			viewPager.setCurrentItem(TAB_RECENTLY);
			break;
		case R.id.rb4:
			viewPager.setCurrentItem(TAB_DOWNLOAD);
			break;
		}

	}

	private void addListener() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case TAB_LOCAL:
					rb1.setChecked(true);
					rb1.setTextColor(R.color.lv);
					break;
				case TAB_NETWORK:
					rb2.setChecked(true);
					rb2.setTextColor(R.color.lv);
					break;
				case TAB_RECENTLY:
					rb3.setChecked(true);
					rb3.setTextColor(R.color.lv);
					break;
				case TAB_DOWNLOAD:
					rb4.setChecked(true);
					rb4.setTextColor(R.color.lv);
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	// 自定义的BroadcastReceiver，负责监听从Service传回来的广播
	public class HomeReceiver extends BroadcastReceiver {

		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(PlayerService.MUSIC_CURRENT)) {
				currentTime = intent.getIntExtra("currentTime", 0);
				maxTime = intent.getIntExtra("maxTime", 0);
				MusicInfo info = (MusicInfo) intent
						.getSerializableExtra("info");

				if (!MusicName.equals(info.getTitle())) {
					// mLyricAdapter = new LyricAdapter(PlayActivity.this);

					MusicName = info.getTitle();
					Log.e("a", "MusicName" + MusicName);
					tv_mian_1.setText(MusicName + "(" + info.getAuthor() + ")");
					tv_mian_3.setText(C.getTimeFromInt(maxTime) + "");
					// img_2.setBackgroundResource(R.drawable.mini_pause_normal);

					// C.SLog("1");
					// loadLyric(info);
					// C.SLog("2");
					// mLyricLoadHelper.setLyricListener(mLyricListener);
					// C.SLog("3"+mLyricAdapter.getCount());

					// mLrcListView.setAdapter(mLyricAdapter);
					// mLrcListView.setEmptyView(mLrcEmptyView);
					// mLrcListView.startAnimation(AnimationUtils.loadAnimation(PlayActivity.this,
					// android.R.anim.fade_in));
					// mLrcListView.smoothScrollToPositionFromTop(1,
					// mLrcListView.getHeight() / 2, 500);
					//
					seekBar.setMax((int) maxTime);
					seekBar.setEnabled(true);
				}
				if(C.isMyPlay){
						tv_mian_2.setText(C.getTimeFromInt(currentTime) + "");
						seekBar.setProgress((int) currentTime);
				}
			
				// mLyricLoadHelper.notifyTime(currentTime);

				// C.SLog("当前播放的位置是" + (int) currentTime / 1000 + "/"
				// + (int) maxTime / 1000);
			} else if (action.equals(PlayerService.UPDATE_ACTION)) {
				// 获取Intent中的current消息，current代表当前正在播放的歌曲

			}
			// else if (action.equals(DownloadMusicService.DOWNLOAD_CURRENT)) {
			// int currentTime = intent.getIntExtra("currentTime", 0);
			// int duration = intent.getIntExtra("duration", 0);
			// C.logshow("当前下载的位置是" + currentTime + "最大下载" + duration);
			// if (currentTime == duration) {
			//
			// }
			// }
		}

	}

	
	public void setReceiver() {
		IntentFilter filter = new IntentFilter();
		homeReceiver = new HomeReceiver();
		// 指定BroadcastReceiver监听的Action
		filter.addAction(PlayerService.UPDATE_ACTION);
		filter.addAction(PlayerService.MUSIC_CURRENT);
		filter.addAction(PlayerService.MUSIC_DURATION);
		// filter.addAction(REPEAT_ACTION,PlayerService.);
		// filter.addAction(SHUFFLE_ACTION);
		//
		filter.addAction(PlayerService.PLAY_SDMUSIC);
		filter.addAction(DownloadMusicService.DOWNLOAD_MUSIC);
		filter.addAction(DownloadMusicService.DOWNLOAD_CURRENT);
		filter.addAction(DownloadMusicService.DOWNLOAD_DELE);
		filter.addAction(DownloadMusicService.DOWNLOAD_LRC);
		filter.addAction(DownloadMusicService.DOWNLOAD_INFO);
		// 注册BroadcastReceiver
		registerReceiver(homeReceiver, filter);

	}

	
	// g
	private static void udptvleng(int length) {
		tv_mian_3.setText(C.getTimeFromInt(length) + "");
	}
	

	private static void udptvcount(int length) {
		tv_mian_2.setText(C.getTimeFromInt(length) + "");
	}
	private static void udpInfo(MusicInfo info) {
		tv_mian_1.setText(info.getTitle()+"("+info.getAuthor()+")");
		Deiviceutis.info=info;
	}
	
	public static void unpInfonull(){
		tv_mian_1.setText("");
		tv_mian_2.setText("00:00");
		tv_mian_3.setText("00:00");
		seekBar.setProgress(0);
		C.bitmap=null;
		img_1.setBackgroundResource(R.drawable.pic);
		img_2.setBackgroundResource(R.drawable.play);
	}
	private void startDLNAService() {
		DLNAContainer.getInstance().clear();
		Intent intent = new Intent(MainActivity.this, DLNAService.class);
		MainActivity.this.startService(intent);
	}

	private void stopDLNAService() {
		Intent intent = new Intent(MainActivity.this, DLNAService.class);
		MainActivity.this.stopService(intent);
	}
}
