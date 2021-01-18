boolean recording = false;        // A boolean to track whether we are recording are not
float globalTimeScale = 0.0005f;        //global amount to scale millis() for all animations
float currentTime = 0;
String title=getClass().getSimpleName();
float avg = 0;
float[] dsts = new float[4096];
void settings()
{
  size(800, 800, P3D);
  smooth(2);                      //enable highest level of anti-aliasing your system can handle
}

void setup() {
  frameRate(120);                  //all code should be frame-rate independent. hint: use currentTime (millis()*globalglobalTimeScale)
  ellipseMode(CENTER);
  rectMode(CENTER);
  imageMode(CENTER);
  blendMode(NORMAL);
  colorMode(HSB, TAU, TAU, TAU);
  background(0, 0, 0);
}

void draw() {
  tick();  //no touch

  PVector p1 = new PVector(random(0,width),random(0,height));
  PVector p2 = new PVector(random(0,width),random(0,height));
  dsts[frameCount] = PVector.dist(p1,p2);
  
  float currentAvg = 0;
  for(int i=0; i < frameCount; i++){
    currentAvg+=dsts[i];
  }
  currentAvg/=frameCount;
  print("frame: "+frameCount);
  print("|distance: "+dsts[frameCount]);
  println("|current average: "+currentAvg);
  
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
}
void tick() {
  currentTime = millis()*globalTimeScale;
}
void tock() {
  if (recording) {
    saveFrame("/output/"+title+"_frame_####.png");
  }
  ui();
}
void ui() {
  drawFPS(255);
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
