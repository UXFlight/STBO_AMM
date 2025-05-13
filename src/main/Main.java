package main;

import AgentCB.JavaAgentCB;
import com.ingescape.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.NotDefinedOnUTMGridException;
import uk.me.jstott.jcoord.UTMRef;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;


import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class Main extends Application implements AgentEventListener, WebSocketEventListener {

	private static Main instance;

	public static Main getInstance() {return instance;}

	private static Logger _logger = LoggerFactory.getLogger(Application.class);
	@Override
	public void handleAgentEvent(Agent agent, AgentEvent event, String uuid, String name, Object eventData) {
		//_logger.debug("**received agent event for {} ({}): {} with data {}", name, uuid, event, eventData);
	}

	@Override
	public void handleWebSocketEvent(WebSocketEvent event, Throwable t) {
		if (t != null) { // (event == WebSocketEvent.IGS_WEB_SOCKET_FAILED)
			_logger.error("**received web socket event {} with exception {}", event, t.toString());
		}
		else {
			_logger.debug("**received web socket event {}", event);
		}

	}
	
	// PARAMS THAT CAN BE CHANGED //
	private static final String AIRPORT_ICAO = "KLAX";
	
	public static final String PATH_TO_APT_DATA = "apt.dat";
	static long UPDATE_RATE = 50;  // not a rate but rather the pause time between loops
	private static long UPDATE_TRAFFIC_RATE = 1000; // not a rate but rather the pause time between loops
	private static Boolean DEBUG = false;
	
	private static double ALT_OFFSET = 2.9; // 1.5 for Cessna // 2.9 for b738.
	
	private static double WINDOW_WIDTH = 1000;
	private static double WINDOW_HEIGHT = 800;
	private static double LEFT_PANE_WIDTH = 200;
	
	private static double LEN_OF_TAXI_SHOW_NO_ID = 85;
	private static double DIST_BETWEEN_RUNWAY_IDS = 500;
	
	private static final int NUMBER_OF_SPEED_GRADUATIONS = 10;
	private static final int STEP_OF_SPEED_GRADUATIONS = 5;
	private static final int SPACE_BETWEEN_SPEED_GRADUATIONS = 100;
	
	private static final int LEFT_BAR_TOP_OFFSET = 100;
	// END OF PARAMS THAT CAN BE CHANGED //
	
	private static AptParser aptParser;
	private static Airport airport;
	private static XPConnect xpConnect;
	private static XPConnectTraffic xpConnectTraffic;
	public static CPDLCManager cpdlcManager;
	
	private static Stage primStage;
	private static Group airportMap;
	private static Rotate rotate;
	private static Scale airportMapScale;
	
	private static Group ownshipGroup;
	private static Group taxiwayIdentifiersGroup;
	private static Group runwayIdentifiersGroup;
	private static Group cpdlcFlightPlanGroup;
	private static Group trafficGroup;
	private static Group movingGroup;
	private static Group speedBars;
	private static Group indicatorsGroup;
	
	private static Text currentSpeedText;
	
	private static Text textLat;
	private static Text textLon;
	private static Text textHeading;
	private static Text textX;
	private static Text textY;
	
	private static double lat;
	private static double lon;
	private static double utm_x;
	private static double utm_y;
	private static double currentSpeed;
	
	private static LatLng ownshipLatLon;
	private static UTMRef ownshipUTMRef;
	private static double heading;
	
	private static Boolean OVERRIDE_PAN = false;
	private static Boolean taxiViewStarted = false;
	
	public static void main(String[] args) {

		// START XPLANE CONNECT //
		xpConnect = new XPConnect(Main.UPDATE_RATE);
		xpConnect.start();
		xpConnectTraffic = new XPConnectTraffic(Main.UPDATE_RATE);
		xpConnectTraffic.start();

		//Ingescape agent definition and start //

		//Global globalContext = new Global("ws://localhost:9009");
//		Global globalContext = new Global("ws://172.20.10.3:9009");
		//Global globalContext = new Global("ws://132.207.231.96:5625");
		  Global globalContext = new Global("ws://10.10.10.142:5925");


		Main main = new Main();
		globalContext.observeWebSocketEvents(main); //NEEDED

		JavaAgentCB AmmAgent = new JavaAgentCB(main, xpConnect);

		Agent a = globalContext.agentCreate("AmmAgent");
		a.observeAgentEvents(main);

		a.definition.setName("AMM");
		a.definition.setDescription("AMM agent");
		a.definition.setVersion("1.0");

		a.definition.inputCreate("Reset", IopType.IGS_IMPULSION_T);

		a.definition.inputCreate("lat", IopType.IGS_DOUBLE_T);
		a.definition.inputCreate("lon", IopType.IGS_DOUBLE_T);
		a.definition.inputCreate("height", IopType.IGS_DOUBLE_T);
		a.definition.inputCreate("pitch", IopType.IGS_DOUBLE_T);
		a.definition.inputCreate("roll", IopType.IGS_DOUBLE_T);
		a.definition.inputCreate("heading", IopType.IGS_DOUBLE_T);
		a.definition.inputCreate("elevator", IopType.IGS_DOUBLE_T);
		a.definition.inputCreate("aileron", IopType.IGS_DOUBLE_T);
		a.definition.inputCreate("rudder", IopType.IGS_DOUBLE_T);
		a.definition.inputCreate("fuelQuantity", IopType.IGS_DOUBLE_T);
		a.definition.inputCreate("groundSpeed", IopType.IGS_DOUBLE_T);
		a.definition.inputCreate("brakingAction", IopType.IGS_DOUBLE_T);
		a.definition.inputCreate("thrustCmd", IopType.IGS_DOUBLE_T);
		a.definition.inputCreate("simTimeSinceStart", IopType.IGS_DOUBLE_T);

		a.definition.inputCreate("dashed_lines", IopType.IGS_IMPULSION_T);
		a.definition.inputCreate("solid_lines", IopType.IGS_IMPULSION_T);
		a.definition.inputCreate("alert_lines", IopType.IGS_IMPULSION_T);
		
		a.observeInput("lat", AmmAgent);
		a.observeInput("lon", AmmAgent);
		a.observeInput("height", AmmAgent);
		a.observeInput("pitch", AmmAgent);
		a.observeInput("roll", AmmAgent);
		a.observeInput("heading", AmmAgent);
		a.observeInput("elevator", AmmAgent);
		a.observeInput("aileron", AmmAgent);
		a.observeInput("rudder", AmmAgent);
		a.observeInput("fuelQuantity", AmmAgent);
		a.observeInput("groundSpeed", AmmAgent);
		a.observeInput("brakingAction", AmmAgent);
		a.observeInput("thrustCmd", AmmAgent);
		a.observeInput("simTimeSinceStart", AmmAgent);
		a.observeInput("dashed_lines", AmmAgent);
		a.observeInput("solid_lines", AmmAgent);
		a.observeInput("alert_lines", AmmAgent);
		a.observeInput("Reset", AmmAgent);

		a.start();

    	// GET THE AIRPORT //
    	Main.aptParser = new AptParser(Main.PATH_TO_APT_DATA);
    	Main.airport = aptParser.getAirport(Main.AIRPORT_ICAO);
    	System.out.println("GOT AIRPORT " + Main.airport.getIcaoCode());

    	// LAUNCH AMM //
    	launch(args);
		
	}
	
	@Override
	public void start (Stage primaryStage) throws InterruptedException {

		instance = this;
		
		// INIT ATTRIBUTES TO ALLOW ACCESS TO STAGE IN UPDATE LOOP //
		Main.primStage = primaryStage;
		
		// STOP PROGRAM WHEN CLOSING WINDOW //
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) {
		    	System.out.println("TERMINATING ...");
		        Platform.exit();
		        System.exit(0);
		    }
		});
		
		// MANAGE WINDOW RESIZING //
		primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
			Main.WINDOW_WIDTH = (double) newVal;
		});
		primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
			Main.WINDOW_HEIGHT = (double) newVal;
		});
		
		// INIT STAGE //
		primaryStage.setTitle("CPDLC AMM");
		BorderPane root = new BorderPane();
		primaryStage.setScene(new Scene(root, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));
