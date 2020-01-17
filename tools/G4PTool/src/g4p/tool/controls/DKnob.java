package g4p.tool.controls;

import g4p.tool.G;
import g4p.tool.ToolMessages;
import g4p.tool.gui.propertygrid.EditorBase;
import g4p.tool.gui.propertygrid.EditorJComboBox;
import g4p.tool.gui.propertygrid.Validator;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectInputStream;

@SuppressWarnings("serial")
public class DKnob extends DValue1D {

	public float 		_0411_dial_start_angle = 110;
	public String		dial_start_angle_label = "Dial starts at angle";
	public String		dial_start_angle_tooltip = "0-360 degrees";
	public Boolean 		dial_start_angle_edit = true;
	public Boolean 		dial_start_angle_show = true;
	public Validator 	dial_start_angle_validator = Validator.getValidator(float.class, 0, 360);

	public float 		_0412_dial_end_angle = 70;
	public String		dial_end_angle_label = "Dial ends at angle";
	public String		dial_end_angle_tooltip = "0-360 degrees";
	public Boolean 		dial_end_angle_edit = true;
	public Boolean 		dial_end_angle_show = true;
	public Validator 	dial_end_angle_validator = Validator.getValidator(float.class, 0, 360);

	public String 		_0415_controller = "HORIZONTAL";
	transient public 	EditorBase controller_editor = new EditorJComboBox(KNOB_CTRL);
	public Boolean		controller_edit = true;
	public Boolean		controller_show = true;
	public String		controller_label = "Mouse controller scheme";
	public String		controller_updater = "updateController";
	
	public Float 		_0416_sensitivity = 1.0f;
	public String 		sensitivity_label = "Drag sensitivity";
	public Boolean 		sensitivity_edit = true;
	public Boolean 		sensitivity_show = true;
	public Validator 	sensitivity_validator = Validator.getValidator(float.class, 0.2f, 5.0f);

	public Float 		_0418_grip_ratio = 0.8f;
	public String 		grip_ratio_label = "Grip size (ratio)";
	public Boolean 		grip_ratio_edit = true;
	public Boolean 		grip_ratio_show = true;
	public Validator 	grip_ratio_validator = Validator.getValidator(float.class, 0.0f, 1.0f);

	public Boolean		_0422_over_arc_only  = false;
	public Boolean		over_arc_only_edit = true;
	public Boolean		over_arc_only_show = true;
	public String		over_arc_only_label = "Mouse over arc only";

	public Boolean		_0421_over_grip_only  = true;
	public Boolean		over_grip_only_edit = true;
	public Boolean		over_grip_only_show = true;
	public String		over_grip_only_label = "Mouse over grip only";

	public Boolean		_0430_show_arc_only  = false;
	public Boolean		show_arc_only_edit = true;
	public Boolean		show_arc_only_show = true;
	public String		show_arc_only_label = "Show arc only";

	public Boolean		_0431_show_track  = true;
	public Boolean		show_track_edit = true;
	public Boolean		show_tracky_show = true;
	public String		show_track_label = "Show track";


	public DKnob(){
		super();
		componentClass = "GKnob";
		set_name(NameGen.instance().getNext("knob"));
		set_event_name(NameGen.instance().getNext(get_name()+ "_turn"));
		_0826_width = 60;
		_0827_height = 60;
		_0620_show_ticks = true;
		vtype_show = false;
		precision_show = false;
	}

	public void updateController(){
		if(_0415_controller.equals("ANGULAR")){
			_0416_sensitivity = 1.0f;
			sensitivity_show = false;
		}
		else {
			sensitivity_show = true;			
		}
		propertyModel.createProperties(this);
	}

	/**
	 * Get the creator statement var = new Foo(...);
	 * @return
	 */
	protected String get_creator(DBase parent, String window){
		String s;
		s = ToolMessages.build(CTOR_GKNOB, _0010_name, window, $(_0820_x), $(_0821_y), $(_0826_width), $(_0827_height), $(_0418_grip_ratio));

		s += ToolMessages.build(SET_TURN_RANGE,_0010_name, _0411_dial_start_angle, _0412_dial_end_angle);
		s += ToolMessages.build(SET_CONTROLLER,_0010_name, _0415_controller);
		if(sensitivity_show) // only happens when not angular
			s += ToolMessages.build(SET_DRAG_SENSITIVITY, _0010_name, _0416_sensitivity);	
		s += ToolMessages.build(SET_SHOW_ARC_ONLY, _0010_name, _0430_show_arc_only);
		s += ToolMessages.build(SET_OVER_ARC_ONLY, _0010_name, _0422_over_arc_only);
		s += ToolMessages.build(SET_OVER_GRIP_ONLY, _0010_name, !_0421_over_grip_only);
		s += ToolMessages.build(SET_SHOW_TRACK, _0010_name, _0431_show_track);
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
		g.translate(_0826_width/2, _0827_height/2);

		int s = Math.min(_0826_width, _0827_height), hs = s/2;
		// Bezel
		g.setColor(jpalette[5]);
		g.fillOval(-hs, -hs, s, s);
		// grip
		int gs = Math.round(0.7f * s), hgs = gs/2;
		g.setColor(jpalette[4]);
		g.fillOval(-hgs, -hgs, gs, gs);
		g.setColor(jpalette[14]);
		// Needle
		g.setStroke(needleStroke);
		g.drawLine(0, 0, Math.round(0.707f*hs), Math.round(0.707f*hs));

		g.translate(-_0826_width/2, -_0827_height/2);
		displayString(g, globalDisplayFont, name);
		if(this == selected)
			drawSelector(g);
		G.popMatrix(g);;
	}

	protected void read(){
		super.read();
		controller_editor = new EditorJComboBox(KNOB_CTRL);	
		controller_editor.setSelected(_0415_controller);
	}
	
	private void readObject(ObjectInputStream in)
	throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		read();
	}


}
