boolean recording = false;        // A boolean to track whether we are recording are not
float timeScale = 0.0005f;        //global amount to scale millis() for all animations
PImage t;
void settings()
{
  size(800, 800, P3D);
}

void setup() {
  frameRate(30);                  //all code should be frame-rate independent. hint: use millis()
  smooth(2);                      //enable highest level of anti-aliasing your system can handle
  ellipseMode(CENTER);
  rectMode(CENTER);
  imageMode(CENTER);
  blendMode(NORMAL);
  background(0, 0, 0);
}

void draw() {
  fill(0,0,0,8);
  stroke(255);
  strokeWeight(1);
  pushMatrix();
  translate(width/2, height/2);

  rotate(millis()*timeScale*2);
  noiseDetail(4, 0.6);
  float s = noise(millis()*timeScale*2)*(width);
  s = width*(1+(noise(millis()*timeScale*2)*0.1));
  rect(0, 0, s, s);
  popMatrix();
  //background(0, 0, 0);
  //noFill();
  t = get();
  //background(0);
  pushMatrix();
  translate(width/2, height/2);
  rotate((millis()*timeScale*0.1)%PI);
  scale(0.89+(sin(millis()*timeScale*1)*0.1));
  //background(0);
  image(t, 0, 0);
  popMatrix();


  //for (int i=0; i < 100; i++) {
  //  PVector p = new PVector(random(0, width), random(0, height));
  //  stroke(255);
  //  strokeWeight(10);
  //  point(p.x, p.y);
  //}


  //ui();                       //draw recording UI
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
}
