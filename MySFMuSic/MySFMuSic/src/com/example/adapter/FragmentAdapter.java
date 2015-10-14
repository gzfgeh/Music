package com.example.adapter;

import com.example.activity.MainActivity;
import com.example.fragment.DownloadManagerFragment;
import com.example.fragment.LocalMusicFragment;
import com.example.fragment.NetworkMusicFragment;
import com.example.fragment.RecentlyBroadcastFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class FragmentAdapter extends FragmentPagerAdapter {
	public final static int TAB_COUNT = 4;

	public FragmentAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		switch (arg0) {
		case MainActivity.TAB_LOCAL:
			LocalMusicFragment Localfragment = new LocalMusicFragment();
			return Localfragment;
		case MainActivity.TAB_NETWORK:
			NetworkMusicFragment NetworkFragment = new NetworkMusicFragment();
			return NetworkFragment;
		case MainActivity.TAB_RECENTLY:
			RecentlyBroadcastFragment RecentlyFragment = new RecentlyBroadcastFragment();
			return RecentlyFragment;
		case MainActivity.TAB_DOWNLOAD:
			DownloadManagerFragment DownloadFragment = new DownloadManagerFragment();
			return DownloadFragment;
		default:
			break;
		}
		return null;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return TAB_COUNT;
	}

}
