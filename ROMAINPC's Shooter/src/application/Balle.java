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
//code rélaisé par ROMAINPC
package application;


import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class Balle {
	
	private static int[][][] maille = Monde.getMaille();
	private boolean contact;
	private static Image sang = new Image("sangSplash.gif", 200, 0, true, true);
	
	
	public Balle(double x, double y, double z, double rX, double rY){
		Sphere balle = new Sphere(1);
		balle.setVisible(false);
		balle.setMaterial(new PhongMaterial(Color.BLACK));
		Translate t = new Translate(x,-y,-z);
		balle.getTransforms().addAll(t);
		Main.getRoot().getChildren().add(balle);
		double dirY = Math.sin(Math.toRadians(rX));
		double dirX = Math.sin(Math.toRadians(rY)) * Math.cos(Math.toRadians(rX));
		double dirZ = -Math.cos(Math.toRadians(rY)) * Math.cos(Math.toRadians(rX));
		contact = false;
		AnimationTimer updater = new AnimationTimer(){
        	public void handle(long now){
        		
        		for(int i = 0 ; i < 300 ; i++){
        			t.setX(t.getX()+dirX);
        			t.setY(t.getY()+dirY);
        			t.setZ(t.getZ()+dirZ);
        			
        			if(maille[(int)Math.round((t.getX()+5000)/10)][(int)Math.round((t.getZ()+5000)/10)][(int)Math.round(t.getY()/10)] != 0 ){
        				contact = true;
        				i = 1000;
        			}
        			if(i == 1) balle.setVisible(true);
        		}
        		
        		if(contact){
        			int mX = (int)Math.round((t.getX()+5000)/10);
        			int mZ = (int)Math.round((t.getZ()+5000)/10);
        			int mY = (int)Math.round(t.getY()/10);
        			Main.getRoot().getChildren().remove(balle);
        			
        			//cas de l'impact avec un mur (pas les grillages ou le ciel !)
        			if(maille[mX][mZ][mY] <= -2){
        				Translate t = new Translate(mX*10-5000, mY*10, mZ*10-5000); Rotate rX = new Rotate(90, Rotate.X_AXIS); Rotate rY = new Rotate(0, Rotate.Y_AXIS); Rotate rZ = new Rotate(0, Rotate.Z_AXIS);
    					Cylinder c = new Cylinder(2,0); c.getTransforms().addAll(t, rX, rY, rZ);
    					c.setMaterial(new PhongMaterial(Color.BLACK));
    					
    					
    					
    					if(maille[mX][mZ][mY + 1] == 0 && maille[mX][mZ][mY] != -3){
    						rX.setAngle(0);
    						if(maille[mX+1][mZ][mY] == 0) t.setX(t.getX()-4);
    						if(maille[mX-1][mZ][mY] == 0) t.setX(t.getX()+4);
    						if(maille[mX][mZ+1][mY] == 0) t.setZ(t.getZ()-4);
    						if(maille[mX][mZ-1][mY] == 0) t.setZ(t.getZ()+4);
    						
    					}else if(maille[mX + 1][mZ][mY] == 0){
    						rZ.setAngle(90);
    						if(maille[mX][mZ - 1][mY] == 0) t.setZ(t.getZ() + 4);
    						if(maille[mX][mZ + 1][mY] == 0) t.setZ(t.getZ() - 4);
    						
    						if(maille[mX][mZ][mY] <= -3)t.setX(t.getX() + 10);
    						if(maille[mX][mZ][mY] == -3) rY.setAngle(45);
    						
    					} else if(maille[mX][mZ + 1][mY] == 0){
    						if(maille[mX - 1][mZ][mY] == 0) t.setX(t.getX() + 9);
    						
    						if(maille[mX][mZ][mY] <= -3)t.setZ(t.getZ() + 10);
    						if(maille[mX][mZ][mY] == -3) rX.setAngle(45);
    						
    					}else if(maille[mX - 1][mZ][mY] == 0){
    						rZ.setAngle(90);
    						if(maille[mX][mZ - 1][mY] == 0) t.setZ(t.getZ() + 4);
    						
    						if(maille[mX][mZ][mY] <= -3)t.setX(t.getX() - 10);
    						if(maille[mX][mZ][mY] == -3) rY.setAngle(-45);
    						
    					} else if(maille[mX][mZ - 1][mY] == 0){
    						if(maille[mX][mZ][mY] <= -3)t.setZ(t.getZ() - 10);
    						if(maille[mX][mZ][mY] == -3) rX.setAngle(-45);
    						
    					}
    					
    					if(maille[mX][mZ][mY] <= -3){
    						if(maille[mX + 1][mZ][mY] == 0 && maille[mX][mZ - 1][mY] == 0) rZ.setAngle(45);
    						if(maille[mX + 1][mZ][mY] == 0 && maille[mX][mZ + 1][mY] == 0) rZ.setAngle(-45);
    						if(maille[mX - 1][mZ][mY] == 0 && maille[mX][mZ - 1][mY] == 0) rZ.setAngle(135);
    						if(maille[mX - 1][mZ][mY] == 0 && maille[mX][mZ + 1][mY] == 0) rZ.setAngle(-135);
    					}
    					
    					Main.getRoot().getChildren().add(c);
    					new Timeline(new KeyFrame(Duration.seconds(20), ae -> Main.getRoot().getChildren().remove(c))).play();
        			}
        			
        			//contact avec un alien:
        			if(maille[mX][mZ][mY] > 0){
        				ImageView sangV = new ImageView(sang);
        				sangV.setLayoutY(-50);
        				Rotate r = new Rotate(-Math.toDegrees(Math.atan2((Main.getJoueur().getPosition().getX() - t.getX()) , (Main.getJoueur().getPosition().getZ() + t.getZ()))),Rotate.Y_AXIS);
        				sangV.getTransforms().addAll(new Translate(mX*10-5000 - 120*Math.cos(Math.toRadians(r.getAngle())), mY*10, mZ*10-5000 + 120*Math.sin(Math.toRadians(r.getAngle()))), r);
        				Main.getRoot().getChildren().add(sangV);
    					new Timeline(new KeyFrame(Duration.millis(500), ae -> Main.getRoot().getChildren().remove(sangV))).play();
    					
    					Alien.liste.get(maille[mX][mZ][mY] - 1).degats();
        			}
    				
    				this.stop();
        		}
        		
        	}
        };
        updater.start();
		
        
	}
}
