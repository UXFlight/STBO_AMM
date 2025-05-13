package main;

import java.util.ArrayList;
import java.util.List;

import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;

public class Airport {

	private String icaoCode;
	private double elevASM; // in ft
	List<Runway> runways;
	List<TaxiwayNode> taxiNodes;
	List<Taxiway> taxiways;
	List<TaxiwaySegment> taxiwaySegments;
	
	public Airport(String _icaoCode) {
		this.setIcaoCode(_icaoCode);
		this.runways = new ArrayList<Runway>();
		this.taxiways = new ArrayList<Taxiway>();
		this.taxiNodes = new ArrayList<TaxiwayNode>();
		this.taxiwaySegments = new ArrayList<TaxiwaySegment>();
	}
	
	public void buildTaxiways() {
		ArrayList<TaxiwaySegment> segments = new ArrayList<TaxiwaySegment>(this.taxiwaySegments);
		Boolean taxiwayCompleted = true;
		TaxiwaySegment taxiwaySegment = segments.get(0);
		TaxiwayNode startNode = taxiwaySegment.getNodes().get(0);
		TaxiwayNode endNode = taxiwaySegment.getNodes().get(1);
		Taxiway taxiway;
		taxiway = new Taxiway(taxiwaySegment.getName(), taxiwaySegment.getDirections(), taxiwaySegment.getIsTaxiwayOrRunway());
		taxiway.addStartSegment(taxiwaySegment);
		segments.remove(0);
		
		// while we still have unclassed segments //
		while (segments.size() != 0) {
			
//			System.out.println("TAXIWAY SEGMENTS REMAINING : " + segments.size());
			
			if (taxiwayCompleted) {
				this.taxiways.add(taxiway);
				taxiwaySegment = segments.get(0);
				segments.remove(0);
//				System.out.println("STARTING NEW TAXIWAY : " + taxiwaySegment.getName());
				taxiway = new Taxiway(taxiwaySegment.getName(), taxiwaySegment.getDirections(), taxiwaySegment.getIsTaxiwayOrRunway());
				taxiway.addStartSegment(taxiwaySegment);
				startNode = taxiwaySegment.getNodes().get(0);
				endNode = taxiwaySegment.getNodes().get(1);
			}
			
			taxiwayCompleted = true; // base, changed if segment was added //
			
			// go through list and either start a new segment or add segment to taxiway //
//			for (TaxiwaySegment segment : segments) {
			for (int i = 0; i < segments.size(); i++) {
				TaxiwaySegment segment = segments.get(i);
				if (taxiwaySegment.getName().equals(segment.getName())) {
					// if startNode is the same as segment endNode //
					// add at start of list //
					if (startNode.getId() == segment.getNodes().get(1).getId()) {
						startNode = segment.getNodes().get(0);
						taxiway.addStartSegment(segment);
						segments.remove(i);
						taxiwayCompleted = false;
					
					// if endNode is the same as segment startNode //
					// add at end of list //
					} else if (endNode.getId() == segment.getNodes().get(0).getId()) {
						endNode = segment.getNodes().get(1);
						taxiway.addStartSegment(segment);
						segments.remove(i);
						taxiwayCompleted = false;
						
					// if both end nodes are the same //
					// need to reverse taxiway segment //
					// and at at end of list //
					} else if (endNode.getId() == segment.getNodes().get(1).getId()) {
						segment.reverse();
						taxiway.addStartSegment(segment);
						segments.remove(i);
						taxiwayCompleted = false;
						
					// if both start nodes are the same //
					// need to reverse taxiway segment //
					// and at at end of list //
					} else if ( startNode.getId() == segment.getNodes().get(1).getId()) {
						segment.reverse();
						taxiway.addStartSegment(segment);
						segments.remove(i);
						taxiwayCompleted = false;
					}
				}
			}
		}
	}
	
