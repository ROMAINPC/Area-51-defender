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
	
import java.awt.Robot;
import java.util.HashSet;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {
	
	private static Group root;
	private static Group root2D;
	
	private static Joueur joueur;
	private static Robot bot;
	{try{bot = new Robot();} catch (Exception e){}}
	private static Scene scene;
	private static boolean pause = false;
	private static int firstP = 0;
	private static int vitesse = 10;
	
	private static boolean saut;
	private static int sautCount;
	private static boolean vol;
	
	private static Rectangle cache1; private static Circle cache2; private static Shape viseur;
	private static boolean vise;
	
	private static boolean tir;
	private static int cadence;
	private static int feu;
	
	private static int index;
	private static int alienCount;
	private static int prochainSpawn;
	
	
	private static long SCORE;
	private static Text lblScore;
	private static IntegerProperty VIE;
	private static Rectangle jauge; private static Rectangle jauge2;
	private static Rectangle cacheRouge;
	private static boolean cacheRVisible;
	private static boolean finDuJeu;
	
	
	private static AnimationTimer updaterSouris;
	private static AnimationTimer updater;
	private static AnimationTimer frame;
	private static AnimationTimer aliensupdater;
	
	private static SubScene scene3D;
	
	private static Group croix; private static Arme arme;
	
	private static boolean ignoreSouris;
	
	private static MediaPlayer player;
	
	public void start(Stage primaryStage) throws Exception{
		try {
			
			Media intro = new Media(getClass().getResource("/Intro.WAV").toExternalForm());
	        MediaPlayer introMP = new MediaPlayer(intro);
	        introMP.play();
			
			//scenes:
			root = new Group();
			root2D = new Group();
			scene3D = new SubScene(root,1000,700,true,SceneAntialiasing.BALANCED);
			scene = new Scene(root2D,1000,700,false,SceneAntialiasing.BALANCED);
			scene.setFill(Color.TRANSPARENT);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene3D.heightProperty().bind(scene.heightProperty());
			scene3D.widthProperty().bind(scene.widthProperty());
			
			//ciel:
			scene3D.setFill(Color.rgb(100, 100, 255));
			
			
			//chargement du monde:
			Monde mondeDeBase = new Monde();
			
			int[][][] maille = Monde.getMaille();
			for(int i = 0 ; i < 1000 ; i++)
				for(int j = 0 ; j < 1000 ; j++){
					maille[i][j][0] = -2;
					maille[i][j][200] = -1;
				}
			for(int i = 0 ; i < 1000 ; i++)
				for(int j = 0 ; j < 201 ; j++){
					maille[1][i][j] = -1; maille[0][i][j] = -1;
					maille[i][0][j] = -1; maille[i][1][j] = -1;
					maille[i][999][j] = -1; maille[i][998][j] = -1;
					maille[999][i][j] = -1; maille[998][i][j] = -1; 
				}
		    //chargement du joueur:
	        joueur = new Joueur();
	        saut = false;
	        joueur.masque();
	        
	        
	        //croix milieu écran:
	        croix = new Group();
	        Line l1 = new Line();
	        l1.startXProperty().bind(scene.widthProperty().divide(2)); l1.endXProperty().bind(scene.widthProperty().divide(2));
	        l1.startYProperty().bind(scene.heightProperty().divide(2).subtract(scene.heightProperty().divide(100)));
	        l1.endYProperty().bind(scene.heightProperty().divide(2).add(scene.heightProperty().divide(100)));
	        l1.setFill(Color.BLACK);
	        l1.setStrokeWidth(1); l1.setStroke(Color.WHITE); l1.setStrokeType(StrokeType.OUTSIDE);
	        Line l2 = new Line();
	        l2.startYProperty().bind(scene.heightProperty().divide(2)); l2.endYProperty().bind(scene.heightProperty().divide(2));
	        l2.startXProperty().bind(scene.widthProperty().divide(2).subtract(scene.heightProperty().divide(100)));
	        l2.endXProperty().bind(scene.widthProperty().divide(2).add(scene.heightProperty().divide(100)));
	        l2.setStrokeWidth(1); l2.setStroke(Color.WHITE); l2.setStrokeType(StrokeType.OUTSIDE);
	        croix.getChildren().addAll(l1, l2);
	        
	        //vie et score:
	        SCORE = 0;
	        lblScore = new Text(" Score : " + SCORE);
	        lblScore.setFill(Color.GOLDENROD); lblScore.setStroke(Color.BLACK);
	        lblScore.setTextOrigin(VPos.TOP); lblScore.setStrokeWidth(6);
	        lblScore.setFont(Font.font("Verdana", FontWeight.BOLD, scene.getWidth() / 15));
	        lblScore.layoutYProperty().bind(scene.heightProperty().subtract(160));
	        VIE = new SimpleIntegerProperty(); VIE.set(1000);
	        jauge = new Rectangle(); jauge.setHeight(50); jauge.setFill(Color.RED);
	        jauge.setWidth(scene.getWidth() / 2 - 20);
	        jauge.widthProperty().bind((scene.widthProperty().divide(2).subtract(20)).multiply(VIE.divide(1000d)));
	        jauge.setLayoutX(20);
	        jauge.layoutYProperty().bind(scene.heightProperty().subtract(70));
	        jauge2 = new Rectangle(); jauge2.setHeight(50); jauge2.setFill(Color.TRANSPARENT); jauge2.setStroke(Color.BLACK); jauge2.setStrokeWidth(10);
	        jauge2.setStrokeType(StrokeType.OUTSIDE); jauge2.setStrokeLineJoin(StrokeLineJoin.ROUND);
	        jauge2.widthProperty().bind(scene.widthProperty().divide(2).subtract(20)); jauge2.setLayoutX(20);
	        jauge2.layoutYProperty().bind(scene.heightProperty().subtract(70));
	        cacheRouge = new Rectangle(); cacheRouge.setFill(Color.rgb(200, 0, 0, 0.5));
	        cacheRouge.widthProperty().bind(scene.widthProperty()); cacheRouge.heightProperty().bind(scene.widthProperty());
	        cacheRVisible = false;
	        finDuJeu = false;
	        
	        //arme (1ere Personne):
	        arme = new Arme();
	        arme.layoutXProperty().bind(scene.widthProperty()); arme.layoutYProperty().bind(scene.heightProperty());
	        arme.scaleXProperty().bind(scene.widthProperty().divide(100));
	        arme.scaleYProperty().bind(arme.scaleXProperty()); arme.scaleZProperty().bind(arme.scaleXProperty());
	        Rotate rArmeY = new Rotate(-50, Rotate.Y_AXIS); Rotate rArmeX = new Rotate(15, Rotate.X_AXIS);
	        arme.getTransforms().addAll(rArmeY, rArmeX); arme.setTranslateX(-150); arme.setTranslateY(-150);
	        //et viseur:
	        cache1 = new Rectangle(); cache1.heightProperty().bind(scene.heightProperty()); cache1.widthProperty().bind(scene.widthProperty());
	        cache2 = new Circle(); cache2.radiusProperty().bind(scene.heightProperty().divide(2.5d));
	        cache2.layoutXProperty().bind(scene.widthProperty().divide(2)); cache2.layoutYProperty().bind(scene.heightProperty().divide(2));
	        viseur = Shape.subtract(cache1, cache2);
	        scene.heightProperty().addListener(e->{
	        	cache1.heightProperty().bind(scene.heightProperty()); cache1.widthProperty().bind(scene.widthProperty());
		        cache2.radiusProperty().bind(scene.heightProperty().divide(2.5d));
		        cache2.layoutXProperty().bind(scene.widthProperty().divide(2)); cache2.layoutYProperty().bind(scene.heightProperty().divide(2));
		        viseur = Shape.subtract(cache1, cache2);
	        });
	        
	        //ajout des groupes:
	        root.getChildren().addAll(mondeDeBase, joueur);
	        root2D.getChildren().addAll(scene3D, croix, arme, lblScore, jauge, jauge2);
	        scene3D.setCamera(joueur.getCamera());
	        
	        
	        //Menu Pause:
	        Group menuPause = new Group();
	        Rectangle fond = new Rectangle(0,0); fond.heightProperty().bind(scene.heightProperty()); fond.widthProperty().bind(scene.widthProperty()); fond.setFill(Color.rgb(0, 0, 0, 0.6));
	        Label infos = new Label(
	        		"   Appui sur ECHAP pour venir ici.\n"
	        		+ "   Utilise les touches Z Q S D pour te déplacer.\n"
	        		+ "   Bouge la souris pour regarder autour de toi.\n"
	        		+ "   Appui sur A pour courir.\n"
	        		+ "   Appui sur Espace pour sauter.\n"
	        		+ "   Clique Gauche pour tirer.\n"
	        		+ "   Clique Droit pour viser.\n"
	        		+ "   Appui sur F5 pour changer la vue.\n"
	        		+ "\n   Jeu réalisé par ROMAINPC LECHAT"
	        		);
	        infos.setTextFill(Color.WHITE); infos.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
	        menuPause.getChildren().addAll(fond, infos);
	        
	        //ressources:
	        AudioClip sonTir = new AudioClip(getClass().getResource("/canon.mp3").toExternalForm());
	        
	        
	        Media musique = new Media(getClass().getResource("/musique.mp3").toExternalForm());
	        player = new MediaPlayer(musique);
	        player.setCycleCount(MediaPlayer.INDEFINITE);
	        player.setVolume(0.5d);
	        player.play();
	        
	        //actions souris:
	        
	        //rotation caméra:
	        scene.setCursor(Cursor.NONE);
	        ignoreSouris = false;
	        scene.addEventFilter(MouseEvent.ANY, evt ->{
	        	if(!pause && VIE.get() >0){
	        		
	        		if(ignoreSouris) {
	        	        ignoreSouris = false;
	        	    }else{
	        		
	        		
	        		
	        		double pitch = joueur.jTRX.getAngle() - ((evt.getScreenY() - (primaryStage.getY() + (primaryStage.getHeight() / 2.0d)))) / 20.0d;
		        	if(scene3D.getCamera().equals(joueur.getCamera())){
		        	pitch = pitch > 90 ? 90 : pitch;
		        	pitch = pitch < -70 ? -70 : pitch;
		        	joueur.jTRX.setAngle(pitch);
		        	}else{
		        	if(scene3D.getCamera().equals(joueur.getCamera2())){
		        		pitch = joueur.j3PRX.getAngle() - ((evt.getScreenY() - (primaryStage.getY() + (primaryStage.getHeight() / 2.0d)))) / 20.0d;
		        		pitch = pitch > 5 ? 5 : pitch;
		        		pitch = pitch < -10 ? -10 : pitch;
		        		joueur.j3PRX.setAngle(pitch);
		        	}else{
		        		pitch = joueur.j3PRX2.getAngle() - ((evt.getScreenY() - (primaryStage.getY() + (primaryStage.getHeight() / 2.0d)))) / 20.0d;
		        		pitch = pitch > 0 ? 0 : pitch;
		        		pitch = pitch < -15 ? -15 : pitch;
		        		joueur.j3PRX2.setAngle(pitch);
		        	}
		        	}
		        	joueur.jRY.setAngle(joueur.jRY.getAngle() + (evt.getScreenX() - (primaryStage.getX() + (primaryStage.getWidth() / 2.0d))) / 20.0d);
	        		
	        		ignoreSouris = false;
	        		bot.mouseMove((int) (primaryStage.getX() + (primaryStage.getWidth() / 2.0)), (int) (primaryStage.getY() + (primaryStage.getHeight() / 2.0)));
	        	    }
	        		
		        	
	        	}
	        	
	        	
	        	
	        });
	        
	        
	        
	        
	        vise = false;
	        tir = false;
	        scene.setOnMousePressed(e -> {
	        	switch(e.getButton()){
	        	case SECONDARY:
	        		if(firstP == 0 && VIE.get() >0){
	        			vise=true;
	        			root2D.getChildren().add(viseur);
	        			lblScore.toFront();
	        			jauge.toFront(); jauge2.toFront();
	        			root2D.getChildren().remove(arme);
	        			joueur.getCamera().setFieldOfView(joueur.getCamera().getFieldOfView() - 20);
	        		}
	        		break;
	        	case PRIMARY:
	        		tir = true;
	        		break;
	        		
	        	default:
					break;
	        	}
	        });
	        
	        
	        scene.setOnMouseReleased(e -> {
	        	switch(e.getButton()){
	        	case SECONDARY:
	        		if(firstP == 0 && vise){
	        			vise=false;
	        			root2D.getChildren().add(arme);
	        			root2D.getChildren().remove(viseur);
	        			joueur.getCamera().setFieldOfView(joueur.getCamera().getFieldOfView() + 20);
	        		}
	        		break;
	        	case PRIMARY:
	        		tir = false;
	        		cadence = 0;
	        		break;
	        		
	        	default:
					break;
	        	}
	        });
	        
	        cadence = 0;
	         feu = 0;
	        updaterSouris = new AnimationTimer(){
	        	public void handle(long now){
	        		//tir
	            	if(tir){
	            		
	            		if(cadence == 0 || cadence == 10){
	            			sonTir.play();
	            			new Balle(joueur.jT.getX(), joueur.jT.getY(), joueur.jT.getZ(), joueur.jTRX.getAngle(), joueur.jRY.getAngle());
	            			cadence = 1;
	            			arme.addFeu();
	            			joueur.addFeu();
	            			joueur.getArme().addFeu();
	            			feu = 1;
	            			
	            		}
	            		cadence++;
	            	}
	            	if(feu>0) feu++;
	            	if(feu == 5){
	            		arme.removeFeu();
	            		joueur.removeFeu();
	            		joueur.getArme().removeFeu();
	            		feu=0;
	            	}
	            	
	        	}
	        };
	        updaterSouris.start();
	        
	        //actions clavier listeners:
	        
	        int rayon = 50/10;// division par 10 car pour passer dans le maillage
	        
	        HashSet<KeyCode> touches = new HashSet<KeyCode>();
	         updater = new AnimationTimer() {
	            public void handle(long now) {
	            	for (KeyCode code : touches) {
	            		switch (code) {
	            		
	            		case A:
	                    	vitesse = 30;
	                    	break;
	                    
	            		case Z:
	                    	for(int i = 0 ; i < vitesse ; i++)
	                    		if(
	                    				maille[(int) Math.round((joueur.jT.getX()+5000) / 10 +rayon* Math.sin(Math.toRadians(joueur.jRY.getAngle()+45)))][(int)Math.round((-joueur.jT.getZ()+5000) / 10 - rayon*Math.cos(Math.toRadians(joueur.jRY.getAngle()+45)))][(int)(joueur.jT.getY() / -10) - 16] == 0
	                    			&&  maille[(int) Math.round((joueur.jT.getX()+5000) / 10 +rayon* Math.sin(Math.toRadians(joueur.jRY.getAngle()-45)))][(int)Math.round((-joueur.jT.getZ()+5000) / 10 - rayon*Math.cos(Math.toRadians(joueur.jRY.getAngle()-45)))][(int)(joueur.jT.getY() / -10) - 16] == 0
	                    			){
	                    			joueur.jT.setX(joueur.jT.getX() + Math.sin(Math.toRadians(joueur.jRY.getAngle())));
	                    			joueur.jT.setZ(joueur.jT.getZ() + Math.cos(Math.toRadians(joueur.jRY.getAngle())));
	                    		}
	                    	break;
	                    
	                    case S:
	                    	for(int i = 0 ; i < vitesse ; i++)
	                    		if(
	                    				maille[(int) Math.round((joueur.jT.getX()+5000) / 10 -rayon* Math.sin(Math.toRadians(joueur.jRY.getAngle()+45)))][(int)Math.round((-joueur.jT.getZ()+5000) / 10 + rayon*Math.cos(Math.toRadians(joueur.jRY.getAngle()+45)))][(int)(joueur.jT.getY() / -10) - 16] == 0
	                    			&&  maille[(int) Math.round((joueur.jT.getX()+5000) / 10 -rayon* Math.sin(Math.toRadians(joueur.jRY.getAngle()-45)))][(int)Math.round((-joueur.jT.getZ()+5000) / 10 + rayon*Math.cos(Math.toRadians(joueur.jRY.getAngle()-45)))][(int)(joueur.jT.getY() / -10) - 16] == 0
	                    			){
	                    			joueur.jT.setX(joueur.jT.getX() - Math.sin(Math.toRadians(joueur.jRY.getAngle())));
	                    			joueur.jT.setZ(joueur.jT.getZ() - Math.cos(Math.toRadians(joueur.jRY.getAngle())));
	                    		}
	                    	break;
	                    
	                    case Q:
	                    	for(int i = 0 ; i < vitesse ; i++)
	                    		if(
	                    				maille[(int) Math.round((joueur.jT.getX()+5000) / 10 -rayon* Math.cos(Math.toRadians(joueur.jRY.getAngle()+45)))][(int)Math.round((-joueur.jT.getZ()+5000) / 10 - rayon*Math.sin(Math.toRadians(joueur.jRY.getAngle()+45)))][(int)(joueur.jT.getY() / -10) - 16] == 0
	                    			&&  maille[(int) Math.round((joueur.jT.getX()+5000) / 10 -rayon* Math.cos(Math.toRadians(joueur.jRY.getAngle()-45)))][(int)Math.round((-joueur.jT.getZ()+5000) / 10 - rayon*Math.sin(Math.toRadians(joueur.jRY.getAngle()-45)))][(int)(joueur.jT.getY() / -10) - 16] == 0
	                    			){
	                    			joueur.jT.setX(joueur.jT.getX() - Math.cos(Math.toRadians(joueur.jRY.getAngle())));
	                    			joueur.jT.setZ(joueur.jT.getZ() + Math.sin(Math.toRadians(joueur.jRY.getAngle())));
	                    		}
	                    	break;
	                    
	                    case D:
	                    	for(int i = 0 ; i < vitesse ; i++)
	                    		if(
	                    				maille[(int) Math.round((joueur.jT.getX()+5000) / 10 +rayon* Math.cos(Math.toRadians(joueur.jRY.getAngle()+45)))][(int)Math.round((-joueur.jT.getZ()+5000) / 10 + rayon*Math.sin(Math.toRadians(joueur.jRY.getAngle()+45)))][(int)(joueur.jT.getY() / -10) - 16] == 0
	                    			&&  maille[(int) Math.round((joueur.jT.getX()+5000) / 10 +rayon* Math.cos(Math.toRadians(joueur.jRY.getAngle()-45)))][(int)Math.round((-joueur.jT.getZ()+5000) / 10 + rayon*Math.sin(Math.toRadians(joueur.jRY.getAngle()-45)))][(int)(joueur.jT.getY() / -10) - 16] == 0
	                    			){
	                    			joueur.jT.setX(joueur.jT.getX() + Math.cos(Math.toRadians(joueur.jRY.getAngle())));
	                    			joueur.jT.setZ(joueur.jT.getZ() - Math.sin(Math.toRadians(joueur.jRY.getAngle())));
	                    		}
	                    	break;
	                    
	                    
	                    
	                    default:
							break;
	                    }
	                }
	            	
	            	
	            	
	            	
	            }
	        };
	        updater.start();
	        scene.setOnKeyPressed(evt -> {
	        	if(!touches.contains(evt.getCode())) touches.add(evt.getCode());
	        	
	        });
	        
	        firstP = 0;
	        scene.setOnKeyReleased(evt -> {
	        	touches.remove(evt.getCode());
	        	
	            if(evt.getCode() == KeyCode.ESCAPE){
	            	if(scene.getCursor().equals(Cursor.NONE))
	            		scene.setCursor(Cursor.DEFAULT);
	            	else
	            		scene.setCursor(Cursor.NONE);
                	if(pause){
                		pause = false;
                		root2D.getChildren().remove(menuPause);
                	}
                	else {
                		pause = true;
                		root2D.getChildren().add(menuPause);
                		lblScore.toFront();
                	}
	            }
	            if(evt.getCode() == KeyCode.F5){
	            	if(VIE.get()>0){
	            	if(firstP == 0 && !vise){
	            		scene3D.setCamera(joueur.getCamera2());
	            		firstP = 1;
	            		root2D.getChildren().removeAll(croix, arme);
	            		joueur.demasque();
	            	}
	            	else{
	            		if(firstP == 1 && !vise){
	            			scene3D.setCamera(joueur.getCamera3());
	            			firstP = 2;
	            		}else{
	            			if(!vise){
	            				scene3D.setCamera(joueur.getCamera());
	            				firstP = 0;
	            				root2D.getChildren().addAll(croix, arme);
	            				joueur.masque();
	            			}
	            		}
	            	}
	            	}
	            }
	            if(evt.getCode() == KeyCode.A)
	            	vitesse = 10;
	            
	            if(evt.getCode() == KeyCode.SPACE){
	            	if(sautCount == 0 && !vol)
	            		saut = true;
	            }
	        });
	          
	        
	          
			//gravité:
	        sautCount = 0;
	        int rayon2 = 40/10 -2;
	        
	        
			 frame = new AnimationTimer(){
				public void handle(long l){
					if(!saut){	
						for(int i = 0 ; i < 25 ; i++){
							if(maille[(int) Math.round((joueur.jT.getX()+5000) / 10+rayon2* Math.sin(Math.toRadians(joueur.jRY.getAngle()+45)))][(int)Math.round((-joueur.jT.getZ()+5000) / 10 - rayon2*Math.cos(Math.toRadians(joueur.jRY.getAngle()+45)))][(int)(joueur.jT.getY() / -10) - 17] == 0
							&& maille[(int) Math.round((joueur.jT.getX()+5000) / 10+rayon2* Math.sin(Math.toRadians(joueur.jRY.getAngle()-45)))][(int)Math.round((-joueur.jT.getZ()+5000) / 10 - rayon2*Math.cos(Math.toRadians(joueur.jRY.getAngle()-45)))][(int)(joueur.jT.getY() / -10) - 17] == 0
							&& maille[(int) Math.round((joueur.jT.getX()+5000) / 10+rayon2* Math.sin(Math.toRadians(joueur.jRY.getAngle()+135)))][(int)Math.round((-joueur.jT.getZ()+5000) / 10 - rayon2*Math.cos(Math.toRadians(joueur.jRY.getAngle()+135)))][(int)(joueur.jT.getY() / -10) - 17] == 0
							&& maille[(int) Math.round((joueur.jT.getX()+5000) / 10+rayon2* Math.sin(Math.toRadians(joueur.jRY.getAngle()-135)))][(int)Math.round((-joueur.jT.getZ()+5000) / 10 - rayon2*Math.cos(Math.toRadians(joueur.jRY.getAngle()-135)))][(int)(joueur.jT.getY() / -10) - 17] == 0
									){
								joueur.jT.setY(joueur.jT.getY() + 1);
								vol = true;
							}else{
								vol = false;
							}
						}
					}else{
						joueur.jT.setY(joueur.jT.getY() - 20);
						sautCount++;
						if(sautCount == 7){ sautCount = 0;
							saut = false;
						}
						
					}
				
				}
			};
			frame.start();
			
			
			
			//GESTION DES ALIENS:
			index = 1;
			alienCount = 0;
			prochainSpawn = 5;
			Random r = new Random();
			
			aliensupdater = new AnimationTimer(){
				public void handle(long l){
					
					if(alienCount >= prochainSpawn){
						
						//vie
						int valVie = (int) (SCORE*0.002d );
						if(valVie < 1) valVie = 1; if(valVie > 20) valVie=20;
						valVie = valVie - r.nextInt(11);
						if(valVie < 1) valVie = 1;
						
						//puisance
						int valP = (int) (SCORE*0.02d );
						if(valP < 10) valP = 10; if(valP > 200) valP=200;
						valP = valP - r.nextInt(50);
						if(valP < 10) valP = 10;
						
						
						//vitesse
						int valV = (int) (SCORE*0.002d );
						if(valV < 10) valV = 10; if(valV > 25) valV=25;
						valV = valV + (r.nextInt(5+5)-5);
						
						new Alien(valVie, valP, r.nextInt(5 - 1) + 1, valV, index);
						index++;
						alienCount = 0;
						
						int val = (int) (150 - SCORE*0.01d );
						if(val < 50) val = 50;
						prochainSpawn = val - r.nextInt(11);
					}
					alienCount++;
				}
			};
			aliensupdater.start();
			
			//fenêtre
	        primaryStage.setTitle("Area 51 Defender par ROMAINPC");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setMaximized(true);
	        bot.mouseMove((int)(primaryStage.getX() + scene.getX() +scene.getWidth() / 2), (int)(primaryStage.getY() + scene.getY() +scene.getHeight() / 2));
			primaryStage.setOnCloseRequest(e -> Platform.exit());
			primaryStage.getIcons().add(new Image(getClass().getResource("/icone.png").toExternalForm()));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
		launch(args);
	}



	public static Group getRoot() {
		return root;
	}
	public static Joueur getJoueur(){
		return joueur;
	}
	public static void increaseScore(int val){
		SCORE+= val;
		lblScore.setText(" Score : "+SCORE);
	}
	public static void degats(int puissance){
		VIE.set(VIE.get() - puissance);
		if(VIE.get() <= 0 && !finDuJeu){
			finDuJeu = true;
			//fin du Jeu !
			updater.stop();
			updaterSouris.stop();
			frame.stop();
			aliensupdater.stop();
			joueur.demasque();
			PerspectiveCamera camera = new PerspectiveCamera(true);
			camera.setNearClip(1);
			camera.setFarClip(100000); camera.getTransforms().addAll(new Translate(joueur.jT.getX(),0,-joueur.jT.getZ()));
			camera.getTransforms().addAll(new Translate(0,2000,0), new Rotate(90, Rotate.X_AXIS));
			scene3D.setCamera(camera);
			
			if(root2D.getChildren().contains(croix)) root2D.getChildren().remove(croix);
			if(root2D.getChildren().contains(arme)) root2D.getChildren().remove(arme);
			root2D.getChildren().removeAll(jauge,jauge2,lblScore);
			
			joueur.jRZ.setPivotX(joueur.jT.getX()); joueur.jRZ.setPivotZ(joueur.jT.getZ());
			new Timeline(new KeyFrame(Duration.millis(1000), new KeyValue(joueur.jRZ.angleProperty(), 90))).play();
			joueur.jT.setY(joueur.jT.getY() +50);
			Rectangle cacheRouge2 = new Rectangle(); cacheRouge2.setFill(Color.rgb(200, 0, 0, 0.5));
	        cacheRouge2.widthProperty().bind(scene.widthProperty()); cacheRouge2.heightProperty().bind(scene.widthProperty());
				root2D.getChildren().add(cacheRouge2);
				
			Text gameOver = new Text("GAME OVER");
			gameOver.setFill(Color.BLUE); gameOver.setStroke(Color.BLACK);
			gameOver.setTextOrigin(VPos.BOTTOM); gameOver.setStrokeWidth(6);
			gameOver.setFont(Font.font("Verdana", FontWeight.BOLD, scene.getWidth() / 10));
			gameOver.setLayoutX(scene.getWidth() / 2 - (gameOver.getBoundsInLocal().getMaxX() / 2));
			Text scoreOver = new Text("Score : "+SCORE);
			scoreOver.setFill(Color.BLUE); scoreOver.setStroke(Color.BLACK);
			scoreOver.setTextOrigin(VPos.TOP); scoreOver.setStrokeWidth(4);
			scoreOver.setFont(Font.font("Verdana", FontWeight.BOLD, scene.getWidth() / 15));
			scoreOver.setLayoutX(scene.getWidth() / 2 - (scoreOver.getBoundsInLocal().getMaxX() / 2));
			scene.widthProperty().addListener(e -> {
				gameOver.setFont(Font.font("Verdana", FontWeight.BOLD, scene.getWidth() / 10));
				gameOver.setLayoutX(scene.getWidth() / 2 - (gameOver.getBoundsInLocal().getMaxX() / 2));
				scoreOver.setFont(Font.font("Verdana", FontWeight.BOLD, scene.getWidth() / 15));
				scoreOver.setLayoutX(scene.getWidth() / 2 - (scoreOver.getBoundsInLocal().getMaxX() / 2));
			});
			gameOver.layoutYProperty().bind(scene.heightProperty().divide(2));
			scoreOver.layoutYProperty().bind(scene.heightProperty().divide(2));
			root2D.getChildren().addAll(scoreOver, gameOver);
			
			player.stop();
			Media mortSon = new Media(Main.class.getResource("/mort.MP3").toExternalForm());
			MediaPlayer playerMort = new MediaPlayer(mortSon);
			playerMort.play();
		}
		
		if(!cacheRVisible && !finDuJeu){
			cacheRVisible = true;
			root2D.getChildren().add(cacheRouge);
			new Timeline(new KeyFrame(Duration.millis(200), ae ->{ root2D.getChildren().remove(cacheRouge); cacheRVisible = false;})).play();
		}
	}
	public static IntegerProperty getVie(){
		return VIE;
	}
}
