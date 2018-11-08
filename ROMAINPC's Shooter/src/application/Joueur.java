/*******************************************************************************
 * Copyright (C) 2017 ROMAINPC
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
//code réalisé par ROMAINPC
package application;

import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Joueur extends Group{
	private PerspectiveCamera camera;
	private PerspectiveCamera troisEmeP;
	private PerspectiveCamera troisEmeP2;
	
	public Rotate jRX = new Rotate(180, Rotate.X_AXIS);//passage en Y up, ce groupe reste en Y-Down
	public Rotate jRY = new Rotate(0, Rotate.Y_AXIS);
	public Rotate jRZ = new Rotate(0, Rotate.Z_AXIS);
	public Translate   jT = new Translate(0, -180, 0);
	
	public Rotate jTRX = new Rotate(0, Rotate.X_AXIS);
	
	public Rotate j3PRX = new Rotate(0, Rotate.X_AXIS);
	public Rotate j3PRX2 = new Rotate(0, Rotate.X_AXIS);
	
	public Rotate j3PRY = new Rotate(0, Rotate.Y_AXIS);
	
	private Arme arme;
	private Box jambeG; private Box jambeD; private Box brasG; private Box brasD2;
	
	private PointLight feu;
	
	private static Image imgJean = new Image("/jean.jpg", 470, 0, true, true);
	private static PhongMaterial jean = new PhongMaterial(Color.WHITE, imgJean, null, null, null);
	private static Image imgTShirt = new Image("/tshirt.jpeg", 750, 0, true, true);
	private static PhongMaterial tShirt = new PhongMaterial(Color.WHITE, imgTShirt, null, null, null);
	private static Image imgTete = new Image("/visage.jpg", 1024, 0, true, true);
	private static PhongMaterial visage = new PhongMaterial(Color.WHITE, imgTete, null, null, null);
	
	
	public Joueur(){
		//caméra(et tête):
		camera = new PerspectiveCamera(true);
		camera.setNearClip(1);
		camera.setFarClip(100000);
		camera.getTransforms().addAll(jTRX, new Translate(0,0,28));
		camera.setFieldOfView(camera.getFieldOfView() + 10);
		
		
		//3eme personne:
		troisEmeP = new PerspectiveCamera(true);
		troisEmeP.setNearClip(0.1);
		troisEmeP.setFarClip(100000);
		troisEmeP.getTransforms().addAll(new Translate(0, -300, -800), new Rotate(-10, Rotate.X_AXIS), j3PRX, j3PRY);
		
		//2eme 3eme personne:
		troisEmeP2 = new PerspectiveCamera(true);
		troisEmeP2.setNearClip(0.1);
		troisEmeP2.setFarClip(100000);
		troisEmeP2.getTransforms().addAll(new Translate(400, -80, 400), new Rotate(220, Rotate.Y_AXIS), j3PRX2, j3PRY);
		
		//corp:
		Cylinder body = new Cylinder(24,90);
		body.setMaterial(tShirt);
		body.getTransforms().addAll(new Translate(0, 60, 0));
		jambeG = new Box(22,75,22); jambeG.setMaterial(jean);
		jambeG.getTransforms().addAll(new Translate(-14, 142, 0));
		jambeD = new Box(22,75,22); jambeD.setMaterial(jean);
		jambeD.getTransforms().addAll(new Translate(14, 142, 0));
		brasG = new Box(20, 110, 20);
		brasG.setMaterial(tShirt);
		brasG.getTransforms().addAll(new Translate(-35, 70, 0));
		Box epaule = new Box(51,20,20); epaule.setMaterial(tShirt);
		epaule.getTransforms().addAll(new Translate(0, 25, 0));
		
		//bras armé:
		Group brasArme = new Group();
		Box brasD1 = new Box(20, 55, 20); brasD1.setMaterial(tShirt);
		brasD1.getTransforms().addAll(new Translate(35, 40, -20), new Rotate(-45, Rotate.X_AXIS));
		brasD2 = new Box(20, 55, 20); brasD2.setMaterial(tShirt);
		brasD2.getTransforms().addAll(new Translate(35, 60, -19), new Rotate(90, Rotate.X_AXIS));
		arme = new Arme();
		arme.getTransforms().addAll(new Translate(35, 60, 25));
		
		brasArme.getChildren().addAll(brasD1, brasD2, arme);
		
		//tete:
		Sphere tete = new Sphere(30, 20);
		tete.getTransforms().addAll(new Rotate(180, Rotate.Y_AXIS));
		tete.setMaterial(visage);
		
		//divers ajustemenst de pivot
		jRY.pivotXProperty().bind(jT.xProperty());
		jRY.pivotZProperty().bind(jT.zProperty());
		
		feu = new PointLight(Color.YELLOW);
		feu.getTransforms().addAll(new Translate(35, 60, 50));
		feu.setLightOn(false);
		
		
		this.getTransforms().addAll(jRX, jRY, jRZ, jT);
		this.getChildren().addAll(
			camera,
			body, jambeG, jambeD, brasG, epaule,
			brasArme,
			troisEmeP,
			troisEmeP2,
			tete,
			feu
		);
		
		//départ
		jT.setY(-1500);
	}
	public PerspectiveCamera getCamera() {
		return camera;
	}
	public PerspectiveCamera getCamera2(){
		return troisEmeP;
	}
	public PerspectiveCamera getCamera3(){
		return troisEmeP2;
	}
	public Arme getArme(){
		return arme;
	}
	public Translate getPosition(){
		return jT;
	}
	public void masque(){
		jambeG.setVisible(false);
		jambeD.setVisible(false);
		arme.setVisible(false);
		brasG.setVisible(false);
		brasD2.setVisible(false);
		arme.setVisible(false);
	}
	public void demasque(){
		jambeG.setVisible(true);
		jambeD.setVisible(true);
		arme.setVisible(true);
		brasG.setVisible(true);
		brasD2.setVisible(true);
		arme.setVisible(true);
	}
	public void addFeu(){
		feu.setLightOn(true);
	}
	public void removeFeu(){
		feu.setLightOn(false);
	}
}
