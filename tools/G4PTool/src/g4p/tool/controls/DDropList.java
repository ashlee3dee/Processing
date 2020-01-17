package g4p.tool.controls;

import g4p.tool.G;
import g4p.tool.ToolMessages;
import g4p.tool.gui.propertygrid.EditorBase;
import g4p.tool.gui.propertygrid.EditorStringList;
import g4p.tool.gui.propertygrid.Validator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectInputStream;

@SuppressWarnings("serial")
public class DDropList extends DBaseVisual {

	public int	 		_0680_nbr_rows = 3;
	public Boolean 		nbr_rows_edit = true;
	public Boolean 		nbr_rows_show = true;
	public String 		nbr_rows_label = "Max rows to show";
	public Validator 	nbr_rows_validator = Validator.getValidator(int.class, 2, 10);

	public int	 		_0681_selected = 0;
	public Boolean 		selected_edit = true;
	public Boolean 		selected_show = true;
	public String 		selected_label = "Selected row";
	public String 		selected_tooltip = "Rows start at zero";
	public Validator 	selected_validator = Validator.getValidator(int.class, 0, 50);

	public String 		_0682_list_file;
	public Boolean 		list_file_edit = true;
	public Boolean 		list_file_show = true;
	public String 		list_file_label = "Option list file";
	public String 		list_file_tooltip = "Do not change the filename!";
	transient public 	EditorBase list_file_editor = new EditorStringList();

	public int 			_0683_thumb_width = 10;
	public String 		thumb_width_label = "Thumb width";
	public String 		thumb_width_tooltip = "pixels";
	public Boolean 		thumb_width_edit = true;
	public Boolean 		thumb_width_show = true;
	public Validator 	thumb_width_validator = Validator.getValidator(int.class, 8, 50);

	public DDropList(){
		super();
		componentClass = "GDropList";
		set_name(NameGen.instance().getNext("dropList"));
		set_event_name(NameGen.instance().getNext(get_name()+ "_click"));

		_0682_list_file = "list_" + id[0];
		_0826_width = 90;
		_0827_height = 20;
		opaque_show = false;
	}

	protected String get_creator(DBase parent, String window){
		String s = "";
		s = ToolMessages.build(CTOR_DROPLIST, _0010_name, window, 
					 $(_0820_x), $(_0821_y), $(_0826_width), $(_0827_height * (_0680_nbr_rows + 1)) ,
					 $(_0680_nbr_rows), _0683_thumb_width);	
		s += ToolMessages.build(CTOR_SET_LIST,  _0010_name, _0682_list_file, $(_0681_selected));
		s += super.get_creator(parent, window);
		return s;
	}
	

	public void draw(Graphics2D g, DBase selected){
		G.pushMatrix(g);
		g.setFont(DBase.globalDisplayFont);
		g.translate(_0820_x, _0821_y);
		
		// Draw background for item list
		if(this == selected && _0680_nbr_rows > 1){
			g.setColor(Color.WHITE);
			g.fillRect(0, _0827_height, _0826_width, _0827_height * _0680_nbr_rows);
			// Draw row borders
			g.setColor(jpalette[4]);
			for(int i = 1; i <= _0680_nbr_rows; i++)
				g.drawRect(0, _0827_height * i, _0826_width, _0827_height);
		}
		// Main all text back
		g.setColor(jpalette[6]);
		g.fillRect(0, 0, _0826_width, _0827_height);
		// Draw thumb
		g.setColor(jpalette[0]);
		g.fillRect(_0826_width - _0827_height, 0, _0827_height, _0827_height);
		// Draw selected text
		g.setColor(jpalette[2]);
		g.drawRect(0, 0, _0826_width, _0827_height);
		g.drawString(this._0010_name, 4, 12 );

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
		list_file_editor = new EditorStringList();		
	}
	
	private void readObject(ObjectInputStream in)
	throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		read();
	}


}
