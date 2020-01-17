package g4p.tool.controls;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectInputStream;

import g4p.tool.G;
import g4p.tool.ToolMessages;
import g4p_controls.StyledString;

@SuppressWarnings("serial")
public class DButton extends DTextIconAlign {

	private static int CORNER_RADIUS = 12;

	public DButton(){
		super();
		componentClass = "GButton";
		set_name(NameGen.instance().getNext("button"));
		set_event_name(NameGen.instance().getNext(get_name()+ "_click"));
		_0130_text = "Face text";
		_0826_width = 80;
		_0827_height = 30;
		text_tooltip = "text to show on button";
		opaque_show = false;
		eventHandler_edit = eventHandler_show = true;
		PAD = DBase.useRoundCorners ? 4 : 2;
		implChanges();
		stext = new StyledString(_0130_text, textZone.w);
	}

	public void draw(Graphics2D g, DBase selected){
		G.pushMatrix(g);
		g.setFont(DBase.globalDisplayFont);

		g.translate(_0820_x, _0821_y);
		
		int pad = DBase.useRoundCorners ? 4 : 2;
		if(pad != PAD){
			PAD = pad;
			implChanges();
		}
		// Draw button base
		if(useRoundCorners){
			g.setColor(jpalette[4]);
			g.fillRoundRect(0, 0, _0826_width, _0827_height, CORNER_RADIUS, CORNER_RADIUS);
			g.setStroke(stdStroke);
			g.setColor(jpalette[3]);
			g.drawRoundRect(0, 0, _0826_width, _0827_height, CORNER_RADIUS, CORNER_RADIUS);
		}
		else {
			g.setColor(jpalette[4]);
			g.fillRect(0, 0, _0826_width, _0827_height);
			g.setStroke(stdStroke);
			g.setColor(jpalette[3]);
			g.drawRect(0, 0, _0826_width, _0827_height);			
		}
		// Draw icon & text
		super.draw(g, selected);
		// Draw selector
		if(this == selected)
			drawSelector(g);
		G.popMatrix(g);
	}


	/**
	 * Get the creator statement var = new Foo(...);
	 * @return
	 */
	protected String get_creator(DBase parent, String window){
		String s = "";
		s = ToolMessages.build(CTOR_GBUTTON, _0010_name, window, 
				$(_0820_x), $(_0821_y), $(_0826_width), $(_0827_height));
		s += super.get_creator(parent, window);
		return s;
	}

	// Icon is in default position and alignment?
	protected boolean isIconAlignDefaults(){
		return _0154_icon_x_alignment.equals("CENTER") && _0155_icon_y_alignment.equals("MIDDLE");
	}

	// Icon is in default position?
	protected boolean isIconPositionDefault(){
		return _0153_icon_position.equals("EAST");
	}
	
	// Text is using default alignment
	protected boolean isTextAlignDefaults(){
		return (textHAlign == CENTER && textVAlign == MIDDLE);
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
