//<>// //<>// //<>// //<>// //<>// //<>// //<>// //<>// //<>// //<>// //<>// //<>// //<>// //<>//
/// A boolean to track whether we are recording are not
boolean recording = false;
float timeScale = 0.0005f;
float slowTimeScale = timeScale*0.5f;
int maxArms = 5;
int maxRings = 0;
void setup() {
  size(800, 800, P3D);
  frameRate(30);
  smooth(8);
  ellipseMode(CENTER);
  rectMode(CENTER);
}

void draw() {
  //camera(cos(millis()*timeScale*3)*300, sin(millis()*timeScale*2)*100, 600, 0, 0, 0, 0, 1.0, 0);
  background(#090C08);
  float cameraX=cos(millis()*timeScale*3)*150;
  float cameraY=sin(millis()*timeScale*2)*100;
  cameraX+=sin((millis()*timeScale)*0.5)*100;
  cameraY+=cos((millis()*timeScale)*1.3)*100;
  camera(cameraX, cameraY, 600, 0, 0, 0, 0, 1.0, 0);
  for (int i=0, j=maxRings; i<j & i<maxArms; i++) {
    pushMatrix();
    translate(0, 0, -150*i);
    float invert = (i%2==0)?1:-1;
    rotateZ((millis()*timeScale)*invert);
    burst(maxArms-i);
    popMatrix();
  }
  icosahedron();
  translate(0, 0, 100);
  eye();
  record();
}
void eye() {
  //rotateZ(90);
  fill(#090C08);
  strokeWeight(5);
  stroke(#F4D58D);
  bezier(-50, 0, -25, 30, 25, 30, 50, 0);
  bezier(-50, 0, -25, -30, 25, -30, 50, 0);
  strokeWeight(2);
  stroke(#98D2EB);
  fill(255);
  translate(0, 0, 1);
  ellipse(0, 0, 30, 30);
  translate(0, 0, 15);
  noFill();
  concentric(0, 0, 40, 0, 3);
}
void softOrb(float size, int steps, float blur) {
  pushMatrix();
  noStroke();
  for (int i=0; i<steps; i++) {
    translate(0, 0, 1);
    fill(255, 255, 255, 127);
    ellipse(0, 0, size+(i*blur), size+(i*blur));
  }
  popMatrix();
}
void icosahedron() {
  softOrb(100, 6, 4);
  pushMatrix();
  stroke(152, 210, 235, 255);
  strokeWeight(3);
  float scale = 40;
  float theta = (1f+sqrt(5f))/2f;
  float[][] points = {
    {theta, 1, 0}, 
    {theta, -1, 0}, 
    {-theta, 1, 0}, 
    {-theta, -1, 0}, 
    {0, theta, 1}, 
    {0, theta, -1}, 
    {0, -theta, 1}, 
    {0, -theta, -1}, 
    {1, 0, theta}, 
    {-1, 0, theta}, 
    {1, 0, -theta}, 
    {-1, 0, -theta}
  };
  rotateX(millis()*timeScale);
  rotateY(millis()*timeScale);
  rotateZ(millis()*timeScale);
  for (int i=0; i<floor(random(points.length-(points.length/4), points.length)); i++) {
    float alpha = 255;
    int startIndex = floor(random(0, points.length));
    int maxPoints = floor(random(1, points.length));
    for (int j=0; j<maxPoints; j++) {
      stroke(152, 210, 235, alpha);
      int endIndex = floor(random(0, points.length));
      line(points[startIndex][0]*scale, points[startIndex][1]*scale, points[startIndex][2]*scale, 
        points[endIndex][0]*scale, points[endIndex][1]*scale, points[endIndex][2]*scale);
      alpha*=0.75;
    }
  }
  popMatrix();
}
void burst(int rays) {
  pushMatrix();
  noFill();
  stroke(#1A535C);
  strokeWeight(2);
  noiseDetail(3, 0.5);
  translate(0, 0, -75);
  concentric(0f, 0f, width/2, 0, 5);
  //translate(width/2f, height/2f);
  int totalRays = rays;
  for (int ray=0; ray<totalRays; ray++) {
    noiseDetail(4, 0.35f);
    float theta = (TWO_PI/totalRays);
    float currentTime = millis()*timeScale;
    float armOffset = noise(
      cos(theta*ray)+currentTime, 
      sin(theta*ray)+currentTime, 
      currentTime);
    float armLength = noise(
      cos(theta*ray)+currentTime, 
      sin(theta*ray)+currentTime, 
      currentTime);
    pushMatrix();
    rotate(ray*theta);
    translate(armOffset*(width/8f)+(width/10f), 0f);
    arm(armLength*(width/8f)+(width/10f), 5f, 20f, color(255, 255, 255));
    popMatrix();
  }
  popMatrix();
}
void concentric(float x, float y, float ms, int d, int md) {
  pushMatrix();
  translate(0, 0, -(ms/10));
  if (d<md) {
    d++;
    ellipse(x, y, ms, ms);
    float noiseConstant = noise(millis()*timeScale)*0.4;
    concentric(x, y, ms*(0.6+noiseConstant), d, md);
  } else {
    popMatrix();
    return;
  }
  popMatrix();
}
void arm(float l, float w, float wd, color c)
{
  float currentTime = millis()*timeScale;
  stroke(#9D5C63);
  //noFill();
  fill(#090C08);
  strokeWeight(w);
  rect(30+(noise(currentTime+5000)*20), 0, l, wd);
  ellipse(150+(noise(currentTime+2000)*50), 0, 30, 30);
  noFill();
  stroke(#F4D58D);
  strokeWeight(2);
  noiseDetail(6, 0.5);
  translate(0, 0, -25);
  concentric(l, 0f, 135, 0, 3);
}
void record() {
  // If we are recording call saveFrame!
  // The number signs (#) indicate to Processing to 
  // number the files automatically
  if (recording) {
    saveFrame("output/frames####.png");
  }
  // Let's draw some stuff to tell us what is happening
  // It's important to note that none of this will show up in the
  // rendered files b/c it is drawn *after* saveFrame()
  textAlign(CENTER);
  fill(255);
  if (!recording) {
    text("Press r to start recording.", width/2, height-24);
  } else {
    text("Press r to stop recording.", width/2, height-24);
  }

  // A red dot for when we are recording
  stroke(255);
  strokeWeight(2);
  if (recording) {
    fill(255, 0, 0);
  } else { 
    noFill();
  }
  ellipse(width/2, height-48, 16, 16);
}
void keyPressed() {

  // If we press r, start or stop recording!
  if (key == 'r' || key == 'R') {
    recording = !recording;
  }
  if (key == 'q' || key == 'Q') {
    maxRings++;
  }
  if (key == 'a' || key == 'A') {
    maxRings--;
  }
  if (key == 'e' || key == 'E') {
    maxArms++;
  }
  if (key == 'd' || key == 'D') {
    maxArms--;
  }
}
