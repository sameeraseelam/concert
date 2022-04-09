package com.concert.info;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ConcertController {
	
	static List<Artists> artistsList = new ArrayList<Artists>();
	static Map<String, String> artistsAtEvent = new HashMap<String, String>();

	@RequestMapping(method = RequestMethod.GET, path = "/hello-world")
	public String HelloWorld() {
		return "Hello World";
	}

	@GetMapping(path = "/hello-world-bean")
	public HelloWorldBean helloWorldBean() {
		return new HelloWorldBean("Hello World");
	}
	
	@GetMapping(path = "/concert/artist/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> artistWithId(@PathVariable String id) {
	
		String result = fetchDetails(id);
		if(result == null || result.equalsIgnoreCase("")) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist Not Found! (CODE 404)\nValid Artist IDs are 21, 22, 23, 24, 25, 26, 27, 28 and 29");
		}
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	//Get the json file content
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	//Read the events, artists, venues json files from S3 
	public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONArray json_arr = new JSONArray(jsonText);
			return json_arr;
		} finally {
			is.close();
		}
	}
	
	public static String getArtistInfoAndEvents(String id) {

		String eventsList = "";
		String[] eventsListArr = null;

		Set<String> set = new HashSet<String>();

		for (Artists artist : artistsList) {
			if (artist.getId().equalsIgnoreCase(id)) {

				eventsList = artistsAtEvent.get(artist.getId());
				eventsListArr = eventsList.split(",");
				for (int o = 0; o < eventsListArr.length; o++) {
					set.add(eventsListArr[o]);
				}

				eventsList = "Id : " + artist.getId() + ", Name : " + artist.getName() + ", Img Src : "
						+ artist.getImgSrc() + ", URL : " + artist.getUrl() + ", Rank : " + artist.getRank()
						+ ", Events : " + set;
				
				/*System.out.println("Id : " + artist.getId() + ", Name : " + artist.getName() + ", Img Src : " + artist.getImgSrc() +
				", URL : " + artist.getUrl() + ", Rank : " + artist.getRank() + ", Events : " + set);*/
				 
				// System.out.println(eventsList);
			}

			set.removeAll(set);
		}
		return eventsList;
	}
	
	public static String getArtistNames(String id) {
		String artistName = "";
		for (Artists artist : artistsList) {

			if (artist.getId().equalsIgnoreCase(id)) {
				artistName = artist.getName();
				
				 /*System.out.println("Name : " + artist.getName() + " Id : " +
				 artist.getId() + " Img Src : " + artist.getImgSrc() +
				 " URL : " + artist.getUrl() + " Rank : " + artist.getRank());*/
				 
			}
		}
		return artistName;
	}
	
	public String fetchDetails(String id) {
		
		String result = "";
		// End-point contains data for events
		String urlGetEvents = "https://iccp-interview-data.s3-eu-west-1.amazonaws.com/78656681/events.json";

		// End-point contains data for artists
		String urlGetArtists = "https://iccp-interview-data.s3-eu-west-1.amazonaws.com/78656681/artists.json";
		// ReadFileFromS3.fetchFileFromS3("artist", urlGetArtists);

		// End-point contains data for venues
		String urlGetVenues = "https://iccp-interview-data.s3-eu-west-1.amazonaws.com/78656681/venues.json";
		// ReadFileFromS3.fetchFileFromS3("venue", urlGetVenues);

		JSONArray jsonArrayForEvents;
		JSONObject jsonObjForEvents = null;

		JSONArray jsonArrayForVenues;
		JSONObject jsonObjForVenues = null;

		JSONArray jsonArrayForArtists;
		JSONObject jsonObjForArtists = null;

		try {

			// Artists
			jsonArrayForArtists = readJsonFromUrl(urlGetArtists);

			String artistsName = "";
			String artistsId = "";
			String artistsImgSrc = "";
			String artistsURL = "";
			int artistsRank;

			for (int i = 0; i < jsonArrayForArtists.length(); i++) { // iterate
				jsonObjForArtists = (JSONObject) jsonArrayForArtists.get(i);
				// System.out.println("jsonObjForArtists " + jsonObjForArtists);

				artistsName = jsonObjForArtists.getString("name");
				artistsId = jsonObjForArtists.getString("id");
				artistsImgSrc = jsonObjForArtists.getString("imgSrc");
				artistsURL = jsonObjForArtists.getString("url");
				artistsRank = jsonObjForArtists.getInt("rank");

				
				 /*System.out.println("jsonObjForArtists name " + artistsName);
				 * System.out.println("jsonObjForArtists id " + artistsId);
				 * System.out.println("jsonObjForArtists imgSrc " +
				 * artistsImgSrc); System.out.println("jsonObjForArtists url " +
				 * artistsURL); System.out.println("jsonObjForArtists rank " +
				 * artistsRank);*/
				 
				Artists artists = new Artists();
				artists.setName(artistsName);
				artists.setId(artistsId);
				artists.setImgSrc(artistsImgSrc);
				artists.setUrl(artistsURL);
				artists.setRank(artistsRank);
				artists.setEvents(artistsAtEvent);

				artistsList.add(artists);
			}

			// Events
			jsonArrayForEvents = readJsonFromUrl(urlGetEvents);
			Events events = new Events();
			String eventsTitle = "";
			String eventsId;
			String eventsDateStatus;
			String eventsTimezone = "";
			String eventsStartDate = "";
			JSONArray eventsArtists;
			JSONObject eventsVenue;
			boolean eventsHiddenFromSearch = false;
			String artistNameInMap = "";

			for (int i = 0; i < jsonArrayForEvents.length(); i++) {
				jsonObjForEvents = (JSONObject) jsonArrayForEvents.get(i);
				// System.out.println("jsonObjForEvents " + jsonObjForEvents);

				eventsTitle = jsonObjForEvents.getString("title");
				eventsId = jsonObjForEvents.getString("id");
				eventsDateStatus = jsonObjForEvents.getString("dateStatus");
				if (jsonObjForEvents.has("timeZone")) {
					eventsTimezone = jsonObjForEvents.getString("timeZone");
				}
				if (jsonObjForEvents.has("startDate")) {
					eventsStartDate = jsonObjForEvents.getString("startDate");
				}
				eventsArtists = jsonObjForEvents.getJSONArray("artists");
				for (int i1 = 0; i1 < eventsArtists.length(); i1++) {
					artistNameInMap = getArtistNames(eventsArtists.getJSONObject(i1).getString("id"));
					if (artistNameInMap != null && !artistNameInMap.equalsIgnoreCase("")) {
						if (artistsAtEvent.containsKey(eventsArtists.getJSONObject(i1).getString("id"))) {

							artistsAtEvent.put(eventsArtists.getJSONObject(i1).getString("id"),
									artistsAtEvent.get(eventsArtists.getJSONObject(i1).getString("id")) + ","
											+ eventsTitle);
							
							 /* System.out.println("***" +
							 * eventsArtists.getJSONObject(i1).getString("id") +
							 * " = " +
							 * artistsAtEvent.get(eventsArtists.getJSONObject(i1
							 * ).getString("id")) + "," + eventsTitle);*/
							 

						} else {
							artistsAtEvent.put(eventsArtists.getJSONObject(i1).getString("id"), eventsTitle);
							// System.out.println(eventsArtists.getJSONObject(i1).getString("id")
							// + " = " + eventsTitle);
						}
					}
				}

				// artistsAtEvent.put();
				eventsVenue = jsonObjForEvents.getJSONObject("venue");
				if (jsonObjForEvents.has("eventsHiddenFromSearch")) {
					eventsHiddenFromSearch = jsonObjForEvents.getBoolean("hiddenFromSearch");
				}
				
				 /* System.out.println("jsonObjForEvents title " + eventsTitle);
				 * System.out.println("jsonObjForEvents id " + eventsId);
				 * System.out.println("jsonObjForEvents date status " +
				 * eventsDateStatus);
				 * System.out.println("jsonObjForEvents title " +
				 * eventsTimezone);
				 * System.out.println("jsonObjForEvents start date " +
				 * eventsStartDate);
				 * System.out.println("jsonObjForEvents artists " +
				 * eventsArtists); System.out.println("jsonObjForEvents venue "
				 * + eventsVenue);
				 * System.out.println("jsonObjForEvents hidden from search " +
				 * eventsHiddenFromSearch);*/
				 

				events.setTitle(eventsTitle);
				events.setId(eventsId);
				events.setDateStatus(eventsDateStatus);
				events.setTimezone(eventsTimezone);
				events.setStartDate(eventsStartDate);
				events.setArtists(eventsArtists);
				events.setVenue(eventsVenue);
				events.setHiddenFromSearch(eventsHiddenFromSearch);
			}

			// Venues
			jsonArrayForVenues = readJsonFromUrl(urlGetVenues);
			Venues venues = new Venues();
			String venuesName = "";
			String venuesURL = "";
			String venuesCity = "";
			String venuesId = "";
			for (int i = 0; i < jsonArrayForVenues.length(); i++) { // iterate
				jsonObjForVenues = (JSONObject) jsonArrayForVenues.get(i);
				// System.out.println("jsonObjForVenues " + jsonObjForVenues);

				venuesName = jsonObjForVenues.getString("name");
				venuesURL = jsonObjForVenues.getString("url");
				venuesCity = jsonObjForVenues.getString("city");
				venuesId = jsonObjForVenues.getString("id");

				
				 /* System.out.println("jsonObjForVenues name " + venuesName);
				 * System.out.println("jsonObjForVenues url " + venuesURL);
				 * System.out.println("jsonObjForVenues city " + venuesCity);
				 * System.out.println("jsonObjForVenues Id " + venuesId);*/
				 

				venues.setName(venuesName);
				venues.setUrl(venuesURL);
				venues.setCity(venuesCity);
				venues.setId(venuesId);
			}

			// System.out.println("**artists size " + artistsList.size());
			 result = getArtistInfoAndEvents(id);
			 System.out.println("Result - " + result);
			
			 /* System.out.println("Artists and events - "); for
			 * (Map.Entry<String,String> entry : artistsAtEvent.entrySet()) {
			 * System.out.println("Artist = " + entry.getKey() + ", Events = " +
			 * entry.getValue()); }*/
			 

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

}
