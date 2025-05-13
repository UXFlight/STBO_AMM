package main;

import java.util.ArrayList;
import java.util.List;

import com.sun.jna.platform.unix.X11;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;
import javafx.geometry.Pos;

public class CPDLCManager {

	private Group drawingGroup;
	private Airport airport;
	private Stage primaryStage;
	public int step;
	private ArrayList<TaxiwayNode> nodesFlightPlan1;
	private ArrayList<TaxiwayNode> nodesFlightPlan2;
	private ArrayList<TaxiwayNode> nodesFlightPlanYellow;

	public CPDLCManager (Group group, Airport airport, Stage primaryStage) {
		this.drawingGroup = group;
		this.airport = airport;
		this.primaryStage = primaryStage;
		this.step = 0;
		this.nodesFlightPlan1 = new ArrayList<TaxiwayNode>();
		this.nodesFlightPlan2 = new ArrayList<TaxiwayNode>();
		this.nodesFlightPlanYellow = new ArrayList<TaxiwayNode>();
		// create taxiway nodes for flight plan //
		ArrayList<Integer> taxiwayNodesID = new ArrayList<Integer>();
		//test
		//taxiwayNodesID.add(178);
		//taxiwayNodesID.add(179);
		//taxiwayNodesID.add(324);
		//taxiwayNodesID.add(325);
		//taxiwayNodesID.add(326);
		//taxiwayNodesID.add(294);
		//taxiwayNodesID.add(295);
		//taxiwayNodesID.add(296);
		//taxiwayNodesID.add(297);
		//taxiwayNodesID.add(258);
		//taxiwayNodesID.add(298);
		//taxiwayNodesID.add(132);
		//taxiwayNodesID.add(119);
		//taxiwayNodesID.add(120);
		//taxiwayNodesID.add(121);
		//taxiwayNodesID.add(122);
		//taxiwayNodesID.add(123);
		//taxiwayNodesID.add(124);
		//taxiwayNodesID.add(125);
		//taxiwayNodesID.add(127);
		//taxiwayNodesID.add(318);
		//taxiwayNodesID.add(319);
		//taxiwayNodesID.add(317);
		//taxiwayNodesID.add(316);
		//taxiwayNodesID.add(328);
		//taxiwayNodesID.add(364);

		//Non nominal -1
//		taxiwayNodesID.add(188);
//		taxiwayNodesID.add(286);
//		taxiwayNodesID.add(814);
//		taxiwayNodesID.add(290);
//		taxiwayNodesID.add(528);
//		taxiwayNodesID.add(289);
//		taxiwayNodesID.add(288);
//		taxiwayNodesID.add(382);
//		taxiwayNodesID.add(287);
//		taxiwayNodesID.add(291);
//		taxiwayNodesID.add(293);
//		taxiwayNodesID.add(811);
//		taxiwayNodesID.add(810);
//		taxiwayNodesID.add(813);
//		taxiwayNodesID.add(292);
//		taxiwayNodesID.add(808);
//		taxiwayNodesID.add(277);
//		taxiwayNodesID.add(275);
//		taxiwayNodesID.add(274);
//		taxiwayNodesID.add(273);
//		taxiwayNodesID.add(272);
//		taxiwayNodesID.add(248);
//		taxiwayNodesID.add(249);

		//Non nominal -2
//		taxiwayNodesID.add(61);
//		taxiwayNodesID.add(62);
//		taxiwayNodesID.add(64);
//		taxiwayNodesID.add(63);
//		taxiwayNodesID.add(316);
//		taxiwayNodesID.add(317);
//		taxiwayNodesID.add(319);
//		taxiwayNodesID.add(318);
//		taxiwayNodesID.add(127);
//		taxiwayNodesID.add(125);
//		taxiwayNodesID.add(124);
//		taxiwayNodesID.add(123);
//		taxiwayNodesID.add(864);
//		taxiwayNodesID.add(122);
//		taxiwayNodesID.add(863);
//		taxiwayNodesID.add(121);
//		taxiwayNodesID.add(107);
//
//		taxiwayNodesID.add(862);
//		taxiwayNodesID.add(120);
//		taxiwayNodesID.add(861);
//		taxiwayNodesID.add(119);
//		taxiwayNodesID.add(860);
//		taxiwayNodesID.add(132);
//		taxiwayNodesID.add(859);
//		taxiwayNodesID.add(298);
//		taxiwayNodesID.add(858);
//		taxiwayNodesID.add(258);
//		taxiwayNodesID.add(297);
//		taxiwayNodesID.add(854);
//		taxiwayNodesID.add(296);
//		taxiwayNodesID.add(295);
//		taxiwayNodesID.add(294);
//		taxiwayNodesID.add(852);
//		taxiwayNodesID.add(849);
//		taxiwayNodesID.add(324);
//		taxiwayNodesID.add(178);

		//non nominal-3
		taxiwayNodesID.add(178);
		taxiwayNodesID.add(847);
		taxiwayNodesID.add(324);
		taxiwayNodesID.add(849);
		taxiwayNodesID.add(852);
		taxiwayNodesID.add(294);
		taxiwayNodesID.add(295);
		taxiwayNodesID.add(296);
		taxiwayNodesID.add(854);
		taxiwayNodesID.add(297);
		taxiwayNodesID.add(258);
		taxiwayNodesID.add(858);
		taxiwayNodesID.add(298);
		taxiwayNodesID.add(859);
		taxiwayNodesID.add(132);
		taxiwayNodesID.add(860);
		taxiwayNodesID.add(119);
		taxiwayNodesID.add(861);
		taxiwayNodesID.add(120);
		taxiwayNodesID.add(862);
		taxiwayNodesID.add(121);
		taxiwayNodesID.add(863);
		taxiwayNodesID.add(122);
		taxiwayNodesID.add(864);
		taxiwayNodesID.add(123);
		taxiwayNodesID.add(866);
		taxiwayNodesID.add(124);
		taxiwayNodesID.add(125);
		taxiwayNodesID.add(127);
		taxiwayNodesID.add(318);
		taxiwayNodesID.add(319);
		taxiwayNodesID.add(317);
		taxiwayNodesID.add(316);
		taxiwayNodesID.add(328);
		taxiwayNodesID.add(66);
		taxiwayNodesID.add(364);

		//non nominal- 4
//		taxiwayNodesID.add(133);
//		taxiwayNodesID.add(131);
//		taxiwayNodesID.add(132);
//		taxiwayNodesID.add(860);
//		taxiwayNodesID.add(119);
//		taxiwayNodesID.add(861);
//		taxiwayNodesID.add(120);
//		taxiwayNodesID.add(862);
//		taxiwayNodesID.add(121);
//		taxiwayNodesID.add(863);
//		taxiwayNodesID.add(122);
//		taxiwayNodesID.add(864);
//		taxiwayNodesID.add(123);
//		taxiwayNodesID.add(866);
//		taxiwayNodesID.add(124);
//		taxiwayNodesID.add(125);
//		taxiwayNodesID.add(127);
//		taxiwayNodesID.add(318);
//		taxiwayNodesID.add(319);
//		taxiwayNodesID.add(317);
//		taxiwayNodesID.add(316);
//		taxiwayNodesID.add(328);
//		taxiwayNodesID.add(66);
//		taxiwayNodesID.add(364);

		//departure nominal-5
//		taxiwayNodesID.add(86);
//		taxiwayNodesID.add(87);
//		taxiwayNodesID.add(700);
//		taxiwayNodesID.add(318);
//		taxiwayNodesID.add(319);
//		taxiwayNodesID.add(317);
//		taxiwayNodesID.add(316);
//		taxiwayNodesID.add(63);
//		taxiwayNodesID.add(64);
//		taxiwayNodesID.add(62);
//		taxiwayNodesID.add(61);

		//arrival nominal-6
//		taxiwayNodesID.add(145);
//		taxiwayNodesID.add(143);
//		taxiwayNodesID.add(160);
//		taxiwayNodesID.add(159);
//		taxiwayNodesID.add(23);
//		taxiwayNodesID.add(24);
//		taxiwayNodesID.add(829);
//		taxiwayNodesID.add(297);
//		taxiwayNodesID.add(854);
//		taxiwayNodesID.add(296);
//		taxiwayNodesID.add(21);
//		taxiwayNodesID.add(775);
//		taxiwayNodesID.add(20);

		for (int i : taxiwayNodesID) {
			TaxiwayNode node = this.airport.getNodeById(i);
			this.nodesFlightPlan1.add(node);
		}
		
		ArrayList<Integer> taxiwayNodesID2 = new ArrayList<Integer>();
		//test
//		taxiwayNodesID2.add(178);
//		taxiwayNodesID2.add(179);
//		taxiwayNodesID2.add(324);
//		taxiwayNodesID2.add(325);
//		taxiwayNodesID2.add(326);
//		taxiwayNodesID2.add(294);
//		taxiwayNodesID2.add(295);
//		taxiwayNodesID2.add(296);
//		taxiwayNodesID2.add(297);
//		taxiwayNodesID2.add(258);
//		taxiwayNodesID2.add(298);
//		taxiwayNodesID2.add(132);
//		taxiwayNodesID2.add(119);
//		taxiwayNodesID2.add(120);
//		taxiwayNodesID2.add(121);
//		taxiwayNodesID2.add(122);
//		taxiwayNodesID2.add(123);
//		taxiwayNodesID2.add(124);
//		taxiwayNodesID2.add(125);
//		taxiwayNodesID2.add(127);
//		taxiwayNodesID2.add(318);
//		taxiwayNodesID2.add(314);
//		taxiwayNodesID2.add(80);

		//non nominal 1
//		taxiwayNodesID2.add(291);
//		taxiwayNodesID2.add(293);
//		taxiwayNodesID2.add(811);
//		taxiwayNodesID2.add(810);
//		taxiwayNodesID2.add(813);
//		taxiwayNodesID2.add(292);
//		taxiwayNodesID2.add(808);
//		taxiwayNodesID2.add(277);
//		taxiwayNodesID2.add(275);
//		taxiwayNodesID2.add(274);
//		taxiwayNodesID2.add(273);
//		taxiwayNodesID2.add(272);
//		taxiwayNodesID2.add(276);
//		taxiwayNodesID2.add(321);
//		taxiwayNodesID2.add(336);
//		taxiwayNodesID2.add(329);
//		taxiwayNodesID2.add(330);
//		taxiwayNodesID2.add(327);
//		taxiwayNodesID2.add(157);

		//non nominal -2 arrival
//		taxiwayNodesID2.add(61);
//		taxiwayNodesID2.add(62);
//		taxiwayNodesID2.add(64);
//		taxiwayNodesID2.add(63);
//		taxiwayNodesID2.add(316);
//		taxiwayNodesID2.add(317);
//		taxiwayNodesID2.add(319);
//		taxiwayNodesID2.add(318);
//		taxiwayNodesID2.add(127);
//		taxiwayNodesID2.add(125);
//		taxiwayNodesID2.add(124);
//		taxiwayNodesID2.add(123);
//		taxiwayNodesID2.add(864);
//		taxiwayNodesID2.add(122);
//		taxiwayNodesID2.add(863);
//		taxiwayNodesID2.add(121);
//		taxiwayNodesID2.add(862);
//		taxiwayNodesID2.add(120);
//		taxiwayNodesID2.add(861);
//		taxiwayNodesID2.add(119);
//		taxiwayNodesID2.add(860);
//		taxiwayNodesID2.add(132);
//		taxiwayNodesID2.add(859);
//		taxiwayNodesID2.add(298);
//		taxiwayNodesID2.add(858);
//		taxiwayNodesID2.add(258);
//		taxiwayNodesID2.add(297);
//		taxiwayNodesID2.add(854);
//		taxiwayNodesID2.add(296);
//		taxiwayNodesID2.add(295);
//		taxiwayNodesID2.add(294);
//		taxiwayNodesID2.add(852);
//		taxiwayNodesID2.add(849);
//		taxiwayNodesID2.add(324);
//		taxiwayNodesID2.add(178);

		//non nominal-3
		taxiwayNodesID2.add(178);
		taxiwayNodesID2.add(847);
		taxiwayNodesID2.add(324);
		taxiwayNodesID2.add(849);
		taxiwayNodesID2.add(852);
		taxiwayNodesID2.add(294);
		taxiwayNodesID2.add(295);
		taxiwayNodesID2.add(296);
		taxiwayNodesID2.add(854);
		taxiwayNodesID2.add(297);
		taxiwayNodesID2.add(258);
		taxiwayNodesID2.add(858);
		taxiwayNodesID2.add(298);
		taxiwayNodesID2.add(859);
		taxiwayNodesID2.add(132);
		taxiwayNodesID2.add(860);
		taxiwayNodesID2.add(119);
		taxiwayNodesID2.add(861);
		taxiwayNodesID2.add(120);
		taxiwayNodesID2.add(862);
		taxiwayNodesID2.add(121);
		taxiwayNodesID2.add(863);
		taxiwayNodesID2.add(122);
		taxiwayNodesID2.add(864);
		taxiwayNodesID2.add(123);
		taxiwayNodesID2.add(866);
		taxiwayNodesID2.add(124);
		taxiwayNodesID2.add(125);
		taxiwayNodesID2.add(127);
		taxiwayNodesID2.add(318);
		taxiwayNodesID2.add(319);
		taxiwayNodesID2.add(317);
		taxiwayNodesID2.add(316);
		taxiwayNodesID2.add(63);
		taxiwayNodesID2.add(64);

		//non nominal- 4
//		taxiwayNodesID2.add(133);
//		taxiwayNodesID2.add(131);
//		taxiwayNodesID2.add(132);
//		taxiwayNodesID2.add(860);
//		taxiwayNodesID2.add(119);
//		taxiwayNodesID2.add(861);
//		taxiwayNodesID2.add(120);
//		taxiwayNodesID2.add(862);
//		taxiwayNodesID2.add(121);
//		taxiwayNodesID2.add(863);
//		taxiwayNodesID2.add(122);
//		taxiwayNodesID2.add(864);
//		taxiwayNodesID2.add(123);
//		taxiwayNodesID2.add(866);
//		taxiwayNodesID2.add(124);
//		taxiwayNodesID2.add(125);
//		taxiwayNodesID2.add(127);
//		taxiwayNodesID2.add(782);
//		taxiwayNodesID2.add(313);
//		taxiwayNodesID2.add(312);
//		taxiwayNodesID2.add(311);
//		taxiwayNodesID2.add(310);
//		taxiwayNodesID2.add(309);
//		taxiwayNodesID2.add(308);
//		taxiwayNodesID2.add(307);
//		taxiwayNodesID2.add(306);
//		taxiwayNodesID2.add(305);
//		taxiwayNodesID2.add(304);
//		taxiwayNodesID2.add(303);
//		taxiwayNodesID2.add(302);
//		taxiwayNodesID2.add(301);
//		taxiwayNodesID2.add(300);
//		taxiwayNodesID2.add(299);
//		taxiwayNodesID2.add(335);
//		taxiwayNodesID2.add(334);
//		taxiwayNodesID2.add(333);
//		taxiwayNodesID2.add(332);
//		taxiwayNodesID2.add(327);
//		taxiwayNodesID2.add(157);
//		taxiwayNodesID2.add(158);

		for (int i : taxiwayNodesID2) {
			TaxiwayNode node = this.airport.getNodeById(i);
			this.nodesFlightPlan2.add(node);
		}

		ArrayList<Integer> taxiwayNodesYellow = new ArrayList<Integer>();
		//test
//		taxiwayNodesYellow.add(119);
//		taxiwayNodesYellow.add(120);
//		taxiwayNodesYellow.add(121);
//		taxiwayNodesYellow.add(122);
//		taxiwayNodesYellow.add(123);
//		taxiwayNodesYellow.add(124);
//		taxiwayNodesYellow.add(125);
//		taxiwayNodesYellow.add(127);
//		taxiwayNodesYellow.add(318);
//		taxiwayNodesYellow.add(314);
//		taxiwayNodesYellow.add(80);

		//non nominal 1
//		taxiwayNodesYellow.add(272);
//		taxiwayNodesYellow.add(248);
//		taxiwayNodesYellow.add(249);

		//non nominal 2-arrival
//		taxiwayNodesYellow.add(121);
//		taxiwayNodesYellow.add(107);

		//non nominal 3
		taxiwayNodesYellow.add(316);
		taxiwayNodesYellow.add(328);
		taxiwayNodesYellow.add(66);
		taxiwayNodesYellow.add(364);

		//NON NOMINAL 4
//		taxiwayNodesYellow.add(127);
//		taxiwayNodesYellow.add(318);
//		taxiwayNodesYellow.add(319);
//		taxiwayNodesYellow.add(317);
//		taxiwayNodesYellow.add(316);
//		taxiwayNodesYellow.add(328);
//		taxiwayNodesYellow.add(66);
//		taxiwayNodesYellow.add(364);

		for (int i : taxiwayNodesYellow) {
			TaxiwayNode node = this.airport.getNodeById(i);
			this.nodesFlightPlanYellow.add(node);
		}
		
	}
	
