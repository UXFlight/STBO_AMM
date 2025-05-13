package main;
import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;

public class CoordConverter {
	
	public double[] UTMfromLatLon (double lat, double lon) {
	    LatLng ll4 = new LatLng(lat, lon);
	    UTMRef utm = ll4.toUTMRef();
	    double[] coords = new double[4];
	    coords[0] = utm.getEasting();
	    coords[1] = utm.getNorthing();
//	    coords[2] = utm.getLatZone();
//	    coords[3] = utm.getLngZone();
	    
	    return coords;
	}
	
	public double[] LatLonFromUTM (double x, double y, char latZone, int lgnZone) {
		double[] coords = new double[2];
		UTMRef utm = new UTMRef(lgnZone, latZone, x, y);
		LatLng ll = utm.toLatLng();
		coords[0] = ll.getLatitude();
		coords[1] = ll.getLongitude();
		return coords;
	}
}
