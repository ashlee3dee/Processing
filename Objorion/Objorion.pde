import nervoussystem.obj.*;
import peasy.PeasyCam;
boolean recordObj = false;
boolean recording = false;        // A boolean to track whether we are recording are not
boolean colorType = false;
float timeScale = 0.0005f;        //global amount to scale millis() for all animations
float currentTime = 0;
String title=getClass().getSimpleName();
XML xml;
XML[] children;
PeasyCam cam;

String file="/myship";

void settings()
{
  size(800, 800, P3D);
}

void setup() {
  frameRate(30);                  //all code should be frame-rate independent. hint: use millis()
  smooth(2);                      //enable highest level of anti-aliasing your system can handle
  ellipseMode(CENTER);
  rectMode(CENTER);
  shapeMode(CORNER);
  imageMode(CENTER);
  blendMode(NORMAL);
  colorMode(RGB);
  background(0, 0, 0);
  float fov = PI/3.0;
  float cameraZ = (height/2.0) / tan(fov/2.0);
  perspective(fov, float(width)/float(height), 
    cameraZ/100.0, cameraZ*10.0);
  cam = new PeasyCam(this, 500);
  
  xml = loadXML(file+".xml");
  //sometimes the actual blocks are within a "ship plan" that lies at index 1
  if (xml.getChildCount()==3) {
    children = xml.getChild(1).getChildren("item");
  } else {  
    children = xml.getChildren("item");
  }
}

void draw() {
  background(0);
  tick();  //no touch
  for (int i = 0; i < children.length; i++) {
    XML block = children[i].getChild("block");
    float lx=block.getFloat("lx");
    float ly=block.getFloat("ly");
    float lz=block.getFloat("lz");
    float ux=block.getFloat("ux");
    float uy=block.getFloat("uy");
    float uz=block.getFloat("uz");

    //for some reason certain builtin models use a different
    //naming scheme for their attributes
    //float lx=block.getFloat("lowerX");
    //float ly=block.getFloat("lowerY");
    //float lz=block.getFloat("lowerZ");
    //float ux=block.getFloat("upperX");
    //float uy=block.getFloat("upperY");
    //float uz=block.getFloat("upperZ");

    float cx=lx+((ux-lx)/2);
    float cy=ly+((uy-ly)/2);
    float cz=lz+((uz-lz)/2);

    float sx=ux-lx;
    float sy=uy-ly;
    float sz=uz-lz;

    float scl=10;
    String s = block.getString("color");
    int ci = unhex(s);
    color c = color(0, 0, 0);
    if (colorType) {
      c = color(
        map(block.getInt("look"), 0, 5, 0, 255), 
        map(block.getInt("up"), 0, 5, 0, 255), 
        map(block.getInt("index"), 0, 100, 0, 255)
        );
    } else {
      c = color(red(ci), green(ci), blue(ci));
    }
    pushMatrix();
    strokeWeight(0);
    stroke(255);
    fill(c);
    translate(cx*scl, -(cy*scl), cz*scl);
    box(sx*scl, -(sy*scl), sz*scl);
    popMatrix();
  }

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
  if (key==' ') {
    recordObj=true;
  }
  if (key == 't' || key == 'T') {
    colorType=!colorType;
  }
}
void mousePressed() {
  //recordObj = true;
}
void tick() {
  currentTime = millis()*timeScale;
  lights();
  //directionalLight(241, 242, 245, 4, 4, 2);
  //ambientLight(63, 66, 63);
  if (recordObj) {
    beginRecord("nervoussystem.obj.OBJExport", file+".obj");
  }
}
void tock() {
  if (recording) {
    saveFrame("/output/"+title+"_frame_####.png");
  }
  if (recordObj) {
    endRecord();
    recordObj = false;
  }
  ui();
}
void ui() {
  cam.beginHUD();
  drawFPS(255);
  cam.endHUD();
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