	public void drawNext () {
		switch (this.step) {
			case 0:
				this.drawFlightPlanStep1();
				break;
			case 1:
				this.drawFlightPlanStep2();
				break;
			case 2:
				this.drawFlightPlanStep3();
				this.drawPopup();
				break;
			case 3:
				this.resetFlightPlan();
				break;
		}
		this.step += 1;
	}
	
	
	public void drawFlightPlanStep1 () {
		Platform.runLater(() -> {
			System.out.println("DRAWING STEP 1");
			this.drawingGroup.getChildren().clear();
			// DRAW FP 1 IN PINK DASHED LINE //
			for (int i = 0; i < this.nodesFlightPlan1.size() - 1; i++) {
				TaxiwayNode nodeStart = this.nodesFlightPlan1.get(i);
				TaxiwayNode nodeEnd = this.nodesFlightPlan1.get(i + 1);
				LatLng latLonStart = new LatLng(nodeStart.getCoords()[0], nodeStart.getCoords()[1]);
				LatLng latLonEnd = new LatLng(nodeEnd.getCoords()[0], nodeEnd.getCoords()[1]);
				UTMRef utmStart = latLonStart.toUTMRef();
				UTMRef utmEnd = latLonEnd.toUTMRef();
				double xStart = utmStart.getEasting();
				double yStart = utmStart.getNorthing();
				double xEnd = utmEnd.getEasting();
				double yEnd = utmEnd.getNorthing();
				Line line = new Line(xStart, -yStart, xEnd, -yEnd);
				line.setStrokeWidth(30);
				line.setStroke(Color.PURPLE);
				line.getStrokeDashArray().addAll(10d, 60d);
				this.drawingGroup.getChildren().add(line);
			}
		});
	}
	
