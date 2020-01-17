package g4p.tool.controls;

import g4p.tool.G4PTool;
import g4p.tool.TDataConstants;
import g4p.tool.TFileConstants;
import g4p.tool.TGuiConstants;
import g4p.tool.ToolMessages;
import g4p.tool.gui.propertygrid.CtrlPropModel;
import g4p.tool.gui.propertygrid.Validator;
import g4p.tool.gui.tabview.MutableDBase;
import g4p_controls.GCScheme;
import g4p_controls.StyledString;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.tree.DefaultMutableTreeNode;

import processing.app.ui.Editor;

/**
 * Every control in the G4P library has its equivalent class in this package, for instance
 * GButton --> DButton. The classes in this package provide information about the properties
 * available in the property grid for a particular component. <br>
 * If an attribute identifier in one of these classes starts with an underscore followed by a
 * 4 digit numbers then it is to become a property e.g. <pre>_0131_height</pre><br>
 * The 4 digit number is used to decide the order properties should appear in the property
 * grid. <br>
 * The part after the second underscore is the property name and this is used to provide 
 * additional information about the property, so for <pre>_0131_height</pre><br>
 * <pre>height_label</pre> is the text that describes the property in the grid. <br>
 * <pre>height_edit</pre> can the property be edited. <br>
 * <pre>height_show</pre> is the property visible in the grid. <br>
 * <pre>height_tooltip</pre> the tooltip text for the property grid. <br>
 * <pre>height_editor</pre> the object used to edit the property. <br>
 * <pre>height_validator</pre> the object used to validate the proerty value. <br>
 * <pre>height_updater</pre> the name of the method to call after the property has changed. <br>
 * 
 * If an editor and or validator is not specified for each property a default one based on the 
 * data type is provided. <br>
 * 
 * The program uses reflection to get these attributes which are used to create a table
 * model (CtrlPropModel class) for the property grid table (CtrlPropView class). <br>
 * 
 * @author Peter Lager
 *
 */
@SuppressWarnings("serial")
public abstract class DBase extends DefaultMutableTreeNode implements Serializable, TDataConstants, TFileConstants, TGuiConstants {

	/**
	 * This method was added to overcome a problem using the MessageFormat class where
	 * the locale insisted in inserting thousand separators in numbers.
	 * Instead of passing an int to MessageFormat this method means we can pass a String. 
	 */
	protected static String $(int i){
		return String.valueOf(i);
	}
	
	/**
	 * This method was added to overcome a problem using the MessageFormat class where
	 * the locale insisted in inserting thousand separators in numbers.
	 * Instead of passing a float to MessageFormat this method means we can pass a String. 
	 */
	protected static String $(float f){
		return String.valueOf(f);
	}
	
	protected static boolean useRoundCorners = true;
	
	/**
	 * The property model used in the table view
	 */
	transient protected CtrlPropModel propertyModel;

	// Whether it is selectable in the WindowView
	// set to false for DOptionGroup and DTimer
	protected boolean selectable = true;
	// can the control be resized?
	protected boolean resizeable = true;
	// Can the control be moved
	protected boolean moveable = true;

	// The name of the equivalent class in G4P
	public String componentClass = "";

	StyledString name = null;
	
	// Global Colour scheme
	public static int globalColorSchemeID = GCScheme.BLUE_SCHEME; // Blue
	public static String globalColorSchemeName = "BLUE_SCHEME"; // Blue
	public static Color[] globalJpalette = GCScheme.getJavaColor(globalColorSchemeID);

	
	// GText font
    public static Font globalDisplayFont = new Font("Arial", Font.PLAIN, 12);
    public static Font globalInputFont = new Font("Arial", Font.PLAIN, 12);
    public static Font globalSliderFont = new Font("Arial", Font.BOLD, 11);
	
	// Unique id numbers to identify event handlers and used to capture
	// user code.
	public Integer[] id = new Integer[1];
	
	public String 		_0010_name = "APPLICATION";
	public String 		name_label = "Variable Name";
	public String 		name_tooltip = "Use Java naming rules";
	public Boolean 		name_edit = true;
	public Boolean 		name_show = true;
	public Validator 	name_validator = Validator.getValidator(COMPONENT_NAME);
	public String		name_updater = "variableNameChanger";

