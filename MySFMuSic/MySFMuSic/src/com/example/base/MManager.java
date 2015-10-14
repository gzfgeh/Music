package com.example.base;

import android.os.Handler;
import android.os.Message;

public class MManager {
	public static void sendMessage(Handler handler, MessageType msg, Object obj) {
		if (handler == null) {
			return;
		}

		Message message = Message.obtain();

		message.what = msg.ordinal();
		message.obj = obj;
		handler.sendMessage(message);
	}

	public static void sendMessageArg1(Handler handler, MessageType msg,
			Object obj, int arg1) {
		if (handler == null) {
			return;
		}
		Message message = Message.obtain();

		message.what = msg.ordinal();
		message.obj = obj;
		message.arg1 = arg1;
		handler.sendMessage(message);
	}

	public static enum MessageType {
		/**
		 * 音乐广播 返回 MusicInfo
		 */
		MSG_PALY_TRUE,
		/**
		 * 音乐广播 返回 播放进度 int
		 */
		MSG_PALY_POSITIO,

		/**
		 * 暂停音乐播放
		 */
		MSG_PALY_PAUSE,
		/**
		 * 继续音乐播放
		 */
		MSG_PALY_GOON,
		/**
		 * 音乐播放位置
		 */
		MSG_PALY_SEEK,
				/**
		 * 音乐开始播放  int 播放的歌曲长度
		 */
		MSG_PALY_MUSICLENGTH;
	}
}