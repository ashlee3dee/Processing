import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.lang.reflect.Field; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Dungeon extends PApplet {

boolean recording = false;        // A boolean to track whether we are recording are not
float timeScale = 0.0005f;        //global amount to scale millis() for all animations
float currentTime = 0;
String title=getClass().getSimpleName();
BSPTree dungeon;
public void settings()
{
  size(800, 800, P3D);
}

public void setup() {
  frameRate(30);                  //all code should be frame-rate independent. hint: use millis()
                        //enable highest level of anti-aliasing your system can handle
  ellipseMode(CENTER);
  rectMode(CENTER);
  imageMode(CENTER);
  blendMode(BLEND);
  colorMode(HSB);
  background(0, 0, 0);
  dungeon = new BSPTree(4, 2, width);
}

public void draw() {
  tick();  //no touch
  background(0);
  //dungeon.reset();
  dungeon.draw();
  tock();  //no touch
}

/**
 ================================
 Mandatory Code
 ================================
 **/
public void keyPressed() {
  if (key == 'r' || key == 'R') {
    recording = !recording;
  }
  if (key == 'j' || key == 'J') {
    println("################################RESET################################");

    //dungeon.reset();
    //dungeon.split();
    //println(dungeon.info());
    //dungeon.split();
    //dungeon.draw();
    //saveFrame("/output/"+title+"_frame_####.png");
  }
}
public void tick() {
  currentTime = millis()*timeScale;
}
public void tock() {
  if (recording) {
    saveFrame("output/frame_####.png");
  }
  //ui();
}
public void ui() {
  drawFPS(255);
}
public void drawFPS(int textColor) 
{
  pushMatrix();
  noStroke();
  fill(0);
  rect(0, 0, 100, 40);
  fill(textColor);
  text(frameRate, 0, 15);
  popMatrix();
}

// linear interpolate two colors in HSB space
public int lerpColorHSB(int c1, int c2, float amt) {
  amt = min(max(0.0f, amt), 1.0f);
  float h1 = hue(c1), s1 = saturation(c1), b1 = brightness(c1);
  float h2 = hue(c2), s2 = saturation(c2), b2 = brightness(c2);
  // figure out shortest direction around hue
  float z = g.colorModeZ;
  float dh12 = (h1>=h2) ? h1-h2 : z-h2+h1;
  float dh21 = (h2>=h1) ? h2-h1 : z-h1+h2;
  float h = (dh21 < dh12) ? h1 + dh21 * amt : h1 - dh12 * amt;
  if (h < 0.0f) h += z;
  else if (h > z) h -= z;
  colorMode(HSB);
  return color(h, lerp(s1, s2, amt), lerp(b1, b2, amt));
}


class BSPTree extends BSPNode {  
  Root root;
  int maxDepth;
  int minSplitDepth;
  float treeScale;
  final float gizmoSize=20;
  boolean minSplitEnabled;
  public BSPTree(int maxDepth, int minSplitDepth, float treeScale) {
    this.maxDepth       =  maxDepth;
    this.treeScale      =  treeScale;
    this.minSplitDepth  =  minSplitDepth;
    root = new Root();
    root.split();
  }
  public void draw() {
    pushMatrix();
    pushStyle();
 
    stroke(255);
    fill(0);

    translate(width/2, height/2);

    //line(0, 0, gizmoSize/2, -gizmoSize/2);
    beginShape(LINES);
    vertex(0, 0);
    vertex(gizmoSize/2, -gizmoSize/2);
    endShape();
    ellipse(0, 0, gizmoSize, gizmoSize);


    fill(255);
    textSize(10);
    text("Root:"+this.toString(), gizmoSize/2, -gizmoSize/2);
    popStyle();
    popMatrix();
  }

  public void reset() {
    //if (    hasChildren) {
    //  children[0].reset();
    //  children[1].reset();
    //}
    //depth=0;
    //hasChildren=false;
    //size = new PVector(1, 1);
    //position = new PVector(0, 0);
    //aspect = random(1)>0.5?true:false;
    //finalSize.x = map(PVector.random2D().x, 0, 1, 0.5, 0.85);
    //finalSize.y = map(PVector.random2D().y, 0, 1, 0.5, 0.85);
    //finalSize.mult(2);
    ////split();
  }
  public void reset(int newMaxDepth) {
    //maxDepth = newMaxDepth;
    //reset();
  }
}
enum NodeType {
  GENERIC, 
    ROOT, 
    BRANCH, 
    LEAF
}
private class Root extends BSPNode {
  private Root() {
    type = NodeType.ROOT;
  }
  public void split(){
    
  }
}
private class Branch extends BSPNode {
  private Branch() {
    type = NodeType.BRANCH;
    hasChildren = false;
  }
  public void split() {
    hasChildren=true;
    //children[0] = new BSPNode(this, true);
    //children[1] = new BSPNode(this, false);
    //depth = parent.depth+1;
    //aspect =! parent.aspect;
    //if (parent.aspect) { //if the parent is tall we divide wide
    //  size = new PVector(
    //    parent.size.x, 
    //    parent.size.y/2f);
    //  if (child) { //top child
    //    position = new PVector(
    //      parent.position.x, 
    //      parent.position.y-(parent.size.y/2f));
    //  } else { //bottom child
    //    position = new PVector(
    //      parent.position.x, 
    //      parent.position.y+(parent.size.y/2f));
    //  }
    //} else { //if the parent is wide we divide tall
    //  size = new PVector(
    //    parent.size.x/2f, 
    //    parent.size.y);
    //  if (child) { //top child
    //    position = new PVector(
    //      parent.position.x-(parent.size.x/2f), 
    //      parent.position.y);
    //  } else { //bottom child
    //    position = new PVector(
    //      parent.position.x+(parent.size.x/2f), 
    //      parent.position.y);
    //  }
    //}
    ////if (depth<maxDepth) {
    ////  if (random(1)>0.25f||depth<minDepth) {
    ////   // split();
    ////  }
    ////}
    //println(info());
  }
}
private class Leaf extends BSPNode {
  private Leaf() {
    hasChildren = false;
    type = NodeType.LEAF;
  }
  public void draw() {
    pushStyle();
    pushMatrix();
      // //fill(0,0,0,0);
      // //noFill();
      // stroke(255);
      // //PVector finalSize = PVector.random2D();
      // //size.dot(finalSize);
      // rect(position.x*treeScale, 
      //   position.y*treeScale, 
      //   size.x*treeScale, 
      //   size.y*treeScale);
      // popStyle();
      // pushStyle();
      // noStroke();
      // fill(255);
      // text(this.toString(), 
      //   position.x*treeScale, 
      //   position.y*treeScale, 
      //   64, 56);
    popMatrix();
    popStyle();
  }
}

private class BSPNode {
  NodeType type = NodeType.GENERIC;
  boolean hasChildren=false;
  PVector size;
  PVector position;
  int depth = 0;
  boolean aspect; //true is tall false is wide
  ArrayList<BSPNode> children;
  private BSPNode() {
    depth = 0;
    size = new PVector(1, 1);
    position = new PVector(0, 0);
    aspect = random(1)>0.5f?true:false;
  }
}
public String info(Object o) {
  StringBuilder result = new StringBuilder();
  String newLine = System.getProperty("line.separator");

  result.append( o.getClass().getName() );
  result.append( " Object {" );
  result.append(newLine);

  //determine fields declared in this class only (no fields of superclass)
  Field[] fields = this.getClass().getDeclaredFields();

  //print field names paired with their values
  for ( Field field : fields  ) {
    result.append("  ");
    try {
      result.append( field.getName() );
      result.append(": ");
      //requires access to private field:
      result.append( field.get(o) );
    } 
    catch ( IllegalAccessException ex ) {
      System.out.println(ex);
    }
    result.append(newLine);
  }
  result.append("}");

  return result.toString();
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "Dungeon" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