	public void drawFlightPlanStep2 () {
		Platform.runLater(() -> {
			System.out.println("DRAWING STEP 2");
			this.drawingGroup.getChildren().clear();
			// DRAW FP 1 IN PINK FULL LINE //
			for (int i = 0; i < this.nodesFlightPlan1.size() - 1; i++) {
				TaxiwayNode nodeStart = this.nodesFlightPlan1.get(i);
				TaxiwayNode nodeEnd = this.nodesFlightPlan1.get(i + 1);
				LatLng latLonStart = new LatLng(nodeStart.getCoords()[0], nodeStart.getCoords()[1]);
				LatLng latLonEnd = new LatLng(nodeEnd.getCoords()[0], nodeEnd.getCoords()[1]);
				UTMRef utmStart = latLonStart.toUTMRef();
				UTMRef utmEnd = latLonEnd.toUTMRef();
				double xStart = utmStart.getEasting();
				double yStart = utmStart.getNorthing();
				double xEnd = utmEnd.getEasting();
				double yEnd = utmEnd.getNorthing();
				Line line = new Line(xStart, -yStart, xEnd, -yEnd);
				line.setStrokeWidth(30);
				line.setStroke(Color.PURPLE);
				this.drawingGroup.getChildren().add(line);
			}
		});
	}
	
