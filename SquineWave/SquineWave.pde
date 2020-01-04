
void setup() {
  size(400, 400);
}

void draw() {
  background(0);
  stroke(255);
  strokeWeight(2);
  pushMatrix();
  translate(0, height/2);
  for (int i=0; i < width; i++) {
    point(i, squine(i/50f, 10f)*100);
  }
  popMatrix();
}
float squine(float t, float k) {
  return 1f-(2/(1f+pow(exp(k), (sin(t)))));
}
