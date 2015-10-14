package com.example.utis;

import java.util.List;

public class ListChannels {
	private List<Channels> channels;

	public ListChannels() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ListChannels(List<Channels> channels) {
		super();
		this.channels = channels;
	}

	public List<Channels> getChannels() {
		return channels;
	}

	public void setChannels(List<Channels> channels) {
		this.channels = channels;
	}

	@Override
	public String toString() {
		return "ListChannels [channels=" + channels + "]";
	}

}
