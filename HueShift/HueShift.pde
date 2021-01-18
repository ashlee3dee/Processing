boolean recording = false;        // A boolean to track whether we are recording are not
float globalTimeScale = 0.001f;        //global amount to scale millis() for all animations
float currentTime = 0;
String title=getClass().getSimpleName();
PImage basePImage;

void settings()
{
  size(800, 1200, P3D);
  smooth(2);                      //enable highest level of anti-aliasing your system can handle
}

void setup() {
  frameRate(60);                  //all code should be frame-rate independent. hint: use currentTime (millis()*globalglobalTimeScale)
  ellipseMode(CENTER);
  rectMode(CENTER);
  imageMode(CENTER);
  blendMode(NORMAL);
  colorMode(HSB, TAU, TAU, TAU);
  background(0, 0, 0);
  basePImage = loadImage("riku.png");
}

void draw() {
  tick();  //no touch

  //code here
  background(0, 0, 0); //draw bg, clears frame
  basePImage.loadPixels(); //load base image pixels into memory first

  //instantiate buffer image objects
  PImage phasePImage = createImage(basePImage.width, basePImage.height, RGB);
  PImage outputPImage = createImage(basePImage.width, basePImage.height, RGB);

  //display the base image
  pushMatrix();
  translate(basePImage.width/2, basePImage.height/2);
  image(basePImage, 0, 0);
  popMatrix();

  //generate the phase shift image
  phasePImage.loadPixels();
  for (int i=0; i < phasePImage.pixels.length; i++) {
    float px = i%phasePImage.width;
    float py = i/phasePImage.width;
    float waveScale = 0.03;
    PVector heading = new PVector(1, 0.5);
    heading.rotate(currentTime*0.1f);
    float phaseShiftAmplitude = map(sin(((px*heading.x)+(py*heading.y))*waveScale), -1, 1, 0, 1);
    phasePImage.pixels[i] = color(0, 0, (brightness(basePImage.pixels[i])+(phaseShiftAmplitude*TAU))/2);
  }
  phasePImage.updatePixels();

  //display the phase shift image
  pushMatrix();
  translate(phasePImage.width+(phasePImage.width/2), phasePImage.height/2);
  image(phasePImage, 0, 0);
  popMatrix();

  outputPImage.loadPixels();
  for (int i=0; i < outputPImage.pixels.length; i++) {
    color pixelColor = basePImage.pixels[i];
    color pixelPhase = phasePImage.pixels[i];
    outputPImage.pixels[i] = color(
      (brightness(pixelPhase)+(hue(pixelColor)+currentTime))%TAU, 
      saturation(pixelColor), 
      brightness(pixelColor));
  }
  outputPImage.updatePixels();

  pushMatrix();
  translate(outputPImage.width+(outputPImage.width/2), outputPImage.height+(outputPImage.height/2));
  image(outputPImage, 0, 0);
  popMatrix();
  tock();  //no touch
}

/**
 ================================
 Mandatory Code
 ================================
 **/
void keyPressed() {
  if (key == 'r' || key == 'R') {
    recording = !recording;
  }
}
void tick() {
  currentTime = millis()*globalTimeScale;
}
void tock() {
  if (recording) {
    saveFrame("/outputPImage/"+title+"_frame_####.png");
  }
  ui();
}
void ui() {
  drawFPS(255);
}
void drawFPS(int textColor)
{
  pushMatrix();
  noStroke();
  fill(0);
  rect(0, 0, 100, 40);
  fill(textColor);
  text(frameRate, 0, 15);
  popMatrix();
}

// linear interpolate two colors in HSB space
color lerpColorHSB(color c1, color c2, float amt) {
  amt = min(max(0.0, amt), 1.0);
  float h1 = hue(c1), s1 = saturation(c1), b1 = brightness(c1);
  float h2 = hue(c2), s2 = saturation(c2), b2 = brightness(c2);
  // figure out shortest direction around hue
  float z = g.colorModeZ;
  float dh12 = (h1>=h2) ? h1-h2 : z-h2+h1;
  float dh21 = (h2>=h1) ? h2-h1 : z-h1+h2;
  float h = (dh21 < dh12) ? h1 + dh21 * amt : h1 - dh12 * amt;
  if (h < 0.0) h += z;
  else if (h > z) h -= z;
  colorMode(HSB);
  return color(h, lerp(s1, s2, amt), lerp(b1, b2, amt));
}
