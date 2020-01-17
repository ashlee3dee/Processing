package g4p.tool.controls;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import g4p.tool.ToolMessages;
import g4p.tool.gui.propertygrid.EditorBase;
import g4p.tool.gui.propertygrid.EditorJComboBox;
import g4p.tool.gui.propertygrid.EditorJFileChooser;
import g4p.tool.gui.propertygrid.Validator;
import g4p_controls.StyledString;
import processing.core.PApplet;

@SuppressWarnings("serial")
public class DTextIconAlign extends DTextStyle {

	transient protected BufferedImage icon = null;

	public String width_updater = "sizeChanged";
	public String height_updater = "sizeChanged";


	protected int GUTTER = 4;

	protected Zone iconZone = new Zone("Icon");
	protected int iconX, iconY;
	protected int iconW, iconH;
	protected int iconAlignH, iconAlignV, iconPos;
	
	public int iconNo = 0;
	
	public String 		_0140_text_x_alignment = "CENTER";
	transient public 	EditorBase text_x_alignment_editor = new EditorJComboBox(H_ALIGN);
	public Boolean 		text_x_alignment_edit = true;
	public Boolean 		text_x_alignment_show = true;
	public String 		text_x_alignment_label = "Text X align";
	public String 		text_x_alignment_updater = "textAlignChanged";

	public String 		_0141_text_y_alignment = "MIDDLE";
	transient public 	EditorBase text_y_alignment_editor = new EditorJComboBox(V_ALIGN);
	public Boolean 		text_y_alignment_edit = true;
	public Boolean 		text_y_alignment_show = true;
	public String 		text_y_alignment_label = "Text Y align";
	public String 		text_y_alignment_updater = "textAlignChanged";

	public String 		_0150_icon_file = "";
	transient public 	EditorJFileChooser icon_file_editor = new EditorJFileChooser();
	public Boolean 		icon_file_edit = true;
	public Boolean 		icon_file_show = true;
	public String 		icon_file_label = "Icon file";
	public String 		icon_file_updater = "iconChanged";

	public int	 		_0152_nbr_tiles = 1;
	public Boolean 		nbr_tiles_edit = true;
	public Boolean 		nbr_tiles_show = false;
	public String 		nbr_tiles_label = "Nbr of tiles in icon";
	public String 		nbr_tiles_updater = "nbrTilesChanged";
	public Validator 	nbr_tiles_validator = Validator.getValidator(int.class, 1, 3);

	public String 		_0153_icon_position = "EAST";
	transient public 	EditorBase icon_position_editor = new EditorJComboBox(ICON_POS);
	public Boolean 		icon_position_edit = true;
	public Boolean 		icon_position_show = false;
	public String 		icon_position_label = "Icon Positionn";
	public String 		icon_position_updater = "iconPosChanged";

	public String 		_0154_icon_x_alignment = "RIGHT";
	transient public 	EditorBase icon_x_alignment_editor = new EditorJComboBox(H_ALIGN);
	public Boolean 		icon_x_alignment_edit = true;
	public Boolean 		icon_x_alignment_show = false;
	public String 		icon_x_alignment_label = "Icon X align";
	public String 		icon_x_alignment_updater = "iconAlignChanged";

	public String 		_0155_icon_y_alignment = "MIDDLE";
	transient public 	EditorBase icon_y_alignment_editor = new EditorJComboBox(V_ALIGN);
	public Boolean 		icon_y_alignment_edit = true;
	public Boolean 		icon_y_alignment_show = false;
	public String 		icon_y_alignment_label = "Icon Y align";
	public String 		icon_y_alignment_updater = "iconAlignChanged";

	public DTextIconAlign(){
		super();
		iconPos = ListGen.instance().getIndexOf(ICON_POS, _0153_icon_position);
		iconAlignH = ListGen.instance().getIndexOf(H_ALIGN, _0154_icon_x_alignment);
		iconAlignV = ListGen.instance().getIndexOf(V_ALIGN, _0155_icon_y_alignment);
		textHAlign = ListGen.instance().getIndexOf(H_ALIGN, _0140_text_x_alignment);
		textVAlign = ListGen.instance().getIndexOf(V_ALIGN, _0141_text_y_alignment);
	}

