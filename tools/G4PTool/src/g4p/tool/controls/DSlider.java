package g4p.tool.controls;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectInputStream;

import g4p.tool.G;
import g4p.tool.ToolMessages;
import g4p.tool.gui.propertygrid.Validator;

@SuppressWarnings("serial")
public class DSlider extends DLinearTrack {

	
	public Float 		_0300_thick = 10.0f;
	public String 		thick_label = "Track thickness";
	public String 		thick_tooltip = "track thickness";
	public Boolean 		thick_edit = true;
	public Boolean 		thick_show = true;
	public Validator 	thick_validator = Validator.getValidator(float.class, 8, 30);

	public DSlider(){
		super();
		componentClass = "GSlider";
		set_name(NameGen.instance().getNext("slider"));
//		name = new StyledString(_0010_name);
		set_event_name(NameGen.instance().getNext(get_name()+ "_change"));
		_0826_width = 100;
		_0827_height = 40;
	}
	
	/**
	 * Get the creator statement var = new Foo(...);
	 * @return
	 */
	protected String get_creator(DBase parent, String window){
		String s;
		String x, y, w, h;
		if(_0640_vert){
			x = $(_0820_x + _0826_width);
			y = $(_0821_y);
			w = $(_0827_height);
			h = $(_0826_width);		
		}
		else {
			x = $(_0820_x);
			y = $(_0821_y);
			w = $(_0826_width);
			h = $(_0827_height);
		}
		s = ToolMessages.build(CTOR_GSLIDER, _0010_name, window, 
				x, y, w, h, $(_0300_thick));
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
		int thick, topY, leftX;
		
		thick = Math.round(_0300_thick);
		
		if(_0640_vert){
			leftX =  (_0826_width - thick) /2;
			topY = (_0827_height - thick) /2;
			// Track
			g.setColor(jpalette[5]);
			g.fillRect(leftX, 2, thick, _0827_height - 4);
		}
		else {
			leftX =  (_0826_width - thick) /2;
			topY = (_0827_height - thick) /2;
			// track
			g.setColor(jpalette[5]);
			g.fillRect(2, topY, _0826_width - 4, thick);
		}
		// thumb
		g.setColor(jpalette[0]);
		g.fillRect(leftX, topY, thick, thick);
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
