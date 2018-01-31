import at.mukprojects.console.*;

Console console;
float zoom; // distance in noise space to increment, per pixel
float xpos, ypos; // offset from 0,0 in 2d noise space
float moveSpeed; // amount to add/subtract from xpos/ypos upon a press of the arrow keys
int nDetail; // noise detail. higher values = exponentially slower rendering
boolean showConsole; // controls whether the console be shown
float filterCutoff;
FilterMode fm; //
Palette p;
void setup() {
  colorMode(HSB);
  frameRate(5);
  size(512, 512); //create 512x512 pixel window
  frameRate(30);
  noiseDetail(1);
  filterCutoff = 0.0;
  fm = FilterMode.NONE;
  xpos = 0;
  ypos = 0;
  zoom = 0.01;
  nDetail=2;
  moveSpeed = 0.1;
  // start console neatly
  //StartConsole();
  //palette test code
  p = new Palette("testPalette01");
  p.AddColor(color((256/1)-1, 255, 255), "Color 1");
  p.AddColor(color((256/2)-1, 255, 255), "Color 2");
  p.AddColor(color((256/3)-1, 255, 255), "Color 3");
  p.AddColor(color((256/4)-1, 255, 255), "Color 4");
  p.AddColor(color((256/6)-1, 255, 255), "Color 5");
  p.AddColor(color((256/7)-1, 255, 255), "Color 6");
}

void draw() {
  float xoff = xpos;
  loadPixels();
  for (int x = 0; x < width; x++) {
    float yoff = ypos;
    for (int y = 0; y < height; y++) {
      int index = (x+y*width);
      float value = noise(xoff, yoff);
      pixels[index] = filterPixel(value, p);
      yoff+=zoom;
    }
    xoff+=zoom;
  }
  updatePixels();
  // Finally, draw the console to the screen.
  DrawConsole();
}

color filterPixel(float input, Palette pal) {
  return 
}

enum FilterMode {
  LOWER, 
    HIGHER, 
    EQUAL, 
    NONE
}

void StartConsole() {
  // initialize the console
  console = new Console(this);
  // start the console
  console.start();
  // incluide timestamps
  console.setTimestamp(true);
}

void DrawConsole() {
  if (showConsole) {
    // (x, y, width, height, preferredTextSize, minTextSize, linespace, padding, strokeColor, backgroundColor, textColor)
    console.draw();
  }
}

//INPUT HANDLING CODE

void keyPressed() {

  // Randomize map
  if (key == 'r') {
    long ns = (long)random(255);
    noiseSeed(ns);
    println("[INFO] new seed: " + ns);
  }
  // Switch to filter mode LOWER
  if (key == '1') {
    fm = FilterMode.LOWER;
    println("switched to filtermode: LOWER");
  }
  // Zoom in
  if (key == '=') {
    zoom = zoom - 0.001;
    println("[INFO] zoom level: " + zoom);
  }
  // Zoom out
  if (key == '-') {
    zoom = zoom + 0.001;
    println("[INFO] zoom level: " + zoom);
  }
  // Toggle visibility of console when `~ key is pressed 
  if (key == '`') {
    showConsole = !showConsole; // toggle showConsole value
  }
  // change noise detail
  if (key == 'a') {
    if (nDetail > 1)
    {
      nDetail--;
      noiseDetail(nDetail);
      println("[INFO] noise detail: " + nDetail);
    } else {
      println("[ERROR] noise detail already at minimum value!");
    }
  }
  if (key == 's') {
    nDetail++;
    noiseDetail(nDetail);
    println("[INFO] noise detail: " + nDetail);
  }
  // adjust filter cutoff
  if (key == 'z') {
    filterCutoff+=0.01;
    println("filter cutoff: " + filterCutoff);
  }
  if (key == 'x') {
    filterCutoff-=0.01;
    println("filter cutoff: " + filterCutoff);
  }
  // handle movement
  if (key == CODED) {
    if (keyCode==UP) {
      ypos-=moveSpeed;
    }
    if (keyCode==DOWN) {
      ypos+=moveSpeed;
    }
    if (keyCode==LEFT) {
      xpos-=moveSpeed;
    }
    if (keyCode==RIGHT) {
      xpos+=moveSpeed;
    }
    println("[INFO] x: "+ xpos + ", y: " + ypos);
  }
}