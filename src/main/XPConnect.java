package main;
import XPlaneConnect.XPlaneConnect;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;


public class XPConnect extends Thread{
	
	private class XPInstruction {
		
		private Instruction instruction;
		private ArrayList<Double> params;
		
		public XPInstruction(Instruction instruction, ArrayList<Double> params) {
			this.instruction = instruction;
			this.params = params;
		}
		
		public Instruction getInstruction () {
			return this.instruction;
		}
		
		public ArrayList<Double> getParams () {
			return this.params;
		}
		
	}
	
	private enum Instruction {
		SET_THRUST_COMMAND, 
		SET_BRAKES_COMMAND, 
		SET_STEER_COMMAND, 
		PLACE_AC_AT, 
		PLACE_AC_ON_GROUND_AT,
		PAUSE_SIM, UNPAUSE_SIM,
	}
	
	private ArrayList<XPInstruction> instructionBuffer;
	
	private long UPDATE_RATE;
	
	//	params of ownship   //
    public double lat;
	public double lon;
	public double height;
	public double pitch;
	public double roll;
	public double heading;
	public double aileron;
	public double elevator;
	public double rudder;
	public double fuelQuantity;
	public double brakingAction;
	public double thrustCmd;
	public double simTimeSinceStart;
	
	// traffic data //
	private ArrayList<double[]> trafficData;
	
	public double groundSpeed;
		
    public XPConnect(double updateRate) {
    	this.UPDATE_RATE = (long) updateRate;
    	this.instructionBuffer = new ArrayList<XPInstruction>();
    	this.trafficData = new ArrayList<double[]>();
    }
    
    @Override
    public void run() {
    	System.out.println("STARTING XPCONNECT");
    	
    	// def datarefs string //
    	String groundSpeedDref = "sim/flightmodel/position/groundspeed";
    	String thrustDref = "sim/flightmodel/engine/ENGN_thro_use";
		String parkingBrakeDref = "sim/cockpit2/controls/parking_brake_ratio";
		String fuelQuantityRightDref = "sim/cockpit2/fuel/fuel_level_indicated_right";
		String fuelQuantityLeftDref = "sim/cockpit2/fuel/fuel_level_indicated_left";
    	String simTimeSinceStartDref = "sim/time/total_flight_time_sec";
		
        try (XPlaneConnect xpc = new XPlaneConnect())
        {
        	/*
            while(true)
            {
            	// update ownship data //
                double[] posi = xpc.getPOSI(0);
                float[] ctrl = xpc.getCTRL(0);
                
                this.groundSpeed = xpc.getDREF(groundSpeedDref)[0];
                
                this.fuelQuantity = xpc.getDREF(fuelQuantityLeftDref)[0] + xpc.getDREF(fuelQuantityRightDref)[0];
                
//                this.brakingAction = 0.5 * (xpc.getDREF(leftBrakeDref3)[0] + xpc.getDREF(rightBrakeDref3)[0]);
                this.brakingAction = xpc.getDREF(parkingBrakeDref)[0];
                
                this.thrustCmd = xpc.getDREF(thrustDref)[0];
                
                this.simTimeSinceStart = xpc.getDREF(simTimeSinceStartDref)[0] * 1000;
//                System.out.println(this.simTimeSinceStart);
                
                this.lat = posi[0];
                this.lon = posi[1];
                this.height = posi[2];
	            this.pitch = posi[3];
	            this.roll = posi[4];
	            this.heading = posi[6];
                this.elevator = ctrl[0];
                this.aileron = ctrl[1];
                this.rudder = ctrl[2];
                
                // send instructions //
                for (XPInstruction instruction : this.instructionBuffer) {
                	switch (instruction.getInstruction()) {
                	case Instruction.SET_THRUST_COMMAND:
                		double thrustCmd = instruction.getParams().get(0);
                		xpc.sendDREF(thrustDref, (float) thrustCmd);
                		break;
                	case Instruction.SET_BRAKES_COMMAND:
                		double brakeCmd = instruction.getParams().get(0);
                		xpc.sendDREF(leftBrakeDref, (float) brakeCmd);
                		xpc.sendDREF(rightBrakeDref, (float) brakeCmd);
                		break;
					case SET_STEER_COMMAND:
						double steerCmd = instruction.getParams().get(0);
						xpc.sendDREF(steeringDref, (float) steerCmd);
						break;
					case PLACE_AC_AT:
						double placeAcAtValues[] = {instruction.getParams().get(0), instruction.getParams().get(1), instruction.getParams().get(2), -998, -998, instruction.getParams().get(3)};
						xpc.sendPOSI(placeAcAtValues, 0);
						break;
					case PLACE_AC_ON_GROUND_AT:
						double terrainAlt = xpc.getTerr(instruction.getParams().get(0).floatValue(), instruction.getParams().get(1).floatValue());
						double placeAcOnGroundAtValues[] = {instruction.getParams().get(0), instruction.getParams().get(1), terrainAlt + instruction.getParams().get(3), -998, -998, instruction.getParams().get(2)};
						xpc.sendPOSI(placeAcOnGroundAtValues, 0);
						break;
					case PAUSE_SIM:
						xpc.sendDREF("sim/time/sim_speed", 0);
						break;
					case UNPAUSE_SIM:
						xpc.sendDREF("sim/time/sim_speed", 1);
						break;
					default:
						break;
                	}
                		
                }
                
                this.instructionBuffer.clear();
                
                try
                {
//                    Thread.sleep(UPDATE_RATE);
                	Thread.sleep(10);
                }
                catch (InterruptedException ex) {}

                if(System.in.available() > 0)
                {
                    System.out.println("System.in.available() > 0");
                	break;
                }
            }
			*/
        }
        catch(IOException ex)
        {
            System.out.println("Error: in XPConnect main loop");
            System.out.println(ex.getMessage());
            System.out.println("Restarting XPConnect");
            this.run();
        }

    }
    
