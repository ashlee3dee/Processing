float t = 0;
void setup() {
  frameRate(30);
  size(512, 512);
}
void draw() {
  background(0);  //clear the screen
  loadPixels(); //grab an array of pixels from screen
  //clearScreen();
  // Loop through every pixel column
  pixelLoop();
  updatePixels();
  t+= PI*0.5;
}
void pixelLoop() {
  for (int x = 0; x < width; x++) {
    // Loop through every pixel row
    for (int y = 0; y < height; y++) {
      // Use the formula to find the 1D lovals[0]tion
      int loc = x+y*width;
      float[] xy = {sin(x+t)*255, sin(y+t)*255};
      pixels[loc] = color(avg(xy));
    }
  }
}
float avg(float[] a) {
  int b=0;
  for (int i = 0; i<a.length; i++) {
    b+=a[i];
  }
  return b/a.length;
}