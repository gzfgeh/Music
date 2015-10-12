package com.example.adapter;

import java.util.List;

import com.example.activity.MainActivity;
import com.example.base.C;
import com.example.mysfmusic.R;
import com.example.service.AppConstant;
import com.example.service.DownloadMusicService;
import com.example.service.PlayerService;
import com.example.utis.Downlnfos;
import com.example.utis.MusicInfo;

import android.R.integer;
import android.R.menu;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadAdapter extends BaseAdapter {

	private static List<Downlnfos> infos;
	private static ViewHolder viewHolder = null;
	Context context;

	public DownloadAdapter(Context context, List<Downlnfos> infos) {
		this.context = context;
		this.infos = infos;
		// genxin();
		// mImageLoader = new ImageLoader(context);

	}

	public static void genxin() {
		infos = MainActivity.dbHelper.queryDowninfo();
		// notifyDataSetChanged();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (infos == null) ? 0 : infos.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return infos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return super.getItemViewType(position);
	}

	public class ViewHolder {
		TextView textViewItem01;
		TextView textViewItem02;
		ProgressBar bar;
		Button bt;

	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		genxin();
		final Downlnfos info = (Downlnfos) getItem(position);
		// C.SLog("到这来了");
		if (convertView == null) {
			viewHolder = new ViewHolder();
			// C.SLog("新建convertView,position="+position);
			convertView = (View) View.inflate(context,
					R.layout.download_list_item, null);
			viewHolder = new ViewHolder();
			// viewHolder.imageView = (ImageView) convertView
			// .findViewById(R.id.iv_list_item_icon);

			viewHolder.textViewItem01 = (TextView) convertView
					.findViewById(R.id.fm_4_title);
			viewHolder.textViewItem02 = (TextView) convertView
					.findViewById(R.id.fm_4_speed);
			viewHolder.bar = (ProgressBar) convertView
					.findViewById(R.id.fm_4_progress_bar);
			viewHolder.bt = (Button) convertView
					.findViewById(R.id.fm_4_btn_down);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			// C.logshow("旧的convertView,position="+position);
		}
		viewHolder.textViewItem01.setText(info.getTitle() + "");
		if (info.getLeng() != null && info.getCout() != null) {
			viewHolder.bar.setMax(Integer.parseInt(info.getLeng()));
			viewHolder.bar.setProgress(Integer.parseInt(info.getCout()) + 0);
			viewHolder.textViewItem02
					.setText((Integer.parseInt(info.getCout()) + 0) + "/"
							+ (Integer.parseInt(info.getLeng()) + 0));
		}

		return convertView;
	}

}
