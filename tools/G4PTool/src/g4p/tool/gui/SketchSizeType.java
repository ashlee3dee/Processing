package g4p.tool.gui;

/**
 * Used to store details of the size and renderer of the sketch.
 * 
 * @author Peter Lager
 *
 */
class SketchSizeType {
	public final int width;
	public final int height;
	public final String type;


	public SketchSizeType(String ws, String hs, String ts){
		int w, h;
		try {
			w = Integer.parseInt(ws);	
		}
		catch(NumberFormatException e){
			w = 0;
		}
		width = w;
		try {
			h = Integer.parseInt(hs);	
		}
		catch(NumberFormatException e){
			h = 0;
		}
		height = h;
		String t = "JAVA2D";
		if(ts.startsWith("P2D"))
			t = "P2D";
		else if(ts.startsWith("P3D"))
			t = "P3D";
		else if(ts.startsWith("OPENGL"))
			t = "OPENGL";
		type = t;	
	}

}
