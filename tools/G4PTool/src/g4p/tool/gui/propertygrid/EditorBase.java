package g4p.tool.gui.propertygrid;

import g4p.tool.TFileConstants;

import javax.swing.AbstractCellEditor;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableCellEditor;


@SuppressWarnings("serial")
public abstract class EditorBase extends AbstractCellEditor implements TFileConstants, TableCellEditor, UIResource {

	
	// Validation object 
	public Validator validator = null;
	
	public void setSelected(Object value) {}
	
}
