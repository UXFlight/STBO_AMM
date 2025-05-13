package main;

import java.util.ArrayList;

import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;

public class Taxiway {
	
	private ArrayList<TaxiwaySegment> segments;
	private String directions;
	private String isTaxiwayOrRunway; // "taxiway" or "runway"
	private String name;

	public Taxiway(String name, String directions, String isTaxiwayOrRunway) {
		this.name = name;
		this.directions = directions;
		this.isTaxiwayOrRunway = isTaxiwayOrRunway;
		
		this.segments = new ArrayList<TaxiwaySegment>();
	}
	
	public void addEndSegment (TaxiwaySegment segment) {
		this.segments.add(segment);
	}
	
	public void addStartSegment (TaxiwaySegment segment) {
		this.segments.add(0, segment);
	}
	
	public ArrayList<TaxiwaySegment> getSegments() {
		return this.segments;
	}
	
	public String getName() {
		return this.name;
	}
	
	public TaxiwayNode getStartNode() {
		return this.segments.get(0).getNodes().get(0);
	}
	
	public TaxiwayNode getEndNode() {
		return this.segments.get(this.segments.size() - 1).getNodes().get(1);
	}
	
	public String getIsTaxiwayOrRunway () {
		return this.isTaxiwayOrRunway;
	}
	
	public double getLength() {
		double length = 0;
		for (TaxiwaySegment segment : this.segments) {
			LatLng latLon0 = new LatLng(segment.getNodes().get(0).getCoords()[0], segment.getNodes().get(0).getCoords()[1]);
			LatLng latLon1 = new LatLng(segment.getNodes().get(1).getCoords()[0], segment.getNodes().get(1).getCoords()[1]);
			UTMRef utm0 = latLon0.toUTMRef();
			UTMRef utm1 = latLon1.toUTMRef();
			double x0 = utm0.getEasting();
			double y0 = utm0.getNorthing();
			double x1 = utm1.getEasting();
			double y1 = utm1.getNorthing();
			double distance = Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2));
			length += distance;
		}
		
		return length;
	}
	
}