	public void drawFlightPlanStep3 () {
		Platform.runLater(() -> {
			System.out.println("DRAWING STEP 3");
			this.drawingGroup.getChildren().clear();
			// DRAW FP 1 IN YELLOW FULL LINE AND FP 2 ABOVE IN PINK FULL LINE //
			for (int i = 0; i < this.nodesFlightPlanYellow.size() - 1; i++) {
				TaxiwayNode nodeStart = this.nodesFlightPlanYellow.get(i);
				TaxiwayNode nodeEnd = this.nodesFlightPlanYellow.get(i + 1);
				LatLng latLonStart = new LatLng(nodeStart.getCoords()[0], nodeStart.getCoords()[1]);
				LatLng latLonEnd = new LatLng(nodeEnd.getCoords()[0], nodeEnd.getCoords()[1]);
				UTMRef utmStart = latLonStart.toUTMRef();
				UTMRef utmEnd = latLonEnd.toUTMRef();
				double xStart = utmStart.getEasting();
				double yStart = utmStart.getNorthing();
				double xEnd = utmEnd.getEasting();
				double yEnd = utmEnd.getNorthing();
				Line line = new Line(xStart, -yStart, xEnd, -yEnd);
				line.setStrokeWidth(60);
				line.setStroke(Color.YELLOW);
				this.drawingGroup.getChildren().add(line);
			}
			for (int i = 0; i < this.nodesFlightPlan2.size() - 1; i++) {
				TaxiwayNode nodeStart = this.nodesFlightPlan2.get(i);
				TaxiwayNode nodeEnd = this.nodesFlightPlan2.get(i + 1);
				LatLng latLonStart = new LatLng(nodeStart.getCoords()[0], nodeStart.getCoords()[1]);
				LatLng latLonEnd = new LatLng(nodeEnd.getCoords()[0], nodeEnd.getCoords()[1]);
				UTMRef utmStart = latLonStart.toUTMRef();
				UTMRef utmEnd = latLonEnd.toUTMRef();
				double xStart = utmStart.getEasting();
				double yStart = utmStart.getNorthing();
				double xEnd = utmEnd.getEasting();
				double yEnd = utmEnd.getNorthing();
				Line line = new Line(xStart, -yStart, xEnd, -yEnd);
				line.setStrokeWidth(30);
				line.setStroke(Color.PURPLE);
				line.getStrokeDashArray().addAll(10d, 60d);
				this.drawingGroup.getChildren().add(line);
			}
		});
	}
	
