boolean recording = false;        // A boolean to track whether we are recording are not
float timeScale = 0.0005f;        //global amount to scale millis() for all animations
boolean redraw = true;
float currentTime = 0;
int noderes = 4;

BSPNode[] bspnodes = new BSPNode[noderes*noderes];


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
  noiseSeed((long)random(2^32));
  for (int ix=0; ix < noderes; ix++) {
    for (int iy=0; iy < noderes; iy++) {
      BSPNode b = new BSPNode((int)random(2, 13), (width*0.9/(noderes)));
      bspnodes[noderes * iy + ix] = b;
    }
  }
}

void draw() {
  tick();  //no touch
  PImage p = get();
  if (redraw) {
    background(0, 0, 0);
    //println(floor(frameCount%fameRate));
    //bsp.reset();
    //bsp.split();
    for (int ix=0; ix < noderes; ix++) {
      for (int iy=0; iy < noderes; iy++) {
        //if (random(1)>0.75) {
          pushMatrix();
          pushStyle();
          translate((ix+0.5)*(width/noderes), (iy+0.5)*(height/noderes));
          PVector s = bspnodes[noderes * iy + ix].size;
          translate(s.x*2, s.y*2);
          //bspnodes[noderes * iy + ix].reset();
          bspnodes[noderes * iy + ix].draw();
          popStyle();
          popMatrix();
        //}
      }
    }
    redraw=false;
  }

  pushMatrix();
  translate(width/2, height/2);
  rotate(PI+((TAU/32)*currentTime)%(TAU));
  scale(0.9);
  tint(238, 255);
  //image(p, 0, 0);
  popMatrix();
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
  if (key == 'a' || key == 'A') {
    //saveFrame("output/frame_####.png");
    for (BSPNode b : bspnodes) {

      b.reset();
      redraw=true;
    }
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
