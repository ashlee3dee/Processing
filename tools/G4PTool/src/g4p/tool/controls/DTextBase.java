package g4p.tool.controls;

import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.util.LinkedList;

import g4p.tool.G;
import g4p.tool.gui.propertygrid.Validator;
import g4p_controls.StyledString;
import g4p_controls.StyledString.TextLayoutInfo;


@SuppressWarnings("serial")
public abstract class DTextBase extends DBaseVisual {

	protected int PAD = 2;

	protected Zone textZone = new Zone("Text");
	protected int textHAlign, textVAlign;

	public StyledString stext = null;

	public String 		_0130_text = "";
	public String 		text_label = "Text";
	public String 		text_tooltip = "component label text";
	public Boolean 		text_edit = true;
	public Boolean 		text_show = true;
	public Validator 	text_validator = Validator.getDefaultValidator(String.class);
	public String		text_updater = "textChanged";

	public DTextBase(){
		super();
		selectable = true;
		resizeable = true;
		moveable = true;
		allowsChildren = false;
		_0826_width = 80;
		_0827_height = 22;
		textZone.x = PAD;
		textZone.y = PAD;
		textZone.w = _0826_width - 2 * PAD;
		textZone.h = _0827_height - 2 * PAD;
		eventHandler_edit = eventHandler_show = true;
	}

	// Override this method if needed
	public void textChanged(){
		stext = new StyledString(_0130_text, textZone.w);
		propertyModel.hasBeenChanged();
	}

	public String get_text(){
		return _0130_text;
	}

	/**
	 * Default implementation for displaying the control's text. Child classes may override this if needed.
	 */
	protected void displayText(Graphics2D g, LinkedList<TextLayoutInfo> lines){
		G.pushMatrix(g);
		float sx = 0, tw = 0;
		// Get vertical position of text start based on alignment
		float textY;
		switch(textVAlign){
		case TOP:
			textY = 0;
			break;
		case BOTTOM:
			textY = textZone.h - stext.getTextAreaHeight();
			break;
		case MIDDLE:
		default:
			textY = (textZone.h - stext.getTextAreaHeight()) / 2;
		}
		// Now translate to text start position
		g.translate(textZone.x, textZone.y + textY);
		// Now display each line
		for(TextLayoutInfo lineInfo : lines){
			TextLayout layout = lineInfo.layout;
			g.translate(0, layout.getAscent());
			switch(textHAlign){
			case CENTER:
				tw = layout.getVisibleAdvance();
				tw = (tw > textZone.w) ? tw - textZone.w : tw;
				sx = (textZone.w - tw)/2;
				break;
			case RIGHT:
				tw = layout.getVisibleAdvance();
				tw = (tw > textZone.w) ? tw - textZone.w : tw;
				sx = textZone.w - tw;
				break;
			case LEFT:
			default:
				sx = 0;		
			}
			// display text
			g.setColor(jpalette[2]);
			layout.draw(g, sx, 0);
			g.translate(0, layout.getDescent() + layout.getLeading());	
		}
		G.popMatrix(g);
	}
	
	protected void read(){
		super.read();
		if(stext == null){
			stext = new StyledString(_0130_text, textZone.w);
		}
	}


}
