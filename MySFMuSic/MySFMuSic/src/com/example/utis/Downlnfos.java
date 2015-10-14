package com.example.utis;

public class Downlnfos {
	private String id;// 歌曲id
	private String title;// 歌曲名字
	private String src;// 歌曲下载路径
	private String cout;// 歌曲下载长度
	private String leng;// 歌曲长度

	@Override
	public String toString() {
		return "Downlnfos [id=" + id + ", title=" + title + ", src=" + src
				+ ", cout=" + cout + ", leng=" + leng + "]";
	}

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

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getCout() {
		return cout;
	}

	public void setCout(String cout) {
		this.cout = cout;
	}

	public String getLeng() {
		return leng;
	}

	public void setLeng(String leng) {
		this.leng = leng;
	}

	public Downlnfos(String id, String title, String src, String cout,
			String leng) {
		super();
		this.id = id;
		this.title = title;
		this.src = src;
		this.cout = cout;
		this.leng = leng;
	}

	public Downlnfos() {
		super();
		// TODO Auto-generated constructor stub
	}

}
