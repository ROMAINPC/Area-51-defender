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

import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class Alien extends Group{
	public static ArrayList<Alien> liste = new ArrayList<Alien>();
	private static Random r = new Random();
	
	private static AudioClip sonDegatsJ = new AudioClip(Alien.class.getResource("/coupdepoing2.wav").toExternalForm());
	private static AudioClip cri = new AudioClip(Alien.class.getResource("/CriCochon.mp3").toExternalForm());
	
	private static Image imgPeau = new Image("/ecaille.jpg", 1024, 0, true, true);
	private static PhongMaterial peau = new PhongMaterial(Color.WHITE, imgPeau, null, null, null);
	private static Image imgtete = new Image("/peauTete.jpg", 1024, 0, true, true);
	private static PhongMaterial peauTete = new PhongMaterial(Color.WHITE, imgtete, null, null, null);
	
	private Translate t;
	private Rotate rX; private Rotate rY; private Rotate rZ;
	private int PV;
	private AnimationTimer timer;
	private int puissanceIni;
	private int degatsCount;
	private int sonCount;
	
	public Alien(int vie, int puissance, int soucoupe, int vitesse, int index){
		PV = vie; puissanceIni = puissance;
		liste.add(this);
		t = new Translate(0,1700,0); rX = new Rotate(0, Rotate.X_AXIS); rY = new Rotate(0, Rotate.Y_AXIS); rZ = new Rotate(0, Rotate.Z_AXIS);
		int rayon = 50/10;
		//départ:
		switch (soucoupe){
			case 1: t.setX(4000); t.setZ(4000); break;
			case 2: t.setX(-4000); t.setZ(-4000); break;
			case 3: t.setX(4000); t.setZ(-4000); break;
			case 4: t.setX(-4000); t.setZ(4000); break;
		}
		degatsCount = 99;
		sonCount = 0;
		boolean[][] tab = new boolean[4][21]; //<-- tableau pour vérifier que on place bien la hitbox sur une zone qui n'est pas un mur ou élément du décor
		
		//corps de l'alien:
		Cylinder corps = new Cylinder(25,80);
		corps.setMaterial(peau);
		corps.getTransforms().add(new Translate(0, 130, 0));
		corps.setOpacity(0.5);
		
		Sphere tete = new Sphere(25);
		tete.setMaterial(peauTete);
		tete.getTransforms().add(new Translate(0, 185, 0));
		
		Box jambeD = new Box(18,90+20,18); jambeD.setMaterial(peau);
		jambeD.getTransforms().add(new Translate(12, 45, 0));
		
		Box jambeG = new Box(18,90+20,18); jambeG.setMaterial(peau);
		jambeG.getTransforms().add(new Translate(-12, 45, 0));
		
		Box brasD = new Box(16, 16, 70); brasD.setMaterial(peau);
		brasD.getTransforms().add(new Translate(-20, 160, -27));
		
		Box brasG = new Box(16, 16, 70); brasG.setMaterial(peau);
		brasG.getTransforms().add(new Translate(20, 160, -27));
		
		
		this.getChildren().addAll(corps, tete, jambeD, jambeG, brasG, brasD);
		this.getTransforms().addAll(t, rX, rY, rZ);
		
		Main.getRoot().getChildren().add(this);
		
		//1ere hitbox:
		int valX = (int)Math.round((t.getX()+5000)/10); int valZ = (int)Math.round((t.getZ()+5000)/10); int valY = (int)Math.round((t.getY()+10)/10);
		for(int i = 0 ; i < 4; i++) for(int j = 7 ; j <= 20;j++) tab[i][j] = false;
		for(int i = 7; i <= 20 ; i++){
			if(Monde.getMaille()[valX][valZ+1][valY + i] >= 0){
			Monde.getMaille()[valX][valZ+1][valY + i] = index; tab[0][i] = true;}
			if(Monde.getMaille()[valX][valZ-1][valY + i] >= 0){
				Monde.getMaille()[valX][valZ-1][valY + i] = index; tab[1][i] = true;}
			if(Monde.getMaille()[valX+1][valZ][valY + i] >= 0){
			Monde.getMaille()[valX+1][valZ][valY + i] = index; tab[2][i] = true;}
			if(Monde.getMaille()[valX-1][valZ][valY + i] >= 0){
			Monde.getMaille()[valX-1][valZ][valY + i] = index; tab[3][i] = true;}
		}
		
		//timer:
		timer = new AnimationTimer(){
			public void handle(long l){
				
				if(sonCount >= r.nextInt(100000 - 100) + 100){
					double val = Math.sqrt((Main.getJoueur().getPosition().getX() - t.getX())*(Main.getJoueur().getPosition().getX() - t.getX()) + (Main.getJoueur().getPosition().getZ() + t.getZ())*(Main.getJoueur().getPosition().getZ() + t.getZ())) * 0.001d;
					cri.setVolume(1-val < 0 ? 0 : 1-val);
					cri.play();
					sonCount =0;
				}
					sonCount++;
				
				//enlève hitbox
				int valX = (int)Math.round((t.getX()+5000)/10); int valZ = (int)Math.round((t.getZ()+5000)/10); int valY = (int)Math.round((t.getY()+10)/10);
				for(int i = 7; i <= 20 ; i++){
					if(tab[0][i])Monde.getMaille()[valX][valZ+1][valY + i] = 0;
					if(tab[1][i])Monde.getMaille()[valX][valZ-1][valY + i] = 0;
					if(tab[2][i])Monde.getMaille()[valX+1][valZ][valY + i] = 0;
					if(tab[3][i])Monde.getMaille()[valX-1][valZ][valY + i] = 0;
				}
				
				//regarde vers le joueur	
				rY.setAngle(-Math.toDegrees(Math.atan2((Main.getJoueur().getPosition().getX() - t.getX()) , (Main.getJoueur().getPosition().getZ() + t.getZ()))));
				
				//déplacement si ya pas de mur devant seulement
				for(int i = 0 ; i < vitesse ; i++)
					if(
            				Monde.getMaille()[(int) Math.round((t.getX()+5000) / 10 -rayon* Math.sin(Math.toRadians(rY.getAngle())))][(int)Math.round((t.getZ()+5000) / 10 - rayon*Math.cos(Math.toRadians(rY.getAngle())))][(int)(t.getY() / 10)+1] == 0
            			&&	Monde.getMaille()[(int) Math.round((t.getX()+5000) / 10 -rayon* Math.sin(Math.toRadians(rY.getAngle())))][(int)Math.round((t.getZ()+5000) / 10 - rayon*Math.cos(Math.toRadians(rY.getAngle())))][(int)(t.getY() / 10)+10] == 0
            			&&  Math.sqrt((Main.getJoueur().getPosition().getX() - t.getX())*(Main.getJoueur().getPosition().getX() - t.getX()) + (Main.getJoueur().getPosition().getZ() + t.getZ())*(Main.getJoueur().getPosition().getZ() + t.getZ())) > 100
            				){
            			
            			t.setX(t.getX() - Math.sin(Math.toRadians(rY.getAngle())));
            			t.setZ(t.getZ() - Math.cos(Math.toRadians(rY.getAngle())));
            		}

				//montée si 
				for(int i = 0 ; i < 25 ; i++){
					if(
            				Monde.getMaille()[(int) Math.round((t.getX()+5000) / 10 -rayon* Math.sin(Math.toRadians(rY.getAngle())))][(int)Math.round((t.getZ()+5000) / 10 - rayon*Math.cos(Math.toRadians(rY.getAngle())))][(int)(t.getY() / 10)+1] == 0
            			){
							t.setY(t.getY() - 1);
						}else if(i%6==0
		            			&&	Monde.getMaille()[(int) Math.round((t.getX()+5000) / 10 -rayon* Math.sin(Math.toRadians(rY.getAngle())))][(int)Math.round((t.getZ()+5000) / 10 - rayon*Math.cos(Math.toRadians(rY.getAngle())))][(int)(t.getY() / 10)+1] <= 0
									)
								t.setY(t.getY() + 1);
						
				}
				
				//remet nouvelle hitbox:
				valX = (int)Math.round((t.getX()+5000)/10); valZ = (int)Math.round((t.getZ()+5000)/10); valY = (int)Math.round((t.getY()+10)/10);
				for(int i = 0 ; i < 4 ; i++) for(int j = 7 ; j <= 20;j++) tab[i][j] = false;
				for(int i = 7; i <= 20 ; i++){
					if(Monde.getMaille()[valX][valZ+1][valY + i] >= 0){
						Monde.getMaille()[valX][valZ+1][valY + i] = index; tab[0][i] = true;}
					if(Monde.getMaille()[valX][valZ-1][valY + i] >= 0){
						Monde.getMaille()[valX][valZ-1][valY + i] = index; tab[1][i] = true;}
						if(Monde.getMaille()[valX+1][valZ][valY + i] >= 0){
						Monde.getMaille()[valX+1][valZ][valY + i] = index; tab[2][i] = true;}
						if(Monde.getMaille()[valX-1][valZ][valY + i] >= 0){
						Monde.getMaille()[valX-1][valZ][valY + i] = index; tab[3][i] = true;}
				}
				
				
				//dégats au joueur:
				if(Math.sqrt((Main.getJoueur().getPosition().getX() - t.getX())*(Main.getJoueur().getPosition().getX() - t.getX()) + (Main.getJoueur().getPosition().getZ() + t.getZ())*(Main.getJoueur().getPosition().getZ() + t.getZ())) <= 100){
					if(!(t.getY()+180 < -Main.getJoueur().getPosition().getY()-150 || -Main.getJoueur().getPosition().getY() < t.getY()+30)){ //<-- vérifier en Y
						degatsCount++;
						if(degatsCount >= 100){
							
							if(Main.getVie().get() > 0)
								sonDegatsJ.play();
							
							degatsCount = 0;
							Main.degats(puissance);
						}
					}
				} else {
					degatsCount = 99;
				}
				
				
			}
		};
		timer.start();
		
		
	}
	
	public void degats(){
		PV--;
		Main.increaseScore(1);
		//mort:
		if(PV <= 0){
			timer.stop();
			Main.increaseScore(puissanceIni);
			//enlève hitbox
			int valX = (int)Math.round((t.getX()+5000)/10); int valZ = (int)Math.round((t.getZ()+5000)/10); int valY = (int)Math.round((t.getY()+10)/10);
			for(int i = 7; i <= 20 ; i++){
				Monde.getMaille()[valX][valZ+1][valY+i] = 0;
				Monde.getMaille()[valX][valZ-1][valY+i] = 0;
				Monde.getMaille()[valX+1][valZ][valY+i] = 0;
				Monde.getMaille()[valX-1][valZ][valY+i] = 0;
			}
			int sol = 0;
			for(int i = 0 ; i <= 110 ; i++){
				if(Monde.getMaille()[(int) Math.round((t.getX()+5000) / 10)][(int) Math.round((t.getZ()+5000) / 10)][i] < 0) sol = i;
			}
			new Timeline(new KeyFrame(Duration.millis(100), new KeyValue(rY.angleProperty(), rY.getAngle()+r.nextInt(90+90)-90)),new KeyFrame(Duration.millis(500), new KeyValue(t.yProperty(), sol*10 + 15)), new KeyFrame(Duration.millis(500), new KeyValue(rZ.angleProperty(), r.nextInt(2) == 0 ?90:-90)),  new KeyFrame(Duration.seconds(20), ae -> Main.getRoot().getChildren().remove(this))).play();
		}
	}
	
	
}
