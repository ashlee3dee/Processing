package g4p.tool.controls;

import g4p.tool.G;
import g4p.tool.ToolMessages;
import g4p.tool.gui.propertygrid.EditorBase;
import g4p.tool.gui.propertygrid.EditorJComboBox;
import g4p.tool.gui.propertygrid.Validator;
import g4p.tool.gui.tabview.CtrlTabView;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Enumeration;

@SuppressWarnings("serial")
public final class DWindow extends DBase {

	// Set to true for main skecth and false for secondary windows
	public boolean 		mainSketch = false;

	public String 		_0012_title = "";
	public String 		title_label = "Frame title text";

	public String 		_0031_renderer = "JAVA2D";
	public String 		renderer_label = "Renderer";
	transient public EditorBase renderer_editor = new EditorJComboBox(RENDERER);
	public Boolean 		renderer_edit = true;
	public Boolean 		renderer_show = true;

	public String 		_0032_close_action = "KEEP_OPEN";
	public String 		close_action_label = "Action on close";
	transient public EditorBase close_action_editor = new EditorJComboBox(CLOSE_ACTION);
	public Boolean 		close_action_edit = false;
	public Boolean 		close_action_show = false;

	public String		_0008_display_scale = "100";
	public String		display_scale_label = "Scale (%)";
	transient public EditorBase display_scale_editor = new EditorJComboBox(SCALE);
	public Boolean 		display_scale_edit = true;
	public Boolean 		display_scale_show = true;
	public String 		display_scale_updater = "displayScaleChange";

	public String 		_0020_wdraw = "";
	public String 		wdraw_label = "Draw method";
	public String 		wdraw_tooltip = "The draw() method for this window";
	public Boolean 		wdraw_edit = true;
	public Boolean 		wdraw_show = true;
	public Validator 	wdraw_validator = Validator.getValidator(COMPONENT_NAME_0);

	public String 		_0021_wmouse = "";
	public String 		wmouse_label = "Mouse method";
	public String 		wmouse_tooltip = "The mouseEvent() method for this window";
	public Boolean 		wmouse_edit = true;
	public Boolean 		wmouse_show = true;
	public Validator 	wmouse_validator = Validator.getValidator(COMPONENT_NAME_0);

	public String 		_0022_wkey = "";
	public String 		wkey_label = "Key method";
	public String 		wkey_tooltip = "The keyEvent() method for this window";
	public Boolean 		wkey_edit = true;
	public Boolean 		wkey_show = true;
	public Validator 	wkey_validator = Validator.getValidator(COMPONENT_NAME_0);

	public String 		_0023_wpre = "";
	public String 		wpre_label = "Pre method";
	public String 		wpre_tooltip = "The pre() method for this window";
	public Boolean 		wpre_edit = true;
	public Boolean 		wpre_show = true;
	public Validator 	wpre_validator = Validator.getValidator(COMPONENT_NAME_0);

	public String 		_0024_wpost = "";
	public String 		wpost_label = "Post method";
	public String 		wpost_tooltip = "The post() method for this window";
	public Boolean 		wpost_edit = true;
	public Boolean 		wpost_show = true;
	public Validator 	wpost_validator = Validator.getValidator(COMPONENT_NAME_0);

	public String 		_0025_wclose = "";
	public String 		wclose_label = "On Close method";
	public String 		wclose_tooltip = "Method to call when window is closed";
	public Boolean 		wclose_edit = true;
	public Boolean 		wclose_show = true;
	public Validator 	wclose_validator = Validator.getValidator(COMPONENT_NAME_0);


