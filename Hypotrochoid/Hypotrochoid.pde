GraphObject g = new GraphObject(1, 1, 1, 10, 10);
void setup() {
  background(0);
  size(800, 800);
  frameRate(60);
  noFill();
}

void draw() {
  g.display();
}

float avg(float[] n) {
  float a = 0;
  for (int i = 0; i<n.length; i++) {
    a+=n[i];
  }
  return a/n.length;
}
class GraphObject {
  int a, b, h, t, s, st;
  void display() {
  }
  GraphObject(int p1, int p2, int p3, int sampleres, int dotspertick) {
    a = p1;
    b = p2;
    h = p3;
    s = sampleres;
    st = dotspertick;
  }
  PVector NextPoint() {
    PVector nP = new PVector();
    nP.x = (a-b)*cos(t)+h*cos((a-b/b)*t);
    nP.y = (a-b)*sin(t)-h*sin((a-b/b)*t);
    t+=s;
    return nP;
  }
}
void drawDot(float x, float y) {
}
void drawGuides(float x, float y) {
  stroke(255, 255, 255, 1);
  strokeWeight(1);
  line(-width/2, y, width/2, y);
  line(x, -height/2, x, height/2);
}
