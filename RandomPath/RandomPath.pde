
/*
RANDOM PATH
Jeff Thompson | 2017 | jeffreythompson.org
Generates a random path between two predefined points.
*/

import java.util.Date;                // imports for timestamp
import java.text.SimpleDateFormat;


float lineLen =      20;            // length of segments
float maxAngle =     radians(90);   // range of random angle towards end
float noiseInc =     0.01;          // increment in Perlin noise
float minDistToEnd = 50;            // how close to the end before we quit?

float noiseOffset = 0;             // current position in Perlin noise
PVector end;                       // target point


void setup() {
  size(1000, 1000);
  background(30);

  // create end point at center of screen
  end = new PVector(width/2, height/2);
  
  // create random paths to the center!
  for (int i=0; i<20; i++) {
    randomPath();
  }
  
  // draw the end point
  fill(0,150,255);
  noStroke();
  ellipse(end.x, end.y, 10,10);
}


void draw() {
  // nothing here!
  lineLen =      20;            // length of segments
  maxAngle =     radians(90);   // range of random angle towards end
  noiseInc =     0.01;          // increment in Perlin noise
  minDistToEnd = 50;            // how close to the end before we quit?

}


// 's' to save the frame, any other key re-draws new random paths
void keyPressed() {
  if (key == 's') {
    String filename = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss'.jpg'").format(new Date());
    save("output/" + filename);
  }
  else {
    setup();
  }
}


void randomPath() {
  
  // create random starting point
  PVector start = new PVector(random(50, width-50), random(50, height-50)); 
  PVector current = start.copy();
  
  // start line
  stroke(255, 150);
  noFill();
  beginShape();
  vertex(current.x, current.y);
  
  // run until we hit the endpoint!
  int i=0; 
  while (true) {
    
    // compute angle b/w current position and the end point
    float xDist = end.x - current.x;
    float yDist = end.y - current.y;
    float between = atan2(yDist, xDist); 
    
    // use Perlin noise to compute a random value (0-1), change
    // to range of the maxAngle (this makes the line vary instead of
    // heading straight towards the endpoint!)
    float newAngle = between + (noise(noiseOffset) * maxAngle - maxAngle/2);
    
    // calculate new x/y position based on the angle, draw a line
    float x = current.x + cos(newAngle) * lineLen;
    float y = current.y + sin(newAngle) * lineLen;
    vertex(x, y);

    // update current position to the end of the line, figure
    // out how far we have left to go
    current = new PVector(x, y);
    float distLeft = PVector.dist(current, end);
    
    // if we're near the endpoint, draw a line to the end and quit
    if (distLeft <= minDistToEnd) {
      line(current.x, current.y, end.x, end.y);
      break;
    }
    
    // update count and Perlin noise variables, continue
    i += 1;
    noiseOffset += noiseInc;
  }
  
  // finish the shape!
  endShape();
  
  // draw the starting point on top
  fill(255,150,0);
  noStroke();
  ellipse(start.x, start.y, 10,10);
}
