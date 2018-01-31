PImage i;
float scale = 0.1;
void setup() {
  //some enviroment setup
  frameRate(60);
  colorMode(HSB);
  noiseDetail(2);
  size(600, 600);
  //initialize images
  i = new PImage(width, height);
  //generate noise map
}
void draw() {
  i.loadPixels();
  scale = 0.01;
  noiseDetail(2);
  for (int ix = 0; ix < i.width; ix++) {
    for (int iy=0; iy < i.height; iy++) {
      i.pixels[ix + (iy * i.width)] = color(noise(ix*scale, iy*scale)*255);
    }
  }
  i.updatePixels();
  image(i, 0, 0);
}