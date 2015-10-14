package com.example.utis;

import java.util.List;

public class ListChannel {
	private String r;
	private String is_show_quick_start;
	private List<Channel> song;

	public String getR() {
		return r;
	}

	public void setR(String r) {
		this.r = r;
	}

	public String getIs_show_quick_start() {
		return is_show_quick_start;
	}

	public void setIs_show_quick_start(String is_show_quick_start) {
		this.is_show_quick_start = is_show_quick_start;
	}

	public List<Channel> getSong() {
		return song;
	}

	public void setSong(List<Channel> song) {
		this.song = song;
	}

	public ListChannel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ListChannel(String r, String is_show_quick_start, List<Channel> song) {
		super();
		this.r = r;
		this.is_show_quick_start = is_show_quick_start;
		this.song = song;
	}

	@Override
	public String toString() {
		return "ListChannel [r=" + r + ", is_show_quick_start="
				+ is_show_quick_start + ", song=" + song + "]";
	}

}