	/**
	 * Create a Window object
	 * @param mainSketch true if main sketch
	 */
	public DWindow(boolean mainSketch) {
		super();
		id = new Integer[6];
		for(int i = 0; i < id.length; i++)
			id[i] = IdGen.instance().getNext();

		selectable = true;
		resizeable = false;
		moveable = false;
		allowsChildren = true;
		componentClass = "GWindow";
		this.mainSketch = mainSketch;

		if(mainSketch){
			_0010_name = "Main window";
			name_label = "SKETCH";
			name_edit = false;
			x_edit = y_edit = false;
			x_show = y_show = false;	
			eventHandler_show = false;
			_0826_width = 600;
			_0827_height = 400;
			_0012_title = "Sketch Window";
			wdraw_edit = wdraw_show = false;
			wmouse_edit = wmouse_show = false;
			wkey_edit = wkey_show = false;
			wpre_edit = wpre_show = false;
			wpost_edit = wpost_show = false;
			wclose_edit = wclose_show = false;
		}
		else {
			set_name(NameGen.instance().getNext("window"));
			name_label = "Variable name";
			name_edit = true;
			x_edit = y_edit = true;
			x_show = y_show = true;	
			close_action_edit = close_action_show = true;
			_0826_width = 240;
			_0827_height = 120;
			_0012_title = "Window title";
			renderer_edit = renderer_show = true;
			// Create a draw method for this window
			_0020_wdraw = NameGen.instance().getNext("win_draw");
		}
		width_edit = height_edit = true;
		width_show = height_show = true;
		eventHandler_show = false;
		resizeable = false;
	}

	public void displayScaleChange(){
		propertyModel.hasBeenChanged();
	}

	/**
	 * Completely overrides version in DBase
	 */
	protected String get_event_definition(){
		StringBuilder sb = new StringBuilder();

		if(_0020_wdraw.length() > 0){
			sb.append(ToolMessages.build(WIN_DRAW, _0020_wdraw, _0010_name, $(id[0])).replace('[', '{'));  // event header
			sb.append(get_event_code(0) + get_event_end(0));
		}
		if(_0021_wmouse.length() > 0){
			sb.append(ToolMessages.build(WIN_MOUSE, _0021_wmouse, _0010_name, $(id[1])).replace('[', '{'));  // event header
			sb.append(get_event_code(1) + get_event_end(1));
		}
		if(_0022_wkey.length() > 0){
			sb.append(ToolMessages.build(WIN_KEY, _0022_wkey, _0010_name, $(id[2])).replace('[', '{'));  // event header
			sb.append(get_event_code(2) + get_event_end(1));
		}
		if(_0023_wpre.length() > 0){
			sb.append(ToolMessages.build(WIN_PRE, _0023_wpre, _0010_name, $(id[3])).replace('[', '{'));  // event header
			sb.append(get_event_code(3) + get_event_end(3));
		}
		if(_0024_wpost.length() > 0){
			sb.append(ToolMessages.build(WIN_POST, _0024_wpost, _0010_name, $(id[4])).replace('[', '{'));  // event header
			sb.append(get_event_code(4) + get_event_end(4));
		}
		if(_0025_wclose.length() > 0){
			sb.append(ToolMessages.build(ON_CLOSE, _0025_wclose, _0010_name, $(id[5])).replace('[', '{'));  // event header
			sb.append(get_event_code(5) + get_event_end(5));
		}
		return new String(sb);
	}

	/**
	 * Get the event code if none then return generic message
	 * @param n 0-3 depending on method
	 * @return
	 */
	protected String get_event_code(int n){
		String ev_code = Code.instance().get(id[n]);
		if(ev_code == null){
			switch(n){
			case 0:
				ev_code = CODE_GWINDOW_DRAW;
				break;
			case 1:
				ev_code = ToolMessages.build(CODE_GWINDOW_MOUSE, _0010_name);
				break;
			case 2:
				ev_code = ToolMessages.build(CODE_GWINDOW_KEY, _0010_name);
				break;
			case 3:
				ev_code = ToolMessages.build(CODE_GWINDOW_PRE, _0010_name);
				break;
			case 4:
				ev_code = ToolMessages.build(CODE_GWINDOW_POST, _0010_name);
				break;
			case 5:
				ev_code = ToolMessages.build(CODE_GWINDOW_CLOSE, _0010_name);
				break;
			}
		}
		return ev_code;
	}

