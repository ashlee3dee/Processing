package g4p.tool.controls;

import g4p.tool.G;
import g4p.tool.ToolMessages;
import g4p.tool.gui.ToolImage;
import g4p.tool.gui.propertygrid.EditorJFileChooser;
import g4p.tool.gui.propertygrid.Validator;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * 
 * @author Peter Lager
 *
 */
@SuppressWarnings("serial")
public class DImageToggleButton extends DBaseVisual {

	transient BufferedImage image = null;

	public String 		_0042_img_off = "";
	transient public 	EditorJFileChooser img_off_editor = new EditorJFileChooser();
	public Boolean 		img_off_edit = true;
	public Boolean 		img_off_show = true;
	public String 		img_off_label = "Mouse off image";
	public String 		img_off_updater = "imageChanged_off";
	
	public String 		_0044_img_over = "";
	transient public 	EditorJFileChooser img_over_editor = new EditorJFileChooser();
	public Boolean 		img_over_edit = true;
	public Boolean 		img_over_show = true;
	public String 		img_over_label = "Mouse over image";

	public int 			_0052_cols = 2;
	public String 		cols_label = "No. of tiles horizontally";
	public Boolean 		cols_edit = true;
	public Boolean 		cols_show = true;
	public Validator 	cols_validator = Validator.getValidator(int.class, 1, 100);
	public String 		cols_updater = "nbrColsChanged";

	public int 			_0053_rows = 1;
	public String 		rows_label = "No. of tiles vertically";
	public Boolean 		rows_edit = true;
	public Boolean 		rows_show = true;
	public Validator 	rows_validator = Validator.getValidator(int.class, 1, 100);
	public String 		rows_updater = "nbrRowsChanged";

	
	public DImageToggleButton(){
		super();
		componentClass = "GImageToggleButton";
		set_name(NameGen.instance().getNext("imgTogButton"));
		set_event_name(NameGen.instance().getNext(get_name()+ "_click"));
		col_scheme_show = false;
		opaque_show = false;
		image = ToolImage.getImage("IMG_TOG_BTN_ICON");
		_0826_width = image.getWidth()/_0052_cols;
		_0827_height = image.getHeight()/_0053_rows;
	
		resizeable = false;
		width_edit = height_edit = false;
		width_show = height_show = false;
	}

	/**
	 * Get the creator statement var = new Foo(...);
	 * @return
	 */
	protected String get_creator(DBase parent, String window){
		String s = "";

		String offImage = (_0042_img_off.length() == 0) ? null : _0042_img_off;
		String overImage = (_0044_img_over.length() == 0) ? null : _0044_img_over;

		if(offImage == null){
			s += ToolMessages.build(CTOR_IMG_TOG_BTN_0, _0010_name, window, $(_0820_x), $(_0821_y));
		}
		else {
			if(overImage == null){
				s += ToolMessages.build(CTOR_IMG_TOG_BTN_1, _0010_name, window, $(_0820_x), $(_0821_y), offImage, $(_0052_cols), $(_0053_rows) );
			}
			else {			
				s += ToolMessages.build(CTOR_IMG_TOG_BTN_2, _0010_name, window, $(_0820_x), $(_0821_y), offImage, overImage, $(_0052_cols), $(_0053_rows) );
			}

		}
		s += super.get_creator(parent, window);
		return s;
	}

	
	public void draw(Graphics2D g, DBase selected){
		G.pushMatrix(g);
		g.translate(_0820_x, _0821_y);
		if(image != null){
			g.drawImage(image, 0, 0, _0826_width, _0827_height, 0, 0, _0826_width, _0827_height, null);
		}
		else {
			g.setColor(DASHED_EDGE_COLOR);
			g.setStroke(dashed);
			g.drawRect(0, 0, _0826_width, _0827_height);		
		}
		
		if(this == selected)
			drawSelector(g);
		G.popMatrix(g);
	}

	public void imageChanged_off(){
		if(_0042_img_off.length() > 0){
			image = getImageFromDataFolder(_0042_img_off);
			if(image != null){
				_0826_width = image.getWidth()/_0052_cols;
				_0827_height = image.getHeight()/_0053_rows;				
			}
			propertyModel.hasBeenChanged();
		}
	}

	public void nbrColsChanged(){
		// Make sure itr is valid
		if(_0052_cols < 1)
			_0052_cols = 1;
		// Make sure at least 2 toggle states
		if(_0052_cols * _0053_rows < 2)
			_0053_rows = 2;
		_0826_width = image.getWidth()/_0052_cols;
		_0827_height = image.getHeight()/_0053_rows;
		propertyModel.hasBeenChanged();		
	}
	
	public void nbrRowsChanged(){
		// Make sure itr is valid
		if(_0053_rows < 1)
			_0053_rows = 1;
		// Make sure at least 2 toggle states
		if(_0052_cols * _0053_rows < 2)
			_0052_cols = 2;
		_0826_width = image.getWidth()/_0052_cols;
		_0827_height = image.getHeight()/_0053_rows;
		propertyModel.hasBeenChanged();		
	}
	
	protected void read(){
		super.read();
		img_off_editor = new EditorJFileChooser();
		img_over_editor = new EditorJFileChooser();
		if(_0042_img_off.length() > 0)
			image = getImageFromDataFolder(_0042_img_off);
		else
			image = ToolImage.getImage("IMG_TOG_BTN_ICON");
		_0826_width = image.getWidth()/_0052_cols;
		_0827_height = image.getHeight()/_0053_rows;			
	}

	private void readObject(ObjectInputStream in)
	throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		read();
	}


}