	public void drawPopup () {
		Platform.runLater(() -> {
			Stage popup = new Stage();

			popup.setX(500);
			popup.setY(150);

			Button acceptFP = new Button("EXECUTE");
			acceptFP.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					popup.hide();
					drawFlightPlanStep4(); // Uncomment this line if you have this function
				}
			});

			//Button rejectFP = new Button("CANCEL");

			Text textAlert = new Text("ROUTE CHANGE ALERT");
			textAlert.setFont(new Font(20));
			textAlert.setFill(Color.RED);  // Set the text color to red
			textAlert.setTextAlignment(TextAlignment.CENTER);

//			Text textInfo = new Text("EMERGENCY LANDING OF D123 ON RWY 06R.\n TAXIWAY HAS BEEN MODIFIED. CONFIRM NEW ROUTE");
//			Text textInfo = new Text("HANGAR C7 is CLOSED.\n TAXIWAY HAS BEEN MODIFIED. CONFIRM NEW ROUTE");
			Text textInfo = new Text("TAXIWAY HAS BEEN MODIFIED. CONFIRM NEW ROUTE");
			textInfo.setFont(new Font(15));
			textInfo.setTextAlignment(TextAlignment.CENTER);


			VBox mainLayout = new VBox(10);  // 10 is the spacing between elements
			HBox buttonLayout = new HBox(10);  // 10 is the spacing between buttons