	public void buildTaxiways2 () {
		// new method --- we want one id between each intersection //
		// so our taxiways will be straight lines with same name //
		// untill and intersection is met //
		// step 1: identify which nodes are intersections (more than two taxiways using them) //
		// or which are end //
		// or which are a not an interest but the segments change name //
		// step 2: build taxiways similarly to 1 but stop when intersection //
		
		// step 1 //
		for (TaxiwaySegment segment : this.taxiwaySegments) {
			segment.getNodes().get(0).addTaxiwayConnected(segment.getName());
			segment.getNodes().get(1).addTaxiwayConnected(segment.getName());
		}
		
		// step 2 //
		// build taxiways according to nodes //
		// if node isEnd or isChangeName or isIntersect -- stop //
		ArrayList<TaxiwaySegment> segments = new ArrayList<TaxiwaySegment>(this.taxiwaySegments);
		Boolean taxiwayCompleted = false;
		TaxiwaySegment taxiwaySegment = segments.get(0);
		Taxiway taxiway = new Taxiway(taxiwaySegment.getName(), taxiwaySegment.getDirections(), taxiwaySegment.getIsTaxiwayOrRunway());
		taxiway.addStartSegment(taxiwaySegment);
		TaxiwayNode startNode = taxiway.getStartNode();
		TaxiwayNode endNode = taxiway.getEndNode();
		segments.remove(0);
		
		// while we still have unclassed segments //
		while (segments.size() != 0) {
			
//			System.out.println("TAXIWAY SEGMENTS REMAINING : " + segments.size());
			
			if (taxiwayCompleted) {
				this.taxiways.add(taxiway);
				taxiwaySegment = segments.get(0);
				segments.remove(0);
//				System.out.println("STARTING NEW TAXIWAY : " + taxiwaySegment.getName());
				taxiway = new Taxiway(taxiwaySegment.getName(), taxiwaySegment.getDirections(), taxiwaySegment.getIsTaxiwayOrRunway());
				taxiway.addStartSegment(taxiwaySegment);
				startNode = taxiway.getStartNode();
				endNode = taxiway.getEndNode();
				
			}
			
			taxiwayCompleted = true; // base, changed if segment was added //
			
			// go through list and either start a new segment or add segment to taxiway //
//			for (TaxiwaySegment segment : segments) {
			for (int i = 0; i < segments.size(); i++) {
				
				taxiwaySegment = segments.get(i);
				
				// if startNode is the same as segment endNode //
				// add at start of list //
				if (startNode.getId() == taxiwaySegment.getNodes().get(1).getId() && taxiwaySegment.getName().equals(taxiway.getName())
						&& !startNode.getIsChangeOfName() && !startNode.getIsEnd() && !startNode.getIsInteresect()) {
//					startNode = taxiwaySegment.getNodes().get(0);
					taxiway.addStartSegment(taxiwaySegment);
					segments.remove(i);
					taxiwayCompleted = false;
					startNode = taxiway.getStartNode();
					endNode = taxiway.getEndNode();
				
				// if endNode is the same as segment startNode //
				// add at end of list //
				} else if (endNode.getId() == taxiwaySegment.getNodes().get(0).getId() && taxiwaySegment.getName().equals(taxiway.getName())
						&& !endNode.getIsChangeOfName() && !endNode.getIsEnd() && !endNode.getIsInteresect()) {
//					endNode = taxiwaySegment.getNodes().get(1);
					taxiway.addEndSegment(taxiwaySegment);
					segments.remove(i);
					taxiwayCompleted = false;
					startNode = taxiway.getStartNode();
					endNode = taxiway.getEndNode();
					
				// if both end nodes are the same //
				// need to reverse taxiway segment //
				// and add at end of list //
				} else if (endNode.getId() == taxiwaySegment.getNodes().get(1).getId() && taxiwaySegment.getName().equals(taxiway.getName())
						&& !endNode.getIsChangeOfName() && !endNode.getIsEnd() && !endNode.getIsInteresect()) {
//					segment.reverse();
					TaxiwaySegment reversedSegment = new TaxiwaySegment(taxiwaySegment.getName(),
							taxiwaySegment.getDirections(), taxiwaySegment.getIsTaxiwayOrRunway(), 
							taxiwaySegment.getNodes().get(1), taxiwaySegment.getNodes().get(0));
					taxiway.addEndSegment(reversedSegment);
					segments.remove(i);
					taxiwayCompleted = false;
					startNode = taxiway.getStartNode();
					endNode = taxiway.getEndNode();
					
				// if both start nodes are the same //
				// need to reverse taxiway segment //
				// and add at start of list //
				} else if (startNode.getId() == taxiwaySegment.getNodes().get(0).getId() && taxiwaySegment.getName().equals(taxiway.getName())
						&& !startNode.getIsChangeOfName() && !startNode.getIsEnd() && !startNode.getIsInteresect()) {
//					segment.reverse();
					TaxiwaySegment reversedSegment = new TaxiwaySegment(taxiwaySegment.getName(),
							taxiwaySegment.getDirections(), taxiwaySegment.getIsTaxiwayOrRunway(), 
							taxiwaySegment.getNodes().get(1), taxiwaySegment.getNodes().get(0));
					taxiway.addStartSegment(reversedSegment);
					segments.remove(i);
					taxiwayCompleted = false;
					startNode = taxiway.getStartNode();
					endNode = taxiway.getEndNode();
				}
			}
		}
		
	}
	
	public void setElev (double elev) {
		this.elevASM = elev;
	}
	
	public double getElev () {
		return this.elevASM;
	}
	
	public void addRunway (Runway runway) {
		this.runways.add(runway);
	}
	
	public List<Runway> getRunways() {
		return this.runways;
	}
	
	public void addTaxiwaySegment (TaxiwaySegment taxiway) {
		this.taxiwaySegments.add(taxiway);
	}
	
	public List<TaxiwaySegment> getTaxiwaySegments () {
		return this.taxiwaySegments;
	}
	
	public List<Taxiway> getTaxiways () {
		return this.taxiways;
	}
	
	public void addNode (TaxiwayNode node) {
		this.taxiNodes.add(node);
	}
	
	public List<TaxiwayNode> getAllNodes () {
		return this.taxiNodes;
	}
	
	public TaxiwayNode getNodeById (int id) {
		for (int i = 0; i < this.taxiNodes.size(); i ++) {
			TaxiwayNode node = this.taxiNodes.get(i);
			if (node.getId() == id) {
				return node;
			}
		}
		return null;
	}
	
	// for now center is middle of first runway //
	public double[] getCenter () {
		LatLng latLon1 = new LatLng(this.runways.get(0).getLat1(), this.runways.get(0).getLon1());
		LatLng latLon2 = new LatLng(this.runways.get(0).getLat2(), this.runways.get(0).getLon2());
		UTMRef utm1 = latLon1.toUTMRef();
		UTMRef utm2 = latLon2.toUTMRef();
		double x1 = utm1.getEasting();
		double y1 = utm1.getNorthing();
		double x2 = utm2.getEasting();
		double y2 = utm2.getNorthing();
		double[] ret = new double[2]; 
		ret[0] = (x1 + x2) / 2;
		ret[1] = (y1 + y2) / 2;
		return(ret);
	}

	public String getIcaoCode() {
		return icaoCode;
	}

	public void setIcaoCode(String icaoCode) {
		this.icaoCode = icaoCode;
	}
	
	
}

