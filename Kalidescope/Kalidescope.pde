import processing.video.*;
boolean recording = false;        // A boolean to track whether we are recording are not
int targetFPS = 60;
float globalTimeScale = 0.0005f;        //global amount to scale millis() for all animations
float simTime = 0;
String title=getClass().getSimpleName();
public String inputDirectory = "/input/";
public String outputDirectory = "output/";

public Movie movieA, movieB;

void settings()
{
  size(displayWidth, displayHeight, P2D);
  smooth(0);                      //enable highest level of anti-aliasing your system can handle
}

void setup() {
  //fullScreen();
  frameRate(targetFPS);                  //all code should be frame-rate independent. hint: use currentTime (millis()*globalglobalTimeScale)
  ellipseMode(CENTER);
  rectMode(CENTER);
  imageMode(CENTER);
  blendMode(NORMAL);
  colorMode(HSB, TAU, TAU, TAU);
  background(0, 0, 0);
  noiseDetail(2, 0.50);
  //global vars
  movieA = new Movie(this, "01w3rmeD-S2Fzd6_.mp4");
  //movieB = new Movie(this, inputDirectory+"movieB.mov");
  movieA.loop();
}
void movieEvent(Movie m) {
  if (m.time()>=(m.duration()-0.25f)) {
    m.jump(0.0f);
  }
  m.read();
}
public float sinewave(float frequency) {
  float t = sin((millis()/1000f)*frequency*TAU);
  println(t);
  return t;
}
void draw() {
  //PImage bg;
  //bg = this.get(0, 0, width, height);
  //pushMatrix();
  //translate(width/2, height/2);
  //scale(0.9);
  //image(bg, 0, 0);
  //popMatrix();
  tick();  //no touch
  if (millis()>5000) {
    PVector sampleOffset = new PVector((abs(movieA.width-width)/4)*sin(simTime*5), (abs(movieA.height-height)*0.75)*cos(simTime*7));
    pushMatrix();
    translate(width/2, height/2);
    translate(sampleOffset.x, sampleOffset.y);
    float minScale=0.30;
    float maxScale=1.90;
    float noiseSpeed = 1.20;
    float rotSpeed = 1.8;
    int loops = 13;
    for (int i=0; i < loops; i++) {
      scale(map(noise(simTime*noiseSpeed, 5.3), 0.00, 1.00, minScale, maxScale));
      rotate(simTime+(map(noise(simTime*rotSpeed, 1.5f),0f,1f,-(TAU/loops),TAU/loops)));
      image(movieA, 0, 0);
      scale(map(noise(simTime),0,1,0.6,0.95));
    }
    popMatrix(); 

    //noFill();
    //stroke(color(0, 0, 255));
    //strokeWeight(5);
    //rect(width/2, height/2, width/2, height/2);
    //println(sampleOffset);
    drawSymmetry();
  }
  tock();  //no touch
}

public void drawSymmetry() {
  //this.loadPixels();
  PImage buf;
  buf = this.get(width/4, height/4, width/2, height/2);
  //upper left quadrant
  pushMatrix();
  translate(width/4, height/4);
  //scale(-1.0, 1.0);// flip x axis backwards
  image(buf, 0, 0);
  popMatrix();

  //upper right quadrant
  pushMatrix();
  translate(width, 0); //move origin to upper right
  scale(-1.0, 1.0);// flip x axis backwards
  translate(width/4, height/4);
  image(buf, 0, 0);
  popMatrix();

  //lower right quadrant
  pushMatrix();
  translate(width, height); //move origin to lower right
  scale(-1.0, -1.0);// flip x and y axis backwards
  translate(width/4, height/4);
  image(buf, 0, 0); //not sure why it needed a 1px offset
  popMatrix();

  //lower left quadrant
  pushMatrix();
  translate(0, height); //move origin to lower left
  scale(1.0, -1.0);// flip y axis backwards
  translate(width/4, height/4);
  image(buf, 0, 0);
  popMatrix();
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
}
void tick() {
  simTime = millis()*globalTimeScale;
}
void tock() {
  if (recording) {
    saveFrame("/output/"+title+"_frame_####.png");
  }
  ui();
}
void ui() {
  drawFPS(color(TAU, 0, TAU));
}
void drawFPS(int textColor)
{
  pushMatrix();
  noStroke();
  fill(0);
  rect(0, 0, 100, 40);
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
