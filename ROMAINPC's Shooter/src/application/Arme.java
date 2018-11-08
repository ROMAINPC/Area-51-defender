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
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Arme extends Group{
	private Sphere feu;
	
	private static Image img = new Image("/metal.jpg", 700, 0, true, true);
	private static PhongMaterial metal = new PhongMaterial(Color.rgb(20, 20, 20), img, null, null, null);
	
	public Arme(){
		//matériaux
		metal.setSpecularColor(Color.SILVER);
		metal.setSpecularPower(8);
		
		//corp
		Box arme1 = new Box(10, 10, 35); arme1.setMaterial(metal);
		arme1.getTransforms().addAll(new Translate(0, 0, 0));
		
		//canon
		Cylinder arme2 = new Cylinder(2, 10); arme2.setMaterial(metal);
		arme2.getTransforms().addAll(new Translate(0, 0, 22), new Rotate(90, Rotate.X_AXIS));
		
		//poignée
		Box arme3 = new Box(10, 15, 10); arme3.setMaterial(metal);
		arme3.getTransforms().addAll(new Translate(0, 10, -15));
		
		//chargeur
		Box arme4 = new Box(8, 10, 8); arme4.setMaterial(metal);
		arme4.getTransforms().addAll(new Translate(0, 10, 10));
		
		//mire:
		Cylinder viseur = new Cylinder(3,25); viseur.setMaterial(metal);
		viseur.getTransforms().addAll(new Translate(0, -8, -5), new Rotate(90, Rotate.X_AXIS));
		
		//feu:
		feu = new Sphere(3); feu.setVisible(false);
		feu.getTransforms().addAll(new Translate(0, 0, 30));
		PhongMaterial flamme = new PhongMaterial();
		flamme.setDiffuseColor(Color.ORANGE);
		flamme.setSpecularColor(Color.YELLOW); flamme.setSpecularPower(1);
		feu.setMaterial(flamme);
		
		
		this.getChildren().addAll(
				feu,
				viseur,
				arme4,
				arme2,
				arme3,
				arme1
		);
	}
	
	public void addFeu(){
		feu.setVisible(true);
	}
	
	public void removeFeu(){
		feu.setVisible(false);
	}
	
}
