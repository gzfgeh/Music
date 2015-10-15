package com.example.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.Header;

import com.example.XMLStructure.SAXPlaylistStructure;
import com.example.base.BaseActivity;
import com.example.base.C;
import com.example.base.Deiviceutis;
import com.example.base.MManager.MessageType;
import com.example.dbdao.DBHelper;
import com.example.fragment.LocalMusicFragment;
import com.example.mysfmusic.R;
import com.example.service.AppConstant;
import com.example.service.PlayerService;
import com.example.utis.MusicInfo;
import com.example.utis.Playlist;

import com.ldw.music.lrc.LyricAdapter;
import com.ldw.music.lrc.LyricLoadHelper;
import com.ldw.music.lrc.LyricSentence;
import com.ldw.music.lrc.LyricLoadHelper.LyricListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.SaxAsyncHttpResponseHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class PlayActivity extends BaseActivity {

	private boolean mIsFavorite = false;
	private LyricAdapter mLyricAdapter;
	private int current = 0;
	private int progres = 0;// 拖动的位置
	private HomeReceiver homeReceiver;
	private Playlist curPlaylist;
	private SeekBar seekBar, pysb1;
	private String lrcPath;
	private String MusicName = "";
	private int INTERVAL = 5;// 歌词每行的间隔

	private int SIZEWORD = 20;// 歌词文字大小
	private int currentTime = 0;
	private int maxTime = 0;
	private ListView mLrcListView;
	private TextView play_01, play_02, play_03;
	private MusicInfo info;
	private ImageButton imgbt_01, imgbt_02;
	private static ImageButton imgbt_03;
	private ImageButton imgbt_04;
	private ImageButton imgbt_05;
	private DBHelper dbHelper;

	// private TextView mLrcEmptyView;

	private LyricLoadHelper mLyricLoadHelper;

	private Handler mHandler = new Handler() {

		@SuppressLint("NewApi")
		public void handleMessage(Message msg) {
			MessageType mt = MessageType.values()[msg.what];
			switch (mt) {
			case MSG_PALY_MUSICLENGTH:
				int a = (Integer) msg.obj;
				Deiviceutis.length = a;
				Log.e("MainActivity", "到这来了MSGMSG_PALY_MUSICLENGTH" + a);
				seekBar.setMax((int) a);
				seekBar.setEnabled(true);
				udptvleng((a * 1000));
				break;
			case MSG_PALY_TRUE:
				MusicInfo ms = (MusicInfo) msg.obj;
				udpInfo(ms);
				Log.e("MainActivity", "到这来了MSG_PALY_TRUE" + ms.toString());
				break;
			case MSG_PALY_POSITIO:
				int posi = (Integer) msg.obj;
				seekBar.setProgress((int) posi);
				udptvcount(posi * 1000);

				currentTime = posi * 1000;
				maxTime = Deiviceutis.length * 1000;
				info = Deiviceutis.info;
				C.logshow("currentTime:" + currentTime + "maxTime:" + maxTime);
				if (!MusicName.equals(info.getTitle())) {

					C.logshow(info.toString());
					mLyricAdapter = new LyricAdapter(PlayActivity.this);
					loadLyric(info);
					mLyricLoadHelper.setLyricListener(mLyricListener);
					mLrcListView.setAdapter(mLyricAdapter);
					// mLrcListView.setEmptyView(mLrcEmptyView);
					mLrcListView.startAnimation(AnimationUtils.loadAnimation(
							PlayActivity.this, android.R.anim.fade_in));
					mLrcListView.smoothScrollToPositionFromTop(1,
							mLrcListView.getHeight() / 2, 500);
					seekBar.setEnabled(true);
					MusicName = info.getTitle();
					if (maxTime != 0) {
						play_03.setText(C.getTimeFromInt(maxTime) + "");
					}
				}
				if (currentTime != 0) {
					play_02.setText(C.getTimeFromInt(currentTime) + "");
				}

				if (Deiviceutis.mPlaying) {
					imgbt_03.setBackgroundResource(R.drawable.tabd);
				} else {
					imgbt_03.setBackgroundResource(R.drawable.tabc);
				}
				mLyricLoadHelper.notifyTime(currentTime);

				break;
			case MSG_PALY_PAUSE:
				imgbt_03.setBackgroundResource(R.drawable.tabd);
				break;
			case MSG_PALY_GOON:
				imgbt_03.setBackgroundResource(R.drawable.tabc);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_play);

		// DisplayMetrics dm = new DisplayMetrics();
		// 取得窗口属性
		// getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 窗口高度
		// int screenHeight = dm.heightPixels;
		// lyricView.setVIEWHEIGHT(screenHeight - 100);
		// lyricView.SetTextSize();
		// lyricView.setSIZEWORD(SIZEWORD);
		initBtn();

		// 歌词秀设置---------------------------------------------------------------
	}

	private void initBtn() {

		// mLrcEmptyView = (TextView) findViewById(R.id.lyric_empty);
		lrcPath = C.LRCSRC;
		C.isoff = true;
		mLyricLoadHelper = new LyricLoadHelper();
		mLyricLoadHelper.setLyricListener(mLyricListener);
		mLrcListView = (ListView) findViewById(R.id.lyricshow);

		// imgbt_01 = (ImageButton) findViewById(R.id.play_imgbt_01);
		layout = (FrameLayout) findViewById(R.id.player_frame_content);

		imgbt_02 = (ImageButton) findViewById(R.id.play_imgbt_02);
		imgbt_02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				imgbt_03.setBackgroundResource(R.drawable.tabc);
				if (LocalMusicFragment.queryMusicinfo != null) {
					if (C.isdevies) {

						LocalMusicFragment.Selected = LocalMusicFragment.Selected - 1;
						if (LocalMusicFragment.Selected <= 0) {
							LocalMusicFragment.Selected = 0;
						}
						if (!(LocalMusicFragment.queryMusicinfo.get(
								LocalMusicFragment.Selected).getSDLRC() + "")
								.equals("null")) {
							String path = "http://"
									+ LocalMusicFragment.getLocalIpAddress()
									+ ":1326/"
									+ LocalMusicFragment.queryMusicinfo.get(
											LocalMusicFragment.Selected)
											.getId() + ".mp3";
							// String
							// path="http://"+getLocalIpAddress()+":8089/啊啊啊.mp3";
							Log.e("a", "path:" + path);
						//	C.dao.stop();
							C.dao.paly(path, LocalMusicFragment.queryMusicinfo
									.get(LocalMusicFragment.Selected));

						} else {
						//	C.dao.stop();
							C.dao.paly(LocalMusicFragment.queryMusicinfo
									.get(LocalMusicFragment.Selected));
						}
					} else {
						Intent intent = new Intent();
						intent.setAction(PlayerService.CTL_ACTION);
						intent.putExtra("MSG",
								AppConstant.PlayerMsg.PRIVIOUS_MSG);

						sendBroadcast(intent);
					}
				} else {
					Toast.makeText(getApplicationContext(), "本地木有歌曲無法上一曲",
							Toast.LENGTH_SHORT).show();

				}
			}
		});

		imgbt_03 = (ImageButton) findViewById(R.id.play_imgbt_03);

		imgbt_03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (C.isdevies) {
					if (Deiviceutis.mPlaying) {
						C.dao.pause();
					} else {
						C.dao.goon();
					}
				} else {
					if (C.isMyPlay) {
						imgbt_03.setBackgroundResource(R.drawable.tabc);
						Intent intent = new Intent();
						intent.setAction(PlayerService.CTL_ACTION);
						intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
						sendBroadcast(intent);
					} else {
						imgbt_03.setBackgroundResource(R.drawable.tabd);
						Intent intent = new Intent();
						intent.setAction(PlayerService.CTL_ACTION);
						intent.putExtra("MSG",
								AppConstant.PlayerMsg.CONTINUE_MSG);
						sendBroadcast(intent);
					}
				}

			}
		});

		imgbt_04 = (ImageButton) findViewById(R.id.play_imgbt_04);
		imgbt_04.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				imgbt_03.setBackgroundResource(R.drawable.tabc);
				if (LocalMusicFragment.queryMusicinfo != null) {
					if (C.isdevies) {
						LocalMusicFragment.Selected = LocalMusicFragment.Selected + 1;
						if (LocalMusicFragment.Selected >= LocalMusicFragment.queryMusicinfo
								.size()) {
							LocalMusicFragment.Selected = 0;
						}

						if (!(LocalMusicFragment.queryMusicinfo.get(
								LocalMusicFragment.Selected).getSDLRC() + "")
								.equals("null")) {
							String path = "http://"
									+ LocalMusicFragment.getLocalIpAddress()
									+ ":1326/"
									+ LocalMusicFragment.queryMusicinfo.get(
											LocalMusicFragment.Selected)
											.getId() + ".mp3";
							// String
							// path="http://"+getLocalIpAddress()+":8089/啊啊啊.mp3";
							Log.e("a", "path:" + path);
						//	C.dao.stop();
							C.dao.paly(path, LocalMusicFragment.queryMusicinfo
									.get(LocalMusicFragment.Selected));

						} else {
						//	C.dao.stop();
							C.dao.paly(LocalMusicFragment.queryMusicinfo
									.get(LocalMusicFragment.Selected));
						}
					} else {
						Intent intent = new Intent();
						intent.setAction(PlayerService.CTL_ACTION);
						intent.putExtra("MSG", AppConstant.PlayerMsg.NEXT_MSG);
						sendBroadcast(intent);
					}
				} else {
					Toast.makeText(getApplicationContext(), "本地木有歌曲無法下一曲",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		// imgbt_05 = (ImageButton) findViewById(R.id.play_imgbt_05);

		play_01 = (TextView) findViewById(R.id.lyric_empty);

		play_01.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		play_01.getPaint().setAntiAlias(true);// 抗锯齿
		play_02 = (TextView) findViewById(R.id.paly_xml_01);
		play_03 = (TextView) findViewById(R.id.paly_xml_02);
		play_01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (info != null) {
					if (!info.getId().equals("")) {
						doTaskGetSongPlaylist(info.getId());
					} else {
						Toast.makeText(PlayActivity.this, "木有找到歌词ID",
								Toast.LENGTH_SHORT).show();
					}

				}

			}
		});
		pysb1 = (SeekBar) findViewById(R.id.py_sb1);
		pysb1.setMax(100);
		pysb1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int c = 0;

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				C.dao.setVoice(c);

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if (fromUser) {
					c = progress;

				}
			}
		});

		seekBar = (SeekBar) findViewById(R.id.seekbarmusic);
		seekBar.setEnabled(false);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (C.isdevies) {
					String targetPosition = Deiviceutis.secToTime(progres);
					C.dao.seek(targetPosition);
				} else {
					Intent intent = new Intent();
					intent.setAction(PlayerService.CTL_ACTION);
					intent.putExtra("MSG",
							AppConstant.PlayerMsg.PROGRESS_CHANGE);
					intent.putExtra("progress", progres);
					sendBroadcast(intent);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					progres = progress;

				}
			}
		});

	}

	// 自定义的BroadcastReceiver，负责监听从Service传回来的广播
	public class HomeReceiver extends BroadcastReceiver {

		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(PlayerService.MUSIC_CURRENT)) {
				currentTime = intent.getIntExtra("currentTime", 0);
				maxTime = intent.getIntExtra("maxTime", 0);
				info = (MusicInfo) intent.getSerializableExtra("info");
				C.logshow("currentTime:" + currentTime + "maxTime:" + maxTime);
				if (!MusicName.equals(info.getTitle())) {

					C.logshow(info.toString());
					mLyricAdapter = new LyricAdapter(PlayActivity.this);
					loadLyric(info);
					mLyricLoadHelper.setLyricListener(mLyricListener);
					mLrcListView.setAdapter(mLyricAdapter);
					// mLrcListView.setEmptyView(mLrcEmptyView);
					mLrcListView.startAnimation(AnimationUtils.loadAnimation(
							PlayActivity.this, android.R.anim.fade_in));
					mLrcListView.smoothScrollToPositionFromTop(1,
							mLrcListView.getHeight() / 2, 500);
					seekBar.setMax((int) maxTime);
					seekBar.setEnabled(true);
					MusicName = info.getTitle();
					if (maxTime != 0) {
						play_03.setText(C.getTimeFromInt(maxTime) + "");
					}

				}
				if (currentTime != 0) {
					play_02.setText(C.getTimeFromInt(currentTime) + "");
				}

				if (C.isMyPlay) {
					imgbt_03.setBackgroundResource(R.drawable.tabd);
				} else {
					imgbt_03.setBackgroundResource(R.drawable.tabc);
				}
				seekBar.setProgress((int) currentTime);
				mLyricLoadHelper.notifyTime(currentTime);

			}
		}

	}

	public void setReceiver() {
		IntentFilter filter = new IntentFilter();
		homeReceiver = new HomeReceiver();
		// 指定BroadcastReceiver监听的Action
		filter.addAction(PlayerService.UPDATE_ACTION);
		filter.addAction(PlayerService.MUSIC_CURRENT);
		filter.addAction(PlayerService.MUSIC_DURATION);
		// 注册BroadcastReceiver
		registerReceiver(homeReceiver, filter);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("a", "onResume");
		dbHelper = new DBHelper(PlayActivity.this);
		if (C.isdevies) {
			C.dao.setHandler(mHandler);
		} else {
			C.dao.setHandler(new Handler());
			setReceiver();
		}

		// mLrcListView.setEmptyView(mLrcEmptyView);
		// mLrcListView.startAnimation(AnimationUtils.loadAnimation(PlayActivity.this,
		// android.R.anim.fade_in));
		// lyricView = (LyricView) findViewById(R.id.mylrc);
		if (C.isdevies) {
			if (Deiviceutis.mPlaying) {
				imgbt_03.setBackgroundResource(R.drawable.tabd);
				Drawable drawable = new BitmapDrawable(C.bitmap);
				layout.setBackgroundDrawable(drawable);
				udptvleng((Deiviceutis.length * 1000));
				seekBar.setMax((int) Deiviceutis.length);
				seekBar.setEnabled(true);
			}
		} else {
			if (C.isMyPlay) {
				imgbt_03.setBackgroundResource(R.drawable.tabd);
				Drawable drawable = new BitmapDrawable(C.bitmap);
				layout.setBackgroundDrawable(drawable);

			}
		}

		// udpimg();
		// SerchLrc();
	}

	public static void udpimg() {
		if (C.isMyPlay) {
			imgbt_03.setBackgroundResource(R.drawable.tabd);
		}
		Drawable drawable = new BitmapDrawable(C.bitmap);
		layout.setBackgroundDrawable(drawable);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("a", "onPause");
		// unregisterReceiver(homeReceiver);
		if (homeReceiver != null) {
			unregisterReceiver(homeReceiver);
		}
		onDestroy();
	}

	private LyricListener mLyricListener = new LyricListener() {

		@Override
		public void onLyricLoaded(List<LyricSentence> lyricSentences, int index) {
			// Log.i(TAG, "onLyricLoaded");
			if (lyricSentences != null) {
				C.logshow("歌词句子数目=" + lyricSentences.size() + ",当前句子索引="
						+ index + "");

				mLyricAdapter.setLyric(lyricSentences);
				mLyricAdapter.setCurrentSentenceIndex(index);
				mLyricAdapter.notifyDataSetChanged();
				play_01.setVisibility(View.GONE);
				// mLrcListView.setAdapter(mLyricAdapter);
				// 本方法执行时，lyricshow的控件还没有加载完成，所以延迟下再执行相关命令
				// mHandler.sendMessageDelayed(
				// Message.obtain(null, MSG_SET_LYRIC_INDEX, index, 0),
				// 100);
			}
		}

		@SuppressLint("NewApi")
		@Override
		public void onLyricSentenceChanged(int indexOfCurSentence) {
			C.logshow("onLyricSentenceChanged--->当前句子索引=" + indexOfCurSentence);
			mLyricAdapter.setCurrentSentenceIndex(indexOfCurSentence);
			mLyricAdapter.notifyDataSetChanged();
			mLrcListView.smoothScrollToPositionFromTop(indexOfCurSentence,
					mLrcListView.getHeight() / 2, 500);
		}
	};
	private static FrameLayout layout;

	/**
	 * 读取本地歌词文件
	 */
	public void loadLyric(MusicInfo playingSong) {
		if (playingSong == null) {
			return;
		}
		MusicName = playingSong.getTitle();
		// 取得歌曲同目录下的歌词文件绝对路径
		String lyricFilePath = lrcPath + playingSong.getId() + ".lrc";
		File lyricfile = new File(lyricFilePath);
		C.logshow("来了下歌词");
		if (lyricfile.exists()) {
			// 本地有歌词，直接读取
			// Log.i(TAG, "loadLyric()--->本地有歌词，直接读取");
			mLyricLoadHelper.loadLyric(lyricFilePath);
		} else {
			play_01.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 下载歌词到本地 /sdcard/kenge/
	 * 
	 * @param id
	 *            歌曲id
	 */
	private void doTaskdowlrc(String url) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new BinaryHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				if (arg0 == 200) {

					saveBitmap(info.getId() + ".lrc", arg2);
					MusicName = "";
					loadLyric(info);
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		C.isoff = false;
	}

	/**
	 * 保存方法
	 * 
	 * @return
	 */
	public Bitmap saveBitmap(String picName, byte[] buffer) {

		File f = new File(C.LRCSRC, picName);
		if (!f.exists()) {
			try {
				FileOutputStream out = new FileOutputStream(f);
				out.write(buffer);
				out.flush();

				info.setSDLRC(C.LRCSRC + picName);
				dbHelper.updateMusicinfo(info);

				out.close();
				Log.e("a", "已经保存");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;

	}

	/**
	 * 通过歌曲id获取该歌曲详细信息
	 * 
	 * @param id
	 *            歌曲id
	 */
	private void doTaskGetSongPlaylist(String id) {
		Log.e("a", "来了下载歌词");
		final String url = "http://www.xiami.com/song/playlist/id/" + id;
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new SaxAsyncHttpResponseHandler<SAXPlaylistStructure>(
				new SAXPlaylistStructure()) {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					SAXPlaylistStructure saxTreeStructure) {
				if (statusCode == 200) {
					if (saxTreeStructure != null) {
						debugHandler(saxTreeStructure);
						C.logshow("saxTreeStructure:" + saxTreeStructure);
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					SAXPlaylistStructure saxTreeStructure) {
			}

			private void debugHandler(SAXPlaylistStructure saxPlaylistStructure) {
				curPlaylist = new Playlist();
				curPlaylist = savePlaylist(saxPlaylistStructure);
				String lrc = curPlaylist.getLyric() + "";
				if (!lrc.equals("")) {
					if (lrc.substring(lrc.length() - 3, lrc.length()).equals(
							"lrc")) {
						doTaskdowlrc(curPlaylist.getLyric());
						info.setLrc(lrc);
					} else {
						Toast.makeText(PlayActivity.this, "木有找到歌词",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(PlayActivity.this, "木有找到歌词",
							Toast.LENGTH_SHORT).show();
				}

			}

		});
	}

	/**
	 * 保存歌曲详细信息到实例中。
	 * 
	 * @param saxPlaylistStructure
	 */
	protected Playlist savePlaylist(SAXPlaylistStructure saxPlaylistStructure) {
		Playlist curPlaylist = new Playlist();
		for (Playlist t : saxPlaylistStructure.getPlaylists()) {
			curPlaylist.setTitle(t.getTitle());
			curPlaylist.setSong_id(t.getSong_id());
			curPlaylist.setAlbum_id(t.getAlbum_id());
			curPlaylist.setAlbum_name(t.getAlbum_name());
			curPlaylist.setObject_id(t.getObject_id());
			curPlaylist.setObject_name(t.getObject_name());
			curPlaylist.setInsert_type(t.getInsert_type());
			curPlaylist.setBackground(t.getBackground());
			curPlaylist.setGrade(t.getGrade());
			curPlaylist.setArtist(t.getArtist());
			curPlaylist.setArtist_url(t.getArtist_url());
			curPlaylist.setLastSongId(t.getLastSongId());
			curPlaylist.setLyric(t.getLyric());
			curPlaylist.setLyric_url(t.getLyric_url());
			curPlaylist.setPic(t.getPic());
			curPlaylist.setAlbum_pic(t.getAlbum_pic());
			curPlaylist.setLength(t.getLength());
			curPlaylist.setTryhq(t.getTryhq());
			curPlaylist.setArtist_id(t.getArtist_id());
			curPlaylist.setMusic_type(t.getMusic_type());
			curPlaylist.setLastSongId(t.getLastSongId());
			curPlaylist.setType(t.getType());
			curPlaylist.setType_id(t.getType_id());
			curPlaylist.setVip(t.getVip());
			curPlaylist.setVip_role(t.getVip_role());
			curPlaylist.setHqset(t.getHqset());
			// Log.e(LOG_TAG, t.getTitle());
			// Log.e(LOG_TAG, t.getSong_id());
			// Log.e(LOG_TAG, t.getAlbum_name());
			// Log.e(LOG_TAG, t.getLyric());
			// Log.e(LOG_TAG, t.getLyric_url());
		}
		return curPlaylist;
	}

	// g
	private void udptvleng(int length) {
		play_03.setText(C.getTimeFromInt(length) + "");
	}

	private void udptvcount(int length) {
		play_02.setText(C.getTimeFromInt(length) + "");
	}

	private void udpInfo(MusicInfo info) {
		Deiviceutis.info = info;
	}
}
