int ORBIT_RADIUS = 100;
int NUM_OF_LINES = 20;
float NOISE_OFFSET = 0;
int SPACING = 20;
int CAM_ANGLE = 15;
float SCROLL_SPEED = 0.01;
void setup() {
  size(512, 512, P3D);
  colorMode(RGB);
  ortho();
}

void draw() {
  background(0);
  lights();
  translate(0, 0, -NUM_OF_LINES*SPACING/2);
  rotateX(radians(CAM_ANGLE));
  Exp1Wrap(NUM_OF_LINES, SPACING);
}
void Exp1Wrap(int lines, float spacing) {
  pushMatrix();
  translate(width/2, height/2);
  //rotateY(radians(frameCount/2));
  for (int i = 0; i <lines; i++)
  {
    Exp1(32, 0, 0.01, i*NOISE_OFFSET);
    translate(0, 0, width/spacing);
  }
  popMatrix();
}
void Exp1(int points, float spacing, float speed, float offset) {
  //experiment one
  float s = width/points;
  pushMatrix();
  noFill();
  int s_color = 0;
  strokeWeight(2);
  beginShape();
  translate(-width/2, height/2);
  for (int i = 0; i < points; i++) {
    stroke(s_color, 255, 255);
    float noise = noise(i, (frameCount+offset)*SCROLL_SPEED);
    curveVertex(i*s, noise*100, 0);
    s_color += 255/points;
  }
  endShape();
  popMatrix();
}

void keyPressed() {
  if (key == 'q') {
    NOISE_OFFSET += 0.1;
  }
  if (key == 'a') {
    NOISE_OFFSET -= 0.1;
  }
  if (key == 'w') {
    NUM_OF_LINES += 1;
  }
  if (key == 's') {
    NUM_OF_LINES -= 1;
  }
  if (key == 'e') {
    SCROLL_SPEED += 0.005;
  }
  if (key == 'd') {
    SCROLL_SPEED -= 0.005;
  }
  if (key == 'i') {
    CAM_ANGLE++;
  }
  if (key == 'k') {
    CAM_ANGLE--;
  }
  if (key == 'c') {
  }
  if (key == 'c') {
  }
}