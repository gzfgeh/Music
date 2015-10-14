package com.example.fragment;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;
import org.cybergarage.upnp.Device;

import com.example.activity.MainActivity;
import com.example.activity.PlayActivity;
import com.example.adapter.MyListBase1Adapter;
import com.example.adapter.MyListBase2Adapter;
import com.example.base.C;
import com.example.base.DeviceDao;
import com.example.dbdao.DBHelper;
import com.example.dbdao.NanoHTTPD;
import com.example.dlna.DLNAContainer;
import com.example.dlna.DLNAService;
import com.example.mysfmusic.R;
import com.example.service.AppConstant;
import com.example.service.PlayerService;
import com.example.utis.MusicInfo;

import com.ldw.music.list.XListView;
import com.ldw.music.list.XListView.IXListViewListener;
import com.realtek.simpleconfig.SCTest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * 本地音乐
 * 
 * @author Administrator
 * 
 */
public class LocalMusicFragment extends Fragment {
	private ListView listView;
	private static MainActivity activity;
	private static TextView tv_1;
	private static ListView lv_1;
	private static DBHelper dbHelper;
	private static MyListBase1Adapter adapter;
	public static ArrayList<MusicInfo> queryMusicinfo;
	private Button bt_1;
	private Button bt_2;
	public  static NanoHTTPD n;
	public  static int Selected=0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_1, container, false);
		inintView(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		C.logshow("到了这里:本地音乐");
		activity = (MainActivity) getActivity();
	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		gengxin();
	}

	public static void gengxin() {
		if (MainActivity.dbHelper != null) {
			dbHelper = MainActivity.dbHelper;
			queryMusicinfo = dbHelper.queryMusicinfo();
			if (queryMusicinfo != null) {
				tv_1.setVisibility(View.GONE);
				C.logshow("FM1:" + queryMusicinfo.get(0).getTitle());
				adapter = new MyListBase1Adapter(activity, queryMusicinfo);
				lv_1.setAdapter(adapter);
				lv_1.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Selected=arg2;
						if (queryMusicinfo.size() > 0) {
							if (C.isdevies) {

								String path = "http://" + getLocalIpAddress()+ ":39500/"
										+ queryMusicinfo.get(arg2).getId()
										+ ".mp3";
								//path = "http://music.baidu.com/data/music/file?xcode=b562edeac4128a3c4394f800f7f2550e&song_id=247911654?link=http://yinyueshiting.baidu.com/data2/music/247912224/24791165410800064.mp3";
								// String
								// path="http://"+getLocalIpAddress()+":8089/啊啊啊.mp3";
								//path = "http://video19.ifeng.com/video07/2013/11/11/281708-102-007-1138.mp4";
								Log.e("b", "path:" + path);
							//	C.dao.stop();
								C.dao.paly(path, queryMusicinfo.get(arg2));

							} else {
								Intent intent = new Intent();
								intent.setAction(PlayerService.PLAY_SDMUSIC);
								intent.putExtra("current", arg2);
								intent.putExtra("musicinfo", queryMusicinfo);
								activity.sendBroadcast(intent);
							}
						} else {
							Log.e("a", "为空了");
						}
						// doTaskGetSongPlaylist(infos.get(arg2).getId());
					}
				});
			}
		}
	}

	private void inintView(View view) {

		tv_1 = (TextView) view.findViewById(R.id.fm_1_01);
		lv_1 = (ListView) view.findViewById(R.id.fm_1_lv);
		bt_1 = (Button) view.findViewById(R.id.fm1_bt1);
		bt_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent intent = new Intent(activity, PeiZhiActivity.class);
				// startActivity(intent);
				String[] strs = new String[1 + activity.mDevices.size()];
				strs[0] = "手机";
				for (int i = 1; i <= activity.mDevices.size(); i++) {
					strs[i] = activity.mDevices.get(i - 1).getFriendlyName() + "";
				}

				new AlertDialog.Builder(activity)
						.setTitle("请选择播放设备")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setSingleChoiceItems(strs, C.postdevies,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										// Toast.makeText(activity,
										// "Item is "+which,
										// Toast.LENGTH_LONG).show();
										if (C.postdevies !=which) {
											activity.unpInfonull();
											C.postdevies = which;
											if (which == 0) {
											
												C.dao.stop();
												C.dao.getleng=0;
												C.dao.info=null;
												C.dao.setHandler(new Handler());
												C.dao.setPositioThre(false);
												activity.setReceiver();
												C.isdevies = false;
												if (n != null) {
													n.stop();
												}
												 Toast.makeText(activity, "连接手机", Toast.LENGTH_SHORT).show();
											}
											 else {												 
													C.dao.setHandler(activity.mHandler);
													PlayerService.stop();
													if (activity.homeReceiver != null) {
														activity.unregisterReceiver(activity.homeReceiver);
														activity.homeReceiver = null;
													}
													DLNAContainer
															.getInstance()
															.setSelectedDevice(
																	activity.mDevices.get(which - 1));
													C.isdevies = true;
													try {
														File wwwroot = new File(
																C.MP3SRC);
														n = new NanoHTTPD(39500, wwwroot);
														Log.e("a", getLocalIpAddress());
													} catch (Exception e) {
														// TODO Auto-generated catch
														// block
														e.printStackTrace();
													}
													 Toast.makeText(activity, "连接"+activity.mDevices.get(which - 1).getFriendlyName()+"成功", Toast.LENGTH_SHORT).show();
												}
										}
										dialog.dismiss();
										
									}
								}).show();
			}
		});

		
		bt_2 = (Button) view.findViewById(R.id.fm1_bt2);
		bt_2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(activity,
						SCTest.class);
				startActivity(intent);
			}
		});
//		bt_2.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				// MusicInfo path=new MusicInfo();
//				// path.setSrc(C.MP3SRC+"1771019424.mp3");
//				// C.dao.paly(path);
//				try {
//					File wwwroot = new File(C.MP3SRC);
//					NanoHTTPD n = new NanoHTTPD(8089, wwwroot);
//
//					Log.e("a", getLocalIpAddress());
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});

	}

	public static String getLocalIpAddress() {
		try {
			// 遍历网络接口
			Enumeration<NetworkInterface> infos = NetworkInterface
					.getNetworkInterfaces();
			while (infos.hasMoreElements()) {
				// 获取网络接口
				NetworkInterface niFace = infos.nextElement();
				Enumeration<InetAddress> enumIpAddr = niFace.getInetAddresses();
				while (enumIpAddr.hasMoreElements()) {
					InetAddress mInetAddress = enumIpAddr.nextElement();
					// 所获取的网络地址不是127.0.0.1时返回得得到的IP
					if (!mInetAddress.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(mInetAddress
									.getHostAddress())) {
						return mInetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException e) {

		}
		return null;
	}

	// @Override
	// public void onRefresh() {
	// // TODO Auto-generated method stub
	// C.logshow("刷新了数据");
	// }
	//
	//
	// @Override
	// public void onLoadMore() {
	// // TODO Auto-generated method stub
	// adapter.notifyDataSetChanged();
	// C.logshow("刷新了数据22");
	// lv_1.stopRefresh();
	// lv_1.stopLoadMore();
	// lv_1.setRefreshTime("刚刚");
	// // lv_1.setAdapter(adapter);
	// }

	private void startDLNAService() {
		DLNAContainer.getInstance().clear();
		Intent intent = new Intent(activity, DLNAService.class);
		activity.startService(intent);
	}

	private void stopDLNAService() {
		Intent intent = new Intent(activity, DLNAService.class);
		activity.stopService(intent);
	}

}
