package g4p.tool.controls;

import g4p.tool.ToolMessages;
import g4p.tool.gui.propertygrid.EditorBase;
import g4p.tool.gui.propertygrid.EditorJComboBox;

@SuppressWarnings("serial")
public class DLinearTrack extends DValue1D {
	

	public Boolean 		_0621_show_value  = false;
	public Boolean 		show_value_edit = true;
	public Boolean 		show_value_show = true;
	public String 		show_value_label = "Show value?";

	public Boolean 		_0622_show_limits  = false;
	public Boolean 		show_limitse_edit = true;
	public Boolean 		show_limits_show = true;
	public String 		show_limits_label = "Show limits?";
	
	public String 		_0630_text_orient = "ORIENT_TRACK";
	transient public EditorBase text_orient_editor = new EditorJComboBox(TEXT_ORIENT);
	public String 		text_orient_label = "Text direction";
	public Boolean 		text_orient_edit = true;
	public Boolean 		text_orient_show = true;

	public Boolean 		_0640_vert  = false;
	public Boolean 		vert_edit = true;
	public Boolean 		vert_show = true;
	public String 		vert_label = "Make vertical slider";

	protected String get_creator(DBase parent, String window){
		String s = "";
		if(_0621_show_value)
			s += ToolMessages.build(SET_SHOW_VALUE, _0010_name, _0621_show_value);
		if(_0622_show_limits)
			s += ToolMessages.build(SET_SHOW_LIMITS, _0010_name, _0622_show_limits);
		if(!_0630_text_orient.equals("ORIENT_TRACK"))
			s += ToolMessages.build(SET_TEXT_ORIENT, _0010_name, _0630_text_orient);
		if(_0640_vert)
			s += ToolMessages.build(MAKE_VERT, _0010_name, "PI/2"); 		
			
		s += super.get_creator(parent, window);		
		return s;
	}

	protected void read(){
		super.read();
		text_orient_editor = new EditorJComboBox(TEXT_ORIENT);
		text_orient_editor.setSelected(_0630_text_orient);
	}
	
}