	protected String get_creator(DBase parent, String window){
		String s = "";
		if(_0150_icon_file.length() > 0) { // Button and label 
			s = ToolMessages.build(SET_ICON, _0010_name, _0150_icon_file, _0152_nbr_tiles, _0153_icon_position, _0154_icon_x_alignment, _0155_icon_y_alignment);
		}
		else if(icon != null){ // checkbox and option because there is no option to change icons for these
			if(!isIconAlignDefaults()) 
				s = ToolMessages.build(SET_ICON_ALIGN, _0010_name, _0154_icon_x_alignment, _0155_icon_y_alignment);
			if(!isIconPositionDefault())
				s = ToolMessages.build(SET_ICON_POS, _0010_name, _0153_icon_position);
		}
		if(!isTextAlignDefaults())
			s += ToolMessages.build(SET_TEXT_ALIGN, _0010_name, _0140_text_x_alignment, _0141_text_y_alignment);
		s += super.get_creator(parent, window);
		return s;
	}

	protected boolean isIconAlignDefaults(){
		return false;
	}

	protected boolean isIconPositionDefault(){
		return false;
	}
	
	protected boolean isTextAlignDefaults(){
		return false;
	}

	public void textChanged(){
		stext = new StyledString(_0130_text, textZone.w);
		if(text_x_alignment_edit || text_y_alignment_edit){
			boolean hasText = _0130_text.length() != 0;
			// If we have changed {has text > no text} or {no text > has text}
			// then we need to modify the property grid
			if(hasText != text_x_alignment_show){
				text_x_alignment_show = hasText;
				text_y_alignment_show = hasText;
				italic_show = hasText;
				bold_show = hasText;
				if(!hasText){
					_0136_bold = false;
					_0137_italic = false;
				}
				propertyModel.createProperties(this);
			}
		}
		propertyModel.hasBeenChanged();
	}
	
	public void nbrTilesChanged(){
		iconW = icon.getWidth() / _0152_nbr_tiles;		
		implChanges();
	}

	// Used to print the text and icon zones during debugging 
	protected void printZones(String title){
		System.out.println("===========================================================");
		if(title.length() > 0)
			System.out.println("===   " + title);
		System.out.println("  Control size " + _0826_width + " x " + _0827_height);
		System.out.println("  Icon at   >" + _0153_icon_position + "   " + iconPos + "  size = " + iconX + "  " + iconY + "  ::  " + iconW + "   " + iconH);
		System.out.println(iconZone);
		System.out.println(textZone);
		System.out.println("===========================================================");
	}
	
	// Control size changed in GUI
	public void updatedInGUI(int action){
		if(action == RESIZED){
			implChanges();
		}
	}

	// Size changed in grid
	public void sizeChanged(){
		implChanges();
	}
	
	/**
	 * This method must be called in the controls constructor after any default settings have been applied. <br/>
	 * This method is also called whenever an icon is added or its position moved.
	 */
	protected void implChanges(){
		fixZones();			 	// Must do icon before text
		calcIconPosInZone(); 	// Where to place the icon
	}

	/**
	 * This method should be called whenever the icon alignment is changed.
	 */
	private void calcIconPosInZone(){
		if(icon != null) {
			switch(iconAlignH){
			case LEFT:
				iconX = iconZone.x;
				break;
			case RIGHT: 
				iconX = iconZone.x + iconZone.w - iconW;
				break;
			case CENTER: // Default
			default:
				iconX = iconZone.x + (iconZone.w - iconW)/2;
				break;
			}
			switch(iconAlignV){
			case TOP:
				iconY = iconZone.y;
				break;
			case BOTTOM:
				iconY = iconZone.y + iconZone.h - iconH;
				break;
			case MIDDLE: // Default
			default:
				iconY = iconZone.y + (iconZone.h - iconH)/2;
			}
		}
	}