    public double getLat() {
    	return this.lat;
    }

	public void setLat(double lat) {
		this.lat = lat;
	}
    
    public double getLon() {
    	return this.lon;
    }

	public void setLon(double lon) {
		this.lon = lon;
	}

    public double getHeading() {
    	return this.heading;
    }

	public void setHeading(double heading) {
		this.heading = heading;
	}
    
    public double getGroundSpeed() {
    	return this.groundSpeed;
    }

	public void setGroundSpeed(double groundSpeed) {
		this.groundSpeed = groundSpeed;
	}

    
    public double getFuelQuantity () {
    	return this.fuelQuantity;
    }

	public void setFuelQuantity(double fuelQuantity) {
		this.fuelQuantity = fuelQuantity;
	}
    
    public double getBrakingAction () {
    	return this.brakingAction;
    }

	public void setBrakingAction(double brakingAction) {
		this.brakingAction = brakingAction;
	}
    
    public double getThrustCmd () {
    	return this.thrustCmd;
    }

	public void setThrustCmd(double thrustCmd) {
		this.thrustCmd = thrustCmd;
	}
    
    public double getSimTimeSinceStart() {
    	return this.simTimeSinceStart;
    }

	public void setSimTimeSinceStart(double simTimeSinceStart) {
		this.simTimeSinceStart = simTimeSinceStart;
	}
    
//    public double[] getTrafficParams(int i) {
//    	return this.trafficData.get(i);
//    }
    
    public void setThrustCommand (double cmd) {
//		String overrideThrust = "sim/operation/override/override_throttles";
//		String thrust = "sim/flightmodel/engine/ENGN_thro_use";
//		
//        try (XPlaneConnect xpc = new XPlaneConnect())
//        {	
//        	xpc.sendDREF(overrideThrust, (float) 1.0);
//        	xpc.sendDREF(thrust, (float) cmd);
//        }
//        catch(IOException ex)
//        {
//            System.out.println("Error:");
//            System.out.println(ex.getMessage());
//        }		
    	ArrayList<Double> params = new ArrayList<Double>();
    	params.add(cmd);
    	XPInstruction instruction = new XPInstruction(Instruction.SET_THRUST_COMMAND, params);
    	this.instructionBuffer.add(instruction);
    }
    
    public void setBrakesCommand (double cmd) {
//		String overrideBrakes = "sim/operation/override/override_gearbrake";
//    	String leftBrake = "sim/cockpit2/controls/left_brake_ratio";
//		String rightBrake = "sim/cockpit2/controls/right_brake_ratio";
//		
//        try (XPlaneConnect xpc = new XPlaneConnect())
//        {	
//        	xpc.sendDREF(overrideBrakes, (float) 1.0);
//        	xpc.sendDREF(rightBrake, (float) cmd);
//        	xpc.sendDREF(leftBrake, (float) cmd);
//        }
//        catch(IOException ex)
//        {
//            System.out.println("Error:");
//            System.out.println(ex.getMessage());
//        }	
        
    	ArrayList<Double> params = new ArrayList<Double>();
    	params.add(cmd);
    	XPInstruction instruction = new XPInstruction(Instruction.SET_BRAKES_COMMAND, params);
    	this.instructionBuffer.add(instruction);
    }
    
