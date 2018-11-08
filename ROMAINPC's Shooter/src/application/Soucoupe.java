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
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;

public class Soucoupe extends Group{
	public Soucoupe(int x, int y, int z){
		PhongMaterial metal = new PhongMaterial(Color.DARKSLATEGRAY);
		metal.setSpecularColor(Color.WHITE); metal.setSpecularPower(1);
		PhongMaterial spawner = new PhongMaterial(Color.LAWNGREEN);
		spawner.setSpecularColor(Color.DEEPSKYBLUE); spawner.setSpecularPower(1);
		PhongMaterial verre = new PhongMaterial(Color.rgb(0, 100, 255, 0.5));
		verre.setSpecularColor(Color.DEEPSKYBLUE);
		
		Cylinder corps = new Cylinder(400, 150);
		corps.setMaterial(metal);
		Cylinder corps2 = new Cylinder(350, 170);
		corps2.setMaterial(metal);
		Cylinder corps3 = new Cylinder(250, 190);
		corps3.setMaterial(metal);
		
		Sphere spawn = new Sphere(150);
		spawn.setMaterial(spawner);
		
		Sphere cockpit = new Sphere(200);
		cockpit.setMaterial(verre);
		cockpit.getTransforms().add(new Translate(0, 120, 0));
		
		
		this.getTransforms().add(new Translate(x, y, z));
		this.getChildren().addAll(
				corps, corps2, corps3,
				spawn,
				cockpit
		);
	}
}
