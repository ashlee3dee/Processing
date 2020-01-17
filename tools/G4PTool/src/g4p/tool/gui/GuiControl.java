package g4p.tool.gui;

import g4p.tool.G4PTool;
import g4p.tool.TDataConstants;
import g4p.tool.TFileConstants;
import g4p.tool.controls.Code;
import g4p.tool.controls.DApplication;
import g4p.tool.controls.DBase;
import g4p.tool.controls.DWindow;
import g4p.tool.controls.IdGen;
import g4p.tool.controls.NameGen;
import g4p.tool.gui.propertygrid.CtrlPropView;
import g4p.tool.gui.tabview.CtrlTabView;
import g4p.tool.gui.treeview.CtrlSketchModel;
import g4p.tool.gui.treeview.CtrlSketchView;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import processing.app.Base;
import processing.app.Sketch;
import processing.app.SketchCode;
import processing.app.Util;
import processing.app.ui.Editor;
import processing.core.PApplet;

/**
 * Provides main functionality for using the tool.
 * 
 * Includes code generation functionality and event handling code capture.
 * 
 * @author Peter Lager
 *
 */
public class GuiControl implements TFileConstants, TDataConstants {

	private Editor editor = null;

	private CtrlTabView tabs;
	private CtrlSketchView tree;
	private CtrlPropView props;

	private String guiPdeBase = "";

	private Pattern pCode, pSize;
	private Matcher m;

