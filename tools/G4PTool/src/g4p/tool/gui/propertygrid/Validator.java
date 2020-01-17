package g4p.tool.gui.propertygrid;

import java.io.Serializable;

import g4p.tool.TDataConstants;
import g4p.tool.controls.ListGen;
import g4p.tool.controls.NameGen;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;


/**
 * A collection of classes to provide validators to control input 
 * into the property grid.
 * 
 * @author Peter Lager
 *
 */
@SuppressWarnings("serial")
public abstract class Validator implements TDataConstants, Serializable {

	private static Validator_Long defaultLong = new Validator_Long();
	private static Validator_Integer defaultInt = new Validator_Integer();
	private static Validator_Short defaultShort = new Validator_Short();
	private static Validator_Float defaultFloat = new Validator_Float();
	private static Validator_Double defaultDouble = new Validator_Double();
	private static Validator_String defaultString = new Validator_String();

	/**
	 * Make sure a validator is always returned
	 * @param c
	 * @return
	 */
	public static Validator getValidator(Class<?> c, Object ... args){
		Validator v = null;
		if(c == short.class || c == Short.class)
			v = new Validator_Short(args);
		else if(c == int.class || c == Integer.class)
			v = new Validator_Integer(args);
		else if(c == long.class || c == Long.class)
			v = new Validator_Long(args);
		else if(c == float.class || c == Float.class)
			v = new Validator_Float(args);
		else if(c == double.class || c == Double.class)
			v = new Validator_Double(args);
		else if(c == String.class)
			v = new Validator_String(args);
		else
			v = new Validator_String(args);
		return v;
	}

	/**
	 * Make sure a validator is always returned
	 * @param c
	 * @return
	 */
	public static Validator getDefaultValidator(Class<?> c){
		Validator v = defaultString;
		if(c == short.class || c == Short.class)
			v = defaultShort;
		else if(c == int.class || c == Integer.class)
			v = defaultInt;
		else if(c == long.class || c == Long.class)
			v = defaultLong;
		else if(c == float.class || c == Float.class)
			v = defaultFloat;
		else if(c == double.class || c == Double.class)
			v = defaultDouble;
		else if(c == String.class)
			v = defaultString;
		return v;
	}

	/**
	 * Create a validator for a JComboBox
	 * 
	 * @param type control string
	 * @return
	 */
	public static Validator getValidator(int type){
		Validator v = defaultString;
		switch(type){
		case COMPONENT_NAME:
			v = new Validator_ControlName();
			break;
		case COMPONENT_NAME_0:
			v = new Validator_ControlName(0);
			break;
		default:
			if(ListGen.instance().hasComboModel(type))
				v = new Validator_Combo(ListGen.instance().getComboBoxModel(type));
			break;
		}
		return v;
	}

	// ==============================================================================
	// ==============================================================================

	// INSTANCE attributes and methods

	// Should be set to remember the original value before
	// editing should be set by the editor
	protected Object originalValue;

	protected int errorType = VALID;

	// Holds the current cell value even if in valid
	protected Object cellValue;

	/**
	 * This should be overridden in the child class to cast the 
	 * return object to the type being validated.
	 * 
	 * @return the cellValue
	 */
	abstract public Object getCellValue();

	/**
	 * Validate the cell contents
	 * @param value the 'value' shown by the editor component
	 * @return
	 */
	abstract public boolean isValid(Object value);


	/**
	 * @return the originalValue
	 */
	public Object getOriginalValue() {
		return originalValue;
	}

	/**
	 * @param originalValue the originalValue to set
	 */
	public void setOriginalValue(Object originalValue) {
		this.originalValue = originalValue;
	}

	public int getError(){
		return errorType;
	}

	public void postEditAction(Object ...args){	}
	public void preEditAction(Object ...args){	}
	//	public Object getModel(){ return null; }

	/**
	 * ====================================================
	 * Validator for variable and method identifiers
	 * ====================================================
	 * @author Peter Lager
	 */
	static class Validator_ControlName extends Validator {

		private int min = 1;
		private int max = 30;

