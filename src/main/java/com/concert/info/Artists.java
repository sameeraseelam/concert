package com.concert.info;

import java.util.Map;

public class Artists {

	private String name;
	private String id;
	private String imgSrc;
	private String url;
	private int rank;
	private Map<String, String> events;
	
	public Map<String, String> getEvents() {
		return events;
	}
	public void setEvents(Map<String, String> artistsAtEvent) {
		this.events = artistsAtEvent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImgSrc() {
		return imgSrc;
	}
	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	
}
