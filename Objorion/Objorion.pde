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
PShape zarrow;
PShape b_001;
PShape b_100;
PShape b_101;
PShape b_102;
PShape b_103;

//String file="BuildShip4";
String file="myship";
void settings()
{
  size(800, 800, P3D);
  smooth(2);  //enable highest level of anti-aliasing your system can handle
}

void setup() {
  frameRate(30); //all code should be frame-rate independent. hint: use currentTime
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
    cameraZ/1000.0, cameraZ*10.0);
  cam = new PeasyCam(this, 10);
  zarrow = loadShape("zarrow.obj");
  b_001 = loadShape("1.obj");
  b_100 = loadShape("100.obj");
  b_101 = loadShape("101.obj");
  b_102 = loadShape("102.obj");
  b_103 = loadShape("103.obj");
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
  //shape(zarrow, 0, 0);
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

    float scl=1;

    String s = block.getString("color");
    int ci = unhex(s);
    color c = color(0, 0, 0);
    int index = block.getInt("index");
    int look=block.getInt("look");
    int up=block.getInt("up");
    if (colorType) {
      c = color(
        map(block.getInt("look"), 1, 5, 0, 255), 
        map(block.getInt("up"), 1, 5, 0, 255), 
        0
        );
    } else {
      c = color(red(ci), green(ci), blue(ci));
    }
    pushMatrix();
    noStroke();
    strokeWeight(5);
    stroke(0);
    fill(c);
    translate((cx*scl), (cy*scl), (cz*scl));
    scale((sx*scl), (sy*scl), (sz*scl));
    PVector rotMatrix = matrixToEuler(lookUpToMatrix(look, up));
    rotateX(rotMatrix.y);
    rotateY(rotMatrix.x);
    rotateZ(rotMatrix.z);
    PShape p;
    if (index==1) {
      p=b_001;
    } else if (index==100) {
      p=b_100;
    } else if (index==101) {
      p=b_101;
    } else if (index==102) {
      p=b_102;
    } else if (index==103) {
      p=b_103;
    } else {
      p=b_001;
    }
    p.setFill(c);
    shape(p, 0, 0);
    popMatrix();
  }
  //pushMatrix();
  //pushStyle();
  //strokeWeight(50);
  //stroke(255, 0, 0);
  //point(2, 0, 0);
  //stroke(0, 255, 0);
  //point(0, 2, 0);
  //stroke(0, 0, 255);
  //point(0, 0, 2);
  //popStyle();
  //popMatrix();
  tock();  //no touch
}
void displayMatrix(int[][] a) {
  println("output:");
  print("[");
  for (int i=0; i < a.length; i++) {
    print("[");
    for (int ii=0; ii < a[i].length; ii++) {
      print(a[i][ii]+",");
    }
    print("]");
  }
  println("]");
}
int[][] lookUpToMatrix(int look, int up) {
  int [][] rotMatrix = {
    {0, 0, 0, 0}, 
    {0, 0, 0, 0}, 
    {0, 0, 0, 0}, 
  };
  if (((look < 0) | (look > 5))|((up < 0) | (up > 5))) {
    println("Invalid orientation: "+"look:"+look+"|up:"+up);
    return rotMatrix;
  }
  //sign = lambda b: 
  //2*b-1
  //sgn
  //  i, j = orientation // 2
  int i = floor(look/2);
  int j = floor(up/2);
  //  k = 3 - i - j
  int k = 3 - i - j;
  //  u, v = sign(orientation % 2)
  int u = (look % 2);
  int v = (up % 2);
  //  w = u*v*sign(i < j)*sign(k != 1)
  int w = u*v*sgn(i<j?1:0)*sgn(k!=1?1:0);
  //  R[i, 0] = u
  //  R[j, 1] = v
  //  R[k, 2] = w
  rotMatrix[i][0]=u;
  rotMatrix[j][1]=v;
  rotMatrix[k][2]=w;
  //  return R
  return rotMatrix;
}
int sgn(int b) {
  return 2*b-1;
}
public PVector matrixToEuler(int[][] A)
{
  PVector angle = new PVector(0, 0, 0);
  angle.y = (float)-Math.asin(A[2][0]);  //Pitch

  //Gymbal lock: pitch = -90
  if ( A[2][0] == 1 ) {    
    angle.x = 0.0;             //yaw = 0
    angle.z = (float)Math.atan2( -A[0][1], -A[0][2] );    //Roll
    System.out.println("Gimbal lock: pitch = -90");
  }

  //Gymbal lock: pitch = 90
  else if ( A[2][0] == -1 ) {    
    angle.x = 0.0;             //yaw = 0
    angle.z = (float)Math.atan2( A[0][1], A[0][2] );    //Roll
    System.out.println("Gimbal lock: pitch = 90");
  }
  //General solution
  else {
    angle.x = (float)Math.atan2(  A[1][0], A[0][0] );
    angle.z = (float)Math.atan2(  A[2][1], A[2][2] );
    System.out.println("No gimbal lock");
  }
  return angle;   //Euler angles in order yaw, pitch, roll
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
  //lights();
  directionalLight(284, 242, 245, 2, -1, -4);
  ambientLight(63, 66, 63);
  if (recordObj) {
    //beginRecord("nervoussystem.obj.OBJExport", file+".obj");
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
