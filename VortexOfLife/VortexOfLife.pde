import peasy.*;
PeasyCam camera;
float r, l, e, m;

void setup() {
  size(512, 512, P3D);
  r = 0.6;
  l = 3;
  e = 0.3;
  m = 200;
  camera = new PeasyCam(this, 30);
  //cam.setSuppressRollRotationMode();
  perspective(200, 1, 0.1, 1000);
  frameRate(60);
}

void draw() {
  r = 0.60;
  l = 4.20;
  e = 0.14;
  m = 200;
  background(0);
  translate(width/2, height/2);
  stroke(255);
  //for (float e = -1.0; e < 1.0; e+=0.100) {
  int fillColor = 0;
  for (float s = 0; s < 2; s+=0.2500) {
    for (float t = -7; s < 10; s+=0.0500) {
      float y = (m*exp(2*e*t) )/ (1+exp(2*e*t));
      float x = 2*y/exp(2*e*l*t/l+1)*sin(t+s*((float)Math.PI))*r;
      if (cos((float)(t+s*Math.PI)) < 0) {
      } else {
        fillColor+=10;
        fill(fillColor);
        pushMatrix();
        translate(x, -y);
        sphere(10);
        popMatrix();
      }
    }
    //}
  }
}
