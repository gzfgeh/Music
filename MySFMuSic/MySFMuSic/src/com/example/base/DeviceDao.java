package com.example.base;

import org.cybergarage.upnp.Device;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.base.MManager.MessageType;
import com.example.dlna.DLNAContainer;
import com.example.dlna.IController;
import com.example.dlna.MultiPointController;
import com.example.service.PlayerService;
import com.example.utis.MusicInfo;

public class DeviceDao {
	private static DeviceDao dao = null;
	private static IController controller = null;
	private Device device = null;
	private int mMediaDuration = 0;// 当前播放的长度
	public static int getleng = 0;// 上次播放的长度
	private String positio;
	private String mediaDuration;
	private Boolean ispositio=false;//是否正在广播播放进度
	public static MusicInfo info;//是否为同首歌
	private int du=0;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			default:
				break;
			}
		};
	};

	public static DeviceDao getDeviceDao() {
		if (dao == null) {
			dao = new DeviceDao();
		}
		if (controller == null) {
			controller = new MultiPointController();
		}
	
		
		return dao;
		

	}

	public void setHandler(Handler handler) {
		this.mHandler = handler;
	}

	private boolean is;
/**
 * 播放音乐
 * @param path
 */
	public void paly(final MusicInfo path) {
		du=0;
		mMediaDuration = 0;
		device = DLNAContainer.getInstance().getSelectedDevice();
		if (device == null || controller == null) {
			// usually can't reach here.
			Log.e("a", "device+controller为空了");
		}
//		controller = new MultiPointController();
		new Thread() {
			public void run() {
				is = false;
				is = controller.play(device, path.getSrc());
				
				Log.e("a", "is" + is);
				if (is) {
					Deiviceutis.mPlaying=true;
					MManager.sendMessage(mHandler, MessageType.MSG_PALY_TRUE,
							path);
					getLength();
				}
			}
		}.start();
		if(path.getCover()+""!=""){
			Bitmap asygetBmp = C.AsygetBmp(path.getCover());
			asygetBmp = null;
		}
		

	}
	public void paly(final String path,final MusicInfo info) {
		du=0;
		mMediaDuration = 0;
		device = DLNAContainer.getInstance().getSelectedDevice();
		if (device == null || controller == null) {
			// usually can't reach here.
			Log.e("a", "device+controller为空了");
		}
		new Thread() {
			public void run() {
				is = false;
				is = controller.play(device, path);
				Log.e("a", "is" + is);
				if (is) {
					Deiviceutis.mPlaying=true;
					MManager.sendMessage(mHandler, MessageType.MSG_PALY_TRUE,
							info);
					getLength();

				}
			}
		}.start();
		
		if(info.getCover()+""!=""){
			Bitmap asygetBmp = C.AsygetBmp(info.getCover());
			asygetBmp = null;
		}
		
//		controller = new MultiPointController();

	}
	/**
	 * 暂停播放
	 */
	public void pause() {

		new Thread() {
			public void run() {
				if(Deiviceutis.mPlaying){
					final boolean isSuccess = controller.pause(device);
						if (isSuccess) {
							Deiviceutis.mPlaying=false;
							MManager.sendMessage(mHandler, MessageType.MSG_PALY_PAUSE,"");
							
						}else{
							Deiviceutis.mPlaying=true;
						}
				}
				
			};
		}.start();
	}
	/**
	 * 暂停播放
	 */
	public void stop() {

		new Thread() {
			public void run() {
				if(Deiviceutis.mPlaying){
					final boolean isSuccess = controller.stop(device);
						if (isSuccess) {
							Deiviceutis.mPlaying=false;
							MManager.sendMessage(mHandler, MessageType.MSG_PALY_PAUSE,"");
							
						}else{
							Deiviceutis.mPlaying=true;
						}
				}
				
			};
		}.start();
	}
	/**
	 * 继续播放
	 * @param pausePosition
	 */
	public void  goon() {
		new Thread() {
			@Override
			public void run() {
				Log.e("a", "到了goon"+positio);
				
				if (device == null)
					device = DLNAContainer.getInstance().getSelectedDevice();
				if (positio == null)
					getPositio();
				
				final boolean isSuccess = controller.goon(device,
						positio);
				
				if (isSuccess) {
					Deiviceutis.mPlaying=true;
					MManager.sendMessage(mHandler, MessageType.MSG_PALY_GOON,"");
				}else{
					Deiviceutis.mPlaying=false;
				}

			}
		}.start();
	}
	
	private void getLength() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				mediaDuration = controller.getMediaDuration(device);
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.e("a", "mediaDuration" + mediaDuration);
						mMediaDuration = Deiviceutis.getIntLength(mediaDuration);
						Log.e("a", "mMediaDuration" + mMediaDuration);
						if (mMediaDuration == 0) {
							getLength();
						} else {
							
							if (mMediaDuration == getleng) {
								du++;
								if(du<20){
									getLength();
								}
								
							} else {
								getleng = mMediaDuration;
								if(!ispositio){
									getPositio();
									ispositio=true;
								}
//									getPositio();
								Deiviceutis.mPlaying=true;
								MManager.sendMessage(mHandler,
										MessageType.MSG_PALY_MUSICLENGTH,
										mMediaDuration);
							}
						}

					}
				}, 1000);

			}
		}).start();
	}

	public void getPositio() {
	//	mHandler.postDelayed(r, 1000);
		new Thread() {
			public void run() {	
				Log.e("a", "lail ");
				positio = controller.getPositionInfo(device);
				Log.e("a", "PositioPositioPositio:" + positio);
				MManager.sendMessage(mHandler,
						MessageType.MSG_PALY_POSITIO,Deiviceutis.getIntLength(positio));
				mHandler.postDelayed(new Runnable() {		
					@Override
					public void run() {
						// TODO Auto-generated method stub
//						if(ispositio){
//							if(positio==mediaDuration){
//								Log.e("a", "长度:"+"positio:"+positio+"mediaDuration:"+mediaDuration);
//								return;
//							}else{
//								getPositio();
//							}
//						}
						getPositio();
						
					}
				},1000);
			};
		}.start();
		
		
//		mHandler.postDelayed(new Runnable() {		
//			@Override
//			public void run() {
//				new Thread(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						positio = controller.getPositionInfo(device);
//						MManager.sendMessage(mHandler,
//								MessageType.MSG_PALY_POSITIO,Deiviceutis.getIntLength(positio));
//					}
//				}).start();
//			}
//		},800);
	}

	
	
	public void  seek(final String targetPosition) {
		new Thread() {
			@Override
			public void run() {
				boolean isSuccess = controller.seek(device, targetPosition);
				if (isSuccess) {
					Deiviceutis.mPlaying=true;
					MManager.sendMessage(mHandler, MessageType.MSG_PALY_GOON,"");
				} else {
					Deiviceutis.mPlaying=false;
				}

			}
		}.start();
	}
	
	
	
	public void setVoice(final int voice){
		new Thread() {
			@Override
			public void run() {
				boolean isSuccess = controller.setVoice(device, voice);
//				if (isSuccess) {
//					Deiviceutis.mPlaying=true;
//					MManager.sendMessage(mHandler, MessageType.MSG_PALY_GOON,"");
//				} else {
//					Deiviceutis.mPlaying=false;
//				}

			}
		}.start();
	}
	

	/**
	 * 传flase关闭读取进度线程
	 * @param posit
	 */
	public void setPositioThre(boolean posit){
		ispositio=false;
	}
	
	
}
