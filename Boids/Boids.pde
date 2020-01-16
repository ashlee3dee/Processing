import controlP5.*;
import peasy.*;
PeasyCam cam;
ControlP5 cp5;
PImage sprites;
Boid[] flock;

int currentMode = 2;

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
  //size(800, 800, P3D);
  size(displayWidth, displayHeight, P3D);
}
void setup() {

  frameRate(30);                  //all code should be frame-rate independent. hint: use millis()
  smooth(2);                      //enable highest level of anti-aliasing your system can handle
  ellipseMode(CENTER);
  rectMode(CENTER);
  textureMode(NORMAL);
  sphereDetail(6, 6);
  cp5 = new ControlP5(this);
  cam = new PeasyCam(this, width*1.5);
  cam.setMinimumDistance(width/2);
  cam.setMaximumDistance(width*2);
  //cam.setActive(false);
  sprites = loadImage("star_sprites.png");
  //stroke(0);
  //strokeWeight(2);
  //rectMode(CORNER);
  //fill(255);
  //rect(5, 5, 55, 40);
  //rect(60, 5, separationRadius, 20);
  //rect(60, 25, cohesionRadius, 20);
  //fill(0);
  //text("P U S H", 11, 21);
  //text("P U L L", 12, 39);

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
    .setValue(180);

  cp5.addBang("mode")
    .setPosition(5, 70)
    .setSize(20, 20)
    .setId(0)
    ;
  cp5.addBang("randomize")
    .setPosition(30, 70)
    .setSize(20, 20)
    .setId(0)
    ;
  cp5.setAutoDraw(false);
  int n = 1024;
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
  //cam.setDistance(abs(sin(millis()*0.00005f))*width*2f);  
  //cam.rotateY(0.001f);
  //cam.rotateZ(0.02f);
  alignmentValue  =  0.5;
  separationValue =  0.5;
  cohesionValue   =  0.5;

  //alignmentRadius = abs(sin((TWO_PI/3)+(millis()*0.0001f)))*100f;
  //separationRadius = 75f+((1+sin((PI/2)+(millis()*0.0001f)))*50f);
  //cohesionRadius = 75f+((1+sin((millis()*0.0001f)))*50f);
  alignmentRadius   =  alignmentSlider.getValue();
  separationRadius  =  separationSlider.getValue();
  cohesionRadius    =  cohesionSlider.getValue();
  //cohesionValue += 10;
  //float sNoise =noise(millis()*0.0001f, 0);
  //float cNoise = noise(0, millis()*0.0001f);
  //separationRadius = sNoise*75f;
  //cohesionRadius = cNoise*50f;
  //cohesionRadius+=25;
  if (currentMode!=1) {
    if (currentMode==2) {
      background(0);
    } else {
      background(255);
      noFill();
      stroke(0);
      strokeWeight(5);
      rectMode(CENTER);
      box(width*2, width*2, width*2);
    }
  } 

  translate(-width, -width, -width);
  for (Boid boid : flock) {
    boid.edges();
    boid.flock(flock);
    boid.update();
    switch(currentMode) {
    case 0:
      boid.showVisual();
      break;
    case 1:
      boid.showSpheres();
      break;
    case 2:
      boid.showSprite();
      break;
    default:
    }
  }
  
  
  
  cam.beginHUD();
  blendMode(NORMAL);
  fill(0);
  stroke(0);
  cp5.draw();
  cam.endHUD();
}

void keyPressed() {
  if (key == ' ') {
    mode();
  }
  if (key=='r' || key == 'R') {
    randomize();
  }
}

void mode() {
  currentMode=(currentMode+1)%3;
  background(255);
  //println(currentMode);
}

void randomize() {
  alignmentSlider.setValue(random(300));
  separationSlider.setValue(random(300));
  cohesionSlider.setValue(random(300));
}
