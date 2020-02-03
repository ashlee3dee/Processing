boolean recording = false;        // A boolean to track whether we are recording are not
float timeScale = 0.0001f;        //global amount to scale millis() for all animations

void settings()
{
  size(800, 800, P2D);
}

void setup() {
  frameRate(30*4);                  //all code should be frame-rate independent. hint: use millis()
  smooth(2);                      //enable highest level of anti-aliasing your system can handle
  ellipseMode(CENTER);
  rectMode(CENTER);
  blendMode(ADD  );
  colorMode(HSB, 100);
  background(0, 0, 0);
}

void draw() {
  //background(0, 0, 0);
  float t = millis()*timeScale;
  float sm = 9.0;
  float sm2 = 3.0;
  float pm = 2.0;
  float pm2 = 5.0;
  float cm = 1.4;
  float size = width*0.5;
  PVector p = new PVector(
    sin(noise(t*pm, t*pm2))*(width*1.0), 
    sin(noise(t*pm2, t*pm))*(height*1.0)
    );
  color c = color(noise(t*cm)*150, 100, 100, 1);
  //translate(width/2, height/2);
  noFill();
  strokeWeight(3);
  stroke(c);
  ellipse(p.x, p.y, noise(t*sm, t*sm2)*size, noise(t*sm2, t*sm)*size);
  ui();                       //draw recording UI
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
