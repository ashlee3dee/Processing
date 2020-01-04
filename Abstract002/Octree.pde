class Octree {
  //lookup tables for splitting octrees
  //could be converted to 8 bit binary and use bitwise math to optimize
  private final boolean[] bx = {
    false, true, false, true, false, true, false, true
  };
  private final boolean[] by = {
    false, false, true, true, false, false, true, true
  };
  private final boolean[] bz = {
    false, false, false, false, true, true, true, true
  };
  boolean hasChildren;
  Octree[] children;
  Type t = Type.NULL;
  color c;    //color of the node if we draw it
  PVector p;  //world position of octree node
  float s;    //size of octree node

  int maxLevel = 3; //how deep can we go?
  int myLevel; //keep track of how many levels deep into the octree

  Octree(float mySize, PVector myPosition, boolean havechildren, int level, color parentColor) {
    s = mySize;
    p = myPosition;
    hasChildren = havechildren;
    myLevel = level;

    if ( hasChildren ) {
      children = new Octree[8];
      float half_s = s/2;            //the children must be scaled
      for (int i=0; i<8; i++) {      //for each 8 children in the octree
        //first create a few temporary variables
        float childScale = half_s;
        boolean hasChildren = myLevel < maxLevel && random(1) < .49;
        PVector childPosition = new PVector(
          p.x+(bx[i]?half_s:0), 
          p.y+(by[i]?half_s:0), 
          p.z+(bz[i]?half_s:0));
        children[i] = new Octree(
          childScale, 
          childPosition, 
          hasChildren, 
          myLevel+1, 
          parentColor
          );
      }
    } else {
      PVector pcol = fastPVectorFromRGB(parentColor);
      t = Type.values()[(int)random(1, 4)];
      c = color(((pcol.x*2)+random(255))/3, ((pcol.y*2)+random(255))/3, ((pcol.z*2)+random(255))/3);
    }
  }  
  void draw() {
    if ( hasChildren ) { //does this instance of Octree have any children?
      for (int i=0; i<8; i++) { //loop through them
        children[i].draw(); //call this function on each child
      }
    } else { //mySize will only be called if we reach the "bottom"
      pushMatrix();
      translate(p.x, p.y, p.z);
      translate(s/2, s/2, s/2);
      //rotateX(rotation);
      //rotateY(rotation);
      //rotateZ(rotation);
      //beginShape(TRIANGLE_FAN);
      //float centerx=0, centery=0, size=s;
      //vertex(centerx, centery);
      //vertex(centerx, centery-size); 
      //vertex(centerx+size, centery); 
      //vertex(centerx, centery+size); 
      //vertex(centerx-size, centery); 
      //vertex(centerx, centery-size); 
      //endShape();

      //if (myLevel > 3) {
      //  noStroke();
      //  fill(c);
      //} else {
      //  noFill();
      //  strokeWeight(map(maxLevel+1-myLevel, 1, maxLevel, 2, 8));
      //  stroke(c);
      //}
      stroke(255);
      fill(0);
      float ns = s;
      ns*=noise((millis()+PVector.dist(new PVector(0, 0, 0), p))*0.0015)+0.25;
      switch(t) {
      case A:
        sphere(ns/2);
        break;
      case B:
        box(ns);
        break;
      case C:
        rotateX(myLevel*(TWO_PI/4));
        rotateY(myLevel*(TWO_PI/4));
        rotateZ(myLevel*(TWO_PI/4));
        tetrahedron(ns/2);
        break;
      case D:
        box(ns);
        break;
      default:
        println("this should never happen");
      }
      popMatrix();
    }
  }
}

static enum Type 
{ 
  NULL, A, B, C, D;
} 
PVector fastPVectorFromRGB(color c) {
  //int a = (c >> 24) & 0xFF;
  int r = (c >> 16) & 0xFF;  // Faster way of getting red(argb)
  int g = (c >> 8) & 0xFF;   // Faster way of getting green(argb)
  int b = c & 0xFF;          // Faster way of getting blue(argb)
  return new PVector(r, g, b);
}

void tetrahedron(float s) {
  beginShape(TRIANGLES);
  vertex(-s, -s, -s);
  vertex( s, -s, -s);
  vertex(   0, 0, s);

  vertex( s, -s, -s);
  vertex( s, s, -s);
  vertex(   0, 0, s);

  vertex( s, s, -s);
  vertex(-s, s, -s);
  vertex(   0, 0, s);

  vertex(-s, s, -s);
  vertex(-s, -s, -s);
  vertex(   0, 0, s);
  endShape();
}

void icosahedron(float s)
{
  float  theta = (1.0 + sqrt(5.0)) / 2.0; //some math apparently??
  float[][] povars = { //big ol honkin list of povars representing the vertecies of a regular icosahedron
    {theta, 1, 0}, 
    {theta, -1, 0}, 
    {-theta, 1, 0}, 
    {-theta, -1, 0}, 
    {0, theta, 1}, 
    {0, theta, -1}, 
    {0, -theta, 1}, 
    {0, -theta, -1}, 
    {1, 0, theta}, 
    {-1, 0, theta}, 
    {1, 0, -theta}, 
    {-1, 0, -theta}
  };
}
