import ch.bildspur.postfx.builder.*;
import ch.bildspur.postfx.pass.*;
import ch.bildspur.postfx.*;
import controlP5.*;
import peasy.*;
//global variables
Boid[] boids;
PImage sprites;

float alignmentValue = .5;
float separationValue = 1;
float cohesionValue = 1;

float alignmentRadius = 25f;
float separationRadius = 50f;
float cohesionRadius = 100f;

float panSpeed = 0.005;
int currentMode = 2;
boolean POST_FX = true;
boolean SIM = true;
//postfx
PostFXSupervisor pFXSuper;
NoisePass noisePass;
BrightPass brightPass;
BloomPass bloomPass;
BlurPass blurPass;
BrightnessContrastPass brightnessContrastPass;
SaturationVibrancePass saturationVibrancePass;
//controlp5
ControlP5 cp5;
CallbackListener cb;
Slider alignmentSlider;
Slider separationSlider;
Slider cohesionSlider;
Slider speedSlider;
Slider panSlider;
Button p;
//peasycam
PeasyCam cam;

void settings()
{
  //size(800, 800, P3D);
  fullScreen(P3D);
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
  {//postfx
    pFXSuper                =new PostFXSupervisor(this);
    noisePass               =new NoisePass(this, 0.5f, 0.1f); 
    brightPass              =new BrightPass(this); 
    bloomPass               =new BloomPass(this, 0.5, 20, 40); 
    blurPass                =new BlurPass(this, 20, 0.5, false); 
    brightnessContrastPass  =new BrightnessContrastPass(this);
    saturationVibrancePass  =new SaturationVibrancePass(this);
  } 
  {//peasycam
    cam = new PeasyCam(this, width);
    cam.setMinimumDistance(width/4);
    cam.setMaximumDistance(width*4);
    cam.setActive(false);
    //frustum(-5.8, 5.8, -5.8, 5.8, 10, 5000);
  }
  {//cp5
    cp5 = new ControlP5(this);
    cp5.setAutoDraw(false);
    alignmentSlider = cp5.addSlider("Align")
      .setId(0)
      .setLabel("Align")
      .setPosition(5, 5)
      .setSize(120, 20)
      .setRange(0, 300)
      .setColorActive(    color(255, 0, 0))
      .setColorBackground(color(0, 0, 0))
      .setColorForeground(color(192, 0, 0))
      .setValue(150)
      ;

    separationSlider = cp5.addSlider("Push")
      .setId(1)
      .setLabel("Push")
      .setPosition(5, 25)
      .setSize(120, 20)
      .setRange(0, 300)
      .setColorActive(    color(0, 255, 0))
      .setColorBackground(color(0, 0, 0))
      .setColorForeground(color(0, 192, 0))
      .setValue(180)
      ;
    cohesionSlider = cp5.addSlider("Pull")
      .setId(2)
      .setLabel("Pull")
      .setPosition(5, 45)
      .setSize(120, 20)
      .setRange(0, 300)
      .setColorActive(    color(0, 0, 255))
      .setColorBackground(color(0, 0, 0))
      .setColorForeground(color(0, 0, 192))
      .setValue(220)
      ;

    speedSlider = cp5.addSlider("Speed")
      .setId(3)
      .setLabel("SPD")
      .setPosition(155, 5)
      .setSize(20, 85)
      .setRange(0.01f, 10f)
      .setColorActive(    color(255, 0, 0))
      .setColorBackground(color(0, 0, 0))
      .setColorForeground(color(192, 0, 0))
      .setValue(5f)
      ;
    cp5.getController("Speed").getValueLabel().align(ControlP5.CENTER, ControlP5.TOP);//.setPaddingX(2);

    panSlider = cp5.addSlider("Pan")
      .setId(4)
      .setLabel("PAN")
      .setPosition(180, 5)
      .setSize(20, 85)
      .setRange(-0.01f, 0.01f)
      .setColorActive(    color(0, 255, 0))
      .setColorBackground(color(0, 0, 0))
      .setColorForeground(color(0, 192, 0))
      .setValue(panSpeed)
      .setSliderMode(Slider.FLEXIBLE)
      ;
    cp5.getController("Pan").getValueLabel().align(ControlP5.CENTER, ControlP5.TOP);

    cp5.addButton("Modify")
      .setId(5)
      .setLabel("MOD")
      .setPosition(5, 70)
      .setSize(20, 20)
      ;
    cp5.addButton("Rand")
      .setId(6)
      .setLabel("RND")
      .setPosition(30, 70)
      .setSize(20, 20)
      ;
    cp5.addButton("Effects")
      .setId(7)
      .setLabel("FX")
      .setPosition(55, 70)
      .setSize(20, 20)
      ;
    cp5.addButton("Free")
      .setId(8)
      .setLabel("CAM")
      .setPosition(80, 70)
      .setSize(20, 20)
      ;
    cp5.addButton("Reset")
      .setId(9)
      .setLabel("RST")
      .setPosition(105, 70)
      .setSize(20, 20)
      ;
    cp5.addButton("Run")
      .setId(10)
      .setLabel("RUN")
      .setPosition(130, 70)
      .setSize(20, 20)
      ;

    cb = new CallbackListener() {
      public void controlEvent(CallbackEvent theEvent) {
        switch(theEvent.getAction()) {
          case(ControlP5.ACTION_ENTER):
          cursor(HAND);
          break;
          case(ControlP5.RELEASED): 
          switch(theEvent.getController().getId()) {
          case 0://alignmentSlider
            alignmentRadius = alignmentSlider.getValue();
            break;
          case 1://separationSlider
            separationRadius = separationSlider.getValue();
            break;
          case 2://cohesionSlider
            cohesionRadius = cohesionSlider.getValue();
            break;
          case 3://speedSlider
            for (Boid boid : boids) {    
              boid.setMaxSpeed(speedSlider.getValue());
            }
            break;
          case 4://panSlider
            panSpeed=panSlider.getValue();
            break;
          case 5://modeButton
            currentMode=(currentMode+1)%4;
            break;
          case 6://randomButton
            alignmentSlider.shuffle();
            separationSlider.shuffle();
            cohesionSlider.shuffle();
            alignmentRadius = alignmentSlider.getValue();
            separationRadius = separationSlider.getValue();
            cohesionRadius = cohesionSlider.getValue();
            break;
          case 7://effectsButton
            POST_FX=!POST_FX;
            break;
          case 8://freecamButton
            cam.setActive(!cam.isActive());
            break;
          case 9://restButton
            for (Boid b : boids) {
              b.position = new PVector(random(width*4), random(width*4), random(width*4));
              b.velocity = PVector.random3D();
              b.velocity.setMag(random(100));
            }
            break;
          case 10://simulateButton
            SIM=!SIM;
            break;
          default:
            print("unknown event detected: ");
            print(theEvent.getController().getLabel()+": ");
            println(theEvent.getController().getId());
          }
          break;
          case(ControlP5.ACTION_LEAVE):
          cursor(ARROW);
          break;
        }
      }
    };
    cp5.addCallback(cb);
  }
  //setup boids
  int n = round(pow(2, 10));
  boids = new Boid[n];
  for (int i = 0; i < n; i++) {
    boids[i] = new Boid();
  }
}

