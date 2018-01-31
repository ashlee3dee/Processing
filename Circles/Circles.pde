void setup() {
  size(512, 512);
}

void draw() {
  // loop thru each pixel on the screen
  for (int x = 0; x < width; x++) {
    for (int y = 0; y < height; y++) {
      float r, g, b; //create reference to each color channel
      r = sin(x*0.05) / cos(sin(x*0.20) + cos(y*0.11)); //red channel
      g = cos(y*2.97 + x*0.25) * sin(y*tan(0.23)); //green channel
      b = tan(x*0.06) - cos(tan(x*0.32) + cos(y*0.40)); //blu channel
      set(x, y, color(r*255, g*255, b*255)); //combine into a color and set the pixel value
    }
  }
}