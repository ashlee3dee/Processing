float noiseScale = 0.1;
float timeScale = 0.01;
int scl = 10;
int cols, rows;
Particle[100] particles;

void setup() {
  frameRate(30);
  size(512, 512);
  cols = floor(width/scl);
  rows = floor(height/scl);
  particles[0] = new Particle();
}

void draw() {
  background(255);
  for (int y =0; y< rows; y++) {
    for (int x = 0; x< cols; x++)
    {
      int index = (x+y*width);
      float r = noise(x*noiseScale, y*noiseScale, frameCount*timeScale);
      PVector v = PVector.fromAngle((TWO_PI)*r);
      stroke(0);
      pushMatrix();
      translate(x * scl, y * scl);
      rotate(v.heading());
      line(0, 0, scl, 0);
      popMatrix();
    }
  }
  particles[0].update();
  particles[0].show();
  pushMatrix();
  fill(0);
  translate(0, height);
  text(frameRate, 0, 0);
  popMatrix();
}