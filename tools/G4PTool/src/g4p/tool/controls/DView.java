package g4p.tool.controls;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;

import g4p.tool.G;
import g4p.tool.ToolMessages;
import g4p.tool.gui.ToolImage;
import g4p.tool.gui.propertygrid.EditorBase;
import g4p.tool.gui.propertygrid.EditorJComboBox;
import g4p.tool.gui.propertygrid.Validator;


@SuppressWarnings("serial")
public class DView extends DBaseVisual {

	transient public BufferedImage icon;

	public String 		_0031_renderer = "JAVA2D";
	public String 		renderer_label = "Renderer";
	transient public EditorBase renderer_editor = new EditorJComboBox(RENDERER);
	public Boolean 		renderer_edit = true;
	public Boolean 		renderer_show = true;

	public Boolean 		_0029_use_peasycam  = false;
	public Boolean 		use_peasycam_edit = true;
	public Boolean 		use_peasycam_show = true;
	public String 		use_peasycam_label = "Use PeasyCam";
	public String		use_peasycam_updater = "viewTypeChanger";

	public float	 	_0661_lookat_x = 0;
	public Boolean 		lookat_x_edit = true;
	public Boolean 		lookat_x_show = false;
	public String 		lookat_x_label = "Lookat direction X";
	public Validator 	lookat_x_validator = Validator.getValidator(float.class, -1000000, +1000000);

	public float	 	_0662_lookat_y = 0;
	public Boolean 		lookat_y_edit = true;
	public Boolean 		lookat_y_show = false;
	public String 		lookat_y_label = "Lookat direction Y";
	public Validator 	lookat_y_validator = Validator.getValidator(float.class, -1000000, +1000000);

	public float	 	_0663_lookat_z = 0;
	public Boolean 		lookat_z_edit = true;
	public Boolean 		lookat_z_show = false;
	public String 		lookat_z_label = "Lookat direction Z";
	public Validator 	lookat_z_validator = Validator.getValidator(float.class, -1000000, +1000000);

	public float	 	_0664_distance = 0.1f;
	public Boolean 		distance_edit = true;
	public Boolean 		distance_show =  false;
	public String 		distance_label = "Lookat distance";
	public Validator 	distance_validator = Validator.getValidator(float.class, 0.1f, +1000000);


	public DView(){
		super();
		componentClass = "GView";
		set_name(NameGen.instance().getNext("view"));
		_0826_width = 80;
		_0827_height = 60;
		col_scheme_show = false;
		opaque_show = false;
		eventHandler_edit = eventHandler_show = false;
		icon = ToolImage.getImage("VIEW_ICON");
	}

	public void viewTypeChanger() {
		if(_0029_use_peasycam) {
			componentClass = "GViewPeasyCam";
			icon = ToolImage.getImage("VIEW_PCAM_ICON");
		}
		else {
			componentClass = "GView";
			icon = ToolImage.getImage("VIEW_ICON");
		}
		renderer_show = !_0029_use_peasycam;
		lookat_x_show = _0029_use_peasycam;
		lookat_y_show = _0029_use_peasycam;
		lookat_z_show = _0029_use_peasycam;
		distance_show = _0029_use_peasycam;
		propertyModel.createProperties(this);
		propertyModel.hasBeenChanged();
	}


	/**
	 * There are no events for this control
	 * @return
	 */
	protected String get_event_definition(){
		return "";
	}

	/**
	 * Get the creator statement var = new Foo(...);
	 * @return
	 */
	protected String get_creator(DBase parent, String window){
		String s = "";
		if(_0029_use_peasycam) { // GViewCam
			if(_0661_lookat_x + _0662_lookat_y + _0663_lookat_z == 0) {
				s = ToolMessages.build(CTOR_VIEW_PCAM_6, _0010_name, window, 
						$(_0820_x), $(_0821_y), $(_0826_width), $(_0827_height), 
						$(_0664_distance) );
			}
			else {
				s = ToolMessages.build(CTOR_VIEW_PCAM_9, _0010_name, window, 
						$(_0820_x), $(_0821_y), $(_0826_width), $(_0827_height), 
						$(_0661_lookat_x), $(_0662_lookat_y), $(_0663_lookat_z),
						$(_0664_distance) );
			}
		}
		else { // GView
			s = ToolMessages.build(CTOR_VIEW, _0010_name, window, 
					$(_0820_x), $(_0821_y), $(_0826_width), $(_0827_height),
					_0031_renderer);
		}
		s += super.get_creator(parent, window);
		return s;
	}

	public void draw(Graphics2D g, DBase selected){
		G.pushMatrix(g);
		g.translate(_0820_x, _0821_y);

		g.setColor(DBase.globalJpalette[6]);
		g.fillRect(0, 0, _0826_width, _0827_height);
		g.drawImage(icon, 0, 0, _0826_width, _0827_height, null);
		g.setStroke(stdStroke);

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
		renderer_editor = new EditorJComboBox(RENDERER);
		renderer_editor.setSelected(_0031_renderer);
		if(_0029_use_peasycam) {
			icon = ToolImage.getImage("VIEW_PCAM_ICON");
		}
		else {
			icon = ToolImage.getImage("VIEW_ICON");
		}
	}

	private void readObject(ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		read();
	}

}
