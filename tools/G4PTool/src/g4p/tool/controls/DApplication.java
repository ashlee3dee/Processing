package g4p.tool.controls;

import java.awt.Font;
import java.io.IOException;
import java.io.ObjectInputStream;

import g4p.tool.ToolMessages;
import g4p.tool.gui.propertygrid.EditorBase;
import g4p.tool.gui.propertygrid.EditorJComboBox;
import g4p.tool.gui.propertygrid.Validator;
import g4p_controls.GCScheme;

/**
 * This class represents the whole Processing sketch. <br>
 * 
 * It will be the root node for the tree view and its children 
 * should only be windows.
 * 
 * @author Peter Lager
 *
 */
@SuppressWarnings("serial")
public final class DApplication extends DBase {


	public Boolean 		_0951_show_messages  = false;
	public Boolean 		show_messages_edit = true;
	public Boolean 		show_messages_show = true;
	public String 		show_messages_label = "Show G4P messages";

	public String 		_0952_col_scheme = "BLUE_SCHEME";
	transient public 	EditorBase col_scheme_editor = new EditorJComboBox(COLOUR_SCHEME);
	public Boolean 		col_scheme_edit = true;
	public Boolean 		col_scheme_show = true;
	public String 		col_scheme_label = "Colour scheme";
	public String 		col_scheme_updater = "colourSchemeChange";

	public Boolean 		_0953_cursor  = false;
	public Boolean 		cursor_edit = true;
	public Boolean 		cursor_show = true;
	public String 		cursor_label = "Enable mouse over";
	public String		cursor_updater = "cursorChanger";

	public String 		_0954_cursor_off = "ARROW";
	transient public 	EditorBase cursor_off_editor = new EditorJComboBox(CURSOR_CHANGER);
	public Boolean 		cursor_off_edit = true;
	public Boolean 		cursor_off_show = true;
	public String 		cursor_off_label = "Cursor off image";

	public Boolean 		_0955_use_round_corners  = true;
	public Boolean 		use_round_corners_edit = true;
	public Boolean 		use_round_corners_show = true;
	public String 		use_round_corners_label = "Use rounded corners GButton control";
	public String		use_round_corners_updater = "roundCornersChanger";

	// Display font
	public String 		_0960_global_display_font = "Arial";
	transient public 	EditorBase global_display_font_editor = new EditorJComboBox(FONT);
	public Boolean 		global_display_font_edit = true;
	public Boolean 		global_display_font_show = true;
	public String 		global_display_font_label = "FONT: Display";
	public String 		global_display_font_tooltip = "Buttons, labels etc.";
	public String 		global_display_font_updater = "displayFontChanger";

	public String 		_0961_global_display_style  = "Plain";
	transient public 	EditorBase global_display_style_editor = new EditorJComboBox(FONT_STYLE);
	public Boolean 		global_display_style_edit = true;
	public Boolean 		global_display_style_show = true;
	public String 		global_display_style_label = "Style";
	public String 		global_display_style_updater = "displayFontChanger";

	public Integer 		_0962_global_display_size = 12;
	public Boolean 		global_display_size_edit = true;
	public Boolean 		global_display_size_show = true;
	public String 		global_display_size_label = "Size";
	public String 		global_display_size_tooltip = "must be >= 6 point";
	public String 		global_display_size_updater = "displayFontChanger";
	public Validator 	global_display_size_validator = Validator.getValidator(int.class, 6, 72);

	public Boolean 		global_display_font_changed = false;
	
	// Input font
	public String 		_0963_global_input_font = "Arial";
	transient public 	EditorBase global_input_font_editor = new EditorJComboBox(FONT);
	public Boolean 		global_input_font_edit = true;
	public Boolean 		global_input_font_show = true;
	public String 		global_input_font_label = "FONT: Text Input";
	public String 		global_input_font_tooltip = "Text fields and areas";
	public String 		global_input_font_updater = "inputFontChanger";

	public String 		_0964_global_input_style  = "Plain";
	transient public 	EditorBase global_input_style_editor = new EditorJComboBox(FONT_STYLE);
	public Boolean 		global_input_style_edit = true;
	public Boolean 		global_input_style_show = true;
	public String 		global_input_style_label = "Style";
	public String 		global_input_style_updater = "inputFontChanger";