	public int 			_0820_x = 0;
	public String 		x_label = "X";
	public String 		x_tooltip = "pixels";
	public Boolean 		x_edit = true;
	public Boolean 		x_show = true;
	public Validator 	x_validator = Validator.getValidator(int.class, -9999, 9999);

	public int 			_0821_y = 0;
	public String 		y_label = "Y";
	public String 		y_tooltip = "pixels";
	public Boolean 		y_edit = true;
	public Boolean 		y_show = true;
	public Validator 	y_validator = x_validator;

	public int 			_0826_width = 0;
	public String 		width_label = "Width";
	public String 		width_tooltip = "pixels";
	public Boolean 		width_edit = true;
	public Boolean 		width_show = true;
	public Validator 	width_validator = Validator.getValidator(int.class, 10, 9999);

	public int 			_0827_height = 0;
	public String 		height_label = "Height";
	public String 		height_tooltip = "pixels";
	public Boolean 		height_edit = true;
	public Boolean 		height_show = true;
	public Validator 	height_validator = width_validator;

	public String		_0030_eventHandler = "Event handler";
	public String 		eventHandler_label = "Event method";
	public String 		eventHandler_tooltip = "unique name for event handler method";
	public Boolean 		eventHandler_edit = true;
	public Boolean 		eventHandler_show = true;
	public Validator 	eventHandler_validator = Validator.getValidator(COMPONENT_NAME_0);  // modified 09-02-2013 to allow zero length

	/**
	 * 
	 */
	public DBase(){
		allowsChildren = true;
		id[0] = IdGen.instance().getNext();
	}

	public void variableNameChanger() {
		name = new StyledString(_0010_name);
	}
	
	// ==========================================================================
	// ==========================================================================
	// ===========   Stuff for control declaration and  generation   ============
	// ==========================================================================
	
	/**
	 * Recursive function to get the event methods for all controls
	 * 
	 * @param lines
	 */
	public void make_event_method(ArrayList<String> lines){
		String methodDef = get_event_definition();
		if(methodDef != null)
			lines.add(methodDef);
		if(allowsChildren){
			Enumeration<?> e = children();
			while(e.hasMoreElements()){
				((DBase)e.nextElement()).make_event_method(lines);
			}
		}		
	}

	/**
	 * Recursive function to get the variable declaration.
	 * 
	 * @param lines
	 */
	public void make_declaration(ArrayList<String> lines){
		String decl = get_declaration();
		if(decl != null)
			lines.add(decl);
		if(allowsChildren){
			Enumeration<?> e = children();
			while(e.hasMoreElements()){
				((DBase)e.nextElement()).make_declaration(lines);
			}
		}		
	}

	/**
	 * Make windows.loop() statements
	 * 
	 * @param lines
	 */
	public void make_window_loop(ArrayList<String> lines){
		String loop = get_window_loop();
		if(loop != null)
			lines.add(loop);
		if(allowsChildren){
			Enumeration<?> e = children();
			while(e.hasMoreElements()){
				((DBase)e.nextElement()).make_window_loop(lines);
			}
		}		
	}

	/**
	 * Recursive function to first get the creator code for this
	 * component then repeat for any children. <br>
	 * This method is overridden in DWindow, DPanel and DOptionGroup.
	 * 
	 * @param lines
	 * @param parent the control that has this as a child default = null
	 * @param window the window responsible for drawing this control default = "this" 
	 */
	public void make_creator(ArrayList<String> lines, DBase parent, String window){
		DBase comp;
		Enumeration<?> e;
		String ccode = get_creator(parent, window);
		if(ccode != null && !ccode.equals(""))
			lines.add(ccode);
		if(allowsChildren){
			e = children();
			while(e.hasMoreElements()){
				comp = (DBase)e.nextElement();
				comp.make_creator(lines, this, window);
			}
		}				
	}
	

	// ==========================================================================
	// ==========================================================================
	// =================   Stuff for event code generation   ====================
	// ==========================================================================

	/**
	 * Get the declaration for this control <pre>Foo variable_name;</pre><br>
	 * 
	 * Overridden in :- DWindow
	 */
	protected String get_declaration(){
		return componentClass + " " + _0010_name+ "; \n";
	}

	
	/**
	 * Get the creator statement <pre>var = new Foo(...);</pre><br>
	 * 
	 * Override this method in all classes.
	 */
	protected String get_creator(DBase parent, String window){
		String s = "";
		// Finally add the event handler if appropriate
		if(eventHandler_show && _0030_eventHandler.trim().length() > 0)
			s += ToolMessages.build(ADD_HANDLER, _0010_name, "this", _0030_eventHandler);
		return s;
	}
	