	/**
	 * @param tabs
	 * @param tree
	 * @param props
	 */
	public GuiControl(Editor editor, CtrlTabView tabs, CtrlSketchView tree, CtrlPropView props) {
		super();
		this.editor = editor;
		this.tabs = tabs;
		this.tree = tree;
		this.props = props;
		if(editor != null){
			try {
				File f = new File(Base.getSketchbookFolder() + SEP + GUI_PDE_BASE);
				guiPdeBase = Util.loadFile(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		pCode = Pattern.compile(CODE_TAG, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		pSize = Pattern.compile(SK_SIZE, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
	}

	public boolean addComponent(DBase comp){
		tree.addComponent(comp);
		return true;
	}

	public boolean removeComponent(){
		tree.removeComponent();
		return true;
	}

	public void setScale(int scale){
		tabs.scaleWindow(scale);
	}

	public void showGrid(boolean show){
		tabs.setShowGrid(show);
	}

	public void snapGrid(boolean snap){
		tabs.setSnapToGrid(snap);
	}

	public void setGridSize(int gs){
		tabs.setGridSize(gs);
	}

	/**
	 * Set the sketch size in the designer window if one is provided
	 * @param sst width and height
	 */
	public void setSketchSize(SketchSizeType sst){
		if(sst != null && !G4PTool.DEBUG){
			DWindow m = (DWindow) tree.getRoot().getChildAt(0);
			if(m._0826_width != sst.width && sst.width > 0){
				m._0826_width = sst.width;
			}
			if(m._0827_height != sst.height && sst.height > 0){
				m._0827_height = sst.height;
			}
			if(!m._0031_renderer.equals(sst.type)){
				m._0031_renderer = sst.type;
			}
			props.modelHasBeenChanged();
			tabs.updateCurrentTabName();
			m.width_edit = false;
			m.height_edit = false;
			m.renderer_edit = false;
			props.updateModelFor(m);		
			tabs.revalidate();
		}
	}

	/**
	 * Get the size of the sketch from the code
	 * @return null if no size found
	 */
	public SketchSizeType getSketchSizeFromCode(){
		if(G4PTool.DEBUG) {
			tabs.repaint();
			return new SketchSizeType("480", "320", "JAVA2D");
		}
		SketchSizeType s = null;
		Sketch sketch = editor.getSketch();
		SketchCode curr =  sketch.getCurrentCode();
		int currIndex = getTabIndex(sketch, curr.getPrettyName());
		sketch.setCurrentCode(0);
		String code = editor.getText();
		code = scrubComments(code);
		m = pSize.matcher(code);
		if(m.find() && m.groupCount() >= 3){
			s = new SketchSizeType(m.group(1), m.group(2), m.group(3));
		}
		sketch.setCurrentCode(currIndex);
		tabs.repaint();
		return s;
	}

	/**
	 * Replace all commented portions of a given String as spaces. <br>
	 * Copied from processing.mode.java.preproc.PdePreprocessor; <br>
	 * so that it will be compatible with 1.5.1
	 * 
	 */
	public String scrubComments(String what) {
		char p[] = what.toCharArray();

		int index = 0;
		while (index < p.length) {
			// for any double slash comments, ignore until the end of the line
			if ((p[index] == '/') &&
					(index < p.length - 1) &&
					(p[index+1] == '/')) {
				p[index++] = ' ';
				p[index++] = ' ';
				while ((index < p.length) &&
						(p[index] != '\n')) {
					p[index++] = ' ';
				}

				// check to see if this is the start of a new multiline comment.
				// if it is, then make sure it's actually terminated somewhere.
			} else if ((p[index] == '/') &&
					(index < p.length - 1) &&
					(p[index+1] == '*')) {
				p[index++] = ' ';
				p[index++] = ' ';
				boolean endOfRainbow = false;
				while (index < p.length - 1) {
					if ((p[index] == '*') && (p[index+1] == '/')) {
						p[index++] = ' ';
						p[index++] = ' ';
						endOfRainbow = true;
						break;

					} else {
						// continue blanking this area
						p[index++] = ' ';
					}
				}
				if (!endOfRainbow) {
					throw new RuntimeException("Missing the */ from the end of a " +
							"/* comment */");
				}
			} else {  // any old character, move along
				index++;
			}
		}
		return new String(p);
	}

	/**
	 * Capture user code in the event handlers
	 */
	public void codeCapture(){
		if(G4PTool.DEBUG) {
			return;
		}
		Sketch sketch = editor.getSketch();
		int currTab = sketch.getCurrentCodeIndex();
		SketchCode gui_tab = getTab(sketch, PDE_TAB_PRETTY_NAME);
		int gui_tab_index = sketch.getCodeIndex(gui_tab);
		sketch.setCurrentCode(gui_tab_index);
		//String code = gui_tab.getProgram();
		String code = editor.getText();
		gui_tab.setProgram(code);
		try {
			gui_tab.save();
		} catch (IOException e) {
		}
		//pCode.matcher(code);
		m = pCode.matcher(code);
		ArrayList<CodeTag> tags = new ArrayList<CodeTag>();
		while(m.find()){
			String[] sa = m.group().split(":");
			tags.add(new CodeTag(sa[2], m.start(), m.end()));
		}
		// Validate tags???
		Code.instance().reset();
		for(int t = 0; t < tags.size(); t += 2){
			String snippet = code.substring(tags.get(t).e + 1, tags.get(t+1).s - 2);
			Code.instance().add(tags.get(t).id, snippet);
		}
		sketch.setCurrentCode(currTab);
	}

	/**
	 * Create the code when then the editor loses focus.
	 */
	public void codeGeneration(){
		String code;
		Sketch sketch = editor.getSketch();
		int currTab = sketch.getCurrentCodeIndex();
		SketchCode gui_tab = getTab(sketch, PDE_TAB_PRETTY_NAME);
		int gui_tab_index = sketch.getCodeIndex(gui_tab);
		sketch.setCurrentCode(gui_tab_index);
		// Generate code here then save using editor.
		code = makeGuiCode();
		// Set the tab code and save it
		gui_tab.setProgram(code);
		// Set the code to show in the editor
		editor.setText(code);
		editor.setSelection(0, 0);
		sketch.setModified(true);

		// See if the first tab has text if not create some basic code
		sketch.setCurrentCode(0);
		String code0 = editor.getText();
		if(code0 != null && code0.length() == 0){
			SketchCode tab0 = sketch.getCurrentCode();
			try {
				File f = new File(Base.getSketchbookFolder() + SEP + TAB0_PDE_BASE);
				String tab0code = Util.loadFile(f);
				Dimension size = tree.getSketchSizeFromDesigner();
				String graphics = tree.getSketchRendererFromDesigner();
				tab0code = tab0code.replace("WIDTH", "" + size.width);
				tab0code = tab0code.replace("HEIGHT", "" + size.height);
				tab0code = tab0code.replace("RENDERER", graphics);
				editor.setText(tab0code);
				tab0.setProgram(tab0code);
				sketch.setModified(true);
			}
			catch(Exception excp){
			}
		}
		editor.repaint();
		sketch.setCurrentCode(currTab);
	}

	private String makeGuiCode(){
		String code = "";
		ArrayList<String> evtMethods = new ArrayList<String>();	
		ArrayList<String> compCreators = new ArrayList<String>();
		ArrayList<String> compDecs = new ArrayList<String>();

		evtMethods.add(guiPdeBase);

		compCreators.add("\n\n");
		compCreators.add("// Create all the GUI controls. \n");
		compCreators.add("// autogenerated do not edit\n");
		compCreators.add("public void createGUI(){\n");

		compDecs.add("// Variable declarations \n");
		compDecs.add("// autogenerated do not edit\n");

		tree.generateDeclarations(compDecs);
		tree.generateEvtMethods(evtMethods);
		tree.generateCreator(compCreators);

		// Close the create method
		compCreators.add("}\n\n");

		// Build up full sketch code
		evtMethods.addAll(compCreators);
		evtMethods.addAll(compDecs);
		String[] lines = evtMethods.toArray((new String[compCreators.size()]));
		code = PApplet.join(lines, "");
		return code;
	}

	/**
	 * For a particular sketch get the SketchCode for a given
	 * tab name.
	 * 
	 * @param s
	 * @param tabName
	 * @return
	 */
	private SketchCode getTab(Sketch s, String tabName){
		SketchCode[] tabs = s.getCode();
		SketchCode gui = null;
		for(SketchCode sc : tabs){
			if(sc.getPrettyName().equals(tabName)){
				gui = sc;
				break;
			}
		}
		return gui;
	}

	/**
	 * For a particular sketch get the index number of a tab
	 * with a given name. 
	 * 
	 * @param s
	 * @param tabName
	 * @return
	 */
	private int getTabIndex(Sketch s, String tabName){
		SketchCode[] tabs = s.getCode();
		int index = -1;
		for(int i = 0; i < tabs.length; i++){
			if(tabs[i].getPrettyName().equals(tabName)){
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * This saves the GUI (tree) model) layout using serialisation.
	 */
	public void saveGuiLayout() {
		File file;
		// Editor == null if run outside of Processing
		if(editor == null){
			file = new File(GUI_MODEL_FILE);
		}
		else {
			file = new File(editor.getSketch().getFolder(), GUI_MODEL_FILE);
		}
		tree.saveModel(file);
	}

	/**
	 * This method loads the serialised GUI layout (tree model) if available
	 * otherwise it creates a blank model.
	 */
	public void loadGuiLayout() {
		DefaultTreeModel dm = null;
		File file = new File(editor.getSketch().getFolder(), GUI_MODEL_FILE);

		if(file.exists()){
			NameGen.instance().reset();
			IdGen.instance().reset();
			dm = tree.loadModel(file);
		}
		else {
			dm = getBaseSketchModel();
		}
		makeGUIfromTreeModel((CtrlSketchModel) dm);
	}

	/**
	 * This method is to prove that the entire GUI can be
	 * created from a Tree data model 
	 * @param m
	 */
	private void makeGUIfromTreeModel(CtrlSketchModel m) {
		tabs.deleteAllWindows();
		// Create Tree view
		tree.setModel((TreeModel)m);
		// Get start node
		DBase app = (DBase) m.getRoot();
		DBase mainDisplay = (DBase) app.getChildAt(0);
		// Get root and initialise the property view
		// Setup window display
		// Create tabbed pane for each window
		Enumeration<?> windows = ((DBase) m.getRoot()).children();
		while(windows.hasMoreElements()){
			DBase win = (DBase) windows.nextElement();
			tabs.addWindow(win);
		}
		props.showProprtiesFor(mainDisplay);
		tree.setSelectedComponent(mainDisplay);
		tabs.setSelectedComponent(mainDisplay);
	}

	/**
	 * Create a blank sketch.
	 * Starts with a DAplication for the root and a single DWindow child to represent
	 * the main
	 * @return
	 */
	private CtrlSketchModel getBaseSketchModel() {
		CtrlSketchModel m = null;
		DApplication app = new DApplication();
		DWindow win1 = new DWindow(true);
		win1._0826_width = 480;
		win1._0827_height = 320;
		app.add(win1);
		m = new CtrlSketchModel(app);
		return m;
	}

	public class CodeTag {
		public Integer id;
		public int s, e;

		/**
		 * @param name
		 * @param s
		 * @param e
		 */
		public CodeTag(String name, Integer s, Integer e) {
			super();
			this.id = Integer.parseInt(name);
			this.s = s;
			this.e = e;
		}
	} // End of code tag class

}
