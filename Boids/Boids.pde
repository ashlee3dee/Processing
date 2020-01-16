import peasy.*;

PeasyCam cam;
PImage sprites;
Boid[] flock;

int mode = 3;

float alignmentValue = .5;
float separationValue = 1;
float cohesionValue = 1;

float alignmentRadius = 25f;
float separationRadius = 50f;
float cohesionRadius = 100f;


void settings()
{
  size(800, 800, P3D);
  //size(displayWidth, displayHeight, P3D);
}
void setup() {

  frameRate(30);                  //all code should be frame-rate independent. hint: use millis()
  smooth(2);                      //enable highest level of anti-aliasing your system can handle
  ellipseMode(CENTER);
  rectMode(CENTER);
  textureMode(NORMAL);
  sphereDetail(6, 6);
  sprites = loadImage("star_sprites.png");
  cam = new PeasyCam(this, width);
  cam.setMinimumDistance(width/2);
  cam.setMaximumDistance(width*2);
  int n = 1500;
  flock = new Boid[n];
  for (int i = 0; i < n; i++) {
    flock[i] = new Boid();
  }

  background(0);
}

void draw() {
  //cam.rotateX(0.01f);
  //cam.setDistance(abs(sin(millis()*0.00005f))*width*2f);  
  cam.rotateY(0.005f);
  //cam.rotateZ(0.02f);
  alignmentValue = 0;
  separationValue = 0.5;
  cohesionValue = 0.5;

  //alignmentRadius = abs(sin((TWO_PI/3)+(millis()*0.0001f)))*100f;
  separationRadius = 75f+((1+sin((PI/2)+(millis()*0.0001f)))*50f);
  cohesionRadius = 75f+((1+sin((millis()*0.0001f)))*50f);
  //separationValue += 10;
  //cohesionValue += 10;
  //float sNoise =noise(millis()*0.0001f, 0);
  //float cNoise = noise(0, millis()*0.0001f);
  //separationRadius = sNoise*75f;
  //cohesionRadius = cNoise*50f;
  //cohesionRadius+=25;
  if (mode!=1) {
    if (mode==3) {
      background(0);
    } else {
      background(255);
      noFill();
      stroke(0);
      strokeWeight(5);
      rectMode(CENTER);
      box(width, width, width);
    }
  } 

  translate(-width/2, -width/2, -width/2);
  for (Boid boid : flock) {
    boid.edges();
    boid.flock(flock);
    boid.update();
    switch(mode) {
    case 0:
      boid.showVisual();
      break;
    case 1:
      boid.showSpheres();
      break;
    case 2:
      boid.showDebug();
      break;
    case 3:
      boid.showSprite();
      break;
    default:
    }
  }
  if (mode!=1) {
    cam.beginHUD();
    blendMode(NORMAL);
    stroke(0);
    strokeWeight(2);
    rectMode(CORNER);
    fill(255);
    rect(5, 5, 55, 40);
    rect(60, 5, separationRadius, 20);
    rect(60, 25, cohesionRadius, 20);
    fill(0);
    text("P U S H", 11, 21);
    text("P U L L", 12, 39);
    cam.endHUD();
  }
}

void keyPressed() {
  if (key == ' ') {
    mode=(mode+1)%4;
    background(255);
    println(mode);
  }
}
