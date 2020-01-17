package g4p.tool.controls;

import g4p.tool.G;
import g4p.tool.ToolMessages;
import g4p_controls.StyledString;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectInputStream;

@SuppressWarnings("serial")
public class DSlider2D extends DValue2D {

	public DSlider2D(){
		super();
		componentClass = "GSlider2D";
		set_name(NameGen.instance().getNext("slider2d"));
		name = new StyledString(_0010_name);
		set_event_name(NameGen.instance().getNext(get_name()+ "_change"));
		
		_0826_width = 50;
		_0827_height = 50;

		vtype_label = "Output value type";
		precision_label = "Output value precision";
		//precision_show = precision_edit = false;
		_0600_opaque  = opaque_show = true;
		eventHandler_edit = eventHandler_show = true;
	}

	
	public void draw(Graphics2D g, DBase selected){
		G.pushMatrix(g);
		g.translate(_0820_x, _0821_y);
		
		if(_0600_opaque){
			g.setColor(jpalette[6]);
			g.fillRect(0, 0, _0826_width, _0827_height);
		}

		int cx = _0826_width/3;
		int cy = _0827_height/3;
		int hs = 4, s = 8;
		g.setColor(jpalette[15]);
		g.drawLine(cx, 0, cx, _0827_height);
		g.drawLine(0, cy, _0826_width, cy);
		
		g.setColor(jpalette[3]);
		g.fillRect(cx - hs, cy - hs, s, s);
		g.setColor(jpalette[15]);
		g.setStroke(stdStroke);
		g.drawRect(cx - hs, cy - hs, s, s);

		g.setColor(jpalette[2]);
		g.setStroke(stdStroke);
		g.drawRect(0, 0, _0826_width, _0827_height);

		// Control name
		displayString(g, DBase.globalSliderFont, name);

		if(this == selected)
			drawSelector(g);
		else {
			g.setColor(DASHED_EDGE_COLOR);
			g.setStroke(dashed);
			g.drawRect(0, 0, _0826_width, _0827_height);		
		}
		G.popMatrix(g);
	}

	
	/**
	 * Get the creator statement var = new Foo(...);
	 * @return
	 */
	protected String get_creator(DBase parent, String window){
		String s = "";
		s = ToolMessages.build(CTOR_GSLIDER2D, _0010_name, window, 
				$(_0820_x), $(_0821_y), $(_0826_width), $(_0827_height));
		s += super.get_creator(parent, window);
		return s;
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
