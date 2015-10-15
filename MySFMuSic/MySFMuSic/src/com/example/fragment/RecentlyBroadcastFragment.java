package com.example.fragment;

import java.util.ArrayList;

import com.example.activity.MainActivity;
import com.example.adapter.MyListBase1Adapter;
import com.example.base.C;
import com.example.dbdao.DBHelper;
import com.example.mysfmusic.R;
import com.example.service.PlayerService;
import com.example.utis.MusicInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 最近播放
 * 
 * @author Administrator
 * 
 */
public class RecentlyBroadcastFragment extends Fragment {
	private static TextView tv_01;
	private static ListView lv_01;
	private static MainActivity activity;
	private static DBHelper dbHelper;
	private static MyListBase1Adapter adapter;
	public static ArrayList<MusicInfo> queryMusicinfo;
	public static int Selected = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		C.logshow("到了这里:最近播放");
		View view = inflater.inflate(R.layout.fragment_3, container, false);
		inintView(view);
		return view;
	}
	private void inintView(View view) {
		tv_01 = (TextView) view.findViewById(R.id.fm_3_01);
		lv_01 = (ListView) view.findViewById(R.id.fm_3_lv);
		lv_01.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				// TODO Auto-generated method stub
				Selected = arg2;
				if (queryMusicinfo.size() > 0) {
						if (C.isdevies) {
							Log.e("a", ("queryMusicinfo.get(Selected).getSDLRC():"+(queryMusicinfo.get(Selected).getSDLRC()+"")));
							if(!(queryMusicinfo.get(Selected).getSDLRC()+"").equals("null")){
								String path = "http://" + LocalMusicFragment.getLocalIpAddress()
										+ ":1326/"
										+ queryMusicinfo.get(arg2).getId()
										+ ".mp3";
								// String
								// path="http://"+getLocalIpAddress()+":8089/啊啊啊.mp3";
								Log.e("a", "path:" + path);
								//C.dao.stop();
								C.dao.paly(path, queryMusicinfo.get(arg2));

							}else{
							//	C.dao.stop();
								C.dao.paly(queryMusicinfo.get(Selected));
							}
							
						}else{
							Intent intent = new Intent();
							intent.setAction(PlayerService.PLAY_MUSIC);
								intent.putExtra("musicinfo", queryMusicinfo);
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
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		C.logshow("到了这里:最近播放");
		gengxin();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		activity = (MainActivity) getActivity();
	}

	public static void gengxin() {
		if (MainActivity.dbHelper != null) {
			dbHelper = MainActivity.dbHelper;
			queryMusicinfo = dbHelper.queryRecent();
			if (queryMusicinfo != null) {
				tv_01.setVisibility(View.GONE);
				C.logshow("FM1:" + queryMusicinfo.get(0).getTitle());
				adapter = new MyListBase1Adapter(activity, queryMusicinfo);
				lv_01.setAdapter(adapter);
			}
		}
	}
}
