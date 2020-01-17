package g4p.tool.controls;

import g4p.tool.G;
import g4p.tool.ToolMessages;
import g4p.tool.gui.ToolImage;
import g4p_controls.StyledString;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;

@SuppressWarnings("serial")
public class DOption extends DCoreSelectable {

	public String selected_updater = "selectionChanged";

	public DOption(){
		super();
		text_label = "Display text";
		componentClass = "GOption";
		set_name(NameGen.instance().getNext("option"));
		set_event_name(NameGen.instance().getNext(get_name()+ "_clicked"));
		// Set up text
		_0130_text = "option text";
		_0140_text_x_alignment = "LEFT";
		textHAlign = ListGen.instance().getIndexOf(H_ALIGN, _0140_text_x_alignment);
		textVAlign = ListGen.instance().getIndexOf(V_ALIGN, _0141_text_y_alignment);
		// Set up icon
		icon = ToolImage.getImage("OP_ICON");
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
		s = ToolMessages.build(CTOR_GOPTION, _0010_name, window, 
				$(_0820_x), $(_0821_y), $(_0826_width), $(_0827_height));
		s += super.get_creator(parent, window);
		return s;
	}

	// Icon is using default alignment?
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

	public void selectionChanged(){
		// test to see if this object has been selected
		// and if so de-select all then select this
		DToggleGroup optg = (DToggleGroup) this.getParent();
		DOption opt;
		Enumeration<?> e = optg.children();
		while(e.hasMoreElements()){
			opt = (DOption)e.nextElement();
			opt.setSelected(false);
			opt.iconNo = 0;
		}
		_0101_selected = true;
		iconNo = 1;
	}
	
	protected void read(){
		super.read();
		icon = ToolImage.getImage("OP_ICON");
	}
	
	private void readObject(ObjectInputStream in)
	throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		read();
	}
	
}
