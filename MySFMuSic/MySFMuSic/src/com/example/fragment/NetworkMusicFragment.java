package com.example.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;

import com.example.XMLStructure.SAXPlaylistStructure;
import com.example.activity.MainActivity;
import com.example.adapter.MyListBase2Adapter;
import com.example.base.C;
import com.example.base.MManager;
import com.example.base.MManager.MessageType;
import com.example.mysfmusic.R;
import com.example.service.DownloadMusicService;
import com.example.service.PlayerService;
import com.example.utis.MusicInfo;
import com.example.utis.Playlist;
import com.google.gson.Gson;
import com.ldw.music.list.XListView;
import com.ldw.music.list.XListView.IXListViewListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.SaxAsyncHttpResponseHandler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 网络音乐
 * 
 * @author Administrator
 * 
 */
public class NetworkMusicFragment extends Fragment implements
		IXListViewListener {
	private EditText editText;
	private Button button;
	private ArrayList<MusicInfo> infos = new ArrayList<MusicInfo>();
	public static ArrayList<MusicInfo> NewInfos = new ArrayList<MusicInfo>();
	private XListView listView;
	private MyListBase2Adapter adapter;
	private MainActivity activity;
	public static int Selected = 0;
	private Playlist curPlaylist;
	private int count = 1;// 记录当前查找的页数
	private Button bt_gd;
	private String name;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			MessageType mt = MessageType.values()[msg.what];
			switch (mt) {
			case MSG_PALY_MUSICLENGTH:
				int a = (Integer) msg.obj;
				Log.e("a", "到这来了MSGMSG_PALY_MUSICLENGTH" + a);

				break;
			case MSG_PALY_TRUE:
				MusicInfo ms = (MusicInfo) msg.obj;
				Log.e("a", "到这来了MSG_PALY_TRUE" + ms.toString());
				break;
			default:
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		C.logshow("到了这里:网络音乐");
		View view = inflater.inflate(R.layout.fragment_2, container, false);
		inintView(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		activity = (MainActivity) getActivity();

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		C.logshow("到了这里:网络音乐2222222222222");

	}

	private void inintView(View view) {

		editText = (EditText) view.findViewById(R.id.et_musicname);
		button = (Button) view.findViewById(R.id.bt_ok);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				count = 1;
				name = "";
				NewInfos = new ArrayList<MusicInfo>();
				name = editText.getText() + "";
				if (name != "") {
					AsyncHttpGetMusicInfo(name);

				} else {
					name = "冷雨夜";
					AsyncHttpGetMusicInfo("冷雨夜");
				}
			}
		});
		listView = (XListView) view.findViewById(R.id.lv_list_change_content);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(false);
		listView.setXListViewListener(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		C.logshow("到了这里:网络音乐");
	}

	private void startService(Intent intent) {
		// TODO Auto-generated method stub

	}

	public void AsyncHttpGetMusicInfo(String musicName) {
		if (musicName != null) {
			infos = new ArrayList<MusicInfo>();
			final String url = "http://www.xiami.com/web/search-songs?key="
					+ musicName + "&page=" + count;
			C.logshow("url:" + url);
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(url, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {

					// String response=responseBody.toString();
					// Log.e("a", response+"");
					if (statusCode == 200) {
						String response = null;
						listView.stopRefresh();
						listView.stopLoadMore();
						try {
							response = new String(responseBody, "UTF-8");
							C.logshow("response:" + response + ":caca");
							// Log.e("a", response+"");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (!response.equals("null")) {
							Gson gson = new Gson();

							MusicInfo[] musics = gson.fromJson(response,
									MusicInfo[].class);
							C.logshow("response:" + response + ":caca");
							// 成功处理的方法
							System.out.println("statusCode-------------------"
									+ statusCode);

							// Log.e("a", "收到返回"+statusCode);
							// 遍历头信息
							if (musics.length > 0) {
								for (int i = 0; i < musics.length; i++) {
									infos.add(musics[i]);
									Log.e("a", "收到返回1:" + musics[i].getTitle());
								}
								Log.e("a", "infos" + infos.size());
								showByMyBaseAdapter(infos);
							}

						} else {
							Toast.makeText(activity, "木有找到该歌曲",
									Toast.LENGTH_SHORT).show();
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

	}

	public void showByMyBaseAdapter(final ArrayList<MusicInfo> infos) {
		for (int i = 0; i < infos.size(); i++) {
			NewInfos.add(infos.get(i));
		}
		adapter = new MyListBase2Adapter(activity.getApplicationContext(),
				NewInfos);

		adapter.notifyDataSetChanged();
		listView.setAdapter(adapter);
		if (count > 1) {
			listView.setSelection(listView.getBottom()); // 刷新完成自动到底部
		}

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Selected = arg2 - 1;
				C.logshow("点击了：" + Selected);


				if (infos.size() > 0) {
					if (C.isdevies) {
						//C.dao.stop();
						C.dao.paly(NewInfos.get(Selected));

					} else {		
						Intent intent = new Intent();
						intent.setAction(PlayerService.PLAY_MUSIC);
						MusicInfo info = infos.get(0);
						intent.putExtra("musicinfo", NewInfos);
						intent.putExtra("current", Selected);
						activity.sendBroadcast(intent);
					}

				} else {
					Log.e("a", "为空了");
				}

				// doTaskGetSongPlaylist(infos.get(arg2).getId());
			}
		});
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		AsyncHttpGetMusicInfo(name);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		// listView.stopRefresh();
		// listView.stopLoadMore();
		// listView.setRefreshTime("刚刚");
		count++;
		AsyncHttpGetMusicInfo(name);

	}

}
