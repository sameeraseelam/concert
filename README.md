# Getting Started

### Reference Documentation

### HOW TO RUN
* If maven is installed on your system, from cmd execute the following in the application directory:
	1. mvn -v
	2. mvn install (A maven artifact will be generated i.e., a jar file. In the target folder of the application, find the jar file)
	3. cd target
	4. java -jar <fileName>.jar
	5. Find the message "Concert Application is running" in the startup log to confirm that the application is up and running.
* If maven is not installed on your system:
	1. In the application directory, you will see mvnw and mvnw.cmd maven command files (wrappers)
	2. In cmd, run "./mvnw install"
	3. BUILD SUCCESS
	OR
	1. Run from target folder of the application - ./mvnw spring-boot:run
	2. Find the message "Concert Application is running" in the startup log to confirm that the application is up and running.


### Requirements

* Demonstrate use of REST conventions
* Create route to:
        Get artist information for a specific artistId. This should contain all fields of given artist and all the events the artist will perform at.

### Pre-signed AWS S3 URLs
The following URLs were provided for Artists, Events, and Venues respectively:

* https://iccp-interview-data.s3-eu-west-1.amazonaws.com/78656681/artists.json
* https://iccp-interview-data.s3-eu-west-1.amazonaws.com/78656681/events.json
* https://iccp-interview-data.s3-eu-west-1.amazonaws.com/78656681/venues.json
*** There is no need to connect to AWS S3 whatsoever. A simple code to read the URL and its contents work through a java method.
	
### Design Decisions

* The method readJsonFromUrl() was created in order for the service to fetch the files/data from S3
* The details of events, artists, and venues were stored in separate class objects.
* ConcertController.java file has a method artistWithId(id) that takes the {id} value from the URL("/concert/artist/{id}") and produces the json output

### EDGE CASES/ERROR

* In the events json file, there is an artist with an id as 100 but its corresponding information is not available in the artists json file.
* When the URL http://localhost:8080/concert/artist/100 is hit, an exception with HttpStatus.NOT_FOUND with CODE 404 is shown on the browser.
* A list of valid artist ids are also displayed in order to fetch the artist/event info that are valid and produce result.

### TEST CASES
URL to get the Artist info and their event names:

* http://localhost:8080/concert/artist/{id}
* Example(Positive) - http://localhost:8080/concert/artist/27
  Output on the browser is produced as MediaType.APPLICATION_JSON_VALUE - 
		Id : 27, Name : The Crazy World of Arthur Brown, Img Src : //some-base-url/arthur-brown.jpg, URL : /the-crazy-world-of-arthur-brown-tickets/artist/27, Rank : 7, Events : [Paranoid Live, Blues In Space]
* Example (Exception) - http://localhost:8080/concert/artist/100
  Output on the broswer shows an excpetion with an error message -
  		Sat Apr 09 09:18:07 MDT 2022
		There was an unexpected error (type=Not Found, status=404).
		Artist Not Found! (CODE 404) Valid Artist IDs are 21, 22, 23, 24, 25, 26, 27, 28 and 29

