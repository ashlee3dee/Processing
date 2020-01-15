import peasy.*;

PeasyCam cam;

Boid[] flock;

float alignmentValue = .5;
float separationValue = 1;
float cohesionValue = 1;

float alignmentRadius = 25f;
float separationRadius = 50f;
float cohesionRadius = 100f;

void setup() {
  size(800, 800, P3D);
  frameRate(30);                  //all code should be frame-rate independent. hint: use millis()
  smooth(2);                      //enable highest level of anti-aliasing your system can handle
  ellipseMode(CENTER);
  rectMode(CENTER);
  sphereDetail(4, 4);

  cam = new PeasyCam(this, width);

  int n = 500;
  flock = new Boid[n];
  for (int i = 0; i < n; i++) {
    flock[i] = new Boid();
  }

  background(0);
}

void draw() {

  alignmentValue = 0.50;
  separationValue = 0.50;
  cohesionValue = 0.50;

  //alignmentRadius = abs(sin(((2*PI)/3)+(millis()*0.0002f)))*100f;
  separationRadius = abs(sin((PI/3)+(millis()*0.0002f)))*100f;
  cohesionRadius = abs(sin(millis()*0.0002f))*100f;

  background(255);

  noFill();
  stroke(0);
  strokeWeight(5);
  box(width);

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
}
