package g4p.tool.gui.tabview;

import g4p.tool.TGuiConstants;
import g4p.tool.controls.DBase;
import g4p.tool.controls.DWindow;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * GUI control encapsulating a ScrollableArea object.
 *
 * The actual scrollable display must be created from a class that inherits from
 * ScrollableArea.
 *
 * @author Peter Lager
 */
@SuppressWarnings("serial")
public class ScrollControl extends JPanel implements IScrollAreaUser, TGuiConstants {

	public static final Color BACKGROUND = new Color(128, 0, 0);
	public static final Color FOREGROUND = new Color(255, 220, 220);
	public static final Font FONT = new Font("Dialog", Font.BOLD, 10);
	public static final int DELTA_Y = 11; // vertical distance between digits
	public static final int DELTA_X = 10; // horizontal size of digit
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;
	public static final int THICKNESS = 20;
	public static final int TICK_SIZE = 4;
	private Rule columnView;
	private Rule rowView;
	private ScrollableArea area;
	private int areaW, areaH;
	private int unitValue = 100;
	private int unitTickGap = 100;
	private int nbrTicksInGap = 4;

	// Runtime variables
	private CtrlTabView tabCtrl;

	public static int gridSize = 4;
	public static boolean showGrid;
	public static boolean snapToGrid;

	DBase window = null;
	DBase selected = null;

	private MutableDBase selInfo = new MutableDBase();

	private int startX, startY;
	private int deltaX, deltaY;

	public ScrollControl(CtrlTabView tabCtrl, DBase window) {
		this(window._0826_width,  window._0827_height);
		this.tabCtrl = tabCtrl;
		this.window = window;
//		area.setFont(displayFont);
		area.setBackground(new Color(255,240,240));
	}

	public ScrollControl(int w, int h) {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		areaW = w;
		areaH = h;
		// Create the appropriate scroll pane
		area = new ScrollableArea(areaW, areaH);
		initScrollPane(area);
	}

	private void initScrollPane(ScrollableArea area) {
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		//Create the row and column headers.
		columnView = new Rule(HORIZONTAL);
		rowView = new Rule(VERTICAL);
		// Set their preferred lengths
		columnView.setPreferredWidth(areaW);
		rowView.setPreferredHeight(areaH);

		//Set up the scroll pane.
		area.setMaxUnitIncrement(columnView.getIncrement());
		JScrollPane psp = new JScrollPane(area);
		area.setScroller(this);

		psp.setPreferredSize(new Dimension(128, 128));
		psp.setViewportBorder(
				BorderFactory.createLineBorder(Color.black));

		psp.setColumnHeaderView(columnView);
		psp.setRowHeaderView(rowView);

		//Set the corners.
		psp.setCorner(JScrollPane.UPPER_LEADING_CORNER, new Corner());
		psp.setCorner(JScrollPane.LOWER_LEADING_CORNER, new Corner());
		psp.setCorner(JScrollPane.UPPER_TRAILING_CORNER, new Corner());
		psp.getViewport().setBackground(new Color(255,240,240));

		//Put it in this panel.
		add(psp);
		// Set the border for this control

		area.validate();
	}

	public ScrollableArea getScrollableArea(){
		return area;
	}

	public void setCanvasSize(int w, int h){
		if(area.setCanvasSize(w, h)){
			revalidate();
		}
	}

	@Override
	public float getScale() {
		return area.scale;
	}

