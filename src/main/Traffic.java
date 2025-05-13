package main;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Traffic extends Group {
	
	private double SIZE_FACTOR = 4.;
	
	private int acId;
	private Boolean isGround;
	
	private Group groundReprGroup;
	private Group airReprGroup;
	
	public Traffic (int acId) {
		this.acId = acId;
		this.isGround = true;
		
		this.groundReprGroup = new Group();
		this.airReprGroup = new Group();
		
		this.getChildren().add(this.groundReprGroup);
		
		// CREATE GROUND REPR //
		Polygon polyGround = new Polygon();
		polyGround.getPoints().addAll(new Double[] {
				10. * this.SIZE_FACTOR, 0. * this.SIZE_FACTOR, 
				-5. * this.SIZE_FACTOR, -5. * this.SIZE_FACTOR, 
				0. * this.SIZE_FACTOR, 0. * this.SIZE_FACTOR,
				-5. * this.SIZE_FACTOR, 5. * this.SIZE_FACTOR
		});
		polyGround.setFill(Color.ORANGE);
		polyGround.setStroke(Color.BLACK);
		polyGround.setStrokeWidth(1);
		polyGround.setRotate(-90);
		this.groundReprGroup.getChildren().add(polyGround);
		
		// CREATE AIR REPR //
		Polygon polyAir = new Polygon();
		polyAir.getPoints().addAll(new Double[] {
				10. * this.SIZE_FACTOR, 0. * this.SIZE_FACTOR, 
				-5. * this.SIZE_FACTOR, -5. * this.SIZE_FACTOR, 
				0. * this.SIZE_FACTOR, 0. * this.SIZE_FACTOR,
				-5. * this.SIZE_FACTOR, 5. * this.SIZE_FACTOR
		});
		polyAir.setFill(Color.CYAN);
		polyAir.setStroke(Color.BLACK);
		polyAir.setStrokeWidth(1);
		polyAir.setRotate(-90);
		this.airReprGroup.getChildren().add(polyAir);
		
	}
	
	public int getAcId() {
		return this.acId;	
	}
	
	public Boolean getIsGround() {
		return isGround;
	}
	
	public void setIsGround() {
		if (!this.isGround) {
			this.isGround = true;
			this.getChildren().remove(0);
			this.getChildren().add(this.airReprGroup);
		}
	}
	
	public void setIsAir() {
		if (this.isGround) {
			this.isGround = false;
			this.getChildren().remove(0);
			this.getChildren().add(this.groundReprGroup);
		}
	}
 
}
