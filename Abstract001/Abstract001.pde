boolean recording = false;  // A boolean to track whether we are recording are not //<>//
float timeScale = 0.0005f;  //global amount to scale millis() for all animations
float fastTimeScale = timeScale*5f;  //special amount for very slow animations
float slowTimeScale = timeScale*0.3f;  //special amount for very slow animations
int maxArms = 5;  //maximum arms per ring
int maxRings = 0;  //maximum rings

void setup() {
  size(800, 800, P3D);
  frameRate(30);
  smooth(8); //enable highest level of anti-aliasing your system can handle
  ellipseMode(CENTER);
  rectMode(CENTER);
}

void draw() {
  background(#090C08);

  float cameraX=cos(millis()*timeScale*3)*150; //generate 3hz 150px wave
  float cameraY=sin(millis()*timeScale*2)*100; //generate 2hz 100px
  cameraX+=sin((millis()*timeScale)*0.5)*100;  //add 0.5hz 100px wave
  cameraY+=cos((millis()*timeScale)*1.3)*100;  //add 1.3hz 100px wave
  camera(cameraX, cameraY, 600, 0, 0, 0, 0, 1.0, 0);  //move camera to position

  for (int i=0; i<maxRings; i++) { //count up from 0 to maxRings
    pushMatrix();
    translate(0, 0, -150*i); //move matrix away from camera i steps
    float direction = (i%2==0)?1:-1; //set direction  to 1 if i is even and -1 if i is odd
    rotateZ((millis()*timeScale)*direction); //rotate around Z axis by time multiplied by direction
    int currentArms = ((maxArms-i)>2)?(maxArms-i):3;  //never let current arms go below 3
    burst(currentArms); //draw a burst
    popMatrix();
  }
  icosahedron(); //draw an icosahedron
  translate(0, 0, 100); //move the matrix towards the camera
  eye(); //draw an eye
  record(); //draw recording UI
}
void eye() { //draw an eye
  //eye outline
  fill(#090C08);
  stroke(#F4D58D);
  strokeWeight(5);
  noiseDetail(6, 0.40); //set noise detail a little higher for twitchier eye movements
  float eyeWidth = noise(1000f+millis()*timeScale)*20f; //adjust eye width by current noise
  bezier(-50f, 0f, -25f, 30f+eyeWidth, 25f, 30f+eyeWidth, 50f, 0); //top of eye
  bezier(-50f, 0f, -25f, -30f-eyeWidth, 25f, -30f-eyeWidth, 50f, 0); //bottom of eye

  //pupil
  fill(255);
  stroke(#98D2EB);
  strokeWeight(2);
  translate(0, 0, 1); //move pupil towards camera
  ellipse(0, 0, 30, 30);
  //rings
  translate(0, 0, 15); //move rings towards camera
  noFill();
  concentric(0, 0, 40, 0, 3); //draw rings
}
void softOrb(float size, int steps, float blur) { //draw a cheap blurred orb similar to a ball of light
  //size|max diameter of orb
  //steps|number of orbs
  //blur|downscale per orb
  pushMatrix();
  noStroke();
  fill(255, 255, 255, 127);
  for (int i=0; i<steps; i++) { //for each orb
    translate(0, 0, 1); //move the orb towards the camera to avoid z fighting
    ellipse(0, 0, size+(i*blur), size+(i*blur)); //draw the orb at the intended scale
  }
  popMatrix();
}
void icosahedron() { //draw a randomized electrical-looking regular icosahedron
  softOrb(100, 6, 4);
  pushMatrix();
  stroke(152, 210, 235, 255);
  strokeWeight(3);
  float scale = 40; //max size of the icosahedron
  float theta = (1f+sqrt(5f))/2f; //some math apparently??
  float[][] points = { //big ol honkin list of points representing the vertecies of a regular icosahedron
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

  //rotate the object
  rotateX(millis()*timeScale);
  rotateY(millis()*timeScale*-1);
  rotateZ(millis()*timeScale);

  for (int i=0; i<floor(random(points.length-(points.length/4), points.length)); i++) {  //count up from zero to a number less than the number of points
    float alpha = 255; //set the max alpha
    int startIndex = floor(random(0, points.length));  //start the lines from the point with a random index
    int maxPoints = floor(random(1, points.length));  //draw lines to this many points
    for (int j=0; j<maxPoints; j++) {  //count up from zero
      int endIndex = floor(random(0, points.length));  //choose a random point to end the line
      stroke(152, 210, 235, alpha); //update actual alpha before drawing
      line(points[startIndex][0]*scale, points[startIndex][1]*scale, points[startIndex][2]*scale, 
        points[endIndex][0]*scale, points[endIndex][1]*scale, points[endIndex][2]*scale); //draw the line
      alpha*=0.75; //adjust the alpha variable
    }
  }
  popMatrix();
}
void burst(int rays) { //root of the burst
  pushMatrix();
  noiseDetail(3, 0.5);
  translate(0, 0, -75); //move away from camera
  noFill();
  stroke(#1A535C);
  strokeWeight(2);
  concentric(0f, 0f, width/2, 0, 5);  //draw the large inner rings
  noiseDetail(4, 0.35f);
  for (int ray=0; ray<rays; ray++) {  //count up from zero to rays
    float timeOffset = 3000f;  //set an offset so one of the sub-objects can be in the future
    float theta = (TWO_PI/rays);  //calculate angle between rays
    float currentTime = millis()*timeScale; //set current time for quick reference
    float armLength = noise(
      sin(theta*ray)+currentTime+timeOffset, 
      cos(theta*ray)+currentTime+timeOffset, 
      currentTime+timeOffset);  //calculate xy coordinates of point at theta and then move it through time and add an offset 
    float armOffset = noise(
      cos(theta*ray)+currentTime, 
      sin(theta*ray)+currentTime, 
      currentTime);  //calculate xy coordinates of point at theta and then move it through time and add an offset
    pushMatrix();
    rotate(ray*theta);  //rotate the matrix into position
    translate(armOffset*(width/8f)+(width/10f), 0f);  //translate the matrix
    arm(armLength*(width/8f)+(width/10f), 5f, 20f);  //draw an arm
    popMatrix();
  }
  popMatrix();
}
void concentric(float x, float y, float ms, int d, int md) {  //recursively draw a pulsating ring of concentric circles
  pushMatrix();
  translate(0, 0, -(ms/10));  //move the matrix away from the camera
  if (d<md) {  //check if we have hit our max depth
    d++;  //add to current depth
    ellipse(x, y, ms, ms);  //draw a circle
    float noiseConstant = noise(millis()*timeScale)*0.4;  //generate scale noise
    concentric(x, y, ms*(0.6+noiseConstant), d, md);  //call concentric again with new max size
    popMatrix();
  } else {  //if we have, pop the matrix and return
    popMatrix();
    return;
  }
}
void arm(float l, float w, float wd) //draw an arm
{
  float currentTime = millis()*timeScale;  //create quick reference to curent time

  stroke(#9D5C63);
  fill(#090C08);
  strokeWeight(w);
  noiseDetail(4, 0.35f);  //adjust noise detail
  rect(30f+(noise(currentTime+5000f)*20f), 0f, l, wd); //draw bar with noise
  ellipse(150f+(noise(currentTime+2000f)*50f), 0f, 30f, 30f);  //draw dot with noise

  noFill();
  stroke(#F4D58D);
  strokeWeight(2);
  noiseDetail(6, 0.5f);  //adjust noise detail
  translate(0f, 0f, -25f);  //move matrix away from camera
  concentric(l, 0f, 135f, 0, 3);  //draw outer concentric circles
}
void record() {
  // If we are recording call saveFrame!
  // The number signs (#) indicate to Processing to 
  // number the files automatically
  if (recording) {
    saveFrame("output/frames####.png");
  }
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
