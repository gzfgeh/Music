package com.example.base;

import com.example.utis.MusicInfo;

import android.text.TextUtils;

public class Deiviceutis {
	public static boolean mPlaying=false;//当前是否在播放
	public static MusicInfo info;//当前播放的音乐实体累
	public static int length=0;
	/**
	 * String 转00：00：00
	 * @param length
	 * @return
	 */
	public static int getIntLength(String length) {
		if (TextUtils.isEmpty(length)) {
			return 0;
		}
		String[] split = length.split(":");
		int count = 0;
		try {
			if (split.length == 3) {
				count += (Integer.parseInt(split[0])) * 60 * 60;
				count += Integer.parseInt(split[1]) * 60;
				count += Integer.parseInt(split[2]);
			} else if (split.length == 2) {
				count += Integer.parseInt(split[0]) * 60;
				count += Integer.parseInt(split[1]);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return count;
	}
	/**
	 * 00转String
	 * @param time
	 * @return
	 */
	public static String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":"
						+ unitFormat(second);
			}
		}
		return timeStr;
	}
	
	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else if (i >= 10 && i <= 60) {
			retStr = "" + i;
		} else {
			retStr = "00";
		}
		return retStr;
	}
}
