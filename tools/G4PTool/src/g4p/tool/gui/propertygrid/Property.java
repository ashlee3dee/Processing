package g4p.tool.gui.propertygrid;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.table.TableCellRenderer;

/**
 * This class represents a 'property' that is to be shown in the property grid
 * and is linked to a field in the D component object.
 * 
 * @author Peter Lager
 *
 */
public final class Property implements Comparable<Object> {

	public Object fieldFromObject;
	public Field field;
	// Includes ordering key
	public String fieldName;
	public String shortFieldName;
	public String cellLabel;
	public String tooltip = null;
	
	public Method updateMethod = null;
	
	// The validator to use with this property
	public Validator validator = null;
	public EditorBase cell_editor = null;
	public TableCellRenderer renderer = null;
	public boolean allowEdit = true;
	public boolean show = true;
	
	/**
	 * Creates a property from the gien object and field.
	 * @param o
	 * @param f
	 */
	public Property(Object o, Field f){
		fieldFromObject = o;
		field = f;
		fieldName = field.getName(); // e.g. _1234_name
		shortFieldName = fieldName.substring(6);
		cellLabel = shortFieldName;
		
		// Get cell label text if any
		try {
			Field field = fieldFromObject.getClass().getField(shortFieldName + "_label");
			cellLabel =  (String) field.get(fieldFromObject);
		}
		catch(Exception excp){
			// Nothing to do 
		}
		// Get cell editor if any
		try {
			Field field = fieldFromObject.getClass().getField(shortFieldName + "_editor");
			cell_editor =  (EditorBase) field.get(fieldFromObject);
		}
		catch(Exception excp){
			// Nothing to do 
		}
		// Get cell renderer if any
		try {
			Field field = fieldFromObject.getClass().getField(shortFieldName + "_renderer");
			renderer = (TableCellRenderer) field.get(fieldFromObject);
		}
		catch(Exception excp){
			// Nothing to do 
		}
		// Get cell tooltip text if any
		try {
			Field field = fieldFromObject.getClass().getField(shortFieldName + "_tooltip");
			tooltip =  (String) field.get(fieldFromObject);
		}
		catch(Exception excp){
			// Nothing to do 
		}
		// Get validator if any
		try {
			Field field = fieldFromObject.getClass().getField(shortFieldName + "_validator");
			validator = (Validator) field.get(fieldFromObject);
		}
		catch(Exception excp){
			// validator = Validator.getDefaultValidator(field.getType());
		}
		// Get edit status if any
		try {
			Field field = fieldFromObject.getClass().getField(shortFieldName + "_edit");
			allowEdit = (Boolean) field.get(fieldFromObject);
		}
		catch(Exception excp){
			// Nothing to do but assume the fields is editable
		}
		// See if we need to show this property
		try {
			Field field = fieldFromObject.getClass().getField(shortFieldName + "_show");
			show = (Boolean) field.get(fieldFromObject);
		}
		catch(Exception excp){
			// Nothing to do but assume the fields is to be shown
		}
		// Method to call if this property changes in grid view
		try {
			updateMethod = null;
			Field field = fieldFromObject.getClass().getField(shortFieldName + "_updater");
			String methodName =  (String) field.get(fieldFromObject);
			// We have a method name but do we have the method
			try {
				updateMethod = fieldFromObject.getClass().getMethod(methodName, (Class<?>[]) null );
			}
			catch(Exception excp){
				excp.printStackTrace();
			}
		}
		catch(Exception excp){
			// Nothing to do 
		}
	}

	/**
	 * Get the value of this property. Used by the property grid to
	 * decide what should be shown.
	 * @return
	 */
	public Object getValue(){
		return getFieldValue(field, fieldFromObject);
	}

