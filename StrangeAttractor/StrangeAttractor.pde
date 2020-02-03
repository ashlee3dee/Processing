import peasy.*;
boolean recording = false;        // A boolean to track whether we are recording are not
float timeScale = 0.0005f;        //global amount to scale millis() for all animation
float x, y, z;
//peasycam
PeasyCam cam;
float a = random(0, 100);
float b = random(0, 100);
float c = random(0, 100);
ArrayList<PVector> points = new ArrayList<PVector>();
void setup() {
  size(800, 800, P3D);
  frameRate(60);                  //all code should be frame-rate independent. hint: use millis()
  smooth(2);                      //enable highest level of anti-aliasing your system can handle
  ellipseMode(CENTER);
  rectMode(CENTER);
  blendMode(NORMAL);
  x=random(0, 1);
  y=random(0, 1);
  z=random(0, 1);
  println(a, b, c);
  {//peasycam
    cam = new PeasyCam(this, width);
    cam.setMinimumDistance(width/4);
    cam.setMaximumDistance(width*4);
    cam.setActive(true);
    //frustum(-5.8, 5.8, -5.8, 5.8, 10, 5000);
  }
}

void draw() {
  println(a, b, c);
  points.clear();
  for (int i=0; i < 10000; i++) {


    float dt = 0.01;
    float dx = a * (y-x);
    float dy = x * (b-z)-y;
    float dz = x * y - c*z;
    dx*=dt;
    dy*=dt;
    dz*=dt;

    x = x+dx;
    y = y+dy;
    z = z+dz;

    points.add(new PVector(x, y, z));
  }
  //println(x, y, z);
  background(0, 0, 0);
  //translate(width/2, height/2);
  noFill();
  pushMatrix();
  //scale(10);
  beginShape();
  for (int i = 0; i < points.size(); i++) {
    PVector v = points.get(i);
    //stroke(abs(sin(i*0.04))*255, abs(sin(i*0.01))*255, abs(sin(i*0.03))*255, 128);
    stroke(i%255);
    vertex(v.x, v.y, v.z);
  }
  endShape();
  popMatrix();
  cam.beginHUD();
  blendMode(NORMAL);
  fill(0);
  stroke(0);
  ui();   
  cam.endHUD();
  //draw recording UI
}
/**
 ================================
 Mandatory Code
 ================================
 **/
void ui() {
  if (recording) {
    saveFrame("output/frame_####.png");
  }
  drawFPS(255);
}
void drawFPS(int textColor)
{  
  noStroke();
  fill(0);
  rect(0, 0, 100, 40);
  fill(textColor);
  text(frameRate, 0, 15);
}
void keyPressed() {
  if (key == 'r' || key == 'R') {
    recording = !recording;
  }
  if (key == 'q' || key == 'Q') {
    a = random(0, 100);
    b = random(0, 100);
    c = random(0, 100);
    x=0.01;
    y=0.01;
    z=0.01;
    points.clear();
  }
}
