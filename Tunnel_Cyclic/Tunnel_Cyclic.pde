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
  //imageMode(CENTER);
  blendMode(BLEND);
  background(0, 0, 0);
}

void draw() {

  fill(0, 0, 0, 54);
  stroke(255);
  strokeWeight(4);
  pushMatrix();
  translate(width/2, height/2);
  rotate(-millis()*timeScale*2);
  noiseDetail(4, 0.6);
  //float s =width/2;
  float s = noise(millis()*timeScale);
  s = map(s, 0, 1, width/3, width);
  rect(0, 0, s, s);
  popMatrix();

  t = get();

  pushMatrix();

  translate(width/2, height/2);
  rotate(millis());
  scale(0.85);  
  image(t, 0, 0, width, height);
  popMatrix();



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
