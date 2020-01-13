void setup() {
  frameRate(20);
  size(500, 500);
  noSmooth();
}
void draw() {
  background(0);
  int spikes=32;
  float t = millis()*0.0001f;
  float cartesianScale = 0.0143;
  float polarScale = 0.0177;
  loadPixels();
  for (int x=0; x<width; x++) {
    for (int y=0; y<height; y++) {
      //pixels[y*720+x]=color(((atan2(360-x,360-y)+PI)+noise(x*.01,y*.01,(dist(x,y,360,360)-t*5000)*.01+t)+t)%(PI/16)<PI/32?0:255);
      //pixels[y*width+x]=color(((atan2((width/2)-x, (height/2)-y)+TWO_PI)+noise(x*.01, y*.01, (dist(x, y, width/2, height/2)-t)*.01+t)+t)%(TWO_PI/spikes)<PI/spikes?0:255);
      int c = (((fastAtan2((float) (width/2)-x, (float) (height/2)-y)+TWO_PI)+noise(x*cartesianScale, y*cartesianScale, (dist(x, y, width/2f, height/2f)-t)*polarScale+t)+t)%(TWO_PI/spikes)<PI/spikes?0:255);
      pixels[y*width+x]=color(c);
    }
  }
  updatePixels();
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
//you can use this fastAtan2 as a drop-in replacement for the regular atan2
//it is faster and just as precise for most applications
//using fastAtan2 instead of atan2 doubles the fps on my machine
public float fastAtan2(float y, float x) {
  if (x == 0.0f) {
    if (y > 0.0f)
      return PI/2;
    if (y == 0.0f)
      return 0.0f;
    return -PI/2;
  }

  final float atan;
  final float z = y / x;
  if (Math.abs(z) < 1.0f) {
    atan = z / (1.0f + 0.28f * z * z);
    if (x < 0.0f)
      return (y < 0.0f) ? atan - PI : atan + PI;
    return atan;
  } else {
    atan = PI/2 - z / (z * z + 0.28f);
    return (y < 0.0f) ? atan - PI : atan;
  }
}
