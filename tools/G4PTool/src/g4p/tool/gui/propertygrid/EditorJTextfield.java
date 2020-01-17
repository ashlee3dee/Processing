package g4p.tool.gui.propertygrid;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 * Default editor for text and numeric input. <br>
 * The key to this class is selecting the correct validator.
 * 
 * @author Peter Lager
 *
 */
@SuppressWarnings("serial")
public class EditorJTextfield extends EditorBase { 

	protected static JTextField component;

	protected static Color ok = new Color(200,255,200);
	protected static Color notok = new Color(255,200,200);

	protected void doneWithEditing(){
		component.setBackground(Color.WHITE);
		component.setForeground(Color.BLACK);
	}

	protected void setValidHint(boolean valid){
		if(valid)
			component.setBackground(ok);
		else
			component.setBackground(notok);
	}

	/**
	 * Create an integer editor component that accepts any valid integer.
	 */
	public EditorJTextfield() {
		//		System.out.println("JTextField Editor constructor()");
		component = new JTextField();
		component.addKeyListener(new KeyListener(){

			public void keyTyped(KeyEvent e) {
				isValid(component.getText());
			}

			public void keyPressed(KeyEvent e) {}

			public void keyReleased(KeyEvent e) {
				isValid(component.getText());
			}
		});
	}


	/**
	 * See if the user supplied in
	 * @param vo
	 * @return
	 */
	private boolean isValid(Object vo){
		//		System.out.println("JTextField Editor isValid()");
		boolean result = (validator == null) ? true : validator.isValid(vo);
		setValidHint(result);
		return result;
	}

	/**
	 * Get the latest value and make sure it is valid or empty. <br>
	 * If it is invalid then prevent the focus leaving the component.
	 */
	public boolean stopCellEditing() {	
		//		System.out.println("JTextField stopCellEditing()");
		boolean valid = isValid(component.getText());
		if(valid){
			doneWithEditing();
			return super.stopCellEditing();			
		}
		else {
			component.requestFocus();
			return false;
		}
	}

	/**
	 * This is called when the table cell gets focus and returns the editor component.
	 */
	public Component getTableCellEditorComponent(JTable table, Object value, 
			boolean isSelected, int row, int column) {
//		System.out.println("JTextField getTableCellEditorComponent()");
//		System.out.println("Va;idator " + validator);
		validator.setOriginalValue(value);
		validator.preEditAction();
		component.setBorder(new LineBorder(Color.black));
		component.setText(value.toString());
		setValidHint((validator == null) ? true : validator.isValid(value));
		return component;
	}

	/**
	 * This is called when the editor component loses focus and retrieves the final
	 * value for the table model
	 */
	public Object getCellEditorValue() {
//		System.out.println("JTextField getCellEditorValue()");
		validator.postEditAction();
		return validator.getCellValue();
	}



}