	/**
	 * Get the declaration for this window
	 */
	protected String get_declaration(){
		if(mainSketch)
			return null;
		else
			return componentClass + " " + _0010_name+ ";\n";
	}

	/**
	 * Get the creator statement var = new Foo(...);
	 * @return
	 */
	protected String get_creator(DBase parent, String xwindow){
		StringBuilder sb = new StringBuilder("");
		if(mainSketch){
			sb.append(ToolMessages.build(SET_SKETCH_TITLE, _0012_title));
		}
		else {
			sb.append(ToolMessages.build(CTOR_WINDOW, _0010_name, "this", _0012_title,
					$(_0820_x), $(_0821_y), $(_0826_width), $(_0827_height), _0031_renderer));
			// _0032_close_action KEEP_OPEN CLOSE_WINDOW EXIT_APP
			// Add code no matter which option selected. See Ticket 11
			sb.append(ToolMessages.build(SET_ACTION_ON_CLOSE, _0010_name, _0032_close_action));
			if(_0020_wdraw.length() > 0){
				sb.append(ToolMessages.build(ADD_DRAW_HANDLER, _0010_name, "this", _0020_wdraw));
			}
			if(_0021_wmouse.length() > 0){
				sb.append(ToolMessages.build(ADD_MOUSE_HANDLER, _0010_name, "this", _0021_wmouse));
			}
			if(_0022_wkey.length() > 0){
				sb.append(ToolMessages.build(ADD_KEY_HANDLER, _0010_name, "this", _0022_wkey));
			}
			if(_0023_wpre.length() > 0){
				sb.append(ToolMessages.build(ADD_PRE_HANDLER, _0010_name, "this", _0023_wpre));
			}
			if(_0024_wpost.length() > 0){
				sb.append(ToolMessages.build(ADD_POST_HANDLER, _0010_name, "this", _0024_wpost));
			}
			if(_0025_wclose.length() > 0){
				sb.append(ToolMessages.build(ADD_CLOSE_HANDLER, _0010_name, "this", _0025_wclose));
			}
		}
		return new String(sb);
	}

	protected String get_window_loop(){
		if(!mainSketch)
			return ToolMessages.build(LOOP_WINDOW, _0010_name);
		else
			return null;
	}
	

	/**
	 * This is the main method to create the code to create the Window
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
				if(mainSketch)
					comp.make_creator(lines, null, "this");
				else
					comp.make_creator(lines, this, _0010_name);
			}
		}				
	}

	protected void read(){
		super.read();
		renderer_editor = new EditorJComboBox(RENDERER);
		renderer_editor.setSelected(_0031_renderer);

		close_action_editor = new EditorJComboBox(CLOSE_ACTION);
		close_action_editor.setSelected(_0032_close_action);

		display_scale_editor = new EditorJComboBox(SCALE);
		display_scale_editor.setSelected(_0008_display_scale);
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		read();
	}



	@Override
	public void draw(Graphics2D g, DBase selected){
		G.pushMatrix(g);
		g.setColor(winBack);
		g.fillRect(0, 0, _0826_width, _0827_height);
		g.setColor(winEdge);
		g.setStroke(selStroke);
		g.drawRect(0, 0, _0826_width, _0827_height);		

		// Draw snap grid
		if(CtrlTabView.showGrid){
			g.setColor(CtrlTabView.gridCol);
			for(int i = 0; i <= _0826_width; i+= CtrlTabView.gridSize)
				for(int j = 0; j <= _0827_height; j+= CtrlTabView.gridSize)
					g.fillOval(i-2, j-2, 3, 3);	
		}
		// Draw controls
		Enumeration<?> e = children();
		while(e.hasMoreElements()){
			((DBase)e.nextElement()).draw(g, selected);
		}
		G.popMatrix(g);
	}

}