//		primaryStage.getScene().setFill(Color.DARKGREEN);
		primaryStage.getScene().setFill(Color.web("#0a4525"));
				
		// ADD APT VIEW //
		Pane airportMapPane = new Pane();
		Main.airportMap = makeAirportGroup();
		airportMapPane.getChildren().add(Main.airportMap);
		root.setCenter(airportMapPane);
		airportMapPane.setBackground(new Background(new BackgroundFill(Color.web("#0a4525"), null, null)));
		
		// ADD RUNWAY IDENTIFIERS //
		Main.runwayIdentifiersGroup = makeRunwayIdentifiersGroup();
		Main.airportMap.getChildren().add(Main.runwayIdentifiersGroup);
		
		// ADD CPDLC VIEW //
		Main.cpdlcFlightPlanGroup = new Group();
		Main.airportMap.getChildren().add(Main.cpdlcFlightPlanGroup);
		Main.cpdlcManager = new CPDLCManager(Main.cpdlcFlightPlanGroup, Main.airport, primaryStage);
		
		// CREATE TRAFFIC SYMBOLS //
		Main.trafficGroup = new Group();
		for (int i = 0; i < 19; i++) {
			Traffic traffic = new Traffic(i + 1);
			Main.trafficGroup.getChildren().add(traffic);
		}
		Main.airportMap.getChildren().add(Main.trafficGroup);
		
		// ADD TAXIWAY IDENTIFIERS //
		Main.taxiwayIdentifiersGroup = makeTaxiwayIdentifiersGroup();
		Main.airportMap.getChildren().add(Main.taxiwayIdentifiersGroup);
		
		
		// ADD DEBUG INFO IF DEBUG //
		if (DEBUG) {
			Main.airportMap.getChildren().add(makeDebugInfoOnMapGroup());
		}
		
		// CREATE OWNSHIP OBJ //
		Main.ownshipGroup = makeOwnshipGroup();
		// GET INIT POS XPLANE //
		Main.lat = Main.xpConnect.getLat();
		Main.lon = Main.xpConnect.getLon();
		Main.heading = Main.xpConnect.getHeading();
		Main.ownshipLatLon = new LatLng(Main.lat, Main.lon);
		Main.ownshipUTMRef = Main.ownshipLatLon.toUTMRef();
		
		double init_x = Main.ownshipUTMRef.getEasting();
		double init_y = Main.ownshipUTMRef.getNorthing();
		Main.ownshipGroup.setTranslateX(init_x);
		Main.ownshipGroup.setTranslateY(- init_y);
		Main.ownshipGroup.setRotate(Main.heading);
		Main.airportMap.getChildren().add(Main.ownshipGroup);
		System.out.println("INIT POS X = " + (int)Main.ownshipGroup.getTranslateX() + " Y = " + (int)Main.ownshipGroup.getTranslateY());
		
		// ROTATE TO BE CHANGED FROM UPDATE LOOP //
		Main.rotate = new Rotate();
		Main.airportMap.getTransforms().add(Main.rotate);
		
		// INIT TRANSLATE TO AIRPORT CENTER //
		double[] aptCenter = Main.airport.getCenter(); // already in x y
		Main.airportMap.setTranslateX(- aptCenter[0] + Main.WINDOW_WIDTH / 2 - (Main.LEFT_PANE_WIDTH / 2));
		Main.airportMap.setTranslateY(aptCenter[1] + Main.WINDOW_HEIGHT / 2);
			
		// SET INIT ZOOM FOR APT OVERVIEW //
		Main.airportMapScale = new Scale();
		Main.airportMapScale.setPivotX(aptCenter[0]);
		Main.airportMapScale.setPivotY(- aptCenter[1]);
		Main.airportMap.getTransforms().add(airportMapScale);
		Main.airportMapScale.setX(0.1);
		Main.airportMapScale.setY(0.1);
