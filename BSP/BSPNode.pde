class BSPNode {
  boolean hasChildren=false;
  PVector size;
  PVector position;
  BSPNode[] children = new BSPNode[2];
  int maxDepth;
  int depth = 0;
  float treeSize;
  int minDepth = 4;
  boolean aspect; //true is tall false is wide
  BSPNode(int md, float ts) {
    treeSize = ts;
    maxDepth = md;
    depth = 0;
    size = new PVector(1, 1);
    position = new PVector(0, 0);
    aspect = random(1)>0.5?true:false;
    //println(aspect);
    split();
  }
  BSPNode(BSPNode parent, boolean child) {
    this.treeSize = parent.treeSize;
    maxDepth = parent.maxDepth;
    depth = parent.depth+1;
    aspect =! parent.aspect;
    if (parent.aspect) { //if the parent is tall we divide wide
      size = new PVector(parent.size.x, parent.size.y/2f);
      if (child) { //top child
        position = new PVector(parent.position.x, parent.position.y-(parent.size.y/4f));
      } else { //bottom child
        position = new PVector(parent.position.x, parent.position.y+(parent.size.y/4f));
      }
    } else { //if the parent is wide we divide tall
      size = new PVector(parent.size.x/2f, parent.size.y);
      if (child) { //top child
        position = new PVector(parent.position.x-(parent.size.x/4f), parent.position.y);
      } else { //bottom child
        position = new PVector(parent.position.x+(parent.size.x/4f), parent.position.y);
      }
    }
    if ((depth<minDepth||(random(0f, 1f)>0.25f)&&depth<maxDepth))
      split();
  }
  void split() {
    hasChildren=true;
    children[0] = new BSPNode(this, true);
    children[1] = new BSPNode(this, false);
  }
  void draw() {

    if (hasChildren) {
      for (int i=0; i < children.length; i++) {
        children[i].draw();
      }
    } else {
      pushStyle();
      //actually draw it
      stroke(0);
      strokeWeight(1);
      fill(random(255), random(-32, 32)+192, random(192)+64);
      float mult = 1;
      rect(position.x*treeSize*mult, 
        position.y*treeSize*mult, 
        size.x*treeSize*mult, 
        size.y*treeSize*mult);
      //ellipse(position.x*treeSize, position.y*treeSize, 10, 10);
      popStyle();
    }
  }
  void visualA() {
    //stroke(0, 0, 255);
    //stroke(map(depth, 0, maxDepth, 128, 255));
    curveVertex(position.x*treeSize, position.y*treeSize);
    if (hasChildren) {
      for (int i=0; i < children.length; i++) {
        children[round(random(0, children.length-1))].visualA();
      }
    }
  }
  void visualB() {
    if (random(1)>0.25) {
      if (hasChildren) {
        for (int i=0; i < children.length; i++) {
          children[i].visualB();
        }
      } else {
        pushMatrix();
        PVector pt = new PVector(
          map(noise(depth, currentTime), 0, 1, -1, 1)*(treeSize/4), 
          map(noise(currentTime, depth), 0, 1, -1, 1)*(treeSize/4));
        translate(pt.x, pt.y);
        pushStyle();
        fill(0, 0, 0);
        stroke(0, 0, 255);
        if (aspect) {
          ellipse(position.x*treeSize, position.y*treeSize, treeSize/8, treeSize/8);
        } else {
          beginShape();
          int points = round(random(3, 5));

          float radius = treeSize/8f, 
            step = TAU/points, 
            offset = step*floor(random(0, points));
          for (int i=0; i < points; i++) {
            float x = cos(offset+(step*i)) * radius;
            float y = sin(offset+(step*i)) * radius;
            vertex((position.x*treeSize)+x, (position.y*treeSize)+y);
          }
          endShape(CLOSE);
        }
        popStyle();
        popMatrix();
      }
    }
  }
  void reset() {
    children=new BSPNode[2];
    hasChildren=false;
    depth=0;
    size = new PVector(1, 1);
    position = new PVector(0, 0);
    aspect = random(1)>0.5?true:false;
    split();
  }
}
