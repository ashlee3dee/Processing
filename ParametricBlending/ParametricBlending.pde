boolean recording = false; 
void setup() {
  size(400, 400, P2D);
  frameRate(30);
  colorMode(HSB);
}

void draw() {
  loadPixels();
  for (int x=0; x<width; x++) {
    for (int y=0; y<height; y++) {
      //int a = floor(random(0,255));s
      float repetitions = 20.0;
      float b = tan(x*(repetitions/height))+tan(y*(repetitions/width));
      //int c = int((dist(width/2,height/2,x,y)/dist(width/2,height/2,width,height))*255);
      b = b*(squine((x*0.020)+(millis()*0.002), 2)*(sin(millis()*0.001)*64));
      pixels[y*width+x]=color(floor(b), 255, 255);
    }
  }
  updatePixels();
  record(); //draw recording UI
  drawFPS(255);
}
void drawFPS(int c)
{  
  noStroke();
  fill(0);
  rect(0, 0, 50, 20);
  fill(c);
  text(frameRate, 0, 15);
}
float squine(float t, float k) {
  return 1f-(2/(1f+pow(exp(k), (sin(t)))));
}

void keyPressed() {

  // If we press r, start or stop recording!
  if (key == 'r' || key == 'R') {
    recording = !recording;
  }
}

void record() {
  // If we are recording call saveFrame!
  // The number signs (#) indicate to Processing to 
  // number the files automatically
  if (recording) {
    saveFrame("output/frames####.png");
  }
}
