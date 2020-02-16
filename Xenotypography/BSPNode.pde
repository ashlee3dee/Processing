class BSPNode {
  color[] colors1={
    #EBF5F7, 
    #D7E5EB, 
    #D1D7EC, 
    #BED4E5, 
    #F6E2E2, 
    #EBD7DD
  };
  color[] colors2={
    #5ed6c5, 
    #6688d4, 
    #d7aebc, 
    #5f637a, 
    #2b2722, 
    #EBD7DD
  };
  color[] colors3={
    color(25, 213, 172), 
    color(177, 203, 165), 
    color(147, 224, 287), 
    color(137, 155, 135), 
    color(0, 0, 0), 
    color(301, 254, 232)
  };
  boolean hasChildren=false;
  PVector size;
  PVector position;
  BSPNode[] children = new BSPNode[2];
  int maxDepth;
  int depth = 0;
  float treeSize;
  boolean aspect; //true is tall false is wide
  BSPNode(int md, float ts) {
    treeSize = ts;
    maxDepth = md;
    depth = 0;
    size = new PVector(1, 1);
    position = new PVector(0, 0);
    aspect = random(1)>0.5?true:false;
    //println(aspect);
    //split();
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
    if ((random(0f, 1f)>0.1f&depth<maxDepth))
      split();
  }
  color randomColorFromPalette() {
    return colors3[floor(random(colors3.length))];
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
      pushMatrix();
      //actually draw it
      noStroke();
      //strokeWeight(1);
      //fill(random(255), random(-32, 32)+192, random(192)+64);
      fill(randomColorFromPalette());
      size.mult(0.8);
      rect(position.x*treeSize, 
        position.y*treeSize, 
        size.x*treeSize, 
        size.y*treeSize);
      //ellipse(position.x*treeSize, position.y*treeSize, 10, 10);
      popStyle();
      popMatrix();
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
