import peasy.*;

PeasyCam cam;

Boid[] flock;

float alignmentValue = .5;
float separationValue = 1;
float cohesionValue = 1;

float alignmentRadius = 25f;
float separationRadius = 50f;
float cohesionRadius = 100f;


void settings()
{
  size(600, 600, P3D);
  //size(displayWidth, displayHeight, P3D);
}
void setup() {

  frameRate(30);                  //all code should be frame-rate independent. hint: use millis()
  smooth(2);                      //enable highest level of anti-aliasing your system can handle
  ellipseMode(CENTER);
  rectMode(CENTER);
  sphereDetail(4, 4);

  cam = new PeasyCam(this, width);

  int n = 512;
  flock = new Boid[n];
  for (int i = 0; i < n; i++) {
    flock[i] = new Boid();
  }

  background(0);
}

void draw() {

  alignmentValue = 0;
  separationValue = 0.50;
  cohesionValue = 0.50;

  //alignmentRadius = abs(sin((TWO_PI/3)+(millis()*0.0001f)))*100f;
  //separationRadius = abs(sin((PI/3)+(millis()*0.0001f)))*50f;
  //cohesionRadius = abs(sin(millis()*0.0001f))*100f;
  float sNoise =noise(millis()*0.0001f, 0);
  float cNoise = noise(0, millis()*0.0001f);
  separationRadius = sNoise*100f;
  cohesionRadius = cNoise*100f;
  background(255);

  noFill();
  stroke(0);
  strokeWeight(5);
  rectMode(CENTER);
  box(width, height, height);

  translate(-width/2, -height/2, -height/2);
  for (Boid boid : flock) {
    boid.edges();
    boid.flock(flock);
    boid.update();
    //boid.showDebug();
  }
  for (Boid boid : flock) {
    boid.show();
  }
  cam.beginHUD();
  stroke(0);
  fill(255);
  strokeWeight(2);
  rectMode(CORNER);
  rect(10, 10, sNoise*100, 10);
  rect(10, 30, cNoise*100, 10);
  cam.endHUD();
}
