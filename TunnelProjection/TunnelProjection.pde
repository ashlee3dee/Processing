boolean recording = false;        // A boolean to track whether we are recording are not
float timeScale = 0.0005f;        //global amount to scale millis() for all animations
float currentTime = 0;
String title=getClass().getSimpleName();
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
  colorMode(HSB);
  background(0, 0, 0);
}

void draw() {
  tick();  //no touch
  clear();
  noStroke();
  noFill();
  pushStyle();
  colorMode(RGB);
  stroke(255, 255, 255);
  strokeWeight(5);
  noFill();
  beginShape();
  for (int i=0; i < 32; i++) {
    PVector p = new PVector(random(0, height), random(0, height));
    rect(p.x, p.y, 100, 100);
    //point(p.x, p.y);
    //vertex(p.x, p.y);
  }
  endShape(CLOSE);
  popStyle();
  PImage p = get();
  //background(0);
  drawStrip(p);
  tock();  //no touch
}
void drawStrip(PImage img) {
  background(0);
  translate(width/2, height/2);
  scale(0.5);
  //rotateY(map(mouseX, 0, width, -PI, PI));
  beginShape();
  texture(img);
  float quadSize = 500;
  vertex(-quadSize, -quadSize, 0, 0, 0);
  vertex(quadSize, -quadSize, 0, img.width, 0);
  vertex(quadSize, quadSize, 0, img.width, img.height);
  vertex(-quadSize, quadSize, 0, 0, img.height);
  endShape();
  strokeWeight(20);
  stroke(0, 255, 255);
  point(-quadSize, -quadSize, 0);
  stroke(74, 255, 255);
  point(quadSize, -quadSize, 0);
  stroke(163, 255, 255);
  point(quadSize, quadSize, 0);
  stroke(204, 255, 255);
  point(-quadSize, quadSize, 0);
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
