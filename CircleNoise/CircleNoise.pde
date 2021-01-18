
float resolution = 100;
float timeScale = 0.001;
boolean recording = true;

void setup() {
  size(1000, 600);
  frameRate(60);
}
void draw() {
  float ns = 255;
  float scale = (width/3)+(50*(noise(frameCount*timeScale)));
  pushMatrix();
  background(0);
  translate(width/2, height/2);
  strokeWeight(1);
  pushMatrix();
  for (float theta=0.0f; theta<TWO_PI; theta+=(TWO_PI/(resolution))) {
    float noise = getRadialNoise(theta, frameCount * timeScale, 2);
    float s1 = scale+(scale*noise);
    float x1 = s1*cos(theta);
    float y1 = s1*sin(theta);

    float nextTheta = theta+(PI/resolution);
    float noise2 = getRadialNoise(nextTheta, frameCount * timeScale, 2);
    float s2 = scale+(scale*noise2);
    float x2 = s2*cos(nextTheta);
    float y2 = s2*sin(nextTheta);

    strokeWeight(2*noise(frameCount * timeScale));
    stroke(255);
    line(x1/2, y1/2, x2/4, y2/4);

    strokeWeight(15*noise(frameCount * timeScale));
    stroke(255);
    line(x1, y1, x1/2, y1/2);

    strokeWeight(8*noise(frameCount * timeScale));
    stroke(255);
    line(x2/4, y2/4, 0, 0);
  }
  popMatrix();

  pushMatrix();

  stroke(255);
  int c = 255;
  for (int i = 0; i < 10; i++) {
    float weight = 4.0-(i/10.0);
    strokeWeight(weight);
    for (float theta=0.0; theta<TWO_PI; theta+=(TWO_PI/resolution)) {
      float noise = getRadialNoise(theta, (float)frameCount * timeScale, 2);
      float s = scale+(scale*noise);
      float x = cos(theta);
      float y = sin(theta);
      x*=s;
      y*=s;
      float n=noise((frameCount+(i/10))*timeScale)*50;
      int xdir = (int) Math.signum(x);
      int ydir = (int) Math.signum(y);
      float n2=weight*noise((float)frameCount * timeScale);
      float n3=weight*noise(((float)frameCount+1000.0) * timeScale);
      float n4=weight*noise(((float)frameCount+2000.0) * timeScale);
      float n5=weight*noise(((float)frameCount+3000.0) * timeScale);
      if (ydir<0) {
        strokeWeight(n2);
        line(x+n, y+n, x+n, -height/2);
      } else {
        strokeWeight(n3);
        line(x+n, y+n, x+n, height/2);
      }
      if (xdir<0) {
        strokeWeight(n4);
        line(x+n, y+n, -width/2, y+n);
      } else {
        strokeWeight(n5);
        line(x+n, y+n, width/2, y+n);
      }
    }
    c*=0.75;
    stroke(c);
    strokeWeight(weight);
    scale(1.0-(0.75*noise(frameCount*timeScale)));
    rotate(((float)frameCount*timeScale*PI));
  }
  popMatrix();
  if (frameCount<=500) {
    record();
  }
  popMatrix();
  stroke(0, 0, 0);
  fill(0, 0, 0);
  rect(0, 0, 100, 15);
  stroke(0, 255, 0);
  fill(0, 255, 0);

  text(frameCount, 50, 10);
}
void keyPressed() {

  // If we press r, start or stop recording!
  if (key == 'r' || key == 'R') {
    recording = !recording;
  }
}
void record() {  // If we are recording call saveFrame!
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
  if (recording) {
    fill(255, 0, 0);
  } else { 
    noFill();
  }
  ellipse(width/2, height-48, 16, 16);
}
float getRadialNoise(float thetaInRadians, float offset, float noisescale) {
  return noise(
    (sin(thetaInRadians)+offset)*noisescale, 
    (cos(thetaInRadians)+offset)*noisescale
    );
}
