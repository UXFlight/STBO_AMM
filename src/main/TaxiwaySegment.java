package main;
import java.util.ArrayList;
import java.util.List;

import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;


public class TaxiwaySegment {
	
	private String name;
	private String directions;
	private String isTaxiOrRunway;  // "taxiway" or "runway"
	
	private List<TaxiwayNode> nodes;
	
	public TaxiwaySegment (String _name, String _directions, String _isTaxiOrRunway, TaxiwayNode _node1, TaxiwayNode _node2) {
		this.nodes = new ArrayList<TaxiwayNode>();
		this.nodes.add(_node1);
		this.nodes.add(_node2);
		this.name = _name;
		this.directions = _directions;
		this.isTaxiOrRunway = _isTaxiOrRunway;
	}
	
	public List<TaxiwayNode> getNodes () {
		return this.nodes;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getIsTaxiwayOrRunway () {
		return this.isTaxiOrRunway;
	}
	
	public String getDirections() {
		return this.directions;
	}
	
	public void reverse() {
		this.nodes = this.nodes.reversed();
	}
	
	public double getLength() {
		LatLng latLon0 = new LatLng(this.nodes.get(0).getCoords()[0], this.nodes.get(0).getCoords()[1]);
		LatLng latLon1 = new LatLng(this.nodes.get(1).getCoords()[0], this.nodes.get(1).getCoords()[1]);
		UTMRef utm0 = latLon0.toUTMRef();
		UTMRef utm1 = latLon1.toUTMRef();
		double x0 = utm0.getEasting();
		double y0 = utm0.getNorthing();
		double x1 = utm1.getEasting();
		double y1 = utm1.getNorthing();
		double distance = Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2));
		return distance;
	}
}
