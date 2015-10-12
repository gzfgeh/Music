package com.example.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.activity.MainActivity;
import com.example.activity.MainActivity.HomeReceiver;
import com.example.adapter.DownloadAdapter;
import com.example.base.C;
import com.example.mysfmusic.R;
import com.example.service.DownloadMusicService;
import com.example.service.PlayerService;
import com.example.utis.Downlnfos;
import com.example.utis.MusicInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 下载管理
 * 
 * @author Administrator
 * 
 */
public class DownloadManagerFragment extends Fragment {
	private HomeReceiver homeReceiver;
	private static MainActivity activity;
	private TextView tv_01;
	private static ListView lv_01;
	private static DownloadAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_4, container, false);
		inintView(view);
		return view;
	}

	private void inintView(View view) {
		tv_01 = (TextView) view.findViewById(R.id.fm_4_01);
		lv_01 = (ListView) view.findViewById(R.id.fm_4_lv);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// adapter = new DownloadAdapter(activity.getApplicationContext());
		// lv_01.setAdapter(adapter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		activity = (MainActivity) getActivity();
	}

	public static void gengxin() {
		List<Downlnfos> infos = activity.dbHelper.queryDowninfo();
		adapter = new DownloadAdapter(activity.getApplicationContext(), infos);
		lv_01.setAdapter(adapter);
		// lv_01.notify();
		// adapter.notifyDataSetChanged();
	}
}
