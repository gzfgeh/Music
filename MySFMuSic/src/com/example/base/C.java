package com.example.base;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.Header;

import com.example.activity.MainActivity;
import com.example.activity.PlayActivity;
import com.example.utis.MusicInfo;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.Toast;

public class C {

	private static boolean islog = false;

	public static String SRC = "/sdcard/wodemusic/";
	public static String IMGCUN = "/sdcard/wodemusic/files/";
	public static String MP3SRC = "/sdcard/wodemusic/mp3/";
	public static String LRCSRC = "/sdcard/wodemusic/lrc/";
	public static String IMGSRC = "/sdcard/wodemusic/img/";
	/**
	 * 记录当前是否在播放
	 */
	public static DeviceDao dao = DeviceDao.getDeviceDao();
	public static boolean isMyPlay = false;
	public static boolean isoff = false;
	public static Bitmap bitmap = null;
	public static boolean isdevies = false;// 是否连接上设备
	public static int postdevies=0;
	
	public static Bitmap AsygetBmp(String musicName) {

		if (musicName != null) {
			bitmap = null;
			final String url = musicName;
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(url, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {

					// String response=responseBody.toString();
					// Log.e("a", response+"");
					if (statusCode == 200) {
						bitmap = BitmapFactory.decodeByteArray(responseBody, 0,
								responseBody.length);
						MainActivity.img_1.setImageBitmap(bitmap);
						if (C.isoff) {
							PlayActivity.udpimg();

						}
					}

				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// 失败处理的方法
					error.printStackTrace();
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();

				}

			});
		}
		return bitmap;

	}

	/**
	 * 时间的处理
	 * 
	 * @param time
	 * @return
	 */
	public static String getTimeFromInt(int time) {
		if (time <= 0) {
			return "0:00";
		}
		int secondnd = (time / 1000) / 60;
		int million = (time / 1000) % 60;
		String f = String.valueOf(secondnd);
		String m = million >= 10 ? String.valueOf(million) : "0"
				+ String.valueOf(million);
		return f + ":" + m;
	}

	/***
	 * 创建文件夹
	 */
	public static void setfile() {
		File s = new File(C.SRC);
		if (!s.exists()) {
			s.mkdirs();
		}
		s = new File(C.LRCSRC);
		if (!s.exists()) {
			s.mkdirs();
		}
		s = new File(C.MP3SRC);
		if (!s.exists()) {
			s.mkdirs();
		}
		s = new File(C.IMGSRC);
		if (!s.exists()) {
			s.mkdirs();
		}
		s = new File(C.IMGCUN);
		if (!s.exists()) {
			s.mkdirs();
		}
	}

	public static String getSyTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date = sdf.format(new java.util.Date());
		return date;

	}

	/* 将字符串转为时间戳 */
	public static String getTime(String user_time) {
		String re_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date d;
		try {
			d = sdf.parse(user_time);
			long l = d.getTime();
			String str = String.valueOf(l);
			re_time = str.substring(0, 10);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re_time;
	}

	/* 将时间戳转为字符串 */
	public static String getStrTime(String cc_time) {
		String re_Strtime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		// 例如：cc_time = 1291778220 2010年12月08日11时17分00秒
		long lcc_time = Long.valueOf(cc_time);
		re_Strtime = sdf.format(new Date(lcc_time * 1000L));
		return re_Strtime;
	}

	public static void logshow(String log) {
		if (islog) {
			Log.e("a", log);
		}
	}

}
