/* This file is a processing file. Processing files are similar
   to subclasses of the processing class PApplet. In Processing,
   this file is the main file that draws and detects inputs and outputs */

import processing.core.PVector;

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
