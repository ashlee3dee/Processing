package g4p.tool.controls;

import g4p.tool.ToolMessages;
import g4p.tool.gui.propertygrid.EditorBase;
import g4p.tool.gui.propertygrid.EditorJComboBox;
import g4p.tool.gui.propertygrid.Validator;

@SuppressWarnings("serial")
public class DValueBase extends DBaseVisual {

	public String 		_0250_vtype = "DECIMAL";
	transient public EditorBase vtype_editor = new EditorJComboBox(VALUE_TYPE);
	public String 		vtype_label = "Value type to display";
	public Boolean 		vtype_edit = true;
	public Boolean 		vtype_show = true;
	public String		vtype_updater = "validateType";
	
	public Integer 		_0251_precision = 2;
	public String 		precision_label = "Numeric precision";
	public String 		precision_tooltip = "precision to display";
	public Boolean 		precision_edit = true;
	public Boolean 		precision_show = true;
	public Validator 	precision_validator = Validator.getValidator(int.class, 0, 4);
	public String		precision_updater = "validateType";

	
	public void validateType(){
		if(_0250_vtype.equals("INTEGER") && _0251_precision != 0 ){
			_0251_precision = 0;
			roundValueAndLimits();
			propertyModel.hasBeenChanged();
		}
	}

	protected void roundValueAndLimits(){}
	
	protected String get_creator(DBase parent, String window){
		String s = "";
		if(vtype_show)
			s += ToolMessages.build(SET_VALUE_TYPE, _0010_name, _0250_vtype, $(_0251_precision));
		s += super.get_creator(parent, window);		
		return s;
	}

	protected void read(){
		super.read();
		vtype_editor = new EditorJComboBox(VALUE_TYPE);
		vtype_editor.setSelected(_0250_vtype);
	}

}
