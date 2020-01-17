package g4p.tool;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

public class G {

	
	private static LinkedList<AffineTransform> stack = new LinkedList<AffineTransform>();
	
	public static void pushMatrix(Graphics2D g){
		stack.addLast(g.getTransform());
	}
	
	public static void popMatrix(Graphics2D g){
		AffineTransform aff = stack.removeLast();
		g.setTransform(aff);
	}
}
