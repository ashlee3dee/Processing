import ch.bildspur.postfx.builder.*;
import ch.bildspur.postfx.pass.*;
import ch.bildspur.postfx.*;
import controlP5.*;
import peasy.*;

PeasyCam cam;
ControlP5 cp5;
PostFX postFx;

PImage sprites;

Boid[] flock;

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
  //size(750, 750, P3D);
  fullScreen(P3D);
}
void setup() {
  frameRate(30);                  //all code should be frame-rate independent. hint: use millis()
  smooth(2);                      //enable highest level of anti-aliasing your system can handle
  ellipseMode(CENTER);
  rectMode(CENTER);
  textureMode(NORMAL);
  sphereDetail(6, 6);

  sprites = loadImage("star_sprites.png");
  postFx = new PostFX(this);

  cam = new PeasyCam(this, width*1.5);
  cam.setMinimumDistance(width/2);
  cam.setMaximumDistance(width*2);
  //cam.setActive(false);
  float cameraZ=((height/2.0) / tan(PI*60.0/360.0));
  //perspective(PI/3.0, width/height, cameraZ/10.0, cameraZ*100.0);



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
    .setValue(60);
  cohesionSlider = cp5.addSlider("Pull")
    .setPosition(5, 45)
    .setRange(0, 300)
    .setSize(200, 20)
    .setColorActive(color(255, 255, 255))
    .setColorBackground(color(0, 0, 0))
    .setColorForeground(color(0, 0, 192))
    .setValue(220);
  cp5.addBang("mode")
    .setPosition(5, 70)
    .setSize(20, 20)
    .setId(0)
    ;
  cp5.addBang("rand")
    .setPosition(30, 70)
    .setSize(20, 20)
    .setId(1)
    ;
  cp5.addBang("fx")
    .setPosition(55, 70)
    .setSize(20, 20)
    .setId(2)
    ;

  int n = round(pow(2,10));
  println(n);
  flock = new Boid[n];
  for (int i = 0; i < n; i++) {
    flock[i] = new Boid();
  }

  background(0);
}
public void bang() {
  println("### bang(). a bang event. setting background to ");
}
void draw() {
  //cam.rotateX(0.01f);
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
      box(width*4, width*4, width*4);
    }
  } 

  translate(-width*2, -width*2, -width*2);
  for (Boid boid : flock) {
    boid.edges();
    boid.flock(flock);
    boid.update();
  }
  if (currentMode==0)
    for (Boid boid : flock)
      boid.showVisual();
  else if (currentMode==1)
    for (Boid boid : flock)
      boid.showSpheres();
  else if (currentMode==2)
    for (Boid boid : flock)
      boid.showSprite();
  blendMode(ADD);
  if (POST_FX)
    postFx.render()
      .brightPass(0.35f)
      .bloom(0.2, 20, 100)
      .noise(0.25f, 0.1f)
      //.chromaticAberration()
      .blur(0, 2)
      .compose();
  cam.beginHUD();
  //blendMode(ADD);
  //image(canvas, 0, 0);
  blendMode(NORMAL);
  fill(0);
  stroke(0);
  cp5.draw();
  drawFPS(255);
  cam.endHUD();
}

void keyPressed() {
  if (key == ' ') {
    mode();
  }
  if (key=='r' || key == 'R') {
    randomize();
  }
  if (key=='b' || key == 'B') {
    fx();
  }
}

void mode() {
  currentMode=(currentMode+1)%3;
  background(255);
  //println(currentMode);
}
void fx() {
  POST_FX=!POST_FX;
}

void randomize() {
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
