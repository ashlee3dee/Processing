package g4p.tool.controls;

import g4p.tool.G;
import g4p.tool.ToolMessages;
import g4p_controls.StyledString;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectInputStream;

@SuppressWarnings("serial")
public class DLabel extends DTextIconAlign {

	
	public DLabel(){
		super();
		componentClass = "GLabel";
		set_name(NameGen.instance().getNext("label"));
		_0826_width = 80;
		_0827_height = 20;
		_0130_text = "My label";
		eventHandler_edit = eventHandler_show = false;

		implChanges();
		stext = new StyledString(_0130_text, textZone.w);
}
	
	/**
	 * There are no events for this control
	 * @return
	 */
	protected String get_event_definition(){
		return "";
	}

	/**
	 * Get the creator statement var = new Foo(...);
	 * @return
	 */
	protected String get_creator(DBase parent, String window){
		String s = "";
		s = ToolMessages.build(CTOR_GLABEL, _0010_name, window, 
				$(_0820_x), $(_0821_y), $(_0826_width), $(_0827_height));
		s += super.get_creator(parent, window);
		return s;
	}

	// Icon is in default alignment?
	protected boolean isIconAlignDefaults(){
		return _0153_icon_position.equals("EAST") && _0154_icon_x_alignment.equals("CENTER") && _0155_icon_y_alignment.equals("MIDDLE");
	}

	// Icon is in default position?
	protected boolean isIconPositionDefault(){
		return _0153_icon_position.equals("EAST");
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
		
		// Draw border based on 
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