	/** 
	 * Get the generic event method definition including any associated code. <br>
	 * 
	 * 
	 * Overridden in :-  DWindow <br>
	 * 
	 * Modified 09-02-2013 to return null if no method name
	 * 
	 */
	protected String get_event_definition(){
		if(_0030_eventHandler.length() > 0)
			return get_event_header() + get_event_code() + get_event_end();
		else
			return null;
	}
	
	/**
	 * Get the event code if none then return generic message.
	 * 
	 * 
	 * Overridden in :- DTimer
	 * 
	 * @return event handler code
	 */
	protected String get_event_code(){ 
		return get_event_code(0);
	}

	/**
	 * Some controls have more than one event associated with it e.g.
	 * DWindows has 6 one for each of the possible events. <br>
	 * 
	 * This method will get the code associated with one of these id numbers
	 * so this method can get called several times for a control. <br>
	 * 
	 * If it cannot find any code then it will return a generic message. <br>
	 * 
	 * Overridden in :- DWindow
	 * 
	 * @param n the index used for the event id
	 * @return
	 */
	protected String get_event_code(int n){
		if(n < 0 || n >= id.length)
			n = 0;
		String ev_code = Code.instance().get(id[n]);
		if(ev_code == null)
			return ToolMessages.build(CODE_ANY, _0010_name, componentClass);
		else
			return ev_code; 
	}
	
	/**
	 * Get the generic event header for an event<br>
	 * 
	 * 
	 * @return
	 */
	protected String get_event_header(int n){
		if(n < 0 || n >= id.length)
			n = 0;
		return ToolMessages.build(METHOD_START_1, _0030_eventHandler, componentClass, 
				_0010_name, $(id[n])).replace('[', '{');
	}
	
	protected String get_event_header(){
		return get_event_header(0);
	}
	
	
	/**
	 * Get the event method end with tag
	 * @return
	 */
	protected String get_event_end(int n){
		if(n < 0 || n >= id.length)
			n = 0;
		return ToolMessages.build(METHOD_END, _0010_name, 
				$(id[n])).replace(']', '}');
	}
	
	protected String get_event_end(){
		return get_event_end(0);
	}
	
	protected String get_window_loop(){
		return null;
	}
	
	// ==========================================================================
	// ==========================================================================
	// =================   Stuff for code serialisation   =======================
	// ==========================================================================

	protected void read(){
		NameGen.instance().add(_0010_name);
		NameGen.instance().add(_0030_eventHandler);
		for(int i = 0; i < id.length; i++)
			IdGen.instance().add(id[i]);
	}
	
	// ==========================================================================
	// ==========================================================================
	// =================    Stuff for debugging   ===============================
	// ==========================================================================

	/**
	 * Display details - used for debugging only
	 */
	public String show(){
		return ToolMessages.build("{0}  {1} Pos [{2},{3}] Size [{4}, {5}]", this.getClass(), _0010_name, _0820_x, _0821_y, _0826_width, _0827_height);
	}

	/**
	 * Use this to return the name of the component
	 */
	public String toString(){
		return _0010_name;
	}


	// ==========================================================================
	// ==========================================================================
	// ==========    Stuff for drawing controls in design window   ==============
	// ==========================================================================

	/**
	 * Called when the display is painted.
	 * @param g2d Graphics2D context
	 * @param selected currently selected control.
	 */
	public void draw(Graphics2D g2d, DBase selected) { }
	
	
	/**
	 * Called when the control has been changed in the GUI. <br>
	 * Override if needed.
	 * @param action MOVED or RESIZED
	 */
	public void updatedInGUI(int action){ }
	
	/**
	 * Draw a selector if the control is the one being edited.
	 * @param g
	 */
	public void drawSelector(Graphics2D g){
		g.setStroke(stdStroke);
		g.setColor(Color.red);
		g.drawRect(0, 0,_0826_width, _0827_height);
		if(resizeable){
			drawHandle(g, _0826_width - HANDLE_SIZE, (_0827_height - HANDLE_SIZE)/2);
			drawHandle(g, (_0826_width - HANDLE_SIZE) / 2, _0827_height - HANDLE_SIZE);
			drawHandle(g, _0826_width - HANDLE_SIZE, _0827_height - HANDLE_SIZE);
		}
	}
	
