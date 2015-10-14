package com.example.XMLStructure;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.example.utis.Playlist;

public class SAXPlaylistStructure extends DefaultHandler {
	private static final String LOG_TAG = "SAXPlaylistStructure";
	private List<Playlist> playlists = null;
	private Playlist currentPlaylist;
	private String tagName = null;// 当前解析的元素标签

	public List<Playlist> getPlaylists() {
		return playlists;
	}

	// 接收文档开始的通知。当遇到文档的开头的时候，调用这个方法，可以在其中做一些预处理的工作。
	@Override
	public void startDocument() throws SAXException {
		playlists = new ArrayList<Playlist>();
	}

	// 接收元素开始的通知。当读到一个开始标签的时候，会触发这个方法。其中namespaceURI表示元素的命名空间；
	// localName表示元素的本地名称（不带前缀）；qName表示元素的限定名（带前缀）；atts 表示元素的属性集合
	@Override
	public void startElement(String namespaceURI, String localName,
			String rawName, Attributes atts) throws SAXException {
		// Log.e(LOG_TAG, "Start Element: " + rawName);
		if (localName.equals("playlist")) {
			currentPlaylist = new Playlist();
			currentPlaylist.setTitle(atts.getValue(Playlist.TITLE));
			currentPlaylist.setSong_id(atts.getValue(Playlist.SONG_ID));
			currentPlaylist.setTitle(atts.getValue(Playlist.ALBUM_ID));
			currentPlaylist.setAlbum_name(atts.getValue(Playlist.ALBUM_NAME));
			currentPlaylist.setObject_id(atts.getValue(Playlist.OBJECT_ID));
			currentPlaylist.setObject_name(atts.getValue(Playlist.OBJECT_NAME));
			currentPlaylist.setInsert_type(atts.getValue(Playlist.INSET_TYPE));
			currentPlaylist.setBackground(atts.getValue(Playlist.BACKGROUND));
			currentPlaylist.setGrade(atts.getValue(Playlist.GRADE));
			currentPlaylist.setArtist(atts.getValue(Playlist.ARTIST));
			currentPlaylist.setArtist_url(atts.getValue(Playlist.ARTIST_URL));
			currentPlaylist.setLocation(atts.getValue(Playlist.LOCATION));
			currentPlaylist.setLyric(atts.getValue(Playlist.LYRIC));
			currentPlaylist.setLyric_url(atts.getValue(Playlist.LYRIC_URL));
			currentPlaylist.setPic(atts.getValue(Playlist.PIC));
			currentPlaylist.setAlbum_pic(atts.getValue(Playlist.ALBUM_PIC));
			currentPlaylist.setLength(atts.getValue(Playlist.LENGTH));
			currentPlaylist.setTryhq(atts.getValue(Playlist.TRYHQ));
			currentPlaylist.setArtist_id(atts.getValue(Playlist.ARTIST_ID));
			currentPlaylist.setMusic_type(atts.getValue(Playlist.MUSIC_TYPE));
			currentPlaylist.setLastSongId(atts.getValue(Playlist.LASTSONGID));
			currentPlaylist.setType(atts.getValue(Playlist.TYPE));
			currentPlaylist.setType_id(atts.getValue(Playlist.TYPE_ID));
			currentPlaylist.setVip(atts.getValue(Playlist.VIP));
			currentPlaylist.setVip_role(atts.getValue(Playlist.VIP_ROLE));
			currentPlaylist.setHqset(atts.getValue(Playlist.HQSET));

		}
		this.tagName = localName;
	}

	// 接收文档的结尾的通知。在遇到结束标签的时候，调用这个方法。其中，uri表示元素的命名空间；
	// localName表示元素的本地名称（不带前缀）；name表示元素的限定名（带前缀）
	@Override
	public void endElement(String namespaceURI, String localName, String rawName) {
		// Log.e(LOG_TAG, "End Element  : " + rawName);
		if (localName.equals("playlist")) {
			playlists.add(currentPlaylist);
			currentPlaylist = null;
		}

		this.tagName = null;
	}

	// 接收字符数据的通知。该方法用来处理在XML文件中读到的内容，第一个参数用于存放文件的内容，
	// 后面两个参数是读到的字符串在这个数组中的起始位置和长度，使用new String(ch,start,length)就可以获取内容。
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (tagName != null) {
			String data = new String(ch, start, length);
			String tag[] = Playlist.playlist;

			for (int i = 0; i < tag.length; i++) {
				if (tagName == tag[i]) {
					switch (i) {
					case 0:
						this.currentPlaylist.setTitle(data);
						break;
					case 1:
						this.currentPlaylist.setSong_id(data);
						break;
					case 2:
						this.currentPlaylist.setAlbum_id(data);
						break;
					case 3:
						this.currentPlaylist.setAlbum_name(data);
						break;
					case 4:
						this.currentPlaylist.setObject_id(data);
						break;
					case 5:
						this.currentPlaylist.setObject_name(data);
						break;
					case 6:
						this.currentPlaylist.setInsert_type(data);
						break;
					case 7:
						this.currentPlaylist.setBackground(data);
						break;
					case 8:
						this.currentPlaylist.setGrade(data);
						break;
					case 9:
						this.currentPlaylist.setArtist(data);
						break;
					case 10:
						this.currentPlaylist.setArtist_url(data);
						break;
					case 11:
						this.currentPlaylist.setLastSongId(data);
						break;
					case 12:
						this.currentPlaylist.setLyric(data);
						break;
					case 13:
						this.currentPlaylist.setLyric_url(data);
						break;
					case 14:
						this.currentPlaylist.setPic(data);
						break;
					case 15:
						this.currentPlaylist.setAlbum_pic(data);
						break;
					case 16:
						this.currentPlaylist.setLength(data);
						break;
					case 17:
						this.currentPlaylist.setTryhq(data);
						break;
					case 18:
						this.currentPlaylist.setArtist_id(data);
						break;
					case 19:
						this.currentPlaylist.setMusic_type(data);
						break;
					case 20:
						this.currentPlaylist.setLastSongId(data);
						break;
					case 21:
						this.currentPlaylist.setType(data);
						break;
					case 22:
						this.currentPlaylist.setType_id(data);
						break;
					case 23:
						this.currentPlaylist.setVip(data);
						break;
					case 24:
						this.currentPlaylist.setVip_role(data);
						break;
					case 25:
						this.currentPlaylist.setHqset(data);
						break;
					}
				}
			}
		}

	}
}
