boolean recording = false;        // A boolean to track whether we are recording are not
float timeScale = 1f;        //global amount to scale millis() for all animations
float currentTime=0f;
String title=getClass().getSimpleName();
float lastUpdate = 0f;
float bpm=125f/60f;
float updateFrequency = 1000f/bpm;
float udms = bpm/1000f;
PVector[] objs = new PVector[16];
float hs = 0.7;
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
  blendMode(LIGHTEST);
  colorMode(HSB);
  noiseDetail(3, 0.75);
  float fov = PI/3.0;
  float cameraZ = (height/2.0) / tan(fov/2.0);
  perspective(fov, float(width)/float(height), 
    cameraZ/10.0f, cameraZ*1000.0);
  for (int i=0; i < objs.length; i++) {
    objs[i] = new PVector(random(0f, height), random(0f, height));
  }
  background(0, 0, 0);
}

void draw() {
  hs = map(sin(millis()*(udms/4f)), -1f, 1f, 0.20, 0.6);
  tick();  //no touch
  clear();
  //background(0, 0, 0);
  pushStyle();
  colorMode(RGB);
  fill(0);
  stroke(255, 255, 255);
  strokeWeight(5);
  beginShape();
  float udpct = (currentTime-lastUpdate)/updateFrequency;
  if (round(currentTime-lastUpdate)>=updateFrequency*2) {
    for (int i=0; i < objs.length; i++) {
      objs[i] = new PVector(random(0f, height), random(0f, height));
    }
    lastUpdate=currentTime;
  }

  for (int i=0; i < objs.length; i++) {
    rect(lerp(height/2, objs[i].x, udpct), 
      lerp(height/2, objs[i].y, udpct), 
      (map(sin(millis()*(udms)), -1f, 1f, 0.5f, 1f))*noise(1, currentTime*0.0001f)
      *height/4f, 
      (map(sin(millis()*(udms)), -1f, 1f, 0.5f, 1f))*noise(2, currentTime*0.0001f)
      *height/4f);
  }  
  endShape(CLOSE);
  popStyle();
  pushStyle();
  stroke(255);
  strokeWeight(10);
  noFill();
  beginShape(LINES);
  for (int i=0; i < objs.length; i++) {
    vertex(lerp(height/2f, objs[i].x, udpct), objs[i].y);
  }
  endShape();
  popStyle();
  PImage p = get();

  background(0);
  translate(width/2f, height/2f);
  pushMatrix();
  for (int i=0; i < 128; i++) {
    pushMatrix();
    for (int ii=0; ii < 4; ii++) {
      drawStrip(p);
      rotateZ(PI/2f);
    }
    popMatrix();
    translate(0, 0, -height*hs);
    rotateZ((currentTime*(udms/16))*(PI/100));
  }
  popMatrix();
  p=get();
  background(0);
  rotate(millis()*(udms/8f)*TWO_PI);
  image(p, 0, 0);
  tock();  //no touch
}
void drawStrip(PImage img) {
  pushMatrix();
  pushStyle();

  noStroke();
  translate(0, img.height/2f);
  rotateX(PI*0.5f);


  beginShape();
  texture(img);
  vertex(-width/2f, -height*hs, 0, 0, 0);
  vertex(width/2f, -height*hs, 0, img.width, 0);
  vertex(width/2f, 0, 0, img.width, img.height);
  vertex(-width/2f, 0, 0, 0, img.height);
  endShape();
  popStyle();
  popMatrix();
  //pushStyle();
  //strokeWeight(20);
  //stroke(0, 255, 255);
  //point(-img.width, -img.height, 0);
  //stroke(74, 255, 255);
  //point(img.width, -img.height, 0);
  //stroke(163, 255, 255);
  //point(img.width, img.height, 0);
  //stroke(204, 255, 255);
  //point(-img.width, img.height, 0);
  //popStyle();
  //popMatrix();
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