	/**
	 * Set the scale where scale is a proportion e.g.0.75
	 * 
	 * @param scale
	 */
	public void setScale(float scale) {
		if (scale >= 14) {
			unitValue = 5;
			nbrTicksInGap = 0;
		} else if (scale >= 7) {
			unitValue = 10;
			nbrTicksInGap = 0;
		} else if (scale >= 3) {
			unitValue = 20;
			nbrTicksInGap = 1;
		} else if (scale >= 1.5f) {
			unitValue = 50;
			nbrTicksInGap = 4;
		} else if (scale >= 0.6f) {
			unitValue = 100;
			nbrTicksInGap = 4;
		} else if (scale >= 0.4f) {
			unitValue = 500;
			nbrTicksInGap = 4;
		} else {
			unitValue = 1000;
			nbrTicksInGap = 9;
		}
		unitTickGap = Math.round(unitValue * scale);
		area.setScale(scale, columnView.getIncrement());
		Dimension d = area.getPreferredSize();
		columnView.setPreferredWidth(Math.round(d.width));
		rowView.setPreferredHeight(Math.round(d.height));
		columnView.setIncrementAndUnits(unitValue, unitTickGap, nbrTicksInGap);
		rowView.setIncrementAndUnits(unitValue, unitTickGap, nbrTicksInGap);
		repaint();
	}

	/**
	 * Set the scale where scale is a percentage e.g.75%
	 * 
	 * @param scale
	 */
	@Override
	public void setScale(int scale) {
		setScale(scale / 100.0f);
	}

	public Rectangle getVisibleArea(){
		return area.getVisibleArea();

	}
	public void mouseClicked(int mouseX, int mouseY, Rectangle visibleArea) {
		if(selInfo.comp != selected){
			selected = selInfo.comp;
			tabCtrl.componentHasBeenSelected(selected);
		}
	}

	public void mousePressed(int mouseX, int mouseY, Rectangle visibleArea) {
		startX = mouseX;
		startY = mouseY;
		isOver(selInfo, startX, startY);
		if(selInfo.comp != null && selInfo.comp.isSelectable()){
			selected = selInfo.comp;
			tabCtrl.componentHasBeenSelected(selected);   		
		}
	}

	private void isOver(MutableDBase m, int x, int y) {
		selInfo.reset();
		window.isOver(m, x, y);
		if(m.comp != null){
			m.orgX = m.comp._0820_x;
			m.orgY = m.comp._0821_y;
			m.orgW = m.comp._0826_width;
			m.orgH = m.comp._0827_height;
		}		
	}

	public void mouseReleased(int mouseX, int mouseY, Rectangle visibleArea) {
		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
	}

	public void mouseMoved(int mouseX, int mouseY, Rectangle visibleArea) {
	}

	public void mouseDragged(int mouseX, int mouseY, Rectangle visibleArea) {
		if(selInfo.comp != null && selInfo.comp.isSelectable()){
			deltaX = Math.round((mouseX - startX));
			deltaY = Math.round((mouseY - startY));
			if(selInfo.comp.isResizeable()){
				switch(selInfo.selID){
				case OVER_HORZ:
					setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
					selInfo.comp._0826_width = snapValue(selInfo.orgW + deltaX);
					tabCtrl.componentChangedInGUI(selInfo.comp, RESIZED);
					break;
				case OVER_VERT:
					setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
					selInfo.comp._0827_height = snapValue(selInfo.orgH + deltaY);
					tabCtrl.componentChangedInGUI(selInfo.comp, RESIZED);
					break;
				case OVER_DIAG:
					setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
					selInfo.comp._0826_width = snapValue(selInfo.orgW + deltaX);
					selInfo.comp._0827_height = snapValue(selInfo.orgH + deltaY);
					tabCtrl.componentChangedInGUI(selInfo.comp, RESIZED);
					break;
				}
			}
			if(selInfo.comp.isMoveable() && selInfo.selID == OVER_COMP){
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				selInfo.comp._0820_x = snapValue(selInfo.orgX + deltaX);
				selInfo.comp._0821_y = snapValue(selInfo.orgY + deltaY);
				tabCtrl.componentChangedInGUI(selInfo.comp, MOVED);
			}
//			tabCtrl.componentChangedInGUI(selInfo.comp);
			repaint();
		}
	}

	public void mouseEntered(int mouseX, int mouseY, Rectangle visibleArea) {
	}

	public void mouseExited(int mouseX, int mouseY, Rectangle visibleArea) {
	}

