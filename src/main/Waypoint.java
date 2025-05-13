package main;

public class Waypoint {

	private double lat;
	private double lon;
	private double speed;
	
	private double timeOfPassage;
	
	public Waypoint (double lat, double lon, double speed) {
		this.lat = lat;
		this.lon = lon;
		this.speed = speed;
	}
	
	public double getLat () {
		return this.lat;
	}
	
	public double getLon () {
		return this.lon;
	}
	
	public double getSpeed () {
		return this.speed;
	}
	
	public void setTimeOfPassage (double time) {
		this.timeOfPassage = time;
	}
	
	public double getTimeOfPassage () {
		return this.timeOfPassage;
	}
}
