import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.core.PVector; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class src extends PApplet {

/* This file is a processing file. Processing files are similar
   to subclasses of the processing class PApplet. In Processing,
   this file is the main file that draws and detects inputs and outputs */



MainApplication app;

public void settings() {
    size(800, 800);
}

public void mousePressed() {
    InputHandler.handleMousePressed(new PVector(mouseX, mouseY));
}

public void mouseReleased() {
    InputHandler.handleMouseReleased(app, new PVector(mouseX, mouseY));
}

public void mouseDragged() {
    InputHandler.handleMouseDragged(app, new PVector(mouseX, mouseY));
}

public void keyPressed() {
    InputHandler.handleKeyPressed(app, key);
}

public void setup() {
    app = new MainApplication(this);
}

public void draw() {
    app.draw();
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "src" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
