package com.example.utis;

public class Channel {
	private String album;
	private String picture;
	private String ssid;
	private String artist;
	private String url;
	private String company;
	private String title;
	private String rating_avg;
	private String length;
	private String subtype;
	private String public_time;
	private String songlists_count;
	private String sid;
	private String aid;
	private String sha256;
	private String kbps;
	private String albumtitle;
	private String like;

	public Channel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Channel(String album, String picture, String ssid, String artist,
			String url, String company, String title, String rating_avg,
			String length, String subtype, String public_time,
			String songlists_count, String sid, String aid, String sha256,
			String kbps, String albumtitle, String like) {
		super();
		this.album = album;
		this.picture = picture;
		this.ssid = ssid;
		this.artist = artist;
		this.url = url;
		this.company = company;
		this.title = title;
		this.rating_avg = rating_avg;
		this.length = length;
		this.subtype = subtype;
		this.public_time = public_time;
		this.songlists_count = songlists_count;
		this.sid = sid;
		this.aid = aid;
		this.sha256 = sha256;
		this.kbps = kbps;
		this.albumtitle = albumtitle;
		this.like = like;
	}

	@Override
	public String toString() {
		return "Channel [album=" + album + ", picture=" + picture + ", ssid="
				+ ssid + ", artist=" + artist + ", url=" + url + ", company="
				+ company + ", title=" + title + ", rating_avg=" + rating_avg
				+ ", length=" + length + ", subtype=" + subtype
				+ ", public_time=" + public_time + ", songlists_count="
				+ songlists_count + ", sid=" + sid + ", aid=" + aid
				+ ", sha256=" + sha256 + ", kbps=" + kbps + ", albumtitle="
				+ albumtitle + ", like=" + like + "]";
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRating_avg() {
		return rating_avg;
	}

	public void setRating_avg(String rating_avg) {
		this.rating_avg = rating_avg;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public String getPublic_time() {
		return public_time;
	}

	public void setPublic_time(String public_time) {
		this.public_time = public_time;
	}

	public String getSonglists_count() {
		return songlists_count;
	}

	public void setSonglists_count(String songlists_count) {
		this.songlists_count = songlists_count;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getSha256() {
		return sha256;
	}

	public void setSha256(String sha256) {
		this.sha256 = sha256;
	}

	public String getKbps() {
		return kbps;
	}

	public void setKbps(String kbps) {
		this.kbps = kbps;
	}

	public String getAlbumtitle() {
		return albumtitle;
	}

	public void setAlbumtitle(String albumtitle) {
		this.albumtitle = albumtitle;
	}

	public String getLike() {
		return like;
	}

	public void setLike(String like) {
		this.like = like;
	}

}
