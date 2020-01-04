float noiseScale = 0.1;
float timeScale = 0.01;
int scl = 10;
int cols, rows;
Particle[] particles = new Particle[20];  // allocating memory to array

void setup() {
  frameRate(30);
  size(512, 512);
  cols = floor(width/scl);
  rows = floor(height/scl);
  particles[0] = new Particle(new PVector(width/2, height/2));
}

void draw() {
  background(255);
  for (int y =0; y< rows; y++) {
    for (int x = 0; x< cols; x++)
    {
      int index = (x+y*width);
      float r = noise(x*noiseScale, y*noiseScale, frameCount*timeScale);
      PVector v = PVector.fromAngle((PI)*r);
      stroke(0);
      pushMatrix();
      translate(x * scl, y * scl);
      rotate(v.heading());
      line(0, 0, scl, 0);
      popMatrix();
    }
  }
  float r = noise(particles[0].pos.x*noiseScale, 
    particles[0].pos.y*noiseScale, 
    frameCount*timeScale);
  PVector v = PVector.fromAngle((TWO_PI)*r);
  particles[0].applyForce(v);
  particles[0].update();
  particles[0].show();
  pushMatrix();
  fill(0);
  translate(0, height);
  text(frameRate, 0, 0);
  popMatrix();
}
