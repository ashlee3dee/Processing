PImage d;
PImage i;
PImage o;
float scale = 0.1;
ArrayList<River> rivers;
void setup() {
  //some enviroment setup
  frameRate(60);
  colorMode(HSB);
  noiseDetail(2);
  size(600, 600);
  //initialize images
  i = new PImage(width/2, height);
  o = new PImage(width/2, height);
  rivers = new ArrayList<River>();
  //generate noise map
  i.loadPixels();
  for (int ix = 0; ix < i.width; ix++) {
    for (int iy=0; iy < i.height; iy++) {
      i.pixels[ix + (iy * i.width)] = color(noise(ix*scale, iy*scale)*255);
    }
  }
  i.updatePixels();
  image(i, 0, 0);
  //create grid of rivers
  int dimensions = 50;
  for (int ix = 0; ix < dimensions; ix+=1) {
    for (int iy=0; iy < dimensions; iy+=1) {
      rivers.add(new River(i.width/dimensions * ix, i.height/dimensions * iy, color(random(0, 1)*255, random(0.75, 1)*255, random(0.75, 1)*255), 1));
    }
  }
}
void draw() {
  image(i, 0, 0);
  image(o, width/2, 0);
  for (int i = 0; i < rivers.size(); i++) {
    if (rivers.get(i).inBounds()) {
      rivers.get(i).update();
    } else {
      rivers.remove(i);
      println("river removed!");
    }
  }
}