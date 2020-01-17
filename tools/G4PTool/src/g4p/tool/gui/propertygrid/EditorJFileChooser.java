package g4p.tool.gui.propertygrid;

import g4p.tool.G4PTool;
import g4p.tool.gui.GuiDesigner;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import processing.app.Util;
import processing.app.ui.Editor;

/**
 * Editor allowing user to select image files. The selected image file
 * will be copied to the sketch's data folder.
 * 
 * @author Peter Lager
 *
 */
@SuppressWarnings("serial")
public class EditorJFileChooser extends EditorBase {

	protected static JFileChooser chooser = null;
	protected static JTextField component = null;
	
	protected String name = "";
	
	public EditorJFileChooser(){
		if(chooser == null){
			chooser = new JFileChooser();
			chooser.addChoosableFileFilter(new ImageFilter());
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setAccessory(new ImagePreview(chooser));
			chooser.setCurrentDirectory(null);
		}
		if(component == null){
			File f = chooser.getSelectedFile();
			String name = (f != null) ? f.getName() : "";
			component = new JTextField(name);
			component.setFocusable(false);
		}
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
//		chooser = (JFileChooser) chooser;
		GuiDesigner.keepOpen(true);
		Editor editor = G4PTool.base.getActiveEditor();
		int selected = chooser.showDialog(GuiDesigner.instance(), "Use Image");
		if(selected == JFileChooser.APPROVE_OPTION){
			File src = chooser.getSelectedFile();	
			name = src.getName();
			File dest = new File(editor.getSketch().getDataFolder(), name);
			// Copy file to data folder
			try {
				// Make sure we do not attempt to use an image already in the data folder
				if(!src.equals(dest))
					// Source is not inside the data folder so copy it there
					Util.copyFile(src, dest);
			} catch (IOException e1) {
				System.out.println("COPY failed");
			}
			component.setText(name);
			// FORCE update this property
			((CtrlPropView)table).updateProperty(name, row, column);
		}
		GuiDesigner.keepOpen(false);
		this.fireEditingStopped();
		return component;
	}

	@Override
	public Object getCellEditorValue() {
		return component.getText();
	}


	public class ImageFilter extends FileFilter {
		
		//Accept all directories and all gif, jpg, tiff, or png files.
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}
			String extension = getExtension(f);
			if (extension != null) {
				if (extension.equals("tga") ||
						extension.equals("gif") ||
						extension.equals("jpeg") ||
						extension.equals("jpg") ||
						extension.equals("png")) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}

		/*/
		 * Get the extension of a file (make it lowercase
		 */
		public String getExtension(File f) {
			String ext = null;
			String s = f.getName();
			int i = s.lastIndexOf('.');

			if (i > 0 &&  i < s.length() - 1) {
				ext = s.substring(i+1).toLowerCase();
			}
			return ext;
		}

		//The description of this filter
		public String getDescription() {
			return "Just Images";
		}
	}

	public class ImagePreview extends JComponent
	implements PropertyChangeListener {
		ImageIcon thumbnail = null;
		File file = null;

		public ImagePreview(JFileChooser fc) {
			setPreferredSize(new Dimension(100, 100));
			this.setMinimumSize(new Dimension(100,100));
			fc.addPropertyChangeListener(this);
		}

		public void loadImage() {
			if (file == null) {
				thumbnail = null;
				return;
			}
			// Don't use createImageIcon (which is a wrapper for getResource)
			// because the image we're trying to load is probably not one
			// of this program's own resources.
			ImageIcon tmpIcon = new ImageIcon(file.getPath());
			if (tmpIcon != null) {
				if (tmpIcon.getIconWidth() > 90) {
					thumbnail = new ImageIcon(tmpIcon.getImage().
							getScaledInstance(90, -1,
									Image.SCALE_SMOOTH));
				} else { //no need to miniaturize
					thumbnail = tmpIcon;
				}
			}
		}

		public void propertyChange(PropertyChangeEvent e) {
			boolean update = false;
			String prop = e.getPropertyName();

			//If the directory changed, don't show an image.
			if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
				file = null;
				update = true;

				//If a file became selected, find out which one.
			} else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
				file = (File) e.getNewValue();
				update = true;
			}

			//Update the preview accordingly.
			if (update) {
				thumbnail = null;
				if (isShowing()) {
					loadImage();
					repaint();
				}
			}
		}

		protected void paintComponent(Graphics g) {
			if (thumbnail == null) {
				loadImage();
			}
			if (thumbnail != null) {
				int x = getWidth()/2 - thumbnail.getIconWidth()/2;
				int y = getHeight()/2 - thumbnail.getIconHeight()/2;

				if (y < 0) {
					y = 0;
				}

				if (x < 5) {
					x = 5;
				}
				thumbnail.paintIcon(this, g, x, y);
			}
		}
	}

}
