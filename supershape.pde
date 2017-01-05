int t = 0;

void setup() {
  size(800, 600, P3D);
}

void draw() {
  background(0);
  fill(255);
  lights();
  translate(width/2, height/2);
  //sphere(200);
  drawCircle();
  t++;
}

void drawCircle() {
  int points = 360;
  int r = height/4;
  noFill();
  stroke(255);
  beginShape();
  for (int i = 0; i <= points; i++) {
    float modx, mody, x, y, len, freq;
    len = sin(t*0.083)*27;
    freq = 7;
    x = sin(radians((360/points)*i))*(r+(sin(radians(i*freq))*len));
    y = cos(radians((360/points)*i))*(r+(sin(radians(i*freq))*len));
    modx = sin(i)*(r/10);
    mody = 0;
    vertex(x, y+mody);
  }
  endShape();
}