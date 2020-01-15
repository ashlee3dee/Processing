import peasy.*;

PeasyCam cam;

Boid[] flock;

boolean mode = true;

float alignmentValue = .5;
float separationValue = 1;
float cohesionValue = 1;

float alignmentRadius = 25f;
float separationRadius = 50f;
float cohesionRadius = 100f;


void settings()
{
  size(841, 841, P3D);
  //size(displayWidth, displayHeight, P3D);
}
void setup() {

  frameRate(30);                  //all code should be frame-rate independent. hint: use millis()
  smooth(8);                      //enable highest level of anti-aliasing your system can handle
  ellipseMode(CENTER);
  rectMode(CENTER);
  sphereDetail(6, 6);

  cam = new PeasyCam(this, width*1.5f);

  int n = 512;
  flock = new Boid[n];
  for (int i = 0; i < n; i++) {
    flock[i] = new Boid();
  }

  background(0);
}

void draw() {
  //cam.rotateX(0.01f);
  //cam.rotateY(0.01f);
  //cam.rotateZ(0.02f);
  alignmentValue = 0;
  separationValue = 0.5;
  cohesionValue = 0.5;

  //alignmentRadius = abs(sin((TWO_PI/3)+(millis()*0.0001f)))*100f;
  //separationRadius = abs(sin((PI/3)+(millis()*0.0001f)))*50f;
  //cohesionRadius = abs(sin(millis()*0.0001f))*100f;
  float sNoise =noise(millis()*0.0001f, 0);
  float cNoise = noise(0, millis()*0.0001f);
  separationRadius = sNoise*75f;
  cohesionRadius = cNoise*50f;
  cohesionRadius+=25;
  if (mode) {
    background(255);

    noFill();
    stroke(0);
    strokeWeight(5);
    rectMode(CENTER);
    box(width, height, height);
  } 

  translate(-width/2, -height/2, -height/2);
  for (Boid boid : flock) {
    boid.edges();
    boid.flock(flock);
    boid.update();
    if (mode) {
      boid.showVisual();
    } else {
      boid.show();
    }
  }
  if (mode) {
    cam.beginHUD();
    stroke(0);
    fill(255);
    strokeWeight(2);
    rectMode(CORNER);
    rect(10, 10, sNoise*100, 10);
    rect(10, 30, cNoise*100, 10);
    cam.endHUD();
  }
}

void keyPressed() {
  if (key == ' ') {
    mode = !mode;
    background(255);
  }
}
