package com.example.dbdao;

import java.util.ArrayList;

import com.example.base.C;
import com.example.utis.Downlnfos;
import com.example.utis.MusicInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.IInterface;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "mymusicinfo.db";
	private static final String TBL_NAME = "Sdmusicifos";
	private static final String TBL_NAME_DOWNLOAD = "downlifo";
	private static final String TBL_NAME_RECENT = "recent";

	private static final String CREATE_TBL_NAME = " create table "
			+ "Sdmusicifos(_id integer primary key autoincrement,id text,title text, author text,cover text,src text,lrc text,length text,SDimg text,"
			+ "SDsrc text,SDLRC text,playtime integer) ";

	private static final String CREATE_TBL_RECENT = " create table "
			+ "recent(_id integer primary key autoincrement,id text,title text, author text,cover text,src text,lrc text,length text,SDimg text,"
			+ "SDsrc text,SDLRC text,playtime integer) ";

	private static final String CREATE_TBL_NAME2 = " create table "
			+ "downlifo(_id integer primary key autoincrement,id text,title text,src text,cout text,leng text ) ";

	private SQLiteDatabase db;

	public DBHelper(Context c) {
		super(c, DB_NAME, null, 2);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		this.db.execSQL(CREATE_TBL_NAME2);
		this.db.execSQL(CREATE_TBL_NAME);
		this.db.execSQL(CREATE_TBL_RECENT);
	}

	/**
	 * 插入歌曲列表
	 * 
	 * @param info
	 */
	public void insertMusicinfo(MusicInfo info) {
		// SQLiteDatabase db = getWritableDatabase();
		if (db == null) {
			db = getWritableDatabase();
		}
		// 防重复添加
		ArrayList<MusicInfo> queryRecent = queryMusicinfo();
		if (queryRecent != null) {
			for (int i = 0; i < queryRecent.size(); i++) {
				if (queryRecent.get(i).getId().equals(info.getId())) {

					return;
				}
			}

		}
		if (info != null) {
			ContentValues values = new ContentValues();
			values.put("id", info.getId());
			values.put("title", info.getTitle());
			values.put("author", info.getAuthor());
			values.put("cover", info.getCover());
			values.put("src", info.getSrc());
			values.put("lrc", info.getLrc());
			values.put("length", info.getLength());
			values.put("SDimg", info.getSDimg());
			values.put("SDsrc", info.getSDsrc());
			values.put("SDLRC", info.getSDLRC());
			values.put("playtime", info.getPlaytime());
			db.insert(TBL_NAME, null, values);
			C.logshow("增加到了数据库");
			Log.e("a", "增加了数据库" + info.getTitle());
		} else {
			C.logshow("info==null");
		}

	}

	/**
	 * 查询设备列表
	 * 
	 * @return
	 */
	public ArrayList<MusicInfo> queryMusicinfo() {
		// SQLiteDatabase db = getWritableDatabase();
		if (db == null) {
			db = getWritableDatabase();
		}
		C.logshow("查询了数据库");
		Cursor c = db.query(TBL_NAME, null, null, null, null, null, null);

		if (c.getCount() > 0) {
			ArrayList<MusicInfo> infos = new ArrayList<MusicInfo>();
			while (c.moveToNext()) {
				MusicInfo info = new MusicInfo();
				info.setId(c.getString(c.getColumnIndex("id")));
				C.logshow("数据查询得到的数据是:" + c.getString(1));

				info.setTitle(c.getString(c.getColumnIndex("title")));
				info.setAuthor(c.getString(c.getColumnIndex("author")));
				info.setCover(c.getString(c.getColumnIndex("cover")));
				info.setSrc(c.getString(c.getColumnIndex("src")));
				info.setLrc(c.getString(c.getColumnIndex("lrc")));
				info.setLength(c.getString(c.getColumnIndex("length")));
				info.setSDimg(c.getString(c.getColumnIndex("SDimg")));
				info.setSDsrc(c.getString(c.getColumnIndex("SDsrc")));
				info.setSDLRC(c.getString(c.getColumnIndex("SDLRC")));
				info.setPlaytime(c.getInt(c.getColumnIndex("playtime")));

				infos.add(info);
			}
			c.close();
			return infos;
		}
		c.close();
		return null;
	}

	/**
	 * 删除设备列表
	 * 
	 * @param info
	 */
	public void delMusicinfo(MusicInfo info) {
		if (db == null)
			db = getWritableDatabase();

		if (info != null) {
			db.delete(TBL_NAME, "id=?", new String[] { info.getId() });
		}

	}

	/**
	 * 修改设备列表
	 * 
	 * @param info
	 */
	public void updateMusicinfo(MusicInfo info) {

		if (db == null) {
			db = getWritableDatabase();
		}
		if (info != null) {
			ContentValues values = new ContentValues();
			// if(!(info.getId()+"").equals("null")){
			// values.put("id", info.getId());
			// }
			if (!(info.getTitle() + "").equals("null")) {
				values.put("title", info.getTitle());
			}
			if (!(info.getAuthor() + "").equals("null")) {
				values.put("author", info.getAuthor());
			}
			if (!(info.getCover() + "").equals("null")) {
				values.put("cover", info.getCover());
			}
			if (!(info.getSrc() + "").equals("null")) {
				values.put("src", info.getSrc());
			}
			if (!(info.getLrc() + "").equals("null")) {
				values.put("lrc", info.getLrc());
			}

			if (!(info.getLength() + "").equals("null")) {
				values.put("length", info.getLength());
			}

			if (!(info.getSDimg() + "").equals("null")) {
				values.put("SDimg", info.getSDimg());
			}
			if (!(info.getSDsrc() + "").equals("null")) {
				values.put("SDsrc", info.getSDsrc());
			}

			if (!(info.getSDLRC() + "").equals("null")) {
				values.put("SDLRC", info.getSDLRC());
			}
			if (!(info.getSDLRC() + "").equals("null")) {
				values.put("playtime", info.getPlaytime());
			}
			C.logshow("正在修改" + info.getSDLRC());
			db.update(TBL_NAME, values, "id=?", new String[] { info.getId() });
		}

	}

	/**
	 * 插入歌曲下载列表
	 * 
	 * @param info
	 */
	public void insertDowninfo(Downlnfos info) {
		// SQLiteDatabase db = getWritableDatabase();
		if (db == null) {
			db = getWritableDatabase();
		}
		// 防重复添加
		ArrayList<Downlnfos> queryRecent = queryDowninfo();
		if (queryRecent != null) {
			for (int i = 0; i < queryRecent.size(); i++) {
				if (queryRecent.get(i).getId().equals(info.getId())) {
					return;
				}
			}

		}

		if (info != null) {
			ContentValues values = new ContentValues();
			values.put("id", info.getId());
			values.put("title", info.getTitle());
			values.put("src", info.getSrc());
			values.put("cout", info.getCout());
			values.put("leng", info.getLeng());
			db.insert(TBL_NAME_DOWNLOAD, null, values);
			C.logshow("增加到了下载数据库1111111111111111111111111111111111111111111111111");
		} else {
			C.logshow("info==null");
		}

	}

	/**
	 * 查询设备下载列表
	 * 
	 * @return
	 */
	public ArrayList<Downlnfos> queryDowninfo() {
		// SQLiteDatabase db = getWritableDatabase();
		if (db == null) {
			db = getWritableDatabase();
		}
		C.logshow("查询了数据库");
		Cursor c = db.query(TBL_NAME_DOWNLOAD, null, null, null, null, null,
				null);
		if (c.getCount() > 0) {
			ArrayList<Downlnfos> infos = new ArrayList<Downlnfos>();
			while (c.moveToNext()) {
				Downlnfos info = new Downlnfos();
				info.setId(c.getString(c.getColumnIndex("id")));
				info.setTitle(c.getString(c.getColumnIndex("title")));
				info.setSrc(c.getString(c.getColumnIndex("src")));
				info.setCout(c.getString(c.getColumnIndex("cout")));
				info.setLeng(c.getString(c.getColumnIndex("leng")));
				infos.add(info);
			}
			c.close();
			return infos;
		}
		c.close();
		return null;
	}

	/**
	 * 删除设备列表
	 * 
	 * @param info
	 */
	public void delMusicinfo(Downlnfos info) {
		if (db == null)
			db = getWritableDatabase();

		if (info != null) {
			db.delete(TBL_NAME_DOWNLOAD, "id=?", new String[] { info.getId() });
		}
	}

	/**
	 * 修改设备列表
	 * 
	 * @param info
	 */
	public void updateDowninfo(Downlnfos info) {

		if (db == null) {
			db = getWritableDatabase();
		}
		if (info != null) {
			ContentValues values = new ContentValues();
			if (!(info.getTitle() + "").equals("null")) {
				values.put("title", info.getTitle());
			}
			if (!(info.getSrc() + "").equals("null")) {
				values.put("src", info.getSrc());
			}
			if (!(info.getCout() + "").equals("null")) {
				values.put("cout", info.getCout());
			}
			if (!(info.getLeng() + "").equals("null")) {
				values.put("leng", info.getLeng());
			}
			db.update(TBL_NAME_DOWNLOAD, values, "id=?",
					new String[] { info.getId() });
		}
	}

	/**
	 * 插入最近播放列表
	 * 
	 * @param info
	 */
	public void insertRecent(MusicInfo info) {
		// SQLiteDatabase db = getWritableDatabase();
		if (db == null) {
			db = getWritableDatabase();
		}
		// 防重复添加
		ArrayList<MusicInfo> queryRecent = queryRecent();
		if (queryRecent != null) {
			for (int i = 0; i < queryRecent.size(); i++) {
				if (queryRecent.get(i).getId().equals(info.getId())) {
					return;
				}
			}

		}

		if (info != null) {
			ContentValues values = new ContentValues();
			values.put("id", info.getId());
			values.put("title", info.getTitle());
			values.put("author", info.getAuthor());
			values.put("cover", info.getCover());
			values.put("src", info.getSrc());
			values.put("lrc", info.getLrc());
			values.put("length", info.getLength());
			values.put("SDimg", info.getSDimg());
			values.put("SDsrc", info.getSDsrc());
			values.put("SDLRC", info.getSDLRC());
			values.put("playtime", info.getPlaytime());

			db.insert(TBL_NAME_RECENT, null, values);
			C.logshow("增加到了数据库");
		} else {
			C.logshow("info==null");
		}

	}

	/**
	 * 查询最近播放列表
	 * 
	 * @return
	 */
	public ArrayList<MusicInfo> queryRecent() {
		// SQLiteDatabase db = getWritableDatabase();
		if (db == null) {
			db = getWritableDatabase();
		}
		C.logshow("查询了数据库");
		Cursor c = db.query(TBL_NAME_RECENT, null, null, null, null, null,
				"playtime DESC");
		if (c.getCount() > 0) {
			ArrayList<MusicInfo> infos = new ArrayList<MusicInfo>();
			while (c.moveToNext()) {
				MusicInfo info = new MusicInfo();
				info.setId(c.getString(c.getColumnIndex("id")));
				C.logshow("数据查询得到的数据是:" + c.getString(1));

				info.setTitle(c.getString(c.getColumnIndex("title")));
				info.setAuthor(c.getString(c.getColumnIndex("author")));
				info.setCover(c.getString(c.getColumnIndex("cover")));
				info.setSrc(c.getString(c.getColumnIndex("src")));
				info.setLrc(c.getString(c.getColumnIndex("lrc")));
				info.setLength(c.getString(c.getColumnIndex("length")));
				info.setSDimg(c.getString(c.getColumnIndex("SDimg")));
				info.setSDsrc(c.getString(c.getColumnIndex("SDsrc")));
				info.setSDLRC(c.getString(c.getColumnIndex("SDLRC")));
				info.setPlaytime(c.getInt(c.getColumnIndex("playtime")));
				infos.add(info);
			}
			c.close();
			return infos;
		}
		c.close();
		return null;
	}

	/**
	 * 删除最近播放列表
	 * 
	 * @param info
	 */
	public void delRecent(MusicInfo info) {
		if (db == null)
			db = getWritableDatabase();

		if (info != null) {
			db.delete(TBL_NAME_RECENT, "id=?", new String[] { info.getId() });
		}

	}

	/**
	 * 修改最近播放列表
	 * 
	 * @param info
	 */
	public void updateRecent(MusicInfo info) {

		if (db == null) {
			db = getWritableDatabase();
		}
		if (info != null) {
			ContentValues values = new ContentValues();
			// if(!(info.getId()+"").equals("null")){
			// values.put("id", info.getId());
			// }
			if (!(info.getTitle() + "").equals("null")) {
				values.put("title", info.getTitle());
			}
			if (!(info.getAuthor() + "").equals("null")) {
				values.put("author", info.getAuthor());
			}
			if (!(info.getCover() + "").equals("null")) {
				values.put("cover", info.getCover());
			}
			if (!(info.getSrc() + "").equals("null")) {
				values.put("src", info.getSrc());
			}
			if (!(info.getLrc() + "").equals("null")) {
				values.put("lrc", info.getLrc());
			}

			if (!(info.getLength() + "").equals("null")) {
				values.put("length", info.getLength());
			}

			if (!(info.getSDimg() + "").equals("null")) {
				values.put("SDimg", info.getSDimg());
			}
			if (!(info.getSDsrc() + "").equals("null")) {
				values.put("SDsrc", info.getSDsrc());
			}

			if (!(info.getSDLRC() + "").equals("null")) {
				values.put("SDLRC", info.getSDLRC());
			}
			if (!(info.getPlaytime() + "").equals("null")) {
				values.put("playtime", info.getPlaytime());
			}

			C.logshow("正在修改" + info.getSDLRC());
			db.update(TBL_NAME_RECENT, values, "id=?",
					new String[] { info.getId() });
		}

	}

	public void close() {
		if (db != null)
			db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}