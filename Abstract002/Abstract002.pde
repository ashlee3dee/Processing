Octree ot;
float zoom = 0;
float zoomSpeed = 40;
void setup() {
  size(800, 800, P3D);
  frameRate(30);
  colorMode(HSB);
  sphereDetail(8);
  ot = new Octree(width/2, new PVector(0, 0, 0), true, 0, color(random(255*0.75, 255), random(255*0.75, 255), random(255*0.75, 255)));
}

void draw() {
  background(0);
  noiseDetail(2);
  translate(width/2, height/2, zoom);
  //rotateY(map(mouseX, 0, width, -PI, PI));    
  //rotateX(map(mouseY, 0, height, -PI, PI));
  rotateY(squine(3, millis()*0.001f)*PI/2);    
  rotateX(squine(8, millis()*0.001f)*PI/2); 
  translate(-width/4, -width/4, -width/4);
  ot.draw();
}

void mouseWheel(MouseEvent event) {
  float e = event.getCount();
  zoom+=e*zoomSpeed;
}
void mouseClicked() {
  ot = new Octree(width/2, new PVector(0, 0, 0), true, 0, color(random(255*0.75, 255), random(255*0.75, 255), random(255*0.75, 255)));
}

float squine(float k, float t) {
  return 1f-(2/(1f+pow(exp(k), (sin(t)))));
}
