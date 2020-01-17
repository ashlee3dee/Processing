import ch.bildspur.postfx.builder.*;
import ch.bildspur.postfx.pass.*;
import ch.bildspur.postfx.*;
import controlP5.*;
import peasy.*;

PeasyCam cam;
ControlP5 cp5;
PostFX postFx;

PImage sprites;

Boid[] boids;

int currentMode = 2;
boolean POST_FX = true;

float alignmentValue = .5;
float separationValue = 1;
float cohesionValue = 1;

float alignmentRadius = 25f;
float separationRadius = 50f;
float cohesionRadius = 100f;

Slider alignmentSlider;
Slider separationSlider;
Slider cohesionSlider;

void settings()
{
  size(800, 800, P3D);
  //fullScreen(P3D);
}
void setup() {
  frameRate(30);                  //all code should be frame-rate independent. hint: use millis()
  smooth(2);                      //enable highest level of anti-aliasing your system can handle
  ellipseMode(CENTER);
  rectMode(CENTER);
  textureMode(NORMAL);
  sphereDetail(6, 6);
  background(0);

  sprites = loadImage("star_sprites.png");
  postFx = new PostFX(this);

  cam = new PeasyCam(this, width);
  cam.setMinimumDistance(width/4);
  cam.setMaximumDistance(width*4);
  cam.setActive(false);
  frustum(-5.8, 5.8, -5.8, 5.8, 10, 5000);

  int n = round(pow(2, 10));
  boids = new Boid[n];
  for (int i = 0; i < n; i++) {
    boids[i] = new Boid();
  }

  cp5 = new ControlP5(this);
  cp5.setAutoDraw(false);
  alignmentSlider = cp5.addSlider("Align")
    .setPosition(5, 5)
    .setRange(0, 300)
    .setSize(200, 20)
    .setColorActive(color(255, 255, 255))
    .setColorBackground(color(0, 0, 0))
    .setColorForeground(color(192, 0, 0))
    .setValue(150);
  separationSlider = cp5.addSlider("Push")
    .setPosition(5, 25)
    .setRange(0, 300)
    .setSize(200, 20)
    .setColorActive(color(255, 255, 255))
    .setColorBackground(color(0, 0, 0))
    .setColorForeground(color(0, 192, 0))
    .setValue(180);
  cohesionSlider = cp5.addSlider("Pull")
    .setPosition(5, 45)
    .setRange(0, 300)
    .setSize(200, 20)
    .setColorActive(color(255, 255, 255))
    .setColorBackground(color(0, 0, 0))
    .setColorForeground(color(0, 0, 192))
    .setValue(220);
  cp5.addBang("mod")
    .setPosition(5, 70)
    .setSize(20, 20)
    .setId(0)
    ;
  cp5.addBang("rnd")
    .setPosition(30, 70)
    .setSize(20, 20)
    .setId(1)
    ;
  cp5.addBang("fx")
    .setPosition(55, 70)
    .setSize(20, 20)
    .setId(2)
    ;
  cp5.addBang("cam")
    .setPosition(80, 70)
    .setSize(20, 20)
    .setId(3)
    ;
  cp5.addBang("rst")
    .setPosition(105, 70)
    .setSize(20, 20)
    .setId(4)
    ;
}
void draw() {
  if (!cam.isActive())
    cam.rotateY(0.0075f);
  alignmentValue  =  0.5;
  separationValue =  0.5;
  cohesionValue   =  0.5;
  alignmentRadius   =  alignmentSlider.getValue();
  separationRadius  =  separationSlider.getValue();
  cohesionRadius    =  cohesionSlider.getValue();
  if (currentMode!=1) {
    if (currentMode==2) {
      background(0);
    } else {
      background(255);
      noFill();
      stroke(0);
      strokeWeight(5);
      rectMode(CENTER);
      box(width*3, width*3, width*3);
    }
  } 

  translate(-width*1.5, -width*1.5, -width*1.5);
  for (Boid boid : boids) {
    boid.edges();
    boid.flock(boids);
    boid.update();
  }
  if (currentMode==0)
    for (Boid boid : boids)
      boid.showVisual();
  else if (currentMode==1)
    for (Boid boid : boids)
      boid.showSpheres();
  else if (currentMode==2)
    for (Boid boid : boids)
      boid.showSprite();
  else if (currentMode==3)
    for (Boid boid : boids)
      boid.showDebug();


  if (POST_FX) {
    blendMode(ADD);
    postFx.render()
      .brightnessContrast(0.04, 0.17)
      .noise(0.27, 0.1f)
      .blur(7, 4.9)
      .saturationVibrance(0.89, 2.26)
      .bloom(0.13, 53, 3.19)
      .compose();
  }
  blendMode(NORMAL);
  fill(0);
  stroke(0);
  cam.beginHUD();
  cp5.draw();
  drawFPS(255);
  cam.endHUD();
}

void keyPressed() {
  if (key == '1') {
    mod();
  }
  if (key=='2') {
    rnd();
  }
  if (key=='3') {
    fx();
  }
  if (key == '4') {
    cam();
  }
  if (key == '5') {
    rst();
  }
}

void mod() {
  currentMode=(currentMode+1)%4;
  background(255);
  //println(currentMode);
}
void fx() {
  POST_FX=!POST_FX;
}
void cam() {
  cam.setActive(!cam.isActive());
}

void rst() {
  for (Boid b : boids) {
    b.position = new PVector(random(width*3), random(width*3), random(width*3));
    b.velocity = PVector.random3D();
    b.velocity.setMag(random(100));
  }
}
void rnd() {
  alignmentSlider.setValue(random(300));
  separationSlider.setValue(random(300));
  cohesionSlider.setValue(random(300));
}
void drawFPS(int textColor)
{  
  noStroke();
  fill(0);
  rect(0, height-5, 80, 20);
  fill(textColor);
  text(frameRate, 0, height-5);
}
