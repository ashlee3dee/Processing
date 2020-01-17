package g4p.tool;

import java.io.File;

public interface TFileConstants {

	public final String SEP = File.separator;
	
	// Relative to sketch folder
	public final String PDE_TAB_PRETTY_NAME 	= "gui";
	public final String PDE_TAB_NAME 			= PDE_TAB_PRETTY_NAME + ".pde";
	public final String CONFIG_FOLDER 			= "GUI_BUILDER_DATA";
	
	public final String GUI_MODEL_FILE_FILTER	= "gui.ser.";
	public final String GUI_MODEL_FILENAME 		= GUI_MODEL_FILE_FILTER + G4PTool.getCompatibleVersionNo();
	public final String GUI_MODEL_FILE 			= CONFIG_FOLDER + SEP + GUI_MODEL_FILENAME;
	
	// These are relative to the processing sketch folder
	public final String G4P_TOOL_DATA_FOLDER 	= "tools" + SEP + "G4PTool" + SEP + "data";
	public final String G4P_LIB 				= "G4P" + SEP + "library" + SEP + "G4P.jar";
	public final String GUI_PDE_BASE			= G4P_TOOL_DATA_FOLDER + SEP + "gui_base.txt";
	public final String TAB0_PDE_BASE			= G4P_TOOL_DATA_FOLDER + SEP + "tab0.txt";
}
