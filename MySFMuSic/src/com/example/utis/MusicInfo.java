package com.example.utis;

import java.io.Serializable;

public class MusicInfo implements Serializable {
	private String id;// 歌曲ID
	private String title;// 歌曲名字
	private String author;// 歌曲作者
	private String cover;// 歌曲图片
	private String src;// 歌曲地址
	private String lrc;// 歌曲歌词
	private String length;// 歌曲长度
	private String SDimg;// 歌曲本地图片
	private String SDsrc;// 歌曲本地路径
	private String SDLRC;// 歌曲本地地址
	private int playtime;// 播放时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getLrc() {
		return lrc;
	}

	public void setLrc(String lrc) {
		this.lrc = lrc;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getSDimg() {
		return SDimg;
	}

	public void setSDimg(String sDimg) {
		SDimg = sDimg;
	}

	public String getSDsrc() {
		return SDsrc;
	}

	public void setSDsrc(String sDsrc) {
		SDsrc = sDsrc;
	}

	public String getSDLRC() {
		return SDLRC;
	}

	public void setSDLRC(String sDLRC) {
		SDLRC = sDLRC;
	}

	public int getPlaytime() {
		return playtime;
	}

	public void setPlaytime(int playtime) {
		this.playtime = playtime;
	}

	public MusicInfo(String id, String title, String author, String cover,
			String src, String lrc, String length, String sDimg, String sDsrc,
			String sDLRC, int playtime) {
		super();
		this.id = id;
		this.title = title;
		this.author = author;
		this.cover = cover;
		this.src = src;
		this.lrc = lrc;
		this.length = length;
		SDimg = sDimg;
		SDsrc = sDsrc;
		SDLRC = sDLRC;
		this.playtime = playtime;
	}

	@Override
	public String toString() {
		return "MusicInfo [id=" + id + ", title=" + title + ", author="
				+ author + ", cover=" + cover + ", src=" + src + ", lrc=" + lrc
				+ ", length=" + length + ", SDimg=" + SDimg + ", SDsrc="
				+ SDsrc + ", SDLRC=" + SDLRC + ", playtime=" + playtime + "]";
	}

	public MusicInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

}
