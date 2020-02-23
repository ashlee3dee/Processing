import java.lang.reflect.Field;

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

	void display() {
		root.display();
	}

	void reset() {
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

	void reset(int newMaxDepth) {
		//maxDepth = newMaxDepth;
		//reset();
	}

	private class Root extends BSPNode {

		public Root() {
			type = NodeType.ROOT;
			hasChildren = false;
		}

		void split() {
			println(type + " split");
			super.split(this, NodeType.BRANCH);
		}
	}

	private class Branch extends BSPNode {

		public Branch(BSPNode parent) {
			type = NodeType.BRANCH;
			hasChildren = false;
		}

		void split() {
			println(type + " split");
			super.split(this, NodeType.LEAF);
		}
	}

	private class Leaf extends BSPNode {

		private Leaf() {
			hasChildren = false;
			type = NodeType.LEAF;
		}

		void draw() {
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
			aspect = random(1) >= 0.5;
			children = new ArrayList<BSPNode>();
		}

		private BSPNode(BSPNode parent) {
			depth = 0;
			size = new PVector(1, 1);
			position = new PVector(0, 0);
			aspect = random(1) >= 0.5;
			children = new ArrayList<BSPNode>();
		}

		void split(BSPNode parent, NodeType nt) {
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

		void display() {
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