			mainLayout.setAlignment(Pos.CENTER);  // Center the elements in the VBox
			buttonLayout.setAlignment(Pos.CENTER);  // Center the buttons in the HBox

			mainLayout.getChildren().add(textAlert);
			mainLayout.getChildren().add(textInfo);
			buttonLayout.getChildren().add(acceptFP);
			//buttonLayout.getChildren().add(rejectFP);
			mainLayout.getChildren().add(buttonLayout);

			Scene scene = new Scene(mainLayout);
			popup.setScene(scene);
			scene.setFill(Color.BLUE);
			popup.setWidth(550);
			popup.setHeight(200);
			acceptFP.setMinHeight(50);
			acceptFP.setMinWidth(150);
//			rejectFP.setMinHeight(50);
//			rejectFP.setMinWidth(150);

			popup.show();
		});
	}
	
	public void drawFlightPlanStep4 () {
		Platform.runLater(() -> {
			System.out.println("DRAWING STEP 4");
		this.drawingGroup.getChildren().clear();
		// DRAW FP 1 IN YELLOW FULL LINE AND FP 2 ABOVE IN PINK FULL LINE //
		for (int i = 0; i < this.nodesFlightPlan2.size() - 1; i ++) {
			TaxiwayNode nodeStart = this.nodesFlightPlan2.get(i);
			TaxiwayNode nodeEnd = this.nodesFlightPlan2.get(i + 1);
			LatLng latLonStart = new LatLng(nodeStart.getCoords()[0], nodeStart.getCoords()[1]);
			LatLng latLonEnd = new LatLng(nodeEnd.getCoords()[0], nodeEnd.getCoords()[1]);
			UTMRef utmStart = latLonStart.toUTMRef();
			UTMRef utmEnd = latLonEnd.toUTMRef();
			double xStart = utmStart.getEasting();
			double yStart = utmStart.getNorthing();
			double xEnd = utmEnd.getEasting();
			double yEnd = utmEnd.getNorthing();
			Line line = new Line(xStart, - yStart, xEnd, - yEnd);
			line.setStrokeWidth(30);
			line.setStroke(Color.PURPLE);
			this.drawingGroup.getChildren().add(line);
		}
		});
	}

	public void resetFlightPlan() {
		Platform.runLater(() -> {
			System.out.println("RESET FLIGHT PLAN");
		this.drawingGroup.getChildren().clear();
		this.step = 0;
		});
	}
}
