package g4p.tool.controls;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;

import g4p.tool.G;
import g4p.tool.ToolMessages;
import g4p.tool.gui.propertygrid.EditorJFileChooser;

/**
 * 
 * @author Peter Lager
 *
 */
@SuppressWarnings("serial")
public class DImageButton extends DBaseVisual {

	transient BufferedImage image = null;
	
	protected int mem_nbr_tiles = 1;
	protected int style;
	

	public String 		_0042_img_off = "";
	transient public 	EditorJFileChooser img_off_editor = new EditorJFileChooser();
	public Boolean 		img_off_edit = true;
	public Boolean 		img_off_show = true;
	public String 		img_off_label = "Mouse off image";
	public String 		img_off_updater = "imageChanged_off";
	
	public String 		_0043_img_over = "";
	transient public 	EditorJFileChooser img_over_editor = new EditorJFileChooser();
	public Boolean 		img_over_edit = true;
	public Boolean 		img_over_show = false;
	public String 		img_over_label = "Mouse over image";
	public String 		img_over_updater = "imageChanged_over";

	public String 		_0044_img_down = "";
	transient public 	EditorJFileChooser img_down_editor = new EditorJFileChooser();
	public Boolean 		img_down_edit = true;
	public Boolean 		img_down_show = false;
	public String 		img_down_label = "Mouse pressed image";

	public Boolean 		_0045_match_image_size = false;
	public Boolean 		match_image_size_edit = true;
	public Boolean 		match_image_size_show = false;
	public String 		match_image_size_label = "Use image size";
	public String 		match_image_size_updater = "updateSize";

	public Boolean 		_0048_use_mask = false;
	public Boolean 		use_mask_edit = true;
	public Boolean 		use_mask_show = false;
	public String 		use_mask_label = "Use alpha mask";
	public String 		use_mask_updater = "updateUseMask";

	public String 		_0049_img_mask = "";
	transient public 	EditorJFileChooser img_mask_editor = new EditorJFileChooser();
	public Boolean 		img_mask_edit = true;
	public Boolean 		img_mask_show = false;
	public String 		img_mask_label = "Mask image";

	
	public DImageButton(){
		super();
		componentClass = "GImageButton";
		set_name(NameGen.instance().getNext("imgButton"));
		set_event_name(NameGen.instance().getNext(get_name()+ "_click"));
		col_scheme_show = false;
		opaque_show = false;
		width_edit = height_edit = true;
		width_show = height_show = true;
		_0826_width = 100;
		_0827_height = 60;
	}

	
	/**
	 * Get the creator statement var = new Foo(...);
	 * @return
	 */
	protected String get_creator(DBase parent, String window){
		String s = "";
		String f0 = _0042_img_off;
		String f1 = (_0043_img_over.length() > 0) ? _0043_img_over : f0;
		String f2 = (_0044_img_down.length() > 0) ? _0044_img_down : f0;	
		String flist = "new String[] { \"" + f0 + "\", " + "\"" + f1 + "\", " + "\"" + f2 + "\" } " ;

		if(_0048_use_mask &&  _0049_img_mask.length() > 0){ // Has mask file
			if(_0045_match_image_size)
				s += ToolMessages.build(CTOR_IMG_BTN_XYFM, _0010_name, window, $(_0820_x), $(_0821_y), flist, _0049_img_mask);
			else
				s += ToolMessages.build(CTOR_IMG_BTN_XYWHFM, _0010_name, window, $(_0820_x), $(_0821_y), $(_0826_width), $(_0827_height), flist, _0049_img_mask);
		}
		else { // No mask file
			if(_0045_match_image_size)
				s += ToolMessages.build(CTOR_IMG_BTN_XYF, _0010_name, window, $(_0820_x), $(_0821_y), flist);
			else
				s += ToolMessages.build(CTOR_IMG_BTN_XYWHF, _0010_name, window, $(_0820_x), $(_0821_y), $(_0826_width), $(_0827_height), flist);
			
		}
		s += super.get_creator(parent, window);
		return s;
	}
	
	
	public void draw(Graphics2D g, DBase selected){
		G.pushMatrix(g);
		g.translate(_0820_x, _0821_y);

		if(image != null){
			g.drawImage(image, 0, 0, _0826_width, _0827_height, 0, 0, image.getWidth(), image.getHeight() , null);
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
			img_over_show = true;
			match_image_size_show = true;
			use_mask_show = true;
			propertyModel.createProperties(this);			
			propertyModel.hasBeenChanged();
		}
	}
	
	public void imageChanged_over(){
		if(_0042_img_off.length() > 0){
			img_down_show = true;
			propertyModel.createProperties(this);			
			propertyModel.hasBeenChanged();
		}
	}
	
	public void updateSize(){
		if(_0045_match_image_size){
			_0826_width = image.getWidth();
			_0827_height = image.getHeight();
			width_show = height_show = false;
			resizeable = false;
		}
		else {
			width_show = height_show = true;
			resizeable = true;
		}
		propertyModel.createProperties(this);			
		propertyModel.hasBeenChanged();
	}

	public void updateUseMask(){
		img_mask_show = _0048_use_mask;
		propertyModel.createProperties(this);			
		propertyModel.hasBeenChanged();
	}
	
	protected void read(){
		super.read();
		img_off_editor = new EditorJFileChooser();
		img_over_editor = new EditorJFileChooser();
		img_down_editor = new EditorJFileChooser();
		img_mask_editor = new EditorJFileChooser();
		if(_0042_img_off.length() > 0)
			image = getImageFromDataFolder(_0042_img_off);
	}

	private void readObject(ObjectInputStream in)
	throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		read();
	}

	
}
