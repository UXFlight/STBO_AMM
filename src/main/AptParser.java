package main;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.util.Pair;

public class AptParser {
	
	private String path;
	private Airport apt;
	private String icaoCode;
	
	private Boolean isTargetAirport = false;
	private Boolean airportFull = false;
	private Boolean isTaxiway = false;
	
	private Taxiway taxiway;
	

	public AptParser(String _path) {
		path = _path;
	}
	
	
	public Airport getAirport (String _icaoCode) {
		this.apt = new Airport(_icaoCode);
		this.icaoCode = _icaoCode;
		
		System.out.println("LOOKING FOR AIRPORT " + this.icaoCode + " ... ");
		
	    try {
	        File myObj = new File(this.path);
	        Scanner myReader = new Scanner(myObj);
	        while (myReader.hasNextLine() && !this.airportFull) {
	          String data = myReader.nextLine();
	          this.parseLine(data, apt);
	        }
			System.out.println("DONE PARSING");
	        myReader.close();
	      } catch (FileNotFoundException e) {
	        System.out.println("APT DAT FILE NOT FOUND");
	        e.printStackTrace();
	      }
	    
	    System.out.println("BUILDING TAXIWAYS");
//	    this.apt.buildTaxiways();
	    this.apt.buildTaxiways2();
		
		return this.apt;
	}
	
	
	private void parseLine(String line, Airport apt) {
		
		String[] elems = line.split(" +");
		
		if (elems[0].equals("1") && elems[4].equals(this.icaoCode)) {
			
			System.out.println("AIRPORT FOUND: " + elems[4]);
			this.isTargetAirport = true;
			this.apt.setElev(Double.valueOf(elems[1]));
			
		} else if (elems[0].equals("1") && !elems[4].equals(this.icaoCode) && this.isTargetAirport) {
			
			System.out.println("END OF AIRPORT");
			this.isTargetAirport = false;
			this.airportFull = true;
			
		}
		
		if (this.isTargetAirport) {
			
//			System.out.println(line);
			
			switch(elems[0]) {
			
			// Land Runway
			case "100":
				
				double width = Double.valueOf(elems[1]);
				int surfaceCode = Integer.valueOf(elems[2]);  // see surface type code
				int shoulderSurfaceCode = Integer.valueOf(elems[3]);  // see surface type code
				double smoothness = Double.valueOf(elems[4]);  // from 0 (smooth) to 1 (very rough)
				int centerlineLights = Integer.valueOf(elems[5]);  // 0 none, 1 yes
				int edgeLights = Integer.valueOf(elems[6]);  // 0 none, 1 yes, 2 yes with medium intensity
				// int autoGenerateDistSigns = Integer.valueOf(elems[7]); // auto generate distance signs - not used
				// FOR END 1 //
				String runwayNumber1 = elems[8];
				double lat1 = Double.valueOf(elems[9]);  // on runway centerline
				double lon1 = Double.valueOf(elems[10]);  // on runway centerline
				double displacedThresholdLenght1 = Double.valueOf(elems[11]);  // always INSIDE (between) two runway ends
				double blastPadLength1 = Double.valueOf(elems[12]);  // outside runway end
				int runwayMarkingsCode1 = Integer.valueOf(elems[13]);  // see runway marking code
				int approachLightsCode1 = Integer.valueOf(elems[14]);  // see approach lighting code
				int touchDownZoneLighting1 = Integer.valueOf(elems[15]);  // 0 no, 1 yes
				int runwayEndLights1 = Integer.valueOf(elems[16]);  // 0 no, 1 yes onmidirectionnal, 2 yes unidirectional
				// FOR END 2 //
				String runwayNumber2 = elems[17];
				double lat2 = Double.valueOf(elems[18]);  // on runway centerline
				double lon2 = Double.valueOf(elems[19]);  // on runway centerline
				double displacedThresholdLenght2 = Double.valueOf(elems[20]);  // always INSIDE (between) two runway ends
				double blastPadLength2 = Double.valueOf(elems[21]);  // outside runway end
				int runwayMarkingsCode2 = Integer.valueOf(elems[22]);  // see runway marking code
				int approachLightsCode2 = Integer.valueOf(elems[23]);  // see approach lighting code
				int touchDownZoneLighting2 = Integer.valueOf(elems[24]);  // 0 no, 1 yes
				int runwayEndLights2 = Integer.valueOf(elems[25]);  // 0 no, 1 yes onmidirectionnal, 2 yes unidirectional
				
				Runway runway = new Runway(width, surfaceCode, shoulderSurfaceCode, smoothness, centerlineLights, edgeLights,
						runwayNumber1, lat1, lon1, displacedThresholdLenght1, blastPadLength1, runwayMarkingsCode1, 
						approachLightsCode1, touchDownZoneLighting1, runwayEndLights1, runwayNumber2, lat2, lon2,
						displacedThresholdLenght2, blastPadLength2, runwayMarkingsCode2, approachLightsCode2, touchDownZoneLighting2,
						runwayEndLights2);
				
				apt.addRunway(runway);
				
				break;
			
			// TAXIWAYS //	
			// NOTE: for the taxiways, get all nodes and then use them to identify taxiways // 
			
//			// Start a new taxiway
//			case "110":
//				taxiway = new Taxiway(elems[1]);
//				this.isTaxiway = true;
//				break;
//			
//			// node
//			case "111":
//				if (isTaxiway) {
//					taxiway.addNode(new Pair<Double, Double>(Double.valueOf(elems[1]), Double.valueOf(elems[2])));
//				}
//				break;
//				
//			// node with Bezier
//			case "112":
//				if (isTaxiway) {
//					taxiway.addNode(new Pair<Double, Double>(Double.valueOf(elems[1]), Double.valueOf(elems[2])));
//				}
//				break;
//				
//			// closing node
//			case "113":
//				if (isTaxiway) {
//					taxiway.addNode(new Pair<Double, Double>(Double.valueOf(elems[1]), Double.valueOf(elems[2])));
//					apt.addTaxiway(taxiway);
//					this.isTaxiway = false;
//					System.out.println("ADDING TAXIWAY: " + taxiway.getCoords());
//				}
//				break;
//			
//			// closing node with Bezier
//			case "114":
//				if (isTaxiway) {
//					taxiway.addNode(new Pair<Double, Double>(Double.valueOf(elems[1]), Double.valueOf(elems[2])));
//					apt.addTaxiway(taxiway);
//					this.isTaxiway = false;
//					System.out.println("ADDING TAXIWAY: " + taxiway.getCoords());
//				}
//				break;
//				
//			// node to terminate a line 
//			case "115":
//				break;
//				
//			// node to terminate a line with Bezier
//			case "116":
//				break;
				
			// TAXI ROUTING NETWORK //
			// taxi nodes //
			case "1201":
				TaxiwayNode node = new TaxiwayNode(Integer.valueOf(elems[4]), Double.valueOf(elems[1]), 
						Double.valueOf(elems[2]), elems[3]);
				this.apt.addNode(node);
				break;
			
			// taxiway segments (defined by two nodes) //
			case "1202":
				TaxiwayNode node1 = this.apt.getNodeById(Integer.valueOf(elems[1]));
				TaxiwayNode node2 = this.apt.getNodeById(Integer.valueOf(elems[2]));
				String id;
				try {
					id = elems[5];
				} catch (ArrayIndexOutOfBoundsException e) {
					id = "NONAME";
				}
				TaxiwaySegment taxiway = new TaxiwaySegment(id, elems[3], elems[4], node1, node2);
				this.apt.addTaxiwaySegment(taxiway);
				break;
				
			// TODO active zone (runway dept or arrival or ils protection area)
			case "1203":

				break;
			}
			
		}
		
	}
	
}
