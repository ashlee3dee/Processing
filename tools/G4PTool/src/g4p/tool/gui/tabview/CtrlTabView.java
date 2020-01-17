package g4p.tool.gui.tabview;

import g4p.tool.controls.DBase;
import g4p.tool.controls.DWindow;
import g4p.tool.gui.ToolIcon;
import g4p.tool.gui.propertygrid.CtrlPropView;
import g4p.tool.gui.treeview.CtrlSketchView;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * @author Peter Lager
 *
 */
@SuppressWarnings("serial")
public class CtrlTabView extends JTabbedPane implements ChangeListener {

	private CtrlSketchView tree;
	private CtrlPropView props;

	public static int gridSize = 4;
	public static boolean showGrid;
	public static boolean snapToGrid;

	public static Color gridCol = new Color(0,0,0,48);

	HashMap<DBase, ScrollControl> tabMap; 

	public CtrlTabView(){
		tabMap =  new HashMap<DBase, ScrollControl>();
		this.addChangeListener(this);
	}

	public void setViewLinks(CtrlSketchView tree, CtrlPropView props){
		this.tree = tree;
		this.props = props;
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		getSelectedComponent().repaint();
	}

	/* (non-Javadoc)
	 * @see g4p.tool.gui.IWindowView#addWindow(g4p.tool.components.DBase)
	 */
	public void addWindow(DBase winComp){
		ScrollControl winView = new ScrollControl(this, winComp);
		tabMap.put(winComp, winView);
		addTab(winComp.get_name(), ToolIcon.getIcon(DWindow.class), winView);
	}

	/* (non-Javadoc)
	 * @see g4p.tool.gui.IWindowView#deleteWindow(g4p.tool.components.DBase)
	 */
	public boolean deleteWindow(DBase window){
		ScrollControl winView = tabMap.get(window);
		if(winView != null){
			remove(winView); // remove tab
			tabMap.remove(window);
			return true;
		}
		return false;		
	}

	public Rectangle getVisibleArea() {
		ScrollControl winView = (ScrollControl) this.getSelectedComponent();
		return winView.getVisibleArea();
	}

	private void setSelectedTab(DBase window){
		ScrollControl winView = tabMap.get(window);
		if(winView != null)
			setSelectedComponent(winView);
	}

	/**
	 * Set selected component and make it visible in the scroll control.
	 * If necessary change tab and then update component
	 * @param comp
	 */
	public void setSelectedComponent(DBase comp){
		// Clear current selection
		((ScrollControl) getSelectedComponent()).setSelected(null, null);
		// Now check for component passed
		if(comp != null){
			// If the component is a Window then simply make this the current tab
			if(comp instanceof DWindow){
				//winView = tabMap.get(comp);
				setSelectedTab(comp);
			} 
			else { 
				// Whatever else it is make its window the current tab
				DBase window = tree.getWindowFor(comp);
				setSelectedTab(window);
				
				// So its not a window so find its GUI position and make sure it becomes visible
				Point loc = tree.getPosition(comp, null);
				Rectangle compRect = new Rectangle(loc.x - 10, loc.y -10, comp._0826_width + 20, comp._0827_height + 20);
				((ScrollControl) getSelectedComponent()).setSelected(comp, compRect);
			}
			tree.setSelectedComponent(comp);
		}
		repaint();
	}

	/**
	 * Call this if the name of any window has changed
	 */
	public void updateCurrentTabName(){
		int selIdx = this.getSelectedIndex();
		ScrollControl sc = (ScrollControl) getComponentAt(selIdx);
		DWindow window = sc.getWindowComponent();
		String winname = window.get_name();
		setTitleAt(selIdx, winname);
		// First change the canvas size
		sc.setCanvasSize(Math.round(window._0826_width), Math.round(window._0827_height));
		// Now change the scale
		float scale = Float.valueOf(window._0008_display_scale) / 100.0f;
		sc.setScale(scale);
	}



	public void updateCurrentDisplay() {
		for(ScrollControl sc : tabMap.values()){
			sc.getScrollableArea().revalidate();
		}
	}

	/**
	 * Change of state caused by clicking on a tab for another GWindow
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		CtrlTabView sourceTabbedPane = (CtrlTabView) changeEvent.getSource();
		ScrollControl winView = (ScrollControl) sourceTabbedPane.getSelectedComponent();
		if(winView != null){
			DBase comp = winView.getWindowComponent();
			props.showProprtiesFor(comp);
			tree.setSelectedComponent(comp);
		}
	}

	public void componentHasBeenSelected(DBase comp) {
		if(comp != null)
			tree.setSelectedComponent(comp);		
	}

	/**
	 * This method is called if the control is moved or resized in the GUI
	 * @param comp the control
	 * @param action MOVED or RESIZED
	 */
	public void componentChangedInGUI(DBase comp, int action) {
		comp.updatedInGUI(action);
		props.modelHasBeenChanged();
	}

	public void scaleWindow(int scale) {
		ScrollControl winView = (ScrollControl) this.getSelectedComponent();
		winView.setScale(scale);
	}

	public void setGridSize(int gsize) {
		gridSize = gsize;
		repaint();
	}

	public void setShowGrid(boolean show) {
		showGrid = show;		
		repaint();
	}

	public void setSnapToGrid(boolean snap) {
		snapToGrid = snap;		
		repaint();
	}

	public void deleteAllWindows() {
		this.removeAll();
	}

}
