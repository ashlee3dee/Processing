float noiseScale = 0.1;
float timeScale = 0.01;
int scl = 100;
int cols, rows;

void setup() {
  frameRate(30);
  size(512, 512);
  cols = floor(width/scl);
  rows = floor(height/scl);
}

void draw() {
  background(255);
  float rows=10;
  float cols =10;
  for (int y =0; y< rows; y++) {
    for (int x = 0; x< cols; x++)
    {
      int index = (x+y*width);
      float r = noise(x*noiseScale, y*noiseScale, frameCount*timeScale);
      float step = TWO_PI/8;
      r *=8;
      r=int(r);
      PVector v = PVector.fromAngle(step*r);
      v.mult(2);
      stroke(0);
      pushMatrix();
      translate(x * scl, y * scl);
      rotate(v.heading());
      line(0, 0, scl, 0);
      popMatrix();
    }
  }
  pushMatrix();
  fill(0);
  translate(0, height);
  text(frameRate, 0, 0);
  popMatrix();
}