//		Main.airportMap.setScaleX(0.1);
//		Main.airportMap.setScaleY(0.1);
		
		// ADD DEBUG INFO //
		if (DEBUG) {
			airportMapPane.getChildren().add(makeDebugInfoGroup());
		}
		
		// SETUP KEY BINDINGS //
		primaryStage.getScene().setOnKeyPressed(e -> {
//			System.out.println("KEY PRESSED : " + e.getCode());
			if (e.getCode() == KeyCode.SPACE) {
				if (!Main.taxiViewStarted) {
					System.out.println("STARTING TAXI VIEW");
					Main.taxiViewStarted = true;
		    		Main.airportMapScale.setX(1);
		    		Main.airportMapScale.setY(1);
			        // START UPDATE LOOP //
			    	Thread updateThread = new Thread( ) {
			    		@Override
			    		public void run () {
			    			updateLoop();
			    		}
			    	};
			    	updateThread.start();
			    	Thread updateTrafficThread = new Thread( ) {
			    		@Override
			    		public void run () {
			    			//updateTrafficLoop();
			    		}
			    	};
			    	updateTrafficThread.start();
				}
			} else if (e.getCode() == KeyCode.LEFT) {
				Main.airportMap.setTranslateX(Main.airportMap.getTranslateX() - 10 * Main.airportMapScale.getX());
			} else if (e.getCode() == KeyCode.RIGHT) {
				Main.airportMap.setTranslateX(Main.airportMap.getTranslateX() + 10 * Main.airportMapScale.getX());
			} else if (e.getCode() == KeyCode.UP) {
				Main.airportMap.setTranslateY(Main.airportMap.getTranslateY() - 10 * Main.airportMapScale.getY());
			} else if (e.getCode() == KeyCode.DOWN) {
				Main.airportMap.setTranslateY(Main.airportMap.getTranslateY() + 10 * Main.airportMapScale.getY());
			} else if (e.getCode() == KeyCode.O) {
				System.out.println("OVERRIDE_PAN = " + Main.OVERRIDE_PAN);
				Main.OVERRIDE_PAN = !Main.OVERRIDE_PAN;
			} else if (e.getCode() == KeyCode.COMMA) {
	    		Main.airportMapScale.setX(Main.airportMapScale.getX() + 0.1);
	    		Main.airportMapScale.setY(Main.airportMapScale.getY() + 0.1);
			} else if (e.getCode() == KeyCode.PERIOD) {
	    		Main.airportMapScale.setX(Main.airportMapScale.getX() - 0.1);
	    		Main.airportMapScale.setY(Main.airportMapScale.getY() - 0.1);
			} else if (e.getCode() == KeyCode.C) {
				Main.cpdlcManager.drawNext();
			}
		});
		
		// SHOW STAGE //
		primaryStage.show();
	}
	
	public Group makeDebugInfoGroup() {
		Group debugInfoGroup = new Group();
		
		Main.textLat = new Text("LAT = ");
		Main.textLon = new Text("LON = ");
		Main.textHeading = new Text("HEADING = ");
		Main.textX = new Text("X = ");
		Main.textY = new Text("Y = ");
		
		Main.textLat.setTranslateX(20);
		Main.textLat.setTranslateY(25);
		Main.textLon.setTranslateX(20);
		Main.textLon.setTranslateY(50);
		Main.textHeading.setTranslateX(20);
		Main.textHeading.setTranslateY(75);
		Main.textX.setTranslateX(20);
		Main.textX.setTranslateY(100);
		Main.textY.setTranslateX(20);
		Main.textY.setTranslateY(125);
		
		debugInfoGroup.getChildren().addAll(textLat, textLon, textHeading, textX, textY);
		
		return debugInfoGroup;
	}
	
	public Group makeSpeedPaneGroup() {
		
		// layout params //
		double topOffset = Main.LEFT_BAR_TOP_OFFSET;
		
		Group leftPaneGroup = new Group();
		StackPane leftStackPane = new StackPane();
		leftStackPane.setAlignment(Pos.TOP_CENTER);
		leftPaneGroup.getChildren().add(leftStackPane);
		
		Group groupNotMoving = new Group();
		Main.movingGroup = new Group();
		StackPane movingStackPane = new StackPane();
		movingStackPane.setAlignment(Pos.CENTER);
		Main.speedBars = new Group();
		Main.indicatorsGroup = new Group();
		Group groupNotMovingOnTop = new Group();
		
		leftStackPane.getChildren().add(groupNotMoving);
		leftStackPane.getChildren().add(Main.movingGroup);
		Main.movingGroup.getChildren().add(movingStackPane);
		movingStackPane.getChildren().add(Main.speedBars);
		movingStackPane.getChildren().add(Main.indicatorsGroup);
		leftStackPane.getChildren().add(groupNotMovingOnTop);
		
		// GROUP NOT MOVING //
		groupNotMoving.setTranslateY(topOffset);
		
		Rectangle speedBar = new Rectangle (0, 0, 80, Main.WINDOW_HEIGHT - 200);
		speedBar.setFill(Color.DARKGRAY);
		groupNotMoving.getChildren().add(speedBar);
		
		Polygon speedIndicBox = new Polygon();
		double speedIndicSizeFactor = 1.5;
		speedIndicBox.getPoints().addAll(new Double[] {
				30. * speedIndicSizeFactor, 20. * speedIndicSizeFactor, 
				- 20. * speedIndicSizeFactor, 20. * speedIndicSizeFactor, 
				-20. * speedIndicSizeFactor, 5. * speedIndicSizeFactor,
				-30. * speedIndicSizeFactor, 0. * speedIndicSizeFactor,
				-20. * speedIndicSizeFactor, -5. * speedIndicSizeFactor,
				-20. * speedIndicSizeFactor, -20. * speedIndicSizeFactor,
				30. * speedIndicSizeFactor, - 20 * speedIndicSizeFactor
		});
		speedIndicBox.setFill(Color.BLACK);
		speedIndicBox.setStroke(Color.WHITE);
		speedIndicBox.setStrokeWidth(2);
		speedIndicBox.setRotate(180);
		speedIndicBox.setTranslateY(speedBar.getHeight() / 2);
		groupNotMoving.getChildren().add(speedIndicBox);
		
		Main.currentSpeedText = new Text("--.-");
		Main.currentSpeedText.setFont(new Font(30));
		Main.currentSpeedText.setFill(Color.WHITE);
		Main.currentSpeedText.setTranslateX(-30);
		Main.currentSpeedText.setTranslateY(speedBar.getHeight() / 2 + Main.currentSpeedText.getLayoutBounds().getHeight() / 2 - 10);
		groupNotMoving.getChildren().add(Main.currentSpeedText);

		// GROUP NOT MOVING ON TOP //
		Rectangle topRect = new Rectangle(0, 0, Main.LEFT_PANE_WIDTH, Main.LEFT_BAR_TOP_OFFSET);
		topRect.setFill(Color.BLACK);
		groupNotMovingOnTop.getChildren().add(topRect);
		Rectangle bottomRect = new Rectangle(0, Main.LEFT_BAR_TOP_OFFSET + speedBar.getHeight(), Main.LEFT_PANE_WIDTH, Main.WINDOW_HEIGHT - (Main.LEFT_BAR_TOP_OFFSET + speedBar.getHeight()));
		bottomRect.setFill(Color.BLACK);
		groupNotMovingOnTop.getChildren().add(bottomRect);
		
		// MOVING STACK PANE //
		
		// SPEED BARS //
		int numberOfGraduations = Main.NUMBER_OF_SPEED_GRADUATIONS;
		int stepOfGraduations = Main.STEP_OF_SPEED_GRADUATIONS;
		int spaceBetweenGraduations = Main.SPACE_BETWEEN_SPEED_GRADUATIONS;
		for (int i = 0; i <= numberOfGraduations; i ++) {
			Text speedText = new Text(String.valueOf(i * stepOfGraduations));
			speedText.setTranslateY((numberOfGraduations - spaceBetweenGraduations) - i * spaceBetweenGraduations);
			speedText.setFill(Color.WHITE);
			speedText.setFont(new Font(25));
			Main.speedBars.getChildren().add(speedText);
		}
		Main.movingGroup.setTranslateY(- Main.movingGroup.getLayoutBounds().getHeight() / 2 - topOffset);
		Main.speedBars.setTranslateX(120);
		
		return leftPaneGroup;
	}
	
	public Group makeAirportGroup() {
		
		Group airportMap = new Group();
		
		System.out.println("DRAWING AIRPORT " + Main.airport.getIcaoCode());
		System.out.println("AIRPORT CENTER X = " + (int)Main.airport.getCenter()[0] + " Y = " + (int)Main.airport.getCenter()[1]);
		
		// DRAW TAXIWAYS //
		for (TaxiwaySegment taxiway : Main.airport.getTaxiwaySegments()) {
			TaxiwayNode nodeStart = taxiway.getNodes().get(0);
			TaxiwayNode nodeEnd = taxiway.getNodes().get(1);
			LatLng latLonStart = new LatLng(nodeStart.getCoords()[0], nodeStart.getCoords()[1]);
			LatLng latLonEnd = new LatLng(nodeEnd.getCoords()[0], nodeEnd.getCoords()[1]);
			UTMRef utmStart = latLonStart.toUTMRef();
			UTMRef utmEnd = latLonEnd.toUTMRef();
			double xStart = utmStart.getEasting();
			double yStart = utmStart.getNorthing();
			double xEnd = utmEnd.getEasting();
			double yEnd = utmEnd.getNorthing();
			Line line = new Line(xStart, - yStart, xEnd, - yEnd);
			line.setStrokeWidth(50);
			line.setStroke(Color.DARKGRAY);
			airportMap.getChildren().add(line);
//			System.out.println("DRAWING TAXIWAY AT: START X = " + xyStart[0] + ", Y = " + xyStart[1] + " / END X = " 
//			+ xyEnd[0] + ", Y = " + xyEnd[1]);
		}
		
		// DRAW CENTERLINES //
		for (TaxiwaySegment taxiway : Main.airport.getTaxiwaySegments()) {
			TaxiwayNode nodeStart = taxiway.getNodes().get(0);
			TaxiwayNode nodeEnd = taxiway.getNodes().get(1);
			LatLng latLonStart = new LatLng(nodeStart.getCoords()[0], nodeStart.getCoords()[1]);
			LatLng latLonEnd = new LatLng(nodeEnd.getCoords()[0], nodeEnd.getCoords()[1]);
			UTMRef utmStart = latLonStart.toUTMRef();
			UTMRef utmEnd = latLonEnd.toUTMRef();
			double xStart = utmStart.getEasting();
			double yStart = utmStart.getNorthing();
			double xEnd = utmEnd.getEasting();
			double yEnd = utmEnd.getNorthing();
			Line line = new Line(xStart, - yStart, xEnd, - yEnd);
			line.setStrokeWidth(3);
			line.setStroke(Color.YELLOW);
			airportMap.getChildren().add(line);
		}
		
		// DRAW RUNWAYS //
		for (Runway runway : Main.airport.getRunways()) {
			LatLng latLonStart = new LatLng(runway.getLat1(), runway.getLon1());
			LatLng latLonEnd = new LatLng(runway.getLat2(), runway.getLon2());
			UTMRef utmStart = latLonStart.toUTMRef();
			UTMRef utmEnd = latLonEnd.toUTMRef();
			double xStart = utmStart.getEasting();
			double yStart = utmStart.getNorthing();
			double xEnd = utmEnd.getEasting();
			double yEnd = utmEnd.getNorthing();
			Line line = new Line(xStart, - yStart, xEnd, - yEnd);
			line.setStrokeWidth(70);
			line.setStroke(Color.GRAY);
			airportMap.getChildren().add(line);
			Line lineCenterline = new Line(xStart, - yStart, xEnd, - yEnd);
			lineCenterline.setStroke(Color.WHITE);
			lineCenterline.setStrokeWidth(8);
			lineCenterline.getStrokeDashArray().addAll(20d, 20d);
			airportMap.getChildren().add(lineCenterline);
		}
		
		return airportMap;
	}
	
	public Group makeTaxiwayIdentifiersGroup() {
		Group taxiwayIdentifiers = new Group ();
		
		// one id only at middle of taxiway //
		for (Taxiway taxiway : Main.airport.getTaxiways()) {
			if (!taxiway.getIsTaxiwayOrRunway().equals("runway") && !taxiway.getName().equals("NONAME")) {
				if (taxiway.getLength() > Main.LEN_OF_TAXI_SHOW_NO_ID) {
					String name = taxiway.getName();
					
					double targetLength = taxiway.getLength() / 2;
					double currentLength = 0;
					double xID = 0;
					double yID = 0;
					
					for (TaxiwaySegment segment : taxiway.getSegments()) {
						if (currentLength < targetLength && currentLength + segment.getLength() > targetLength) {
							double remainingLength = targetLength - currentLength;
							double factor = remainingLength / segment.getLength();
							TaxiwayNode nodeStart = segment.getNodes().get(0);
							TaxiwayNode nodeEnd = segment.getNodes().get(1);
							LatLng latLonStart = new LatLng(nodeStart.getCoords()[0], nodeStart.getCoords()[1]);
							LatLng latLonEnd = new LatLng(nodeEnd.getCoords()[0], nodeEnd.getCoords()[1]);
							UTMRef utmStart = latLonStart.toUTMRef();
							UTMRef utmEnd = latLonEnd.toUTMRef();
							double xStart = utmStart.getEasting();
							double yStart = utmStart.getNorthing();
							double xEnd = utmEnd.getEasting();
							double yEnd = utmEnd.getNorthing();
							xID = xStart + (xEnd - xStart) * factor;
							yID = yStart + (yEnd - yStart) * factor;
						}
						currentLength += segment.getLength();
					}
					
					Text textTaxiwayName = new Text(name);
					textTaxiwayName.setFont(new Font(25));
					textTaxiwayName.setFill(Color.YELLOW);
					textTaxiwayName.setStrokeWidth(1);
					textTaxiwayName.setStroke(Color.BLACK);
					textTaxiwayName.setTranslateX(xID - textTaxiwayName.getLayoutBounds().getWidth() / 2);
					textTaxiwayName.setTranslateY(- (yID - textTaxiwayName.getLayoutBounds().getHeight() / 2));
					taxiwayIdentifiers.getChildren().add(textTaxiwayName);
				}
			}
		}

		return taxiwayIdentifiers;
	}
	
	
	public Group makeRunwayIdentifiersGroup() {
		Group runwayIdentifiers = new Group();
		
		for (Runway runway : Main.airport.getRunways()) {
			LatLng latLonStart = new LatLng(runway.getLat1(), runway.getLon1());
			LatLng latLonEnd = new LatLng(runway.getLat2(), runway.getLon2());
			UTMRef utmStart = latLonStart.toUTMRef();
			UTMRef utmEnd = latLonEnd.toUTMRef();
			double xStart = utmStart.getEasting();
			double yStart = utmStart.getNorthing();
			double xEnd = utmEnd.getEasting();
			double yEnd = utmEnd.getNorthing();
			
			double totalDist = Math.sqrt(Math.pow(xEnd - xStart, 2) + Math.pow(yEnd - yStart, 2));
			int numberOfID = (int)(totalDist / Main.DIST_BETWEEN_RUNWAY_IDS);
			double distBetween = totalDist/numberOfID;
			
			double factor = distBetween / totalDist;
			
			for (int i = 0; i <= numberOfID; i ++) {
				Text runwayName = new Text(runway.getRunwayID());
				runwayName.setFont(new Font(30));
				runwayName.setFill(Color.WHITE);
				Rectangle background = new Rectangle();
				background.setFill(Color.RED);
				background.setHeight(runwayName.getLayoutBounds().getHeight());
				background.setWidth(runwayName.getLayoutBounds().getWidth());
				background.setFill(Color.RED);
				
				double posX = xStart + (xEnd - xStart) * (factor * i);
				double posY = yStart + (yEnd - yStart) * (factor * i);
				
				runwayName.setTranslateX(posX - runwayName.getLayoutBounds().getWidth() / 2);
				runwayName.setTranslateY(- (posY - runwayName.getLayoutBounds().getHeight() / 2));
				background.setTranslateX(posX - runwayName.getLayoutBounds().getWidth() / 2);
				background.setTranslateY(- (posY + runwayName.getLayoutBounds().getHeight() / 4));
				
				Group group = new Group();
				group.getChildren().add(background);
				group.getChildren().add(runwayName);
				runwayIdentifiers.getChildren().add(group);
				
			}
			
		}
		
		return runwayIdentifiers;
	}
	
	
	public Group makeDebugInfoOnMapGroup() {
		Group debugInfoOnMapGroup = new Group();
		
		for (TaxiwaySegment taxiway : Main.airport.getTaxiwaySegments()) {
			String name = taxiway.getName();
			TaxiwayNode nodeStart = taxiway.getNodes().get(0);
			TaxiwayNode nodeEnd = taxiway.getNodes().get(1);
			LatLng latLonStart = new LatLng(nodeStart.getCoords()[0], nodeStart.getCoords()[1]);
			LatLng latLonEnd = new LatLng(nodeEnd.getCoords()[0], nodeEnd.getCoords()[1]);
			UTMRef utmStart = latLonStart.toUTMRef();
			UTMRef utmEnd = latLonEnd.toUTMRef();
			double xStart = utmStart.getEasting();
			double yStart = utmStart.getNorthing();
			double xEnd = utmEnd.getEasting();
			double yEnd = utmEnd.getNorthing();
			double xMid = (xStart + xEnd) / 2;
			double yMid = (yStart + yEnd) / 2;
			Text textTaxiwayName = new Text(name);
			textTaxiwayName.setTranslateX(xMid);
			textTaxiwayName.setTranslateY(- yMid);
			airportMap.getChildren().add(textTaxiwayName);
			for (int i = 0; i < taxiway.getNodes().size(); i++) {
					TaxiwayNode node = taxiway.getNodes().get(i);
					LatLng nodeLatLon = new LatLng(node.getCoords()[0], node.getCoords()[1]);
					UTMRef nodeUTMRef = nodeLatLon.toUTMRef();
					double nodeX = nodeUTMRef.getEasting();
					double nodeY = nodeUTMRef.getNorthing();
					Text nodeIdText = new Text(String.valueOf(node.getId()));
//					Text nodeIdText = new Text(String.valueOf(node.getId()) + " / " + node.getUsage());
					nodeIdText.setTranslateX(nodeX);
					nodeIdText.setTranslateY(- nodeY);
					if (node.getIsInteresect() || node.getIsChangeOfName() || node.getIsEnd()) {
						nodeIdText.setStroke(Color.RED);
					}
					debugInfoOnMapGroup.getChildren().add(nodeIdText);
				}
			}
		
		return debugInfoOnMapGroup;
	}
	
	public Group makeOwnshipGroup() {
		Group ownshipGroup = new Group();
		
		Polygon ownshipSymbol = new Polygon();
		ownshipSymbol.getPoints().addAll(new Double[]{
                -4.0, -40.0,
                -4.0, -12.0,
                -40.0, -12.0, 
                -40.0, -4.0, 
                -4.0, -4.0,
                -4.0, 32.0, 
                -16.0, 32.0, 
                -16.0, 40.0, 
                -4.0, 40.0,
                -4.0, 48.0, 
                4.0, 48.0,
                4.0, 40.0, 
                16.0, 40.0, 
                16.0, 32.0, 
                4.0, 32.0, 
                4.0, -4.0, 
                40.0, -4.0, 
                40.0, -12.0, 
                4.0, -12.0, 
                4.0, -40.0,
                });
		ownshipSymbol.setFill(Color.BLUE);
		ownshipSymbol.setStroke(Color.BLACK);
		ownshipSymbol.setStrokeWidth(1);
        
        ownshipGroup.getChildren().add(ownshipSymbol);
		
		return ownshipGroup;
	}
	
    public void updateLoop () {
    					
    	while(true) {
    		
    		// GET NEW POS FROM X PLANE //
    		Main.lat = Main.xpConnect.getLat();
    		Main.lon = Main.xpConnect.getLon();
    		Main.heading = Main.xpConnect.getHeading();
    		Main.currentSpeed = Main.xpConnect.getGroundSpeed() * 1.944;
    		
    		// CONVERT, SCALE AND UPDATE POS IN SVS //
    		Main.ownshipLatLon = new LatLng(Main.lat, Main.lon);
    		Main.ownshipUTMRef = Main.ownshipLatLon.toUTMRef();
    		
       		Main.utm_x = Main.ownshipUTMRef.getEasting();
    		Main.utm_y = Main.ownshipUTMRef.getNorthing();
    		
    		Main.airportMapScale.setPivotX(Main.utm_x);
    		Main.airportMapScale.setPivotY(- Main.utm_y);
    		
    		if (!OVERRIDE_PAN) {

    			Main.airportMap.setTranslateX(- Main.utm_x + Main.WINDOW_WIDTH * 0.5 - Main.LEFT_PANE_WIDTH * 0.5);
        		Main.airportMap.setTranslateY(Main.utm_y + Main.WINDOW_HEIGHT * 0.8);
    			
    		}
    		
    		Main.ownshipGroup.setTranslateX(Main.utm_x);
    		Main.ownshipGroup.setTranslateY(- Main.utm_y);
    		Main.ownshipGroup.setRotate(Main.heading);
    		
    		for (Node taxiwayId : Main.taxiwayIdentifiersGroup.getChildren()) {
    			taxiwayId.setRotate(Main.heading);
    		}
    		
    		for (Node runwayID : Main.runwayIdentifiersGroup.getChildren()) {
    			runwayID.setRotate(Main.heading);
    		}
    		
    		if (!OVERRIDE_PAN) {
        		Main.rotate.setPivotX(Main.utm_x);
        		Main.rotate.setPivotY(- Main.utm_y);
        		Main.rotate.setAngle(- Main.heading);
    		}
    		
    		if (DEBUG) {
    			Main.textLat.setText("LAT = " + Main.lat);
    			Main.textLon.setText("LON = " + Main.lon);
    			Main.textHeading.setText("HEADING = " + Main.heading);
    			Main.textX.setText("X = " + Main.utm_x);
    			Main.textY.setText("Y = " + Main.utm_y);
    		}
    		
    		
            try
            {
                Thread.sleep(Main.UPDATE_RATE);
            }
            catch (InterruptedException ex) {}
    	}
    }
    
    public void updateTrafficLoop() {
    	while (true) {
    		
    		for (int i = 0; i < 19; i++) {
    			double[] trafficParams = Main.xpConnectTraffic.getTrafficParams(i);
    			Node ac = Main.trafficGroup.getChildren().get(i);
    			LatLng latLon = new LatLng(trafficParams[0], trafficParams[1]);
    			UTMRef utm = latLon.toUTMRef();
    			double x = utm.getEasting();
    			double y = utm.getNorthing();
    			ac.setTranslateX(x);
    			ac.setTranslateY(- y);
    			ac.setRotate(trafficParams[6]);
    		}
            try
            {
                Thread.sleep(Main.UPDATE_TRAFFIC_RATE);
            }
            catch (InterruptedException ex) {}
    	}
    }
	
}
