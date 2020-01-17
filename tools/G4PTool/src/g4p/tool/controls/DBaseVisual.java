package g4p.tool.controls;

import g4p.tool.G;
import g4p.tool.ToolMessages;
import g4p.tool.gui.propertygrid.EditorBase;
import g4p.tool.gui.propertygrid.EditorJComboBox;
import g4p_controls.GCScheme;
import g4p_controls.StyledString;
import g4p_controls.StyledString.TextLayoutInfo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.util.LinkedList;

/**
 * 
 * @author Peter Lager
 *
 */
@SuppressWarnings("serial")
public class DBaseVisual extends DBase {

	public int colScheme;
	public Color[] jpalette;

//	public StyledString stextxx = null;

	public Boolean 		_0009_lock  = false;
	public Boolean 		lock_edit = true;
	public Boolean 		lock_show = true;
	public String 		lock_label = "GUI LOCK";
	public String 		lock_updater = "lockChange";

	public Boolean 		_0600_opaque  = false;
	public Boolean 		opaque_edit = true;
	public Boolean 		opaque_show = true;
	public String 		opaque_label = "Opaque background?";

	public String 		_0940_col_scheme = DBase.globalColorSchemeName;
	transient public 	EditorBase col_scheme_editor = new EditorJComboBox(COLOUR_SCHEME);
	public Boolean 		col_scheme_edit = true;
	public Boolean 		col_scheme_show = true;
	public String 		col_scheme_label = "Control colour scheme";
	public String 		col_scheme_updater = "colourSchemeChange";


	public DBaseVisual() {
		super();
		colScheme = DBase.globalColorSchemeID;
		_0940_col_scheme = DBase.globalColorSchemeName;
		jpalette = DBase.globalJpalette;
	}

	protected String get_creator(DBase parent, String window){
		String s = "";
		if(colScheme != DBase.globalColorSchemeID)
			s += ToolMessages.build(SET_LOCAL_COLOR, _0010_name, _0940_col_scheme);
		if(opaque_show)
			s += ToolMessages.build(SET_OPAQUE, _0010_name, _0600_opaque);
		s += super.get_creator(parent, window);		
		return s;
	}

	public void lockChange(){
		selectable = ! _0009_lock;
	}

	public void colourSchemeChange(){
		colScheme = ListGen.instance().getIndexOf(COLOUR_SCHEME, _0940_col_scheme);
		jpalette = GCScheme.getJavaColor(colScheme);
		propertyModel.hasBeenChanged();
	}

	protected void displayString(Graphics2D g, Font font, StyledString string){
		G.pushMatrix(g);
		g.setFont(font);
		LinkedList<TextLayoutInfo> lines = string.getLines(g);
		// Now translate to text start position
//		g.translate(2, _0827_height - string.getTextAreaHeight()  );
//		g.translate(2, string.getTextAreaHeight()  );
//		g.translate(2, string.getMaxLineHeight()  );
		g.translate(2, 0  );
		// Now display each line
		for(TextLayoutInfo lineInfo : lines){
			TextLayout layout = lineInfo.layout;
			g.translate(0, layout.getAscent());
			// display text
			g.setColor(jpalette[2]);
			layout.draw(g, 0, 0);
			g.translate(0, layout.getDescent() + layout.getLeading());	
		}
		G.popMatrix(g);
	}
	
	
	
	protected void read(){
		super.read();
		col_scheme_editor = new EditorJComboBox(COLOUR_SCHEME);
		col_scheme_editor.setSelected(_0940_col_scheme);
	}

}