	private int snapValue(int nbr){
		return (CtrlTabView.snapToGrid) ? CtrlTabView.gridSize * Math.round(((float)nbr)/((float)CtrlTabView.gridSize)) : nbr;
	}

	public class Corner extends JComponent {

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			// Fill me with dirty brown/orange.
			g.setColor(BACKGROUND);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}

	public class Rule extends JComponent {

		public int orientation;
		private int unitValue = 100;
		private int unitTickGap = 100;
		private int nbrTicksInGap = 4;

		public Rule(int o) {
			orientation = o;
		}

		public void setIncrementAndUnits(int unitValue, int unitTickGap, int nbrTicksInGap) {
			this.unitValue = unitValue;
			this.unitTickGap = unitTickGap;
			this.nbrTicksInGap = nbrTicksInGap;
		}

		public int getIncrement() {
			return unitTickGap / (nbrTicksInGap + 1);
		}

		public void setPreferredHeight(int ph) {
			setPreferredSize(new Dimension(THICKNESS, ph));
		}

		public void setPreferredWidth(int pw) {
			setPreferredSize(new Dimension(pw, THICKNESS));
		}

		private void paintHorz(Graphics g, Rectangle drawHere) {
			// Tick variables
			int end, start, incSpacing = getIncrement();
			boolean major;
			int tickPos, tickLength, tickLabelNbr;
			// Label variables
			String text;
			int textY;
			start = (drawHere.x / incSpacing);
			end = (((drawHere.x + drawHere.width) / incSpacing) + 1);
			textY = getHeight() - TICK_SIZE - 2;
			for (int i = start; i < end; i++) {
				major = (i % (nbrTicksInGap + 1) == 0);
				tickLength = major ? getHeight() : TICK_SIZE;
				tickPos = i * incSpacing;
				g.drawLine(tickPos, THICKNESS - 1, tickPos, THICKNESS - tickLength - 1);
				if (major) {
					tickLabelNbr = i / (nbrTicksInGap + 1);
					text = Integer.toString(tickLabelNbr * unitValue);
					g.drawString(text, tickPos + 2, textY);
				}
			}
		}

		private void paintVert(Graphics g, Rectangle drawHere) {
			// Tick variables
			int end, start, incSpacing = getIncrement();
			boolean major;
			int tickPos, tickLength, tickLabelNbr;
			// Label variables
			String text;
			start = (drawHere.y / incSpacing);
			end = (((drawHere.y + drawHere.height) / incSpacing) + 1);

			for (int i = start; i < end; i++) {
				major = (i % (nbrTicksInGap + 1) == 0);
				tickLength = major ? drawHere.width : TICK_SIZE;
				tickPos = i * incSpacing;
				g.drawLine(THICKNESS - 1, tickPos, THICKNESS - tickLength - 1, tickPos);
				if (major) {
					tickLabelNbr = i / (nbrTicksInGap + 1);
					int textY = tickPos + DELTA_Y;
					text = Integer.toString(tickLabelNbr * unitValue);
					for (int j = 0; j < text.length(); j++) {
						//digit = text.substring(j, j + 1);
						g.drawString(text.substring(j, j + 1), THICKNESS - TICK_SIZE - DELTA_X, textY);
						textY += DELTA_Y;
					}
				}

			}
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Rectangle drawHere = g.getClipBounds();
			// Fill clipping area with dirty brown/orange.
			g.setColor(BACKGROUND);
			g.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);
			// Do the ruler labels in the selected FONT and colour
			g.setFont(FONT);
			g.setColor(FOREGROUND);

			if (orientation == HORIZONTAL) {
				paintHorz(g, drawHere);
			} else {
				paintVert(g, drawHere);
			}
		}
	}


	@Override
	public DWindow getWindowComponent() {
		return (DWindow)window;
	}

	@Override
	public DBase getSelected() {
		return selected;
	}

	@Override
	public void setSelected(DBase comp, Rectangle compRect) {
		selected = comp;
		if(compRect != null)
			area.scrollRectToVisible(compRect);
	}




}
