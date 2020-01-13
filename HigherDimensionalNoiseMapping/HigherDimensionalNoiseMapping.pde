
void setup() {
  size(400, 400);
  colorMode(HSB);
}

void draw() {
  background(0);
  loadPixels();
  for (int x=0; x < width; x++) {
    for (int y=0; y < height; y++) {
      //stroke(random(255));
      float scale = 0.05f;
      float theta = cart2Polar(x,y).x;
      float ns = noise(scale*x, scale*y, theta*0.10);
      pixels[y*width+x]=color(ns*255, 255, 255);
    }
  }
  updatePixels();
}

public PVector cart2Polar(float x, float y) {
  float x1,y1;
  x1 = (width/2)-x;
  y1 = (height/2)-y;
  double r     = Math.sqrt(x1*x1 + y1*y1);
  double theta = Math.atan2((width/2)-x,(height/2)-y);
  return new PVector((float)r,(float)theta);
}
