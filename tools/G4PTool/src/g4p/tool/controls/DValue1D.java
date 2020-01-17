package g4p.tool.controls;

import g4p.tool.ToolMessages;
import g4p.tool.gui.propertygrid.Validator;

@SuppressWarnings("serial")
public class DValue1D extends DValueBase {

	public Float 		_0220_value = 0.5f;
	public String 		value_label = "Value (initial)";
	public String 		value_tooltip = "initial value to use";
	public Boolean 		value_edit = true;
	public Boolean 		value_show = true;
	public String 		value_updater = "validateLimits";

	public Float 		_0221_min = 0.0f;
	public String 		min_label = "Minimum";
	public String 		min_tooltip = "smallest value slider can return";
	public Boolean 		min_edit = true;
	public Boolean 		min_show = true;
	public String 		min_updater = "validateLimits";

	public Float 		_0222_max = 1.0f;
	public String 		max_label = "Maximum";
	public String 		max_tooltip = "largest value slider can return";
	public Boolean 		max_edit = true;
	public Boolean 		max_show = true;
	public String 		max_updater = "validateLimits";
	
	public Integer 		_0260_nticks = 2;
	public String 		nticks_label = "Number of ticks";
	public String 		nticks_tooltip = "must be at least 2";
	public Boolean 		nticks_edit = true;
	public Boolean 		nticks_show = true;
	public Validator 	nticks_validator = Validator.getValidator(int.class, 2, 999);

	public Boolean 		_0261_stick_to_ticks  = false;
	public Boolean 		stick_to_ticks_edit = true;
	public Boolean 		stick_to_ticks_show = true;
	public String 		stick_to_ticks_label = "Stick to ticks?";
	
	public Float 		_0270_ease = 1.0f;
	public String 		ease_label = "Easing";
	public String 		ease_tooltip = "initial value to use";
	public Boolean 		ease_edit = true;
	public Boolean 		ease_show = true;
	public Validator 	ease_validator = Validator.getValidator(float.class, 1, 30);
	
	public Boolean 		_0620_show_ticks  = false;
	public Boolean 		show_ticks_edit = true;
	public Boolean 		show_ticks_show = true;
	public String 		show_ticks_label = "Show ticks?";
	
	
	public void validateLimits(){
		float t = (_0220_value - _0221_min)/(_0222_max - _0221_min);
		if(t < 0){
			_0220_value = _0221_min;
			propertyModel.hasBeenChanged();
		}
		else if (t > 1){
			_0220_value = _0222_max;
			propertyModel.hasBeenChanged();			
		}
	}
	
	protected void roundValueAndLimits(){
		_0220_value = (float) Math.round(_0220_value);
		_0221_min = (float) Math.round(_0221_min);
		_0222_max = (float) Math.round(_0222_max);
	}



	protected String get_creator(DBase parent, String window){
		String s = "";
		if(_0250_vtype.equals("INTEGER"))
			s += ToolMessages.build(SET_LIMITS, _0010_name, $(Math.round(_0220_value)), $(Math.round(_0221_min)), $(Math.round(_0222_max)));
		else
			s += ToolMessages.build(SET_LIMITS, _0010_name, $(_0220_value), $(_0221_min), $(_0222_max));
		if(_0260_nticks != 2)
			s += ToolMessages.build(SET_NBR_TICKS, _0010_name, $(_0260_nticks));
		if(_0261_stick_to_ticks)
			s += ToolMessages.build(SET_STICK_TICKS, _0010_name, _0261_stick_to_ticks);
		if(_0620_show_ticks)
			s += ToolMessages.build(SET_SHOW_TICKS, _0010_name, _0620_show_ticks);
		if(_0270_ease > 1)
			s += ToolMessages.build(SET_EASING, _0010_name, $(_0270_ease));
		s += super.get_creator(parent, window);		
		return s;
	}

	protected void read(){
		super.read();
//		vtype_editor = new EditorJComboBox(VALUE_TYPE);
	}
}
