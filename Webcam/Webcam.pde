boolean recording = false;        // A boolean to track whether we are recording are not
float timeScale = 0.0005f;        //global amount to scale millis() for all animations
float currentTime = 0;
String title=getClass().getSimpleName();

import gab.opencv.*;
import processing.video.*;
import java.awt.*;
import spout.*;

Capture video;
OpenCV opencv;

Capture cam;

int i = 0;

void settings()
{
  size(640, 480);
}

void setup() {
  frameRate(30);                  //all code should be frame-rate independent. hint: use millis()
  smooth(2);                      //enable highest level of anti-aliasing your system can handle
  ellipseMode(CENTER);
  rectMode(CENTER);
  imageMode(CENTER);
  blendMode(NORMAL);
  colorMode(HSB);
  background(0, 0, 0);

  String[] cameras = Capture.list();

  if (cameras.length == 0) {
    println("There are no cameras available for capture.");
    exit();
  } else {
    println("Available cameras:");
    for (int i = 0; i < cameras.length; i++) {
      println(cameras[i]);
    }

    // The camera can be initialized directly using an 
    // element from the array returned by list():
    video = new Capture(this, width, height, cameras[0]);
  }      
  opencv = new OpenCV(this, width, height);
  opencv.loadCascade(OpenCV.CASCADE_FRONTALFACE);  

  video.start();
}
void captureEvent(Capture c) {
  c.read();
}
void draw() {
  tick();  //no touch

  opencv.loadImage(video);
  opencv.contrast(20);
  opencv.brightness(17);
  image(opencv.getOutput(), width/2, height/2);

  noFill();
  stroke(0, 255, 0);
  strokeWeight(3);
  Rectangle[] faces = opencv.detect();
  println(faces.length);
  for (Rectangle f : faces) {
    pushStyle();
    rectMode(CORNER);
    //rect(f.x, f.y, f.width, f.height);
    PImage tp = get(f.x, f.y, f.width, f.height);
    pushMatrix();
    translate(f.x+(f.width/2), f.y+(f.height/2));
    scale(2);
    for (int i =0; i<10; i++) {
      rotate(currentTime*(1f/10f)*TAU);
      scale(0.9);
      image(tp, 0, 0);
    }
    popMatrix();
    popStyle();
  }
  tock();  //no touch
}

/**
 ================================
 Mandatory Code
 ================================
 **/
void keyPressed() {
  if (key == 'r' || key == 'R') {
    recording = !recording;
  }
  if (key == 'a' || key == 'A') {
    String[] cameras = Capture.list();
    video.stop();
    video = new Capture(this, width, height, cameras[i]);
    video.start();
  }
}
void tick() {
  currentTime = millis()*timeScale;
}
void tock() {
  if (recording) {
    saveFrame("/output/"+title+"_frame_####.png");
  }
  //ui();
}
void ui() {
  drawFPS(255);
}
void drawFPS(int textColor)
{
  pushMatrix();
  noStroke();
  fill(0);
  rect(0, 0, width, height);
  fill(textColor);
  text(frameRate, 0, 15);
  popMatrix();
}

// linear interpolate two colors in HSB space
color lerpColorHSB(color c1, color c2, float amt) {
  amt = min(max(0.0, amt), 1.0);
  float h1 = hue(c1), s1 = saturation(c1), b1 = brightness(c1);
  float h2 = hue(c2), s2 = saturation(c2), b2 = brightness(c2);
  // figure out shortest direction around hue
  float z = g.colorModeZ;
  float dh12 = (h1>=h2) ? h1-h2 : z-h2+h1;
  float dh21 = (h2>=h1) ? h2-h1 : z-h1+h2;
  float h = (dh21 < dh12) ? h1 + dh21 * amt : h1 - dh12 * amt;
  if (h < 0.0) h += z;
  else if (h > z) h -= z;
  colorMode(HSB);
  return color(h, lerp(s1, s2, amt), lerp(b1, b2, amt));
}
