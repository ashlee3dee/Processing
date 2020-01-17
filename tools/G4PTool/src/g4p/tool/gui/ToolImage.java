package g4p.tool.gui;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Keeps a reference to all the 
 * @author Peter Lager
 *
 */
public class ToolImage {

	private static HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	
	
	public static void addImage(String n, BufferedImage icon){
		images.put(n, icon);
	}

	public static BufferedImage getImage(String n){
		return images.get(n);
	}

}
