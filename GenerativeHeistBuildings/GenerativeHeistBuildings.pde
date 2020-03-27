import peasy.*;
PeasyCam camera;

Grid g = new Grid(20, 20, 20);

long lastTime = 0;
long timeDelay = 1000;
void setup() {
  size(800, 800, P3D);
  camera = new PeasyCam(this, 30);
  //cam.setSuppressRollRotationMode();
  perspective(200, 1, 0.1, 1000);
  frameRate(60);
}

void draw() {
  //camera.setRotations(-0.7654983,0.21562867,0.20631076);
  //float[] rotations = camera.getRotations();
  //float[] position = camera.getPosition();
  //println("rotaton("+rotations[0]+","+rotations[1]+","+rotations[2]+")");
  //println("position("+position[0]+","+position[1]+","+position[2]+")");

  background(0);

  g.noiseIterate();
  g.display();
  //drawCylinderBetween(new PVector(0, 0, 0), new PVector(10, 10, 10));
  if (millis()-lastTime>timeDelay) {
    lastTime=millis();
    //camera.lookAt(random(-5, 5), random(-5, 5), random(-5, 5), 0.1, timeDelay);
  }
}

class Grid {
  private float[] data;
  private int xMax = 1;
  private int yMax = 1;
  private int zMax = 1;
  public Grid(int x, int y, int z) {
    xMax = x;
    yMax = y;
    zMax = z;
    data = new float[x*y*z];
    for (int i=0; i <data.length; i++) {
      data[i]=1;
    }
  }
  public int to1D( int x, int y, int z ) {
    return (z * xMax * yMax) + (y * xMax) + x;
  }
  public float to1D( float x, float y, float z ) {
    return (z * xMax * yMax) + (y * xMax) + x;
  }
  public int[] to3D( int idx ) {
    final int z = idx / (xMax * yMax);
    idx -= (z * xMax * yMax);
    final int y = idx / xMax;
    final int x = idx % xMax;
    return new int[]{ x, y, z };
  }
  public PVector to3Dvec( int idx ) {
    final int z = idx / (xMax * yMax);
    idx -= (z * xMax * yMax);
    final int y = idx / xMax;
    final int x = idx % xMax;
    return new PVector( x, y, z );
  }


  public void updateCell(int x, int y, int z, float v) {
    data[to1D(x, y, z)] = v;
  }
  public void updateCell(float x, float y, float z, float v) {
    data[(int)to1D(x, y, z)] = v;
  }
  public void noiseIterate() {
    float ns = 0.144;
    float ts = 0.0003;
    for (int i=0; i < data.length; i++) {
      float t = millis()*ts;
      PVector p = to3Dvec(i);
      updateCell(p.x, p.y, p.z, noise(t+(p.x*ns), t+(p.y*ns), t+(p.z*ns)));
    }
  }
  public void display() {
    rotateX(0.8);
    rotateY(0.8);
    rotateZ(0.0);
    translate(-xMax/2, -yMax/2, -zMax/2);
    ArrayList<PVector> nodes = new ArrayList<PVector>();
    for (int i=0; i<data.length; i++) {
      float v = data[i];
      pushMatrix();
      noStroke();
      fill(255*v);
      PVector p = to3Dvec(i);
      translate(p.x, p.y*(v*v), p.z);
      if (p.y*v>10.5)
      {
        fill(255*v, 0, 0);
        nodes.add(p);
      }
      box(v, v, v);
      popMatrix();
    }
    //PVector lp = nodes.get(nodes.size()-1);
    //for (PVector p : nodes) {
    //  pushMatrix();
    //  translate(p.x, p.y, p.z);
    //  sphere(1);
    //  popMatrix();
    //  //drawCylinderBetween(lp, p);
    //  lp = p;
    //}
  }
}

void drawCylinderBetween(PVector startPoint, PVector endPoint) {
  PVector center = PVector.add(startPoint, endPoint).mult(0.5);
  PVector z = new PVector(0, 1, 0);
  PVector p = PVector.sub(startPoint, endPoint);
  PVector t = z.cross(p);
  float angle = acos(PVector.dot(z, p)/p.mag());
  float h = PVector.dist(startPoint, endPoint);
  pushMatrix();
  translate(center.x, center.y, center.z);
  rotateZ(angle);
  box(0.25, h, 1);
  popMatrix();
  pushMatrix();
  translate(startPoint.x,startPoint.y,startPoint.z);
  sphere(1);
  popMatrix();
  pushMatrix();
  translate(endPoint.x,endPoint.y,endPoint.z);
  sphere(1);
  popMatrix();
}
