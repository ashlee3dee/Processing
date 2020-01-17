package g4p.tool.gui;

import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Singleton class to manage icons used by the tool.
 * 
 * @author Peter Lager
 *
 */
public class ToolIcon {


	@SuppressWarnings("rawtypes")
	private static HashMap<Class, Icon> classIcons = new HashMap<Class, Icon>();
	private static HashMap<String, ImageIcon> namedIcons = new HashMap<String, ImageIcon>();
	
	@SuppressWarnings({ "rawtypes" })
	private ToolIcon(){
		classIcons = new HashMap<Class, Icon>();
		namedIcons = new HashMap<String, ImageIcon>();
	}
	
	
	public static void addIcon(String n, ImageIcon icon){
		namedIcons.put(n, icon);
	}

	public static ImageIcon getIcon(String n){
		ImageIcon icon = namedIcons.get(n);
		return (icon == null) ? null : icon;
	}

	
	@SuppressWarnings("rawtypes")
	public static void addIcon(Class c, Icon icon){
		classIcons.put(c, icon);
	}
	
	@SuppressWarnings("rawtypes")
	public static Icon getIcon(Class c){
		Icon icon = classIcons.get(c);
		return (icon == null) ? null : icon;
	}
	
}
