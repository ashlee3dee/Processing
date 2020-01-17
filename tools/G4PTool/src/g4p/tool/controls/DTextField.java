package g4p.tool.controls;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectInputStream;

import g4p.tool.G;
import g4p.tool.ToolMessages;
import g4p.tool.gui.propertygrid.Validator;

@SuppressWarnings("serial")
public class DTextField extends DTextBase {

	public String 		_0132_ptext = "";
	public String 		ptext_label = "Prompt Text";
	public String 		ptext_tooltip = "text to show when empty";
	public Boolean 		ptext_edit = true;
	public Boolean 		ptext_show = true;
	public Validator 	ptext_validator = Validator.getDefaultValidator(String.class);

	public Boolean 		_0186_horz_scrollbar = false;
	public String		horz_scrollbar_label = "Horizontal scrollbar?";
	public Boolean 		horz_scrollbar_edit = true;
	public Boolean 		horz_scrollbar_show = true;

	public Boolean 		_0188_hide_scrollbar = false;
	public String		hide_scrollbar_label = "Auto-hide scrollbar?";
	public Boolean 		hide_scrollbar_edit = true;
	public Boolean 		hide_scrollbar_show = true;

	
	public DTextField(){
		super();
		componentClass = "GTextField";
		set_name(NameGen.instance().getNext("textfield"));
		set_event_name(NameGen.instance().getNext(get_name()+ "_change"));
		_0826_width = 120;
		_0827_height = 30;
		_0600_opaque  = true;
	}
	
	/**
	 * Get the creator statement var = new Foo(...);
	 * @return
	 */
	protected String get_creator(DBase parent, String window){
		String s;
		String sbpolicy = "G4P.SCROLLBARS_NONE";
		if(_0186_horz_scrollbar){
			sbpolicy = "G4P.SCROLLBARS_HORIZONTAL_ONLY";
			if(_0188_hide_scrollbar)
				sbpolicy += " | G4P.SCROLLBARS_AUTOHIDE";
		}
		s = ToolMessages.build(CTOR_GTEXTFIELD, _0010_name, window, 
				$(_0820_x), $(_0821_y), $(_0826_width), $(_0827_height), sbpolicy);
		if(_0130_text.length() > 0)
			s += ToolMessages.build(SET_TEXT, _0010_name, _0130_text);
		if(_0132_ptext.length() > 0)
			s += ToolMessages.build(SET_PROMPT_TEXT, _0010_name, _0132_ptext);
		s += super.get_creator(parent, window);		
		return s;
	}

	public void draw(Graphics2D g, DBase selected){
		G.pushMatrix(g);
		g.translate(_0820_x, _0821_y);
		
		if(_0600_opaque){
			g.setColor(jpalette[6]);
			g.fillRect(0, 0, _0826_width, _0827_height);
		}
		g.setColor(jpalette[6]);
		g.fillRect(1, 1, _0826_width-2, _0827_height-2);
		g.setStroke(stdStroke);

//		g.setColor(jpalette[2]);
//		g.drawString(this._0010_name, 4, 12 );

		if(_0186_horz_scrollbar){
			g.setColor(jpalette[3]);
			g.fillRect(2, _0827_height - 12, _0826_width-4, 10);
		}
		
		g.setColor(jpalette[2]);
		displayString(g, DBase.globalInputFont, name);
		
		if(this == selected)
			drawSelector(g);
		else {
			g.setColor(DASHED_EDGE_COLOR);
			g.setStroke(dashed);
			g.drawRect(0, 0, _0826_width, _0827_height);		
		}

		G.popMatrix(g);
	}

	protected void read(){
		super.read();
	}

	private void readObject(ObjectInputStream in)
	throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		read();
	}
}
