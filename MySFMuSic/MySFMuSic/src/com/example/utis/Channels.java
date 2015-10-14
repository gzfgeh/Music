package com.example.utis;

public class Channels {
	private String name_en;
	private String seq_id;
	private String abbr_en;
	private String name;
	private String channel_id;

	public Channels() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Channels(String name_en, String seq_id, String abbr_en, String name,
			String channel_id) {
		super();
		this.name_en = name_en;
		this.seq_id = seq_id;
		this.abbr_en = abbr_en;
		this.name = name;
		this.channel_id = channel_id;
	}

	@Override
	public String toString() {
		return "Channels [name_en=" + name_en + ", seq_id=" + seq_id
				+ ", abbr_en=" + abbr_en + ", name=" + name + ", channel_id="
				+ channel_id + "]";
	}

	public String getName_en() {
		return name_en;
	}

	public void setName_en(String name_en) {
		this.name_en = name_en;
	}

	public String getSeq_id() {
		return seq_id;
	}

	public void setSeq_id(String seq_id) {
		this.seq_id = seq_id;
	}

	public String getAbbr_en() {
		return abbr_en;
	}

	public void setAbbr_en(String abbr_en) {
		this.abbr_en = abbr_en;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

}
