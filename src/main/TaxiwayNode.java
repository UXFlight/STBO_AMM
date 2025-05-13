package main;

import java.util.ArrayList;

public class TaxiwayNode {

	private double lat;
	private double lon;
	private int id;
	
	private String usage;  // dest, init both or junc
	
	private Boolean isIntersect;
	private Boolean isEnd;
	private Boolean isChangeOfName;
	private int taxiwayConnected;
	
	private ArrayList<String> taxiwayConnectedNames;
	
	
	public TaxiwayNode (int _id, double _lat, double _lon, String _usage) {
		this.lat = _lat;
		this.lon = _lon;
		this.id = _id;
		this.usage = _usage;
		isIntersect = false;
		taxiwayConnected = 0;
		isEnd = false;
		isChangeOfName = false;
		taxiwayConnectedNames = new ArrayList<String>();
	}
	
	public int getId () {
		return this.id;
	}
	
	public double[] getCoords () {
		double[] ret = new double[2];
		ret[0] = this.lat;
		ret[1] = this.lon;
		return ret;
	}
	
	public String getUsage () {
		return this.usage;
	}
	
	public void addTaxiwayConnected (String name) {
		this.taxiwayConnected += 1;
		
		if (this.taxiwayConnected == 1) {
			this.isEnd = true;
		} else {
			this.isEnd = false;
		}
		
		if (this.taxiwayConnected >= 3 ) {
			this.isIntersect = true;
		}
		
		for (String connectName : this.taxiwayConnectedNames) {
			if (!connectName.equals(name)) {
				this.isChangeOfName = true;
			}
		}
		
		this.taxiwayConnectedNames.add(name);
		
	}
	
	public Boolean getIsInteresect() {
		return this.isIntersect;
	}
	
	public Boolean getIsChangeOfName() {
		return this.isChangeOfName;
	}
	
	public Boolean getIsEnd() {
		return this.isEnd;
	}
	
	public ArrayList<String> getIntersect() {
		return taxiwayConnectedNames;
	}
	
}
