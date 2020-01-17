package g4p.tool.gui.treeview;

import g4p.tool.controls.DApplication;
import g4p.tool.controls.DBase;
import g4p.tool.controls.DOption;
import g4p.tool.controls.DPanel;
import g4p.tool.controls.DTimer;
import g4p.tool.controls.DToggleGroup;
import g4p.tool.controls.DWindow;
import g4p.tool.controls.IdGen;
import g4p.tool.controls.NameGen;
import g4p.tool.gui.ToolIcon;
import g4p.tool.gui.propertygrid.CtrlPropView;
import g4p.tool.gui.tabview.CtrlTabView;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

@SuppressWarnings("serial")
public class CtrlSketchView extends JTree {


	private CtrlTabView tabs;
	private CtrlPropView props;

	/**
	 * Ctor creates an empty tree;
	 *
	 */
	public CtrlSketchView() {
		super();
		initialise();
		this.setRowHeight(24);
	}

	public void setViewLinks(CtrlTabView tabs, CtrlPropView props){
		this.tabs = tabs;
		this.props = props;
	}

	/**
	 * Set the characteristics for the tree
	 *
	 */
	private void initialise() {
		// Only allow single nodes to be selected
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setCellRenderer(new DataCellRenderer(null));

		// What to do when we click on a control in the tree view
		addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent tse) {
				DBase sel = (DBase) getLastSelectedPathComponent();
				tabs.setSelectedComponent(sel);
				props.showProprtiesFor(sel);
			}
		});
		setEditable(false);
	}

	public void setSelectedComponent(DBase comp){
		DefaultTreeModel m = (DefaultTreeModel) getModel();
		TreeNode[] nodes = m.getPathToRoot(comp);
		TreePath tp = new TreePath(nodes);
		setSelectionPath(tp);
		repaint();
	}

	public DBase getWindowFor(DBase comp){
		DefaultTreeModel m = (DefaultTreeModel) getModel();
		TreeNode[] nodes = m.getPathToRoot(comp);
		DBase w =  (DBase) ((nodes != null && nodes.length >= 2) ? nodes[1] : null);
		return w;
	}

	public DBase getGuiContainerFor(DBase comp){
		DefaultTreeModel m = (DefaultTreeModel) getModel();
		TreeNode[] nodes = m.getPathToRoot(comp);
		// Default to main sketch window
		DBase c = (DBase) ((DefaultMutableTreeNode) m.getRoot()).getChildAt(0);
		if(nodes != null){
			for(int i = nodes.length - 1; i > 0; i--){
				if(nodes[i].getAllowsChildren() && nodes[i] instanceof DWindow || nodes[i] instanceof DPanel){
					c = (DBase)nodes[i];
					break;
				}
			}
		}
//		if(c == null)
//			c = getMainSketchWindow();
		return c;
	}

	public DBase getOptionGroupFor(DBase comp){
		DefaultTreeModel m = (DefaultTreeModel) getModel();
		DBase c = null;
		TreeNode[] nodes = m.getPathToRoot(comp);
		if(nodes != null){
			for(int i = nodes.length - 1; i > 0; i--){
				if(nodes[i] instanceof DToggleGroup){
					c = (DBase)nodes[i];
					break;
				}
			}
		}
		return c;
	}

	public Point getPosition(DBase comp, Point loc){
		if(comp == null){
			return new Point();
		}
		else {
			loc = getPosition((DBase)comp.getParent(), loc);
			loc.x += comp._0820_x;
			loc.y += comp._0821_y;
			return loc;
		}
	}

	public Dimension getSketchSizeFromDesigner() {
		DefaultTreeModel m = (DefaultTreeModel) getModel();
		DBase t0 = (DBase) ((DefaultMutableTreeNode) m.getRoot()).getFirstChild();	
		return new Dimension(t0._0826_width, t0._0827_height);
	}

	public String getSketchRendererFromDesigner(){
		DefaultTreeModel m = (DefaultTreeModel) getModel();
		DWindow t0 = (DWindow) ((DefaultMutableTreeNode) m.getRoot()).getFirstChild();	
		return t0._0031_renderer;
	}

	/**
	 * Certain rules must apply
	 * 1) All window components are added to the application node (root)
	 * 2) In general all controls are added to a window
	 * 3) Option buttons must be added to an option group component
	 */
	public void addComponent(DBase comp) {
		DefaultTreeModel m = (DefaultTreeModel) getModel();
		DefaultMutableTreeNode r = (DefaultMutableTreeNode) m.getRoot();
		Rectangle va = tabs.getVisibleArea();

		if(comp instanceof DWindow){
			m.insertNodeInto(comp, r, r.getChildCount());
			tabs.addWindow(comp);
			setSelectedComponent(comp);
		}
		else if(comp instanceof DTimer){
			// always add to root
			// DBase window = (DBase) r.getChildAt(0);
			m.insertNodeInto(comp, r, r.getChildCount());
			setSelectedComponent(comp);			
		}
		else if(comp instanceof DToggleGroup){
			DBase selected = (DBase) getLastSelectedPathComponent();
			DBase window = getGuiContainerFor(selected);
			if(window != null){
				m.insertNodeInto(comp, window, window.getChildCount());
				setSelectedComponent(comp);
			}
			else {
				undoComponent(comp);
			}
		}
		else if(comp instanceof DOption){
			DBase selected = (DBase) getLastSelectedPathComponent();
			DBase window = getGuiContainerFor(selected);
			DBase opg = getOptionGroupFor(selected);
			if(window != null && opg != null){
				//				comp._0820_x = (window._0826_width - comp._0826_width)/ 2;
				//				comp._0821_y = (window._0827_height - comp._0827_height)/ 2;
				comp._0820_x = va.x + (va.width - comp._0826_width)/ 2;
				comp._0821_y = va.y + (va.height - comp._0827_height)/ 2;
				if(opg.getChildCount() == 0){
					((DOption)comp)._0101_selected = true;
					((DOption)comp).iconNo = 1;
				}
				m.insertNodeInto(comp, opg, opg.getChildCount());
				setSelectedComponent(comp);
			}
			else {
				undoComponent(comp);
			}
		}
		else {
			// Get the currently selected component
			DBase selected = (DBase) getLastSelectedPathComponent();
			// Find the window or panel that contains the selected component
			DBase window = getGuiContainerFor(selected);
			if(window == null){
				System.out.println("Can't find window container");
				System.out.println(comp.getRoot());
				return;
			}
			// Only need to do something if we have a window/panel component
			if(window instanceof DWindow) {
				comp._0820_x = va.x + (va.width - comp._0826_width)/ 2;
				comp._0821_y = va.y + (va.height - comp._0827_height)/ 2;
			}
			else if(window instanceof DPanel) {
				comp._0820_x = (window._0826_width - comp._0826_width)/ 2;
				comp._0821_y = (window._0827_height - comp._0827_height)/ 2;
			}
			m.insertNodeInto(comp, window, window.getChildCount());
			setSelectedComponent(comp);
		}
	}

	/**
	 * Removes the currently selected component from the GUI.
	 */
	public void removeComponent() {
		DBase comp = (DBase) getLastSelectedPathComponent();
		DefaultTreeModel m = (DefaultTreeModel) getModel();
		DefaultMutableTreeNode r = (DefaultMutableTreeNode) m.getRoot();
		if(comp instanceof DApplication){
			return;
		}
		if(comp instanceof DWindow){
			if(comp == r.getFirstChild()){
				System.out.println("Main sketch do not delete");
				return;
			}
			// Remove window from tab view
			tabs.deleteWindow(comp);
		}
		// Component is valid for removal
		DefaultMutableTreeNode p = (DefaultMutableTreeNode) comp.getParent();
		m.removeNodeFromParent(comp);
		undoComponent(comp);
		setSelectedComponent((DBase) p);
	}

	/**
	 * Release component id, name and event name
	 * @param comp
	 */
	private void undoComponent(DBase comp){
		NameGen.instance().remove(comp.get_name());			
		NameGen.instance().remove(comp._0030_eventHandler);
		for(int i = 0; i < comp.id.length; i++)
			IdGen.instance().remove(comp.id[i]);
	}

	public DefaultMutableTreeNode getRoot() {
		return (DefaultMutableTreeNode) getModel().getRoot();
	}
