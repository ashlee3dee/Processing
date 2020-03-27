boolean recording = false;        // A boolean to track whether we are recording are not
float timeScale = 0.0001f;        //global amount to scale millis() for all animations

float currentTime = 0;

/**================================
 Setup
 ================================**/
void settings()
{
  size(800, 800, P3D);
}
void setup() {
  frameRate(30);                  //all code should be frame-rate independent. hint: use millis()
  smooth(8);                      //enable highest level of anti-aliasing your system can handle
  ellipseMode(CENTER);
  rectMode(CENTER);
  imageMode(CENTER);
  blendMode(NORMAL);
  colorMode(HSB);
  background(0, 0, 0);
}
/**================================
 Main Loop
 ================================**/
void draw() {
  tick();  //no touch
  PImage i = get();
  pushMatrix();
  translate(width/2, height/2);
  scale(0.9);
  squiggleBox(false);
  squiggleBox(true);
  popMatrix();
  pushMatrix();
  noiseDetail(3);
  float sf = map(noise(currentTime/2), 0, 1, 0.9, 0.98);
  tint(255, 255);
  translate(width/2, height/2);
  rotate(-currentTime*TWO_PI*0.04);
  image(i, 0, 0, width*sf, height*sf);
  popMatrix();
  noStroke();
  fill(0, 0, 0, 0);
  rect(width/2, height/2, width, height);
  tock();  //no touch
}
/**================================
 Functions
 ================================**/
void squiggleBox(boolean mode) {
  translate(0, 0);
  if (mode)
    strokeWeight(3);
  else
    strokeWeight(5);
  noFill();
  PVector[] c = new PVector[6];    //list of corners
  for (int i=0; i < c.length; i++) {
    float step = (TWO_PI/c.length);
    PVector t = new PVector(sin((i*step)+currentTime), cos((i*step)+currentTime));
    c[i] = t;//.mult(0.5);
  }
  int r = 64;  //vertex resolution of an edge
  beginShape();
  for (int i=0; i < c.length; i++) {

    PVector start, end;
    start = c[i];
    end = c[(i+1)%c.length];

    for (int v=0; v < r; v++) {

      noiseDetail(6);
      float    pct = (float)v/r;                                     //(0-1)f  across line
      float    str = sin(pct*PI);                                    //blending strength
      PVector  np  =  PVector.lerp(start, end, pct);                 //new position
      PVector  tan = new PVector(end.y - start.y, start.x - end.x);  //tangent vector
      float    ns  =  noise(currentTime*3)*0.2;                      //noise space scale
      float    nm  =  width/3;                                       //noise max magnitude
      noiseDetail(4);
      float    no  = map(noise(currentTime*2), 0, 1, 0.2, 2);
      float    nv  =  (noise(v*ns, currentTime*2)-no)*nm;            //noise value
      tan.normalize().mult(str).mult(nv);
      np.mult(height/2).add(tan);
      if (mode)
        stroke((currentTime+(255*str))%230, 231, 227);
      else stroke(0, 0, 0, 255);
      curveVertex(np.x, np.y);
    }
  }
  endShape();
}
/**================================
 Mandatory Code
 ================================**/
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