		private static String validChars = "_$abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		private static String firstChar = "_$abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		public Validator_ControlName(Object ... args){
			if(args.length > 0){
				min = Integer.parseInt(args[0].toString());
			}
		}

		@Override
		public Object getCellValue() {
			return (String)cellValue;
		}

		@Override
		public boolean isValid(Object value) {
			String uv = value.toString();
			int len = uv.length();
			boolean valid = true;
			if(len == 0 && min  == 0) {
				valid = true;
			}
			else if(len < min || uv.length() > max){
				errorType = INVALID_LENGTH;
				valid = false;
			}
			else if(NameGen.instance().used(uv)){
				errorType = UNAVAILABLE;
				valid = false;
			}
			else {
				valid = checkCharacters(uv);
			}
			if(valid)
				cellValue = uv;
			return valid;
		}

		public boolean checkCharacters(String uv){
			boolean valid = true;
			//char[] ca = uv.toCharArray();
			if(!firstChar.contains(uv.substring(0, 1))){
				errorType = FIRST_CHAR_INVALID;
				valid = false;
			}
			else if(uv.length() > 1){
				for(int i = 1; i < uv.length(); i++){
					if(!validChars.contains(uv.substring(i, i+1))){
						valid = false;
						errorType = INVALID_CHAR;
						break;
					}
				}
			}
			return valid;	
		}

		public void preEditAction(Object ...args){
			NameGen.instance().remove((String) originalValue);
		}

		public void postEditAction(Object ...args){
			NameGen.instance().add((String) cellValue);
		}

	}

	/**
	 * ====================================================
	 * Validator for long data type
	 * ====================================================
	 * @author Peter Lager
	 */
	static class Validator_Long extends Validator {
		private long min = Long.MIN_VALUE;
		private long max = Long.MAX_VALUE;

		/**
		 * 
		 * @param args
		 */
		public Validator_Long(Object ... args){
			if(args.length > 0){
				min = Long.parseLong(args[0].toString());
				max = Long.parseLong(args[1].toString());
			}
		}

		/**
		 * See if the value passed is valid
		 */
		@Override
		public boolean isValid(Object value) {
			boolean result = false;
			Long v;
			try {
				v = Long.parseLong(value.toString());
				cellValue = v;
				result = (v >= min && v <= max);
			}
			catch(Exception excp){
				cellValue = value;
				result = false;
			}
			return result;
		}

		public Object getCellValue() {
			return (Long)cellValue;
		}

	}

	/**
	 * ====================================================
	 * Validator for int data type
	 * ====================================================
	 * @author Peter Lager
	 */
	static class Validator_Integer extends Validator{
		int min = Integer.MIN_VALUE;
		int max = Integer.MAX_VALUE;

		/**
		 * 
		 * @param args
		 */
		public Validator_Integer(Object ... args){
			if(args.length > 1){
				min = Integer.parseInt(args[0].toString());
				max = Integer.parseInt(args[1].toString());
			}
		}

		/**
		 * See if the value passed is valid
		 */
		@Override
		public boolean isValid(Object value) {
			boolean result = false;
			Integer v = null;
			try {
				v = Integer.parseInt(value.toString());
				cellValue = v;
				result = (v >= min && v <= max);
			}
			catch(Exception excp){
				cellValue = value;
				result = false;
			}
			return result;
		}

		public Object getCellValue() {
			return (Integer)cellValue;
		}

	}

	/**
	 * ====================================================
	 * Validator for short data type
	 * ====================================================
	 * @author Peter Lager
	 */
	static class Validator_Short extends Validator{
		short min = Short.MIN_VALUE;
		short max = Short.MAX_VALUE;

		/**
		 * 
		 * @param args
		 */
		public Validator_Short(Object ... args){
			if(args.length > 1){
				min = Short.parseShort(args[0].toString());
				max = Short.parseShort(args[1].toString());
			}
		}

		/**
		 * See if the value passed is valid
		 */
		@Override
		public boolean isValid(Object value) {
			boolean result = false;
			Short v;
			try {
				v = Short.parseShort(value.toString());
				cellValue = v;
				result = (v >= min && v <= max);
			}
			catch(Exception excp){
				cellValue = value;
				result = false;
			}
			return result;
		}

