package com.example.utis;

public class Playlist {
	public final static String TITLE = "title";
	public final static String SONG_ID = "song_id";
	public final static String ALBUM_ID = "album_id";
	public final static String ALBUM_NAME = "album_name";
	public final static String OBJECT_ID = "object_id";
	public final static String OBJECT_NAME = "object_name";
	public final static String INSET_TYPE = "insert_type";
	public final static String BACKGROUND = "background";
	public final static String GRADE = "grade";
	public final static String ARTIST = "artist";
	public final static String ARTIST_URL = "artist_url";
	public final static String LOCATION = "location";
	public final static String LYRIC = "lyric";
	public final static String LYRIC_URL = "lyric_url";
	public final static String PIC = "pic";
	public final static String ALBUM_PIC = "album_pic";
	public final static String LENGTH = "length";
	public final static String TRYHQ = "tryhq";
	public final static String ARTIST_ID = "artist_id";
	public final static String MUSIC_TYPE = "music_type";
	public final static String LASTSONGID = "lastSongId";
	public final static String TYPE = "type";
	public final static String TYPE_ID = "type_id";
	public final static String VIP = "vip";
	public final static String VIP_ROLE = "vip_role";
	public final static String HQSET = "hqset";

	public final static String playlist[] = { Playlist.TITLE, Playlist.SONG_ID,
			Playlist.ALBUM_ID, Playlist.ALBUM_NAME, Playlist.OBJECT_ID,
			Playlist.OBJECT_NAME, Playlist.INSET_TYPE, Playlist.BACKGROUND,
			Playlist.GRADE, Playlist.ARTIST, Playlist.ARTIST_URL,
			Playlist.LASTSONGID, Playlist.LYRIC, Playlist.LYRIC_URL,
			Playlist.PIC, Playlist.ALBUM_PIC, Playlist.LENGTH, Playlist.TRYHQ,
			Playlist.ARTIST_ID, Playlist.MUSIC_TYPE, Playlist.LASTSONGID,
			Playlist.TYPE, Playlist.TYPE_ID, Playlist.VIP, Playlist.VIP_ROLE,
			Playlist.HQSET };

	private String title;
	private String song_id;
	private String album_id;
	private String album_name;
	private String object_id;
	private String object_name;
	private String insert_type;
	private String background;
	private String grade;
	private String artist;
	private String artist_url;
	private String location;
	private String lyric;
	private String lyric_url;
	private String pic;
	private String album_pic;
	private String length;
	private String tryhq;
	private String artist_id;
	private String music_type;
	private String lastSongId;
	private String type;
	private String type_id;
	private String vip;
	private String vip_role;
	private String hqset;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSong_id() {
		return song_id;
	}

	public void setSong_id(String song_id) {
		this.song_id = song_id;
	}

	public String getAlbum_id() {
		return album_id;
	}

	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}

	public String getAlbum_name() {
		return album_name;
	}

	public void setAlbum_name(String album_name) {
		this.album_name = album_name;
	}

	public String getObject_id() {
		return object_id;
	}

	public void setObject_id(String object_id) {
		this.object_id = object_id;
	}

	public String getObject_name() {
		return object_name;
	}

	public void setObject_name(String object_name) {
		this.object_name = object_name;
	}

	public String getInsert_type() {
		return insert_type;
	}

	public void setInsert_type(String insert_type) {
		this.insert_type = insert_type;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getArtist_url() {
		return artist_url;
	}

	public void setArtist_url(String artist_url) {
		this.artist_url = artist_url;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLyric() {
		return lyric;
	}

	public void setLyric(String lyric) {
		this.lyric = lyric;
	}

	public String getLyric_url() {
		return lyric_url;
	}

	public void setLyric_url(String lyric_url) {
		this.lyric_url = lyric_url;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getAlbum_pic() {
		return album_pic;
	}

	public void setAlbum_pic(String album_pic) {
		this.album_pic = album_pic;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getTryhq() {
		return tryhq;
	}

	public void setTryhq(String tryhq) {
		this.tryhq = tryhq;
	}

	public String getArtist_id() {
		return artist_id;
	}

	public void setArtist_id(String artist_id) {
		this.artist_id = artist_id;
	}

	public String getMusic_type() {
		return music_type;
	}

	public void setMusic_type(String music_type) {
		this.music_type = music_type;
	}

	public String getLastSongId() {
		return lastSongId;
	}

	public void setLastSongId(String lastSongId) {
		this.lastSongId = lastSongId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	public String getVip_role() {
		return vip_role;
	}

	public void setVip_role(String vip_role) {
		this.vip_role = vip_role;
	}

	public String getHqset() {
		return hqset;
	}

	public void setHqset(String hqset) {
		this.hqset = hqset;
	}

}