	/**
	 * Draw a grab handle to resize the control
	 * @param g
	 * @param x
	 * @param y
	 */
	protected void drawHandle(Graphics2D g, int x, int y){
		g.setColor(Color.white);
		g.fillRect(x, y , HANDLE_SIZE, HANDLE_SIZE);
		g.setColor(Color.red);
		g.drawRect(x, y , HANDLE_SIZE, HANDLE_SIZE);
	}

	/**
	 * Determines whether a position is over the control and if it is whether it is
	 * over the body of the control or a resize handle.
	 * @param m
	 * @param x
	 * @param y
	 */
	public void isOver(MutableDBase m, int x, int y) {
		if(selectable){
			if(! (this instanceof DWindow)){
				x -= _0820_x;
				y -= _0821_y;
			}
			// Pick smallest topmost
			if(getSize() <= m.area && isOverRectangle(x, y, 0, 0, _0826_width, _0827_height)){			
				m.selID = OVER_COMP;
				m.comp = this;
				m.area = getSize();
				if(resizeable){
					if(isOverRectangle(x,y, _0826_width - HANDLE_SIZE, (_0827_height - HANDLE_SIZE)/2, HANDLE_SIZE, HANDLE_SIZE))
						m.selID = OVER_HORZ;
					else if(isOverRectangle(x,y, (_0826_width - HANDLE_SIZE) / 2, _0827_height - HANDLE_SIZE, HANDLE_SIZE, HANDLE_SIZE)) 
						m.selID = OVER_VERT;
					else if(isOverRectangle(x,y, _0826_width - HANDLE_SIZE, _0827_height - HANDLE_SIZE, HANDLE_SIZE, HANDLE_SIZE)) 
						m.selID = OVER_DIAG;
				}
			}
		}
		if(allowsChildren){
			Enumeration<?> e = children();
			while(e.hasMoreElements()){
				((DBase)e.nextElement()).isOver(m, x, y);
			}
		}
	}

	/**
	 * Helper method to see if a position is over a given rectangle.
	 * @param px
	 * @param py
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	protected boolean isOverRectangle(int px, int py, int x, int y, int w, int h){
		return px >= x && px < x + w && py >= y && py < y + h;
	}

	
	// ==========================================================================
	// ==========================================================================
	// ==================   Setters and getters   ===============================
	// ==========================================================================

	public void set_name(String c_name){
		name = new StyledString(c_name);
		_0010_name = c_name;
	}

	public void set_event_name(String e_name){
		_0030_eventHandler = e_name;
	}
	
	public String get_name() { return _0010_name; }
	
	public boolean isSelectable(){
		return selectable;
	}

	public boolean isResizeable(){
		return this.resizeable;
	}
	
	public boolean isMoveable(){
		return moveable;
	}
	
	public void makeTableModel(){
		propertyModel = new CtrlPropModel(this);
	}

	/**
	 * Get the property model for this component. If it does not exist then
	 * create it first.
	 * @return
	 */
	public CtrlPropModel getTableModel(){
		if(propertyModel == null)
			makeTableModel();
		return propertyModel;
	}

	/**
	 * Will return null if no image was loaded.
	 * @param filename
	 * @return
	 */
	public BufferedImage getImageFromDataFolder(String filename){
		BufferedImage img = null;
		Editor editor = G4PTool.base.getActiveEditor();
		File f = new File(editor.getSketch().getDataFolder(), filename);
		try {
			img = ImageIO.read(f);
		} 
		catch (IOException e) {
			img = null;
		}
		return img;
	}
	
	/**
	 * Get the actual area of the control
	 * @return
	 */
	public int getSize(){
		return _0826_width * _0827_height;
	}

	/**
	 * Simple class to used to define text and icon zones in this type of control
	 * @author Peter Lager
	 *
	 */
	class Zone implements Serializable {

		public String id;
		public int x, y, w, h;

		public Zone(){
			x = y = w = h = 0;
		}
		
		public Zone(String id){
			this.id = id;
			x = y = w = h = 0;
		}
		
		public Zone(int x, int y, int w, int h) {
			super();
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}

		public void clear(){
			x = y = w = h = 0;
		}
		
		public String toString(){
			return id + " Zone   " + x + "   " + y + "   " + w + "   " + h;
		}
		
	}
}
