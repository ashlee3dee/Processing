void setup() {
  size(512, 512);
}

void draw() {
  float currentTime = millis()*(1f/10000f);
  // loop thru each pixel on the screen
  for (int x = 0; x < width; x++) {
    for (int y = 0; y < height; y++) {
      float tx = currentTime+x;
      float ty = currentTime+y;
      float r, g, b; //create reference to each color channel
      float nv = noise(currentTime);
      r = sin(tx*0.05*nv) / cos(nv*sin(tx*0.20*nv) + cos(ty*0.11*nv)*nv); //red channel
      g = cos(ty*2.97 + tx*0.25*nv) * sin(ty*tan(0.23*nv)); //green channel
      b = tan(tx*0.06*nv) - cos(nv*tan(tx*0.32*nv) + cos(ty*0.40*nv)*nv); //blu channel
      set(x, y, color(r*255, g*255, b*255)); //combine into a color and set the pixel value
    }
  }
}
