package g4p.tool.gui.propertygrid;

import g4p.tool.controls.ListGen;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Editor to select a value from a drop-down list.
 * 
 * @author Peter Lager
 *
 */
@SuppressWarnings("serial")
public class EditorJComboBox extends EditorBase {

	@SuppressWarnings("rawtypes")
	protected static JComboBox component = null;

	protected JTable propTable = null;;
	protected int cellRow, cellColumn;
	
	protected ItemListener listen = null;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public EditorJComboBox(int type){
		validator = Validator.getValidator(type);
		if(component == null) {
			// Get the combo model for this JComboBox
			component = new JComboBox(ListGen.instance().getComboBoxModel(type));
		}
	}


	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		if(listen == null){
			listen = new ChangeListener();
			component.addItemListener(listen);
		}
		// Set the list model and selected item
		validator.preEditAction(this, value);
		propTable = table;
		cellRow = row;
		cellColumn = column;
		
		TableCellRenderer r = table.getCellRenderer(row, column);
		Component c = r.getTableCellRendererComponent(table, value, isSelected, isSelected, row, column);
		if( c!= null){
			component.setOpaque(true);
			component.setBackground(c.getBackground());
			if(c instanceof JComponent)
				component.setBorder(((JComponent)c).getBorder());
			else
				component.setOpaque(false);
		}
		return component;
	}

	/**
	 * This method is called after editing is completed
	 */
	@Override
	public Object getCellEditorValue() {
		if(listen != null){
			component.removeItemListener(listen);
			listen = null;
		}
		propTable = null;
		return component.getSelectedItem().toString();
	}

	public void setSelected(Object value) {
		component.setSelectedItem(value.toString());
	}

	public void setSelectedIndex(int index) {
		component.setSelectedIndex(index);
	}

	public String getSelected() {
		return component.getSelectedItem().toString();
	}

	public int getSelectedIndex() {
		return component.getSelectedIndex();
	}

	/**
	 * Listener for the combo box
	 * @author Peter Lager
	 *
	 */
	public class ChangeListener implements ItemListener {
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e != null && e.getID() == ItemEvent.ITEM_STATE_CHANGED && e.getStateChange() == ItemEvent.SELECTED){
				if(propTable != null){
					((CtrlPropView)propTable).updateProperty(getCellEditorValue(), cellRow, cellColumn);
//					((CtrlPropView)propTable).modelHasBeenChanged();
				}
			}
			fireEditingStopped();				
		}
		
	}
	
}