	/**
	 * Get the value stored in a field and return it as an Object.
	 * 
	 * @param f the field required
	 * @param obj the object it applies to
	 * @return
	 */
	private Object getFieldValue(Field f, Object obj){
		Object fvalue;
		Class<?> c = f.getClass();
		if(c == int.class || c == Integer.class){
			try {
				fvalue = f.getInt(obj);
			} catch (Exception e) {
				System.out.println("FAILED to retrieve Integer value for " + shortFieldName);
				fvalue = new Integer(0);
			} 
		}
		else if(c == float.class || c == Float.class){
			try {
				fvalue = f.getFloat(obj);
			} catch (Exception e) {
				System.out.println("FAILED to retrieve Float value for " + shortFieldName);
				fvalue = new Float(0);
			} 
		}
		else if(c == String.class){
			try {
				fvalue = f.get(obj).toString();
			} catch (Exception e) {
				System.out.println("FAILED to retrieve String value for " + shortFieldName);
				fvalue = new String("");
			} 
		}
		else if(c == boolean.class || c == Boolean.class){
			try {
				fvalue = f.getBoolean(obj);
			} catch (Exception e) {
				System.out.println("FAILED to retrieve Boolean value for " + shortFieldName);
				fvalue = new Boolean(false);
			} 
		}
		else if(c == short.class || c == Short.class){
			try {
				fvalue = f.getShort(obj);
			} catch (Exception e) {
				System.out.println("FAILED to retrieve Short value for " + shortFieldName);
				fvalue = new Short((short) 0);
			} 
		}
		else if(c == long.class || c == Long.class){
			try {
				fvalue = f.getLong(obj);
			} catch (Exception e) {
				System.out.println("FAILED to retrieve Long value for " + shortFieldName);
				fvalue = new Long(0);
			} 
		}
		else if(c == double.class || c == Double.class){
			try {
				fvalue = f.getDouble(obj);
			} catch (Exception e) {
				System.out.println("FAILED to retrieve Double value for " + shortFieldName);
				fvalue = new Double(0);
			} 
		}
		else if(c == char.class || c == Character.class){
			try {
				fvalue = f.getChar(obj);
			} catch (Exception e) {
				System.out.println("FAILED to retrieve Character value for " + shortFieldName);
				fvalue = new Character(' ');
			} 
		}
		else {
			try {
				fvalue = f.get(obj);
			} catch (Exception e) {
				System.out.println("FAILED to retrieve Object value for " + shortFieldName);
				fvalue = new Object();
			} 
		}
		return fvalue;
	}

	// Called when table cell loses focus
	/**
	 * This is a really important method in that it is called when a cell in the property 
	 * grid loses focus. It stores the edited value in the attribute and calls the 
	 * property updater method if any.
	 */
	public void setValue(Object value){
		try {
			// Attempt to store the value
			setFieldValue(field, fieldFromObject, value);
			// If successful attempt to call the updater method if any
			if(updateMethod != null){
				try {
					updateMethod.invoke(fieldFromObject, (Object[]) null);
				} catch (Exception e) {
				}
			}		
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException:  unable to set a field value for "+ shortFieldName + "   value: "+ value.toString());
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException:  unable to set a field value for "+ shortFieldName + "   value: "+ value.toString());
		}
	}

	/**
	 * Store a value in a field.
	 * 
	 * @param f
	 * @param obj
	 * @param value
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void setFieldValue(Field f, Object obj, Object value) throws IllegalArgumentException, IllegalAccessException {
		Class<?> c = f.getClass();
		if(c == boolean.class || c == Boolean.class)
			f.setBoolean(obj, Boolean.valueOf(value.toString()));
		else if(c == int.class || c == Integer.class)
			f.setInt(obj, (Integer)value);
		else if(c == long.class || c == Long.class)
			f.setLong(obj, (Long)value);
		else if(c == float.class || c == Float.class)
			f.setFloat(obj, (Float)value);
		else if(c == double.class || c == Double.class)
			f.setChar(obj, (Character)value);
		else if(c == String.class)
			f.set(obj, (String)value);
		else 
			f.set(obj, value);
	}

	public int compareTo(Object o) {
		Property p = (Property) o; 
		return fieldName.compareTo(p.fieldName);
	}


}
