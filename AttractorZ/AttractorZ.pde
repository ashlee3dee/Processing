boolean recording = false;        // A boolean to track whether we are recording are not
float timeScale = 0.0001f;        //global amount to scale millis() for all animations

color c, n;
float l = 0;
boolean isVivid = true;
boolean doDraw = true;
void settings()
{
  size(800, 800, P3D);
}

void setup() {
  smooth(8);           //enable highest level of anti-aliasing your system can handle
  frameRate(60);       //all code should be frame-rate independent. hint: use millis()
  colorMode(HSB);
  ellipseMode(CENTER);
  rectMode(CENTER);
  blendMode(NORMAL);
  background(0, 0, 0);
  c = randomColor(isVivid);
  n = randomColor(isVivid);
}

void draw() {

  float currentTime = (millis()*timeScale);
  //rect(0, 0, 0, 1);
  if (doDraw) {
    l+=0.01;
    noiseDetail(3, 0.65);
    float ptsMult = 1.5;
    PVector p = new PVector(
      noise(currentTime*ptsMult), 
      noise(1000+(currentTime*ptsMult)));
    blob(p);
    ui();                       //draw recording UI
    if (frameCount<5)
      background(0);
  }
}

void blob(PVector pos) {

  float currentTime = (millis()*timeScale);
  float maxSize = width/6;
  float tsMult = 0.81;
  //float sizeNoise = noise(100+(currentTime*tsMult));
  //maxSize*=sizeNoise;
  PVector[] points = new PVector[32];
  for (int i=0; i < points.length; i++) {
    float percent = (float)i/points.length;
    float theta = TWO_PI*percent;
    noiseDetail(6, 0.65);
    float r = noise(
      sin(theta)+(currentTime*tsMult), 
      cos(theta)+(currentTime*tsMult), 
      (currentTime*tsMult)+1000);
    r*=maxSize;
    PVector newPos = new PVector(sin(theta), cos(theta));
    noiseDetail(5, 0.65);
    //newPos.mult(noise((i*(sin(currentTime)*tsMult))+(currentTime))*r);
    newPos.mult(r);
    points[i] = newPos;
  }
  noFill();
  strokeWeight(4);
  color interA = lerpColorHSB(c, n, l);
  //stroke((noise(5000+(currentTime))*300)-25, 255, 192, 32);
  stroke(interA);
  pushMatrix();
  beginShape();
  translate(width/2, height/2);
  pos.sub(0.5, 0.5);
  translate(pos.x*(width*0.75), pos.y*(height*0.75));
  //translate((pos.y*(width/2))*cos(pos.x*TWO_PI), (pos.y*(height/2))*sin(pos.x*TWO_PI));
  curveVertex(points[0].x, points[0].y);
  for (int i=0; i < points.length; i++) {
    curveVertex(points[i].x, points[i].y);
  }
  curveVertex(points[0].x, points[0].y);
  curveVertex(points[1].x, points[1].y);
  endShape();
  popMatrix();

  //println(l);
  if (l>1.0) {
    //n = color(random(1)*255, 192+(random(1)*64), 255, 32);
    c = n;
    n = randomColor(isVivid);

    l=0;
  }
  println(l);
  //ellipse(pos.x, pos.y, sizeNoise*maxSize, sizeNoise*maxSize);
}

color randomColor(boolean vivid) {
  if (vivid)
    return color(random(1)*255, 255, 192, 255);
  return color(random(1)*255, random(1)*192, 8+(random(1)*16), 1);
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
  if (key == 'd' || key == 'D') {
    doDraw=!doDraw;
  }
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
