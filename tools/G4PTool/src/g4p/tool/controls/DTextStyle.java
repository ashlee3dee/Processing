package g4p.tool.controls;

import g4p.tool.ToolMessages;
import g4p_controls.G4P;

@SuppressWarnings("serial")
public class DTextStyle extends DTextBase {

	public Boolean 		_0136_bold  = false;
	public Boolean 		bold_edit = true;
	public Boolean 		bold_show = false;
	public String 		bold_label = "Bold";
	public String 		bold_updater = "updateStyle";

	public Boolean 		_0137_italic  = false;
	public Boolean 		italic_edit = true;
	public Boolean 		italic_show = false;
	public String 		italic_label = "Italic";
	public String 		italic_updater = "updateStyle";


	public DTextStyle(){
		super();
	}

	public void updateStyle(){
		stext.clearAttributes();
		if(_0136_bold)
			stext.addAttribute(G4P.WEIGHT, G4P.WEIGHT_BOLD);
		if(_0137_italic)
			stext.addAttribute(G4P.POSTURE, G4P.POSTURE_OBLIQUE);
	}

	protected String get_creator(DBase parent, String window){
		String s = "";
		if(_0130_text.length() > 0){
			s += ToolMessages.build(SET_TEXT, _0010_name, _0130_text);
			// Only need bold/italic if some text is set
			if(_0136_bold)
				s += ToolMessages.build(SET_TEXT_BOLD, _0010_name);
			if(_0137_italic)
				s += ToolMessages.build(SET_TEXT_ITALIC, _0010_name);
		}
		s += super.get_creator(parent, window);		
		return s;
	}

	protected void read(){
		super.read();
	}

}
