/**
 * GUI form designer for G4P.
 *
 * (C) 2019
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author		Peter Lager http://www.lagers.org.uk
 * @modified	02/22/2020
 * @version		##version##
 */
package g4p.tool;

import java.awt.Color;
import java.io.File;

import javax.swing.JFrame;

import g4p.tool.gui.GuiDesigner;
import g4p_controls.GCScheme;
import processing.app.Base;
import processing.app.Messages;
import processing.app.Sketch;
import processing.app.tools.Tool;
import processing.app.ui.Editor;

/**
 * 
 * @author Peter Lager
 *
 */
public class G4PTool implements Tool, TFileConstants {
	
	public static boolean DEBUG = false;

	public static Color[] cscheme = { new Color(0,0,0) };
	
	// Keep track of the base and uses this to get the editor
	public static Base base;
	// keep track of the FUI designer for this sketch
	private static GuiDesigner dframe;

	private boolean g4p_error_shown = false;


	public String getMenuTitle() {
		return "G4P GUI builder";
	}

	/**
	 * Get version string of this tool
	 * @return revision number string
	 */
	public static String getVersion(){
		return "4.4.1";
	}

	/**
	 * Get compatible version string of this tool
	 * @return revision number string
	 */
	public static String getCompatibleVersionNo(){
		String n[] = "4.4.1".split("[\\.]");
		return n[0] + "." + n[1];
	}

	/**
	 * Get version number of this tool as an integer with the form  <br>
	 * MMmmii <br>
	 * M = Major revision <br>
	 * m = minor revision <br>
	 * i = internal revision <br>
	 * @return version number as int
	 */
	public static int getVersionNo(){
		String n[] = "4.4.1".split("[\\.]");
		int[] vnp = new int[3];
		for(int i = 0; i < n.length; i++){
			try {
				vnp[i] = Integer.parseInt(n[i]);
			}
			catch(Exception excp){
			}
		}
		return ((vnp[0] * 100) + vnp[1]) * 100 + vnp[2];
	}

	public void clearFrame(){
		dframe = null;
	}
	
	/**
	 * Called once first time the tool is called
	 */
	@Override
	public void init(Base theBase) {
		base = theBase;
	}
	
	/**
	 * This is executed every time we launch the tool using the menu item in Processing IDE
	 * 
	 */
	public void run() {
		Editor editor = base.getActiveEditor();
		boolean terminateTool = false;
		GCScheme.makeColorSchemes();

		Sketch sketch = editor.getSketch();
		File sketchFolder = sketch.getFolder();
		File sketchbookFolder = Base.getSketchbookFolder();
		boolean thisVersion = false, anotherVersion = false;

		// Provide a warning (first time only) if G4P is not loaded
		if (!g4p_error_shown && !g4pJarExists(Base.getSketchbookLibrariesFolder())) {
			Messages.showWarning("GUI Builder warning", 
					"Although you can use this tool the sketch created will not \nwork because the G4P library needs to be installed.\nSee G4P at http://www.lagers.org.uk/g4p/",
					(Exception) null);
			g4p_error_shown = true;
		}
		// The tool is not open so create the designer window
		if (dframe == null) { // Design window does not exist
			
			System.out.println("===================================================");
			System.out.println("   G4P GUI Builder 4.4.1 created by Peter Lager");
			System.out.println("===================================================");

			// If the gui.pde tab does not exist create it
			if (!guiTabExists(sketch)) {
				File source = new File(sketchbookFolder, G4P_TOOL_DATA_FOLDER + SEP + PDE_TAB_NAME);
				sketch.addFile(source);
			}
			// Create data folder if necessary
			sketch.prepareDataFolder();

			// Create a sub-folder called 'GUI_BUILDER_DATA' inside the sketch folder if
			// it doesn't already exist
			File configFolder = new File(sketchFolder, CONFIG_FOLDER);
			if (!configFolder.exists()) {
				configFolder.mkdir();
			}
			else { // if it exists then get list of existing guis
				File[] files = configFolder.listFiles();
				for(File f : files){
					String name = f.getName();
					if(name.equals(GUI_MODEL_FILENAME)){
						// GUI that matches this version
						thisVersion = true;
					}
					else if(name.startsWith(GUI_MODEL_FILE_FILTER)){
						// GUI that matches another version
						anotherVersion = true;
					}
				}
			}
			// Terminate tool if we can find a GUI for another version but not for this one
			terminateTool = !thisVersion & anotherVersion;
			if(terminateTool){
				Messages.showWarning("GUI Builder Version Error", "GUI Builder will close because the GUI found was \ncreated with an earlier version of GUI Builder\n", null);
			}
			else {				
				dframe = new GuiDesigner(this);
				dframe.setVisible(true);
				dframe.setExtendedState(JFrame.NORMAL);
				dframe.toFront();
			}
		}
		else { // Design window exists so make visible, open to normal size and bring to front.
			dframe.setVisible(true);
			dframe.setExtendedState(JFrame.NORMAL);
			dframe.toFront();
		}
	}

	/**
	 * See if the G4P library has been installed in the SketchBook libraries folder correctly
	 * @param sketchbookLibrariesFolder
	 * @return true if found else false
	 */
	private boolean g4pJarExists(File sketchbookLibrariesFolder) {
		File f = new File(sketchbookLibrariesFolder, G4P_LIB);
		boolean exists = f.exists();
		return exists;
	}

	/**
	 * See if the gui.pde tab has been created already if not
	 * @param sketch
	 * @return
	 */
	private boolean guiTabExists(Sketch sketch) {
		File f = new File(sketch.getFolder(), PDE_TAB_NAME);
		return f.exists();
	}

//	public static void main(String[] args) {
//		DEBUG = true;
//		System.out.println("G4PTool Debug mode");
//		dframe = new GuiDesigner();
//		dframe.setVisible(true);
//		dframe.setExtendedState(JFrame.NORMAL);
//		dframe.toFront();
//
//	}

}