		public Object getCellValue() {
			return (Short)cellValue;
		}

	}

	/**
	 * ====================================================
	 * Validator for float data type
	 * ====================================================
	 * @author Peter Lager
	 */
	static class Validator_Float extends Validator{
		float min = -Float.MAX_VALUE;
		float max = Float.MAX_VALUE;

		/**
		 * 
		 * The number of arguments determine their type i.e.
		 * (1) Boolean (empty)
		 * (2) Integer (min, max)
		 * (3) Boolean (min, max, empty)
		 * @param args
		 */
		public Validator_Float(Object ... args){
			if(args.length > 1){
				min = Float.parseFloat(args[0].toString());
				max = Float.parseFloat(args[1].toString());
			}
		}

		/**
		 * See if the value passed is valid
		 */
		@Override
		public boolean isValid(Object value) {
			boolean result = false;
			Float v;
			try {
				v = Float.parseFloat(value.toString());
				cellValue = v;
				result = (v >= min && v <= max);
			}
			catch(Exception excp){
				cellValue = value;
				result = false;
			}
			return result;
		}

		public Object getCellValue() {
			return (Float)cellValue;
		}

	}

	/**
	 * ====================================================
	 * Validator for double data type
	 * ====================================================
	 * @author Peter Lager
	 */
	static class Validator_Double extends Validator{
		double min = -Double.MAX_VALUE;
		double max = Double.MAX_VALUE;

		/**
		 * 
		 * The number of arguments determine their type i.e.
		 * (1) Boolean (empty)
		 * (2) Integer (min, max)
		 * (3) Boolean (min, max, empty)
		 * @param args
		 */
		public Validator_Double(Object ... args){
			if(args.length > 1){
				min = Double.parseDouble(args[0].toString());
				max = Double.parseDouble(args[1].toString());
			}
		}

		/**
		 * See if the value passed is valid
		 */
		@Override
		public boolean isValid(Object value) {
			boolean result = false;
			Double v;
			try {
				v = Double.parseDouble(value.toString());
				cellValue = v;
				result = (v >= min && v <= max);
			}
			catch(Exception excp){
				cellValue = value;
				result = false;
			}
			return result;
		}

		public Object getCellValue() {
			return (Double)cellValue;
		}

	}

	/**
	 * ====================================================
	 * Validator for String data type
	 * ====================================================
	 * @author Peter Lager
	 */
	static class Validator_String extends Validator{
		long min = 0;
		long max = 1000;

		/**
		 * 
		 * The length of args should be 2 i.e.
		 * Integer (min, max)
		 * @param args
		 */
		public Validator_String(Object ... args){
			if(args.length > 1){
				min = Integer.parseInt(args[0].toString());
				max = Integer.parseInt(args[1].toString());
			}
		}

		/**
		 * See if the value passed is valid
		 */
		@Override
		public boolean isValid(Object value) {
			int vs = value.toString().length();
			boolean valid = (vs >= min && vs <= max);
			if(valid)
				cellValue = value;
			return valid;
		}

		public Object getCellValue() {
			return (String)cellValue;
		}

	}

	/**
	 * ====================================================
	 * Validator for String data type in combo box
	 * ====================================================
	 * @author Peter Lager
	 */
	static class Validator_Combo extends Validator{

		@SuppressWarnings("rawtypes")
		DefaultComboBoxModel list;
		/**
		 * 
		 * The length of args should be 2 i.e.
		 * Integer (min, max)
		 * @param args
		 */
		@SuppressWarnings("rawtypes")
		public Validator_Combo(Object ... args){
			list = (DefaultComboBoxModel) args[0];
		}

		// The first argument should be the combo box cell editor
		// the second value the selected item
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public void preEditAction(Object ...args){
			EditorJComboBox.component.setModel((ComboBoxModel) list) ;
			EditorJComboBox.component.setSelectedItem(args[1].toString());
		}

		/**
		 * See if the value passed is valid
		 */
		@Override
		public boolean isValid(Object value) {
			cellValue = value;
			return true;
		}

		public Object getCellValue() {
			return cellValue;
		}

	}


}
