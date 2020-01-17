package g4p.tool.gui.propertygrid;

import g4p.tool.G4PTool;
import g4p.tool.gui.GuiDesigner;
import g4p.tool.gui.ToolIcon;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import processing.app.Util;

/**
 * Editor to get a list of strings - DCombo
 * 
 * @author Peter Lager
 *
 */
@SuppressWarnings("serial")
public class EditorStringList extends EditorBase {

	protected static JTextField component = null;
	protected static JTextArea lister = null;
	protected static JScrollPane pane = null;
	
	
	public EditorStringList(){
		if(lister == null){
			lister = new JTextArea("",10,40);
			pane = new JScrollPane(lister, 
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		}
		if(component == null){
			component = new JTextField();
			component.setFocusable(false);
		}
	}
	

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		String content = "";
		component.setText(value.toString());
		GuiDesigner.keepOpen(true);
		
		String fname = G4PTool.base.getActiveEditor().getSketch().getDataFolder() + SEP + value.toString();
		File file;
		try {
			file = new File(fname);
			if(file.exists())
				content = Util.loadFile(file);
			else
				System.out.println("====  CAN'T FIND FILE ================================================");
		} catch (IOException e1) {
			content = "";
		}
		lister.setText(content);
		// initialise the textarea chooser
		int result = JOptionPane.showConfirmDialog(GuiDesigner.instance(), pane, "ComboBox List (1 option per line)",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, ToolIcon.getIcon("DL_DIALOG_ICON"));
		if(result == JOptionPane.OK_OPTION) {
			try {
				content = lister.getText();
				file = new File(fname);
				Util.saveFile(content, file);
			} catch (IOException e1) {
				System.out.println("CANT SAVE OPTIONS");
			}
		}
		GuiDesigner.keepOpen(false);
		fireEditingStopped();
		return component;
	}

	@Override
	public Object getCellEditorValue() {
		return component.getText();
	}

}