	public Integer 		_0965_global_input_size = 12;
	public Boolean 		global_input_size_edit = true;
	public Boolean 		global_input_size_show = true;
	public String 		global_input_size_label = "Size";
	public String 		global_input_size_tooltip = "must be >= 6 point";
	public String 		global_input_size_updater = "inputFontChanger";
	public Validator 	global_input_size_validator = Validator.getValidator(int.class, 6, 72);

	public Boolean 		global_input_font_changed = false;

	// Slider font
	public String 		_0966_global_slider_font = "Arial";
	transient public 	EditorBase global_slider_font_editor = new EditorJComboBox(FONT);
	public Boolean 		global_slider_font_edit = true;
	public Boolean 		global_slider_font_show = true;
	public String 		global_slider_font_label = "FONT: Slider values";
	public String 		global_slider_font_tooltip = "Limits and values in sliders";
	public String 		global_slider_font_updater = "sliderFontChanger";

	public String 		_0967_global_slider_style  = "Plain";
	transient public 	EditorBase global_slider_style_editor = new EditorJComboBox(FONT_STYLE);
	public Boolean 		global_slider_style_edit = true;
	public Boolean 		global_slider_style_show = true;
	public String 		global_slider_style_label = "Style";
	public String 		global_slider_style_updater = "sliderFontChanger";

	public Integer 		_0968_global_slider_size = 11;
	public Boolean 		global_slider_size_edit = true;
	public Boolean 		global_slider_size_show = true;
	public String 		global_slider_size_label = "Size";
	public String 		global_slider_size_tooltip = "must be >= 6 point";
	public String 		global_slider_size_updater = "sliderFontChanger";
	public Validator 	global_slider_size_validator = Validator.getValidator(int.class, 6, 72);

	public Boolean 		global_slider_font_changed = false;
	
	/**
	 * 
	 */
	public DApplication() {
		super();
		selectable = true;
		resizeable = false;
		moveable = false;
		allowsChildren = true;
		_0010_name = "SKETCH";
		name_label = "PROCCESSING";
		name_edit = false;
		x_show = false;
		y_show = false;
		width_show = false;
		height_show = false;
		eventHandler_show = false;
	}

	public String get_creator(DBase parent, String window){ 
		StringBuilder sb = new StringBuilder();
		sb.append(ToolMessages.build(SET_G4P_MESSAGES, _0951_show_messages));
		sb.append(ToolMessages.build(SET_SKETCH_COLOR, _0952_col_scheme));
		if(_0953_cursor)          {
			sb.append(ToolMessages.build(SET_CURSOR_OFF, _0954_cursor_off));
		}
		else {
			sb.append(ToolMessages.build(SET_MOUSE_OVER_ON, _0953_cursor));		
		}
		if(!_0955_use_round_corners){
			sb.append(ToolMessages.build(SET_ROUND_CORNERS, _0955_use_round_corners));		
		}
		// Global fonts
		if(global_display_font_changed) {
			sb.append(ToolMessages.build(SET_GLOBAL_DISPLAY_FONT, _0960_global_display_font, 
					fontStyleString(_0961_global_display_style), _0962_global_display_size));
		}
		if(global_input_font_changed) {
			sb.append(ToolMessages.build(SET_GLOBAL_INPUT_FONT, _0963_global_input_font, 
					fontStyleString(_0964_global_input_style), _0965_global_input_size));
		}
		if(global_slider_font_changed) {
			sb.append(ToolMessages.build(SET_GLOBAL_SLIDER_FONT, _0966_global_slider_font, 
					fontStyleString(_0967_global_slider_style), _0968_global_slider_size));
		}
		return new String(sb);
	}
	
	public void displayFontChanger() {
		DBase.globalDisplayFont = new Font(_0960_global_display_font, fontStyleValue(_0961_global_display_style), _0962_global_display_size);
		global_display_font_changed = 
				! ("Arial".equalsIgnoreCase(_0960_global_display_font) 
				&& "Plain".equalsIgnoreCase(_0961_global_display_style)
				&& 12 == _0962_global_display_size);
//		System.out.println("Display font changed : " + global_display_font_changed);
//		Log.logger().info("Display font changed to: " + DBase.globalDisplayFont);
	}
	
