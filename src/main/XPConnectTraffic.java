package main;

import XPlaneConnect.XPlaneConnect;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

public class XPConnectTraffic extends Thread {
	
	private long UPDATE_RATE;
	
	// traffic data //
	private ArrayList<double[]> trafficData;

	public XPConnectTraffic(double updateRate) {
		this.UPDATE_RATE = (long) updateRate;
		this.trafficData = new ArrayList<double[]>();
	}
	
	@Override
	public void run() {
		System.out.println("STARTING XPCONNECT TRAFFIC");
		
		try (XPlaneConnect xpc = new XPlaneConnect()){
			
			while (true) {
				// update traffic data //
				for (int i = 0; i < 19; i++) {
					try {
						double[] trafficPos = xpc.getPOSI(i + 1);
						try {
							trafficData.set(i, trafficPos);
						} catch (IndexOutOfBoundsException e) {
							trafficData.add(trafficPos);
						}
					} catch (IOException e) {
//						e.printStackTrace();
//						System.out.println("missed traffic value for traffic id = " + i);
					} catch (NullPointerException e) {
//						e.printStackTrace();
//						System.out.println("missed traffic value for traffic id = " + i);
					}
				}
				
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

	
		} catch (SocketException e) {
            System.out.println("Error: in XPConnectTraffic main loop");
            System.out.println(e.getMessage());
            System.out.println("Restarting XPConnectTraffic");
            this.run();
		}
	}
	
	public double[] getTrafficParams(int i) {
		return this.trafficData.get(i);
	}
	
}