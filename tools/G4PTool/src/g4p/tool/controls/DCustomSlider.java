package g4p.tool.controls;

import g4p.tool.G;
import g4p.tool.ToolMessages;
import g4p.tool.gui.propertygrid.EditorBase;
import g4p.tool.gui.propertygrid.EditorJComboBox;
import g4p_controls.StyledString;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectInputStream;

@SuppressWarnings("serial")
public class DCustomSlider extends DLinearTrack {

	public String _0900_skin = "grey_blue";
	transient public EditorBase skin_editor = new EditorJComboBox(SLIDER_SKIN);
	public Boolean skin_edit = true;
	public Boolean skin_show = true;
	public String skin_label = "Slider skin";


	public DCustomSlider(){
		super();
		componentClass = "GCustomSlider";
		set_name(NameGen.instance().getNext("custom_slider"));
		name = new StyledString(_0010_name);
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
		s = ToolMessages.build(CTOR_GCUSTOMSLIDER, _0010_name, window, 
				x, y, w, h, _0900_skin);
		s += super.get_creator(parent, window);		
		return s;
	}


	public void draw(Graphics2D g, DBase selected){
		G.pushMatrix(g);
		g.setFont(DBase.globalSliderFont);
		g.translate(_0820_x, _0821_y);
		int cx = _0826_width/2;
		int cy = _0827_height/2;

		g.setStroke(stdStroke);

		if(_0600_opaque){
			g.setColor(jpalette[6]);
			g.fillRect(0, 0, _0826_width, _0827_height);
		}
		else {
			g.setColor(csdrBack);
			g.fillRect(0, 0, _0826_width, _0827_height);
		}
		g.setColor(jpalette[5]);
		g.drawRect(0, 0, _0826_width, _0827_height);

		if(_0640_vert){
			g.setColor(csdrSlideBack);
			g.fillRect((_0826_width - 10)/2, 0, 10, _0827_height);
			g.setColor(csdrSlideBorder);
			g.drawRect((_0826_width - 10)/2, 0, 10, _0827_height);			

			g.setColor(csdrThumb);
			g.fillOval(cx - 12, cy - 5, 24, 10);
			g.setColor(csdrSlideBorder);
			g.drawOval(cx - 12, cy - 5, 24, 10);
		}
		else {
			g.setColor(csdrSlideBack);
			g.fillRect(0, (_0827_height - 6)/2, _0826_width, 6);
			g.setColor(csdrSlideBorder);
			g.drawRect(0, (_0827_height - 6)/2, _0826_width, 6);

			g.setColor(csdrThumb);
			g.fillOval(cx - 5, cy - 12, 10, 24);
			g.setColor(csdrSlideBorder);
			g.drawOval(cx - 5, cy - 12, 10, 24);
		}
		// Control name
		displayString(g, DBase.globalSliderFont, name);

		if(this == selected)
			drawSelector(g);
		G.popMatrix(g);
	}

	protected void read(){
		super.read();
		skin_editor = new EditorJComboBox(SLIDER_SKIN);
		skin_editor.setSelected(_0900_skin);
	}

	private void readObject(ObjectInputStream in)
			throws IOException, ClassNotFoundException
			{
		in.defaultReadObject();
		read();
			}
}
