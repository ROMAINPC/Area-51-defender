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

import java.util.Random;

import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Monde extends Group{
	
	private static int maille[][][] = new int[1001][1001][201];
	private static Image imgCube = new Image("/brique.jpg", 0, 1000, true, true);
	private static PhongMaterial mCube = new PhongMaterial(Color.WHITE, imgCube, null, null, null);
	private static Image imgSphere = new Image("/pierre.jpg", 0, 1024, true, true);
	private static PhongMaterial mSphere = new PhongMaterial(Color.WHITE, imgSphere, null, null, null);
	private static Image imgSol = new Image("/terreGrand.jpg", 0, 5000, true, true);
	private static PhongMaterial M = new PhongMaterial(Color.WHITE, imgSol, null, null, null);
	private static Image imgFeuille = new Image("/feuillage.jpg", 0, 1024, true, true);
	private static PhongMaterial feuille = new PhongMaterial(Color.WHITE, imgFeuille, null, null, null);
	private static Image imgTronc = new Image("/tronc.png", 0, 1024, true, true);
	private static PhongMaterial tronc = new PhongMaterial(Color.WHITE, imgTronc, null, null, null);
	
	public Monde(){
		Box sol = new Box(10000,0,10000);
		
	    sol.setMaterial(M);
	    
	    
	    //générations des boites:
	    Random rNb = new Random();
	    for(int i = 0 ; i < rNb.nextInt(81 - 60) + 60 ; i++){
	    	
	    	Random r = new Random();
	    	int type = r.nextInt(4 - 1) + 1;
	    	
	    	if(type == 1){
	    		int h = r.nextInt(51 - 5) + 5;
	    		Box b =  new Box((r.nextInt(50 - 5) + 5)*20, h*20, (r.nextInt(50 - 5) + 5)*20);
	    		b.setTranslateY(h*20 / 2);
	    		b.setTranslateX((r.nextInt(451 + 450) -450)*10);
	    		b.setTranslateZ((r.nextInt(451 + 450) -450)*10);
	    		
	    		
	    		b.setMaterial(mCube);
	    		//maillage:
	    		for(int x = (int)Math.round(b.getBoundsInParent().getMinX() / 10) +500; x <=  (int)Math.round(b.getBoundsInParent().getMaxX() / 10) +500 ; x++){
	    			for(int z = (int)Math.round(b.getBoundsInParent().getMinZ() / 10) +500; z <= (int)Math.round(b.getBoundsInParent().getMaxZ() / 10) +500 ; z++){
	    				for(int y = 0; y <= (int)Math.round(b.getBoundsInParent().getMaxY() / 10); y++){
	    						maille[x][z][y] = -2;
	    				}
	    			}
	    		}
	    		
	    		
	    		this.getChildren().add(b);
	    		
	    	}
	    	
	    	if(type == 2){
	    		Sphere s = new Sphere((r.nextInt(26 - 5) + 5)*20);
	    		s.setTranslateX((r.nextInt(451 + 450) -450)*10);
	    		s.setTranslateZ((r.nextInt(451 + 450) -450)*10);
	    		
	    		s.setMaterial(mSphere);
	    		
	    		//maillage
	    		for(int x = (int)Math.round(s.getBoundsInParent().getMinX() / 10) +500; x <=  (int)Math.round(s.getBoundsInParent().getMaxX() / 10) +500 ; x++){
	    			for(int z = (int)Math.round(s.getBoundsInParent().getMinZ() / 10) +500; z <= (int)Math.round(s.getBoundsInParent().getMaxZ() / 10) +500 ; z++){
	    				for(int y = 0; y <= (int)Math.round(s.getBoundsInParent().getMaxY() / 10); y++){
	    					int dx = (int)(((x-500)*10)-s.getTranslateX());
	    					int dz = (int)(((z-500)*10)-s.getTranslateZ());
	    					int dy = (int)((y*10)-s.getTranslateY());
	    					if(Math.sqrt(dx*dx + dy*dy + dz*dz) <= s.getRadius()){
	    						maille[x][z][y] = -3;
	    					}
	    				}
	    			}
	    		}
	    		
	    		this.getChildren().add(s);
	    	}
	    	
	    	if(type ==3){
	    		int h = r.nextInt(51 - 5) + 5;
	    		Cylinder c = new Cylinder((r.nextInt(16 - 5) + 5)*20, h*20);
	    		c.setTranslateY(h*20 / 2);
	    		c.setTranslateX((r.nextInt(451 + 450) -450)*10);
	    		c.setTranslateZ((r.nextInt(451 + 450) -450)*10);
	    		

	    		c.setMaterial(tronc);
	    		
	    		//maillage:
	    		for(int x = (int)Math.round(c.getBoundsInParent().getMinX() / 10) +500; x <= (int)Math.round(c.getBoundsInParent().getMaxX() / 10) +500; x++){
	    			for(int z = (int)Math.round(c.getBoundsInParent().getMinZ() / 10) +500; z <= (int)Math.round(c.getBoundsInParent().getMaxZ() / 10) +500 ; z++){
	    				if(Math.sqrt(((int)(((x-500)*10)-c.getTranslateX()))*((int)(((x-500)*10)-c.getTranslateX())) + ((int)(((z-500)*10)-c.getTranslateZ()))*((int)(((z-500)*10)-c.getTranslateZ()))) <= c.getRadius()){
	    					for(int y = 0; y <= (int)Math.round(c.getBoundsInParent().getMaxY() / 10); y++)
	    						maille[x][z][y] = -4;
	    				}
	    			}
	    		}
	    		
	    		Sphere s = new Sphere(c.getRadius() * 1.8d);
	    		s.setTranslateX(c.getTranslateX());
	    		s.setTranslateZ(c.getTranslateZ());
	    		s.setTranslateY(c.getHeight());
	    		s.setMaterial(feuille);
	    		
	    		this.getChildren().addAll(c,s);
	    	}
	    	
	    }
	    
	    
	    
	    //grillages:
	    Image img = new Image("grillage.png", 2000, 0, true, true);
	    for(int i = 0 ; i < 5 ; i++){
	    	ImageView iV = new ImageView(img);
	    	iV.getTransforms().addAll(new Translate(-3000 + i*2000,img.getHeight(),5000), new Rotate(180, Rotate.Z_AXIS));
	    	this.getChildren().add(iV);
	    	ImageView iV2 = new ImageView(img);
	    	iV2.getTransforms().addAll(new Translate(5000,img.getHeight(),-5000+i*2000), new Rotate(180, Rotate.Z_AXIS), new Rotate(-90, Rotate.Y_AXIS));
	    	this.getChildren().add(iV2);
	    	ImageView iV3 = new ImageView(img);
	    	iV3.getTransforms().addAll(new Translate(-5000 + i*2000,img.getHeight(),-5000), new Rotate(180, Rotate.Z_AXIS), new Rotate(180, Rotate.Y_AXIS));
	    	this.getChildren().add(iV3);
	    	ImageView iV4 = new ImageView(img);
	    	iV4.getTransforms().addAll(new Translate(-5000,img.getHeight(),-3000 + i*2000), new Rotate(180, Rotate.Z_AXIS), new Rotate(90, Rotate.Y_AXIS));
	    	this.getChildren().add(iV4);
	    }
	    
	    //lumières:
	    PointLight pT = new PointLight(Color.WHITE);
	    pT.setLayoutY(5000);
	    AmbientLight aL = new AmbientLight(Color.rgb(50, 50, 50));
	    
		//ajout des éléments:
		this.getChildren().addAll(
				sol,
				new Soucoupe(4000, 2200, 4000),
				new Soucoupe(-4000, 2200, -4000),
				new Soucoupe(4000, 2200, -4000),
				new Soucoupe(-4000, 2200, 4000),
				pT, aL
		);
	}

	public static int[][][] getMaille(){
		return maille;
	}
	
}
