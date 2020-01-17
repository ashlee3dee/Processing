package g4p.tool.controls;

import g4p.tool.G;
import g4p.tool.ToolMessages;
import g4p.tool.gui.propertygrid.Validator;
import g4p_controls.StyledString;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * A simple password entry control.
 * 
 * @author Peter Lager
 *
 */
@SuppressWarnings("serial")
public class DPassword extends DBaseVisual {
 

	public int 			_0125_pwsize = 10;
	public String 		pwsize_label = "Max length";
	public String 		pwsize_tooltip = "characters";
	public Boolean 		pwsize_edit = true;
	public Boolean 		pwsize_show = true;
	public Validator 	pwsize_validator = Validator.getValidator(int.class, 2, 64);

	public Boolean 		_0186_horz_scrollbar = false;
	public String		horz_scrollbar_label = "Horizontal scrollbar?";
	public Boolean 		horz_scrollbar_edit = true;
	public Boolean 		horz_scrollbar_show = true;

	public Boolean 		_0188_hide_scrollbar = false;
	public String		hide_scrollbar_label = "Auto-hide scrollbar?";
	public Boolean 		hide_scrollbar_edit = true;
	public Boolean 		hide_scrollbar_show = true;

	public DPassword(){
		super();
		componentClass = "GPassword";
		set_name(NameGen.instance().getNext("password"));
		name = new StyledString(_0010_name);
		set_event_name(NameGen.instance().getNext(get_name()+ "_change"));
		_0826_width = 160;
		_0827_height = 30;
		_0600_opaque  = true;
		_0125_pwsize = 10;
	}

	/**
	 * Get the creator statement var = new Foo(...);
	 * @return
	 */
	protected String get_creator(DBase parent, String window){
		String s;
		if(_0186_horz_scrollbar){
			String sbpolicy = "G4P.SCROLLBARS_HORIZONTAL_ONLY";
			if(_0188_hide_scrollbar)
				sbpolicy += " | G4P.SCROLLBARS_AUTOHIDE";
			s = ToolMessages.build(CTOR_GPASSWORD2, _0010_name, window, $(_0820_x), $(_0821_y), $(_0826_width), $(_0827_height), sbpolicy);
		}
		else {
			s = ToolMessages.build(CTOR_GPASSWORD1, _0010_name, window, $(_0820_x), $(_0821_y), $(_0826_width), $(_0827_height));
		}
		s += ToolMessages.build(PWORD_SIZE, _0010_name, $(_0125_pwsize));
		s += super.get_creator(parent, window);		
		return s;
	}

	public void draw(Graphics2D g, DBase selected){
		G.pushMatrix(g);
		g.setFont(DBase.globalInputFont);
		g.translate(_0820_x, _0821_y);

		if(_0600_opaque){
			g.setColor(jpalette[6]);
			g.fillRect(0, 0, _0826_width, _0827_height);
		}
		g.setColor(jpalette[6]);
		g.fillRect(1, 1, _0826_width-2, _0827_height-2);
		g.setStroke(stdStroke);

		g.setColor(jpalette[2]);
		g.drawString("### " + this._0010_name, 4, 12 );

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