	/**
	 * This method is used when the icon position is changed. Do not call this directly instead call the
	 * implChanges method.
	 */
	private void fixZones(){
		if(icon == null){
			// Icon Zone
			iconZone.clear();
			// Text zone
			textZone.x = PAD;
			textZone.y = PAD;
			textZone.w = _0826_width - 2 * PAD;
			textZone.h = _0827_height - 2 * PAD;
		}
		else {
			// Icon zone
			switch(iconPos){
			case WEST:
				iconZone.x = PAD;
				iconZone.y = PAD;
				iconZone.w = iconW;
				iconZone.h = PApplet.max(iconH, _0827_height - 2 * PAD);
				break;
			case EAST:
				iconZone.x = _0826_width - iconW - PAD;
				iconZone.y = PAD;
				iconZone.w = iconW;
				iconZone.h = PApplet.max(iconH, _0827_height - 2 * PAD);
				break;
			case SOUTH:
				iconZone.x = PAD;
				iconZone.y = _0827_height - iconH - PAD;
				iconZone.w = PApplet.max(iconW, _0826_width - 2 * PAD);
				iconZone.h = iconH;
				break;
			case NORTH:
				iconZone.x = PAD;
				iconZone.y = PAD;			
				iconZone.w = PApplet.max(iconW, _0826_width - 2 * PAD);
				iconZone.h = iconH;
				break;
			default:
				break;
			}
			// Text zone
			switch(iconPos){
			case WEST:
				textZone.x = iconZone.w + PAD + GUTTER;
				textZone.y = PAD;
				textZone.w = _0826_width - iconZone.w - 2 * PAD - GUTTER;
				textZone.h = _0827_height - 2 * PAD;
				break;
			case EAST:
				textZone.x = PAD;
				textZone.y = PAD;
				textZone.w = _0826_width - iconZone.w - 2 * PAD - GUTTER;
				textZone.h = _0827_height - 2 * PAD;
				break;
			case SOUTH:
				textZone.x = PAD;
				textZone.y = PAD;
				textZone.w = _0826_width - 2 * PAD;
				textZone.h = _0827_height - iconZone.h - 2 * PAD - GUTTER;
				break;
			case NORTH:
				textZone.x = PAD;
				textZone.y = iconZone.h + PAD + GUTTER;			
				textZone.w = _0826_width - 2 * PAD;
				textZone.h = _0827_height - iconZone.h - 2 * PAD - GUTTER;
				break;
			default:
				break;
			}
		}
		try {
		if(stext != null)
			stext.setWrapWidth((int) textZone.w); 
		}
		catch(Exception e){
			System.err.println("ERROR control not wide enough for text");
		}
	}

	public void iconPosChanged(){
		iconPos = ListGen.instance().getIndexOf(ICON_POS, _0153_icon_position);
		implChanges();
	}
	
	public void iconAlignChanged(){
		iconAlignH = ListGen.instance().getIndexOf(H_ALIGN, _0154_icon_x_alignment);
		iconAlignV = ListGen.instance().getIndexOf(V_ALIGN, _0155_icon_y_alignment);
		calcIconPosInZone();
	}

	/**
	 * This method is called if we have set or changed the icon to be used with this
	 * control.
	 */
	public void iconChanged(){
		icon = this.getImageFromDataFolder(_0150_icon_file);
		if(icon != null){
			iconW = icon.getWidth() / _0152_nbr_tiles;
			iconH = icon.getHeight();
			if(iconW > _0826_width)
				_0826_width = iconW + 30;
			if(iconH > _0827_height)
				_0827_height = iconH + 4;
			nbr_tiles_show = true;
			icon_position_show = true;
			icon_x_alignment_show = true;
			icon_y_alignment_show = true;
			iconAlignChanged();
			propertyModel.createProperties(this);
			propertyModel.hasBeenChanged();
			implChanges();
		}
	}

	public void textAlignChanged(){
		textHAlign = ListGen.instance().getIndexOf(H_ALIGN, _0140_text_x_alignment);
		textVAlign = ListGen.instance().getIndexOf(V_ALIGN, _0141_text_y_alignment);
	}

	public void draw(Graphics2D g, DBase selected){
		if(icon != null)
			g.drawImage(icon, iconX, iconY, iconX + iconW, iconY + iconH, 
					iconNo * iconW, 0, iconNo * iconW + iconW, iconH, null);
		if(stext!= null)
			displayText(g, stext.getLines(g));
	}

	protected void read(){
		super.read();
		text_x_alignment_editor = new EditorJComboBox(H_ALIGN);
		text_x_alignment_editor.setSelected(_0140_text_x_alignment);
		text_y_alignment_editor = new EditorJComboBox(V_ALIGN);
		text_y_alignment_editor.setSelected(_0141_text_y_alignment);
		
		icon_position_editor = new EditorJComboBox(ICON_POS);
		icon_position_editor.setSelected(_0153_icon_position);
		
		icon_x_alignment_editor = new EditorJComboBox(H_ALIGN);
		icon_x_alignment_editor.setSelected(_0154_icon_x_alignment);
		icon_y_alignment_editor = new EditorJComboBox(V_ALIGN);
		icon_y_alignment_editor.setSelected(_0155_icon_y_alignment);
		
		icon_file_editor = new EditorJFileChooser();
		if(_0150_icon_file.length() > 0)
			icon = getImageFromDataFolder(_0150_icon_file);
	}
	

}
