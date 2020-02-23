import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.core.PApplet; 
import java.lang.reflect.Field; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 





public class thing extends PApplet {
	boolean recording = false; // A boolean to track whether we are recording are not
	float timeScale = 0.0005f; //global amount to scale millis() for all animations
	float currentTime = 0;
	String title = getClass().getSimpleName();
	BSPTree dungeon;

	public @Override
	void settings() {
		size(800, 800, P3D);
	}

	public @Override
	void setup() {
		frameRate(30); //all code should be frame-rate independent. hint: use millis()
		smooth(2); //enable highest level of anti-aliasing your system can handle
		ellipseMode(CENTER);
		rectMode(CENTER);
		imageMode(CENTER);
		blendMode(BLEND);
		colorMode(HSB);
		background(0, 0, 0);
		dungeon = new BSPTree(4, 2, width);
	}

	public @Override
	void draw() {
		tick(); //no touch
		background(0);
		//dungeon.reset();
		dungeon.display();
		tock(); //no touch
	}

	/**
 ================================
 Mandatory Code
 ================================
 **/
	public @Override
	void keyPressed() {
		if (key == 'r' || key == 'R') {
			recording = !recording;
		}
		if (key == 'j' || key == 'J') {
			println(
				"################################RESET################################"
			);
			//dungeon.reset();
			//dungeon.split();
			//println(dungeon.info());
			//dungeon.split();
			//dungeon.draw();
			//saveFrame("/output/"+title+"_frame_####.png");
		}
	}

	public void tick() {
		currentTime = millis() * timeScale;
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

	public void drawFPS(int textColor) {
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
		float dh12 = (h1 >= h2) ? h1 - h2 : z - h2 + h1;
		float dh21 = (h2 >= h1) ? h2 - h1 : z - h1 + h2;
		float h = (dh21 < dh12) ? h1 + dh21 * amt : h1 - dh12 * amt;
		if (h < 0.0f) h += z; else if (h > z) h -= z;
		colorMode(HSB);
		return color(h, lerp(s1, s2, amt), lerp(b1, b2, amt));
	}
}


class BSPTree {
	Root root;
	int maxDepth;
	int minSplitDepth;
	float treeScale;
	final float gizmoSize = 20;
	boolean minSplitEnabled;
	String name = new String("name");

	public BSPTree(int maxDepth, int minSplitDepth, float treeScale) {
		println("bsptree created");
		this.maxDepth = maxDepth;
		this.treeScale = treeScale;
		this.minSplitDepth = minSplitDepth;
		root = new Root();
		//root.split();
	}

	public void display() {
		root.display();
	}

	public void reset() {
		//if (    hasChildren) {
		//  children[0].reset();
		//  children[1].r eset();
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

	private class Root extends BSPNode {

		public Root() {
			type = NodeType.ROOT;
			hasChildren = false;
		}

		public void split() {
			println(type + " split");
			super.split(this, NodeType.BRANCH);
		}
	}

	private class Branch extends BSPNode {

		public Branch(BSPNode parent) {
			type = NodeType.BRANCH;
			hasChildren = false;
		}

		public void split() {
			println(type + " split");
			super.split(this, NodeType.LEAF);
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
		NodeType type = NodeType.EMPTY;
		boolean hasChildren = false;
		PVector size;
		PVector position;
		int depth = 0;
		boolean aspect; //true is tall false is wide
		float splitRatio = 0.5f;
		float gizmoSize = 20;
		ArrayList<BSPNode> children;

		private BSPNode() {
			depth = 0;
			size = new PVector(1, 1);
			position = new PVector(0, 0);
			aspect = random(1) >= 0.5f ? true : false;
			children = new ArrayList<BSPNode>();
		}

		private BSPNode(BSPNode parent) {
			depth = 0;
			size = new PVector(1, 1);
			position = new PVector(0, 0);
			aspect = random(1) >= 0.5f ? true : false;
			children = new ArrayList<BSPNode>();
		}

		public void split(BSPNode parent, NodeType nt) {
			hasChildren = true;
			BSPNode c1 = new BSPNode(this);
			BSPNode c2 = new BSPNode(this);
			c1.position = PVector.random2D();
			c2.position = PVector.random2D();
			c1.type = nt;
			c2.type = nt;
			children.add(c1);
			children.add(c2);
			depth = parent.depth + 1;
			aspect = !parent.aspect;
			// if (parent.aspect) { //if the parent is tall we divide wide
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
			// } else { //if the parent is wide we divide tall
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
			// }
		}

		public void display() {
			if (hasChildren) {
				for (BSPNode bsp : children) {
					bsp.display();
				}
			} else {
				drawGizmo();
			}
		}

		public void drawGizmo() {
			pushMatrix();
			pushStyle();

			stroke(255);
			fill(0);

			translate(width / 2, height / 2);
			//translate(position.x * (width), position.y * (height));
			//line(0, 0, gizmoSize/2, -gizmoSize/2);
			beginShape(LINES);
			vertex(0, 0);
			vertex(gizmoSize / 2, -gizmoSize / 2);
			endShape();
			ellipse(0, 0, gizmoSize, gizmoSize);

			fill(255);
			textSize(10);
			text("Node:" + this.toString(), gizmoSize / 2, -gizmoSize / 2);
			popStyle();
			popMatrix();
		}
	}

	public String info(Object o) {
		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");

		result.append(o.getClass().getName());
		result.append(" Object {");
		result.append(newLine);

		//determine fields declared in this class only (no fields of superclass)
		Field[] fields = this.getClass().getDeclaredFields();

		//print field names paired with their values
		for (Field field : fields) {
			result.append("  ");
			try {
				result.append(field.getName());
				result.append(": ");
				//requires access to private field:
				result.append(field.get(o));
			} catch (IllegalAccessException ex) {
				System.out.println(ex);
			}
			result.append(newLine);
		}
		result.append("}");

		return result.toString();
	}
}

enum NodeType {
	EMPTY,
	ROOT,
	BRANCH,
	LEAF
}