//	public DBase getRoot() {
//		Object obj = getModel().getRoot();
//		System.out.println(obj.getClass().getSimpleName());
//		return (DBase) getModel().getRoot();
//	}

	public void generateDeclarations(ArrayList<String> lines) {
		DefaultTreeModel m = (DefaultTreeModel) getModel();
		DBase r = (DBase) m.getRoot();
		r.make_declaration(lines);
	}

	public void generateEvtMethods(ArrayList<String> lines) {
		DefaultTreeModel m = (DefaultTreeModel) getModel();
		DBase r = (DBase) m.getRoot();
		r.make_event_method(lines);
	}

	public void generateCreator(ArrayList<String> lines) {
		DefaultTreeModel m = (DefaultTreeModel) getModel();
		// Start with application
		DBase r = (DBase) m.getRoot();
		r.make_creator(lines, null, "this");
		r.make_window_loop(lines);
	}

	// ==========================================================================
	// ==========================================================================
	// ======================   Load / save routines   ==========================
	// ==========================================================================


	/**
	 * Save the data model to disk
	 * 
	 * @param file
	 */
	public void saveModel(File file){
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject( (CtrlSketchModel) treeModel);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load the data model from file and if successful attach it to 
	 * this tree and if a node was selected when saved ractivate it
	 *  
	 * @param file
	 */
	public DefaultTreeModel loadModel(File file){
		DefaultTreeModel dm = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			dm = (DefaultTreeModel) ois.readObject();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return dm;
	}

	/**
	 * Class to render the tree nodes in the display
	 *
	 * @author Peter Lager
	 *
	 */
	class DataCellRenderer extends DefaultTreeCellRenderer {

		private Icon cellIcon;

		public DataCellRenderer(Icon icon) {
			cellIcon = icon;
		}

		public Component getTreeCellRendererComponent(
				JTree tree,
				Object value,
				boolean sel,
				boolean expanded,
				boolean leaf,
				int row,
				boolean hasFocus) {

			super.getTreeCellRendererComponent(
					tree, value, sel,
					expanded, leaf, row,
					hasFocus);
			cellIcon = ToolIcon.getIcon(value.getClass());
			setIcon(cellIcon);

			return this;
		}
	}

}