void controlEvent(ControlEvent theEvent) {
  //theEvent.id()
  //println("Got a ControlEvent for "+theEvent.name()+" = "+theEvent.value());

  theEvent.getId();
}
void draw() {
  {//tweakable params
    alignmentValue    =  0.5;
    separationValue   =  0.5;
    cohesionValue     =  0.5;
  }
  if (!cam.isActive())
    cam.rotateY(panSpeed);
  if (currentMode==2) {
    blendMode(NORMAL);
    background(0);
  } else {
    blendMode(NORMAL);
    //background(0);
    noFill();
    stroke(0);
    strokeWeight(5);
    rectMode(CENTER);
    box(width*4, width*4, width*4);
  }
  translate(-width*2, -width*2, -width*2);
  if (SIM) {
    for (Boid boid : boids) {
      boid.edges();
      boid.flock(boids);
      boid.update();
    }
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
    //image(this.g, 0, 0);
    blendMode(SCREEN);

    blurPass.setBlurSize(50);
    blurPass.setSigma(3);

    noisePass.setAmount(0.3);
    noisePass.setSpeed(0.2);

    brightPass.setThreshold(0.10);

    saturationVibrancePass.setSaturation(1.7);
    saturationVibrancePass.setVibrance(0.5);

    pFXSuper.render();

    pFXSuper.pass(saturationVibrancePass);

    pFXSuper.pass(brightPass);
    pFXSuper.pass(noisePass);

    blurPass.setHorizontal(true);
    pFXSuper.pass(blurPass);
    blurPass.setHorizontal(false);
    pFXSuper.pass(blurPass);

    noisePass.setAmount(0.1);
    noisePass.setSpeed(0.4);
    pFXSuper.pass(noisePass);

    pFXSuper.compose();
  }
  hint(DISABLE_DEPTH_TEST);
  cam.beginHUD();
  blendMode(NORMAL);
  fill(0);
  stroke(0);
  cp5.draw();
  drawFPS(255);
  cam.endHUD();
  hint(ENABLE_DEPTH_TEST);
}

void keyPressed() {
  if (key == 'x') {
  }
}

void drawFPS(int textColor)
{  
  noStroke();
  rectMode(CORNER);
  fill(0);
  rect(0, height-20, width, 20);
  fill(textColor);
  float[] p =cam.getPosition();
  String renderInfo = "fps: "+String.valueOf(frameRate);
  //String cameraInfo = "pos: "+p[0]+"|"+p[1]+"|"+p[2];

  text(renderInfo, 5, height-9);
  text("pos: ", 70, height-9);
  text(p[0], 85, height-9);
  text(p[1], 135, height-9);
  text(p[2], 185, height-9);
}
