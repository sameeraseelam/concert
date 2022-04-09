package com.concert.info;

import org.json.JSONArray;
import org.json.JSONObject;

public class Events {
	
	private String title;
	private String id;
	private String dateStatus;
	private String timezone;
	private String startDate;
	private JSONArray artists;
	private JSONObject venue;
	private boolean hiddenFromSearch;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDateStatus() {
		return dateStatus;
	}
	public void setDateStatus(String dateStatus) {
		this.dateStatus = dateStatus;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public JSONArray getArtists() {
		return artists;
	}
	public void setArtists(JSONArray eventsArtists) {
		this.artists = eventsArtists;
	}
	public JSONObject getVenue() {
		return venue;
	}
	public void setVenue(JSONObject eventsVenue) {
		this.venue = eventsVenue;
	}
	public boolean isHiddenFromSearch() {
		return hiddenFromSearch;
	}
	public void setHiddenFromSearch(boolean hiddenFromSearch) {
		this.hiddenFromSearch = hiddenFromSearch;
	}
	
}
