package g4p.tool.controls;


@SuppressWarnings("serial")
public class DCoreSelectable extends DTextIconAlign {

	
	public Boolean _0101_selected  = false;
	public Boolean selected_edit = true;
	public Boolean selected_show = true;
	public String selected_label = "Selected?";
	public String selected_updater = "selectionChange";

	
	public DCoreSelectable(){
		super();
		_0826_width = 120;
		_0827_height = 20;
		iconNo = 0;
	}
	

	public void selectionChange(){
		iconNo = _0101_selected ? 1 : 0;
	}
	
	public void setSelected(boolean selected){
		_0101_selected = selected;
	}
}
