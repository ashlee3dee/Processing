boolean recording = false;        // A boolean to track whether we are recording are not
float timeScale = 0.0005f;        //global amount to scale millis() for all animations

float currentTime = 0;
void settings()
{
  size(800, 800, P3D);
}

void setup() {
  frameRate(30);                  //all code should be frame-rate independent. hint: use millis()
  smooth(2);                      //enable highest level of anti-aliasing your system can handle
  ellipseMode(CENTER);
  rectMode(CENTER);
  blendMode(NORMAL);
  background(0, 0, 0);
}

void draw() {
  tick();  //no touch
  background(0);
  translate(width/2, height/2);

  float theta =TWO_PI*0.77;
  PVector start = new PVector(0, 0);
  PVector end = new PVector(width/4, height/4);
  PVector middle = PVector.add(start, end).mult(0.5);
  PVector tangent = new PVector(end.y - start.y, start.x - end.x);
  float mag = tangent.mag();
  //println(tangent.mag()/2f);
  tangent.normalize();
  tangent.mult(mag/2);
  stroke(255*(1f/3f), 255, 255);
  strokeWeight(2);
  line(start.x, start.y, end.x, end.y);
  stroke(255*(2f/3f), 255, 255);
  strokeWeight(10);
  point(middle.x, middle.y);
  line(
    middle.x - tangent.x, 
    middle.y - tangent.y, 
    middle.x + tangent.x, 
    middle.y + tangent.y
    );
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
  currentTime = millis()*timeScale;
}
void tock() {
  if (recording) {
    saveFrame("output/frame_####.png");
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