    public void setSteerCommand (double cmd) {
//    	String overrideSteering = "sim/operation/override/override_wheel_steer";
//    	String steering = "sim/flightmodel/parts/tire_steer_cmd"; // degrees
//    	
//        try (XPlaneConnect xpc = new XPlaneConnect())
//        {	
//        	xpc.sendDREF(overrideSteering, (float) 0.0);
//        	xpc.sendDREF(steering, (float) cmd);
//        }
//        catch(IOException ex)
//        {
//            System.out.println("Error:");
//            System.out.println(ex.getMessage());
//        }	
    	ArrayList<Double> params = new ArrayList<Double>();
    	params.add(cmd);
    	XPInstruction instruction = new XPInstruction(Instruction.SET_STEER_COMMAND, params);
    	this.instructionBuffer.add(instruction);
        
    }
    
    public void placeAcAt (double lat, double lon, double height, double heading) {
//        try (XPlaneConnect xpc = new XPlaneConnect())
//        {
//
//        	// double values[] = {lat, lon, -998, -998, -998, heading, -998};
//        	double values[] = {lat, lon, height, -998, -998, heading};
//        	System.out.println("PLACING AC AT ... ");
//        	System.out.println(values[0]);
//        	System.out.println(values[1]);
//        	System.out.println(values[2]);
//        	System.out.println(values[3]);
//        	System.out.println(values[4]);
//        	System.out.println(values[5]);
//        	
//        	xpc.sendPOSI(values, 0);
//        	
//        }
//        catch(IOException ex)
//        {
//            System.out.println("Error: placing ac at");
//            System.out.println(ex.getMessage());
//        }
    	
    	ArrayList<Double> params = new ArrayList<Double>();
    	params.add(lat);
    	params.add(lon);
    	params.add(height);
    	params.add(heading);
    	XPInstruction instruction = new XPInstruction(Instruction.PLACE_AC_AT, params);
    	this.instructionBuffer.add(instruction);
    }
    
    public void placeAcOnGroundAt (double lat, double lon, double heading, double altOffset) {
//    	try (XPlaneConnect xpc = new XPlaneConnect()){
//    		double terrainAlt = xpc.getTerr((float) lat, (float) lon);
//    		this.placeAcAt(lat, lon, terrainAlt + altOffset, heading);
//    	} catch (IOException e) {
//    		System.out.println("Error:");
//            System.out.println(e.getMessage());
//		}
    	ArrayList<Double> params = new ArrayList<Double>();
    	params.add(lat);
    	params.add(lon);
    	params.add(heading);
    	params.add(altOffset);
    	XPInstruction instruction = new XPInstruction(Instruction.PLACE_AC_ON_GROUND_AT, params);
    	this.instructionBuffer.add(instruction);
    }

    
//    public ArrayList<Double> getTrafficParams (int id) {
//    	ArrayList<Double> l = new ArrayList<Double>();
//    	double[] posi = null;
//    	
//    	try (XPlaneConnect xpc = new XPlaneConnect())
//        {
//    		posi = xpc.getPOSI(id);
//        }
//        catch(IOException ex)
//        {
//            System.out.println("Error: getting traffic");
//            System.out.println(ex.getMessage());
//        }
//    	
//    	if (posi != null) {
//        	l.add(posi[0]); // lat 
//        	l.add(posi[1]); // lon
//        	l.add(posi[2]); // alt
//        	l.add(posi[6]); // heading
//    	} else {
//        	l.add(0.); // lat 
//        	l.add(0.); // lon
//        	l.add(0.); // alt
//        	l.add(0.); // heading
//    	}
//
//    	
//    	return l;
//    }
    
    public void pauseSim () {
    	ArrayList<Double> params = new ArrayList<Double>();
    	XPInstruction instruction = new XPInstruction(Instruction.PAUSE_SIM, params);
    	this.instructionBuffer.add(instruction);
    }
    
    public void unpauseSim () {
    	ArrayList<Double> params = new ArrayList<Double>();
    	XPInstruction instruction = new XPInstruction(Instruction.UNPAUSE_SIM, params);
    	this.instructionBuffer.add(instruction);
    }
    
}