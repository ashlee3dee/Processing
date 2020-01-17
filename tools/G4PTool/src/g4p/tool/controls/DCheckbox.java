package g4p.tool.controls;

import g4p.tool.G;
import g4p.tool.ToolMessages;
import g4p.tool.gui.ToolImage;
import g4p_controls.StyledString;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectInputStream;

@SuppressWarnings("serial")
public class DCheckbox extends DCoreSelectable{

	public DCheckbox(){
		super();
		componentClass = "GCheckbox";
		text_label = "Display text";
		set_name(NameGen.instance().getNext("checkbox"));
		set_event_name(NameGen.instance().getNext(get_name()+ "_clicked"));
		// Set up text
		_0130_text = "checkbox text";
		_0140_text_x_alignment = "LEFT";
		textHAlign = ListGen.instance().getIndexOf(H_ALIGN, _0140_text_x_alignment);
		textVAlign = ListGen.instance().getIndexOf(V_ALIGN, _0141_text_y_alignment);
		// Set up icon
		icon = ToolImage.getImage("CB_ICON");
		_0152_nbr_tiles = 2;
		_0154_icon_x_alignment = "LEFT";
		iconAlignH = ListGen.instance().getIndexOf(H_ALIGN, _0154_icon_x_alignment);
		iconAlignV = ListGen.instance().getIndexOf(V_ALIGN, _0155_icon_y_alignment);
		iconW = icon.getWidth() / _0152_nbr_tiles;
		iconH = icon.getHeight();
		icon_position_show = true;
		icon_x_alignment_show = true;
		icon_y_alignment_show = true;
		icon_file_show = false;
		nbr_tiles_show = false;
		_0153_icon_position = "WEST";
		icon_position_editor.setSelected(_0153_icon_position);
		iconPos = ListGen.instance().getIndexOf(ICON_POS, _0153_icon_position);
		iconAlignChanged();
		
		implChanges();
		stext = new StyledString(_0130_text, textZone.w);
	}

	/**
	 * Get the creator statement var = new Foo(...);
	 * @return
	 */
	protected String get_creator(DBase parent, String window){
		String s;
		s = ToolMessages.build(CTOR_GCHECKBOX, _0010_name, window, 
				$(_0820_x), $(_0821_y), $(_0826_width), $(_0827_height));
		s += super.get_creator(parent, window);
		if(_0101_selected)
			s += ToolMessages.build(SEL_OPTION, _0010_name, "true");
		return s;
	}

	// Icon is in default position and alignment?
	protected boolean isIconAlignDefaults(){
		return _0154_icon_x_alignment.equals("CENTER") && _0155_icon_y_alignment.equals("MIDDLE");
	}

	// Icon is in default position?
	protected boolean isIconPositionDefault(){
		return _0153_icon_position.equals("WEST");
	}

	// Text is using default alignment
	protected boolean isTextAlignDefaults(){
		return (textHAlign == LEFT && textVAlign == MIDDLE);
	}

	public void draw(Graphics2D g, DBase selected){
		G.pushMatrix(g);
		g.setFont(DBase.globalDisplayFont);
		g.translate(_0820_x, _0821_y);
		
		if(_0600_opaque){
			g.setColor(jpalette[6]);
			g.fillRect(0, 0, _0826_width, _0827_height);
		}
		g.setStroke(stdStroke);

		super.draw(g, selected);
		
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
		icon = ToolImage.getImage("CB_ICON");
	}
	
	private void readObject(ObjectInputStream in)
	throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		read();
	}
	
}
