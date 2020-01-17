package g4p.tool.controls;


import g4p.tool.ToolMessages;


@SuppressWarnings("serial")
public class DValue2D extends DValueBase {

	public Float 		_0220_value_x = 0.5f;
	public String 		value_x_label = "X value (initial)";
	public String 		value_x_tooltip = "initial x value to use";
	public Boolean 		value_x_edit = true;
	public Boolean 		value_x_show = true;
	public String 		value_x_updater = "validateLimits_x";

	public Float 		_0221_min_x = 0.0f;
	public String 		min_x_label = "X minimum";
	public String 		min_x_tooltip = "smallest x value slider can return";
	public Boolean 		min_x_edit = true;
	public Boolean 		min_x_show = true;
	public String 		min_x_updater = "validateLimits_x";

	public Float 		_0222_max_x = 1.0f;
	public String 		max_x_label = "X maximum";
	public String 		max_x_tooltip = "largest x value slider can return";
	public Boolean 		max_x_edit = true;
	public Boolean 		max_x_show = true;
	public String 		max_x_updater = "validateLimits_x";

	public Float 		_0225_value_y = 0.5f;
	public String 		value_y_label = "Y value (initial)";
	public String 		value_y_tooltip = "initial y value to use";
	public Boolean 		value_y_edit = true;
	public Boolean 		value_y_show = true;
	public String 		value_y_updater = "validateLimits_y";

	public Float 		_0226_min_y = 0.0f;
	public String 		min_y_label = "Y minimum";
	public String 		min_y_tooltip = "smallest y value slider can return";
	public Boolean 		min_y_edit = true;
	public Boolean 		min_y_show = true;
	public String 		min_y_updater = "validateLimits_y";

	public Float 		_0227_max_y = 1.0f;
	public String 		max_y_label = "Y maximum";
	public String 		max_y_tooltip = "largest y value slider can return";
	public Boolean 		max_y_edit = true;
	public Boolean 		max_y_show = true;
	public String 		max_y_updater = "validateLimits_y";
	
	
	protected String get_creator(DBase parent, String window){
		String s = "";
		s += ToolMessages.build(SET_X_LIMITS, _0010_name, $(_0220_value_x), $(_0221_min_x), $(_0222_max_x));
		s += ToolMessages.build(SET_Y_LIMITS, _0010_name, $(_0225_value_y), $(_0226_min_y), $(_0227_max_y));
		s += super.get_creator(parent, window);
		return s;
	}

	
	public void validateLimits_x(){
		float t = (_0220_value_x - _0221_min_x)/(_0222_max_x - _0221_min_x);
		if(t < 0){
			_0220_value_x = _0221_min_x;
			propertyModel.hasBeenChanged();
		}
		else if (t > 1){
			_0220_value_x = _0222_max_x;
			propertyModel.hasBeenChanged();			
		}
	}
	
	public void validateLimits_y(){
		float t = (_0225_value_y - _0226_min_y)/(_0227_max_y - _0226_min_y);
		if(t < 0){
			_0225_value_y = _0226_min_y;
			propertyModel.hasBeenChanged();
		}
		else if (t > 1){
			_0225_value_y = _0227_max_y;
			propertyModel.hasBeenChanged();			
		}
	}

	protected void roundValueAndLimits(){
		_0220_value_x = (float) Math.round(_0220_value_x);
		_0221_min_x = (float) Math.round(_0221_min_x);
		_0222_max_x = (float) Math.round(_0222_max_x);
		_0225_value_y = (float) Math.round(_0225_value_y);
		_0226_min_y = (float) Math.round(_0226_min_y);
		_0227_max_y = (float) Math.round(_0227_max_y);
	}



}
