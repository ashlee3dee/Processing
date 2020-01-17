package g4p.tool.controls;

import g4p.tool.ToolMessages;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Enumeration;

@SuppressWarnings("serial")
public class DToggleGroup extends DBase {


	public DToggleGroup(){
		super();
		selectable = false;
		resizeable = false;
		moveable = false;

		componentClass = "GToggleGroup";
		set_name(NameGen.instance().getNext("togGroup"));
		name_label = "Variable name";
		name_tooltip = "Java naming rules apply";
		name_edit = true;
		eventHandler_show = false;
		x_show = y_show = width_show = height_show = false;
		allowsChildren = true;
	}

	public void make_creator(ArrayList<String> lines, DBase parent, String window){
		DOption comp;
		Enumeration<?> e;
		String ccode = get_creator(parent, window);
		if(ccode != null && !ccode.equals(""))
			lines.add(ccode);
		if(allowsChildren){
			e = children();
			while(e.hasMoreElements()){
				comp = (DOption)e.nextElement();
				comp.make_creator(lines, this, window);
			}
			// Add options to the option group
			boolean onPanel = (parent instanceof DPanel);
			e = children();
			while(e.hasMoreElements()){
				comp = (DOption)e.nextElement();
				lines.add(ToolMessages.build(ADD_A_CHILD, _0010_name, comp._0010_name));
				if(comp._0101_selected)
					lines.add(ToolMessages.build(SEL_OPTION, comp._0010_name, "true"));
				// If this group is on a panel then add the options
				if(onPanel)
					lines.add(ToolMessages.build(ADD_A_CHILD, parent._0010_name, comp._0010_name));
			}
		}				
	}

	protected String get_creator(DBase parent, String window){
		return ToolMessages.build(CTOR_GOPTIONGROUP, _0010_name);
	}

	/**
	 * This class has no events
	 */
	protected String get_event_definition(){
		return null;
	}

	public void draw(Graphics2D g, DBase selected){
		Enumeration<?> e = children();
		while(e.hasMoreElements()){
			((DBase)e.nextElement()).draw(g, selected);
		}
	}

}
