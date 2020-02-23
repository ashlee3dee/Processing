import geomerative.*;
//import org.apache.batik.svggen.font.*;
//import org.apache.batik.svggen.font.table.*;



boolean recording = false;        // A boolean to track whether we are recording are not
float timeScale = 0.0005f;        //global amount to scale millis() for all animations
float currentTime = 0;
String title=getClass().getSimpleName();
Sun s;
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
  RG.init(this);
  s = new Sun(new PVector(0, 0), 100);
}

void draw() {
  tick();  //no touch
  background(0, 0, 0);
  s.draw();
  tock();  //no touch
}

class Sun
{
  PVector p;
  float s = 10;
  RShape circle;
  RShape rectangle;
  float rWidth;
  int rNum = 8;
  Sun(PVector position, float size) {
    p=position;
    s = size;
    rWidth = s/rNum;
    circle = RShape.createEllipse(p.x, p.y, s, s);
    rectangle = RShape.createRectangle(-s/2, 0, s, rWidth/2);
  }

  void draw() {
    RShape buffer=new RShape(circle);
    pushMatrix();
    translate(width/2, height/2);
    pushStyle();
    noStroke();
    fill(#FE3B22);
    //int spacing = constrain(rWidth,rWidth/rNum,);
    for (int i=0; i < rNum; i++) {
      RShape t = new RShape(rectangle);
      t.height = (i/rNum)*(rWidth/2);
      t.translate(0, rWidth*i);
      buffer = RG.diff(buffer, t);
    }

    //rectangle.
    buffer.draw();
    //ellipse(0, 0, size, size);
    popStyle();
    popMatrix();
  }
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