	public void inputFontChanger() {
		DBase.globalInputFont = new Font(_0963_global_input_font, fontStyleValue(_0964_global_input_style), _0965_global_input_size);
		global_input_font_changed = 
				! ("Arial".equalsIgnoreCase(_0963_global_input_font) 
				&& "Plain".equalsIgnoreCase(_0964_global_input_style)
				&& 12 == _0965_global_input_size);
//		System.out.println("Input font changed : " + global_input_font_changed);
//		Log.logger().info("Input font changed to: " + DBase.globalInputFont);
	}
	
	public void sliderFontChanger() {
		DBase.globalSliderFont = new Font(_0966_global_slider_font, fontStyleValue(_0967_global_slider_style), _0968_global_slider_size);
		global_slider_font_changed = 
				! ("Arial".equalsIgnoreCase(_0966_global_slider_font) 
				&& "Bold".equalsIgnoreCase(_0967_global_slider_style)
				&& 11 == _0968_global_slider_size);
//		System.out.println("Slider font changed : " + global_slider_font_changed);
//		Log.logger().info("Slider font changed to: " + DBase.globalSliderFont);
	}
	
	
	public String fontStyleString(String style) {
		switch(style) {
		case "Bold":
			return "G4P.BOLD";
		case "Italic":
			return "G4P.ITALIC";
		case "Bold-Italic":
			return "G4P.BOLD | G4P.ITALIC";
		default:
			return "G4P.PLAIN";
		}
	}
	
	public int fontStyleValue(String style) {
		switch(style) {
		case "Bold":
			return Font.BOLD;
		case "Italic":
			return Font.ITALIC;
		case "Bold-Italic":
			return Font.BOLD | Font.ITALIC;
		default:
			return Font.PLAIN;
		}
	}
	
	public String get_event_definition(){
		return null;
	}

	public String get_declaration(){
		return null;
	}

	public void cursorChanger(){
		cursor_off_show = _0953_cursor;
		propertyModel.createProperties(this);
		propertyModel.hasBeenChanged();
	}

	public void roundCornersChanger(){
		DBase.useRoundCorners = _0955_use_round_corners;
	}

	public void colourSchemeChange(){
		DBase.globalColorSchemeID = ListGen.instance().getIndexOf(COLOUR_SCHEME, _0952_col_scheme);
		DBase.globalColorSchemeName = _0952_col_scheme;
		DBase.globalJpalette = GCScheme.getJavaColor(globalColorSchemeID);
	}
	
	protected void read(){
		super.read();
		// Remake combo boxes
		col_scheme_editor = new EditorJComboBox(COLOUR_SCHEME);
		cursor_off_editor = new EditorJComboBox(CURSOR_CHANGER);
		DBase.globalColorSchemeID = ListGen.instance().getIndexOf(COLOUR_SCHEME, _0952_col_scheme);
		DBase.globalJpalette = GCScheme.getJavaColor(globalColorSchemeID);
		// Font editors -
		// Display FONT
		global_display_font_editor = new EditorJComboBox(FONT);		
		global_display_font_editor.setSelected(_0960_global_display_font);
		global_display_style_editor = new EditorJComboBox(FONT_STYLE);
		global_display_style_editor.setSelected(_0961_global_display_style);
		DBase.globalDisplayFont = new Font(_0960_global_display_font, fontStyleValue(_0961_global_display_style), _0962_global_display_size);
		// Input FONT
		global_input_font_editor = new EditorJComboBox(FONT);		
		global_input_font_editor.setSelected(_0963_global_input_font);
		global_input_style_editor = new EditorJComboBox(FONT_STYLE);
		global_input_style_editor.setSelected(_0964_global_input_style);
		DBase.globalInputFont = new Font(_0963_global_input_font, fontStyleValue(_0964_global_input_style), _0965_global_input_size);
		// Slider FONT
		global_slider_font_editor = new EditorJComboBox(FONT);		
		global_slider_font_editor.setSelected(_0966_global_slider_font);
		global_slider_style_editor = new EditorJComboBox(FONT_STYLE);
		global_slider_style_editor.setSelected(_0967_global_slider_style);
		DBase.globalSliderFont = new Font(_0966_global_slider_font, fontStyleValue(_0967_global_slider_style), _0968_global_slider_size);
	}

	private void readObject(ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		read();
	}

}
