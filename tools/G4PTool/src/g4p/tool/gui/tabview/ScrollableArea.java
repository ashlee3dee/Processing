/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package g4p.tool.gui.tabview;

import g4p.tool.G;
import g4p.tool.TGuiConstants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

/**
 *
 * @author Peter Lager
 */
@SuppressWarnings("serial")
public class ScrollableArea extends JPanel
implements Scrollable, MouseListener, MouseMotionListener, TGuiConstants {

	// This is used to pass mouse events back to the controller if needed
	protected IScrollAreaUser user = null;
	
	protected int originalW, originalH;
	protected int canvasW, canvasH;
	protected Rectangle visibleArea = new Rectangle();
	protected int mouseX, mouseY;
	protected float scale = 1.0f;
	protected int maxUnitIncrement = 1;

	protected boolean isOver = false;

	protected ScrollableArea() {
		//Let the user scroll by dragging to outside the window.
		setAutoscrolls(true); //enable synthetic drag events
		addMouseListener(this); //mouse click etc.    
		addMouseMotionListener(this); //handle mouse drags    
		setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
		setBackground(Color.white);
	}

    public ScrollableArea(int w, int h) {
        this();
        originalW = canvasW = w;
        originalH = canvasH = h;
        setPreferredSize(new Dimension(canvasW, canvasH));
    }
    
	public void setScroller(IScrollAreaUser pane) {
		if (pane != null && user == null) {
			user = pane;
		}
	}

	public void centerOn(int x, int y) {
		Rectangle r = this.getVisibleRect();
		r.x = x - r.width / 2;
		r.y = y - r.height / 2;
		scrollRectToVisible(r);
	}

	public void setScale(float newScale, int mui) {
		if(newScale != scale){
			// Get new:old scale ratio
			float sf = newScale / scale;
			// Get current position for canvas top-left corner
			Point loc = this.getLocation();
			// Get current visible area size
			Rectangle r = this.getVisibleRect();
			// Calculate the current visible image center
			loc.x = -loc.x + r.width / 2;
			loc.y = -loc.y + r.height / 2;
			// Calculate center for new scale
			loc.x = Math.round(loc.x * sf);
			loc.y = Math.round(loc.y * sf);
			// Calculate and use size of scaled canvas
			canvasW = Math.round(originalW * newScale);
			canvasH = Math.round(originalH * newScale);
			setPreferredSize(new Dimension(canvasW, canvasH));
			// Update max increment
			maxUnitIncrement = mui;
			// Now center image so zoom appears to be on the center
			centerOn(loc.x, loc.y);
			scale = newScale;
			revalidate();
		}
	}

    @Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Point loc = this.getLocation();
		Rectangle bounds = g.getClipBounds();
		visibleArea.x = Math.round(-loc.x / scale);
		visibleArea.y = Math.round(-loc.y / scale);
		visibleArea.width = Math.round(bounds.width / scale);
		visibleArea.height = Math.round(bounds.height / scale);

		Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        G.pushMatrix(g2d);
        
        g2d.scale(scale, scale);
        g2d.setStroke(stdStroke);
        // Draw window and components
        user.getWindowComponent().draw(g2d, user.getSelected());
        G.popMatrix(g2d);
     }
    
    public boolean setCanvasSize(int w, int h) {
        if (w != originalW || h != originalH) {
        	originalW = w;
        	originalH = h;
        	canvasW = Math.round(originalW * scale);
        	canvasH = Math.round(originalH * scale);       	
            int deltaW = w - canvasW;
            int deltaH = w - canvasH;
 
            setPreferredSize(new Dimension(canvasW, canvasH));
            if (deltaW < 0 || deltaH < 0) {
                deltaW = Math.min(deltaW, 0);
                deltaH = Math.min(deltaH, 0);
                Point loc = this.getLocation();
                loc.x += deltaW;
                loc.y += deltaH;
                setLocation(loc);
            }
            revalidate();
            return true;
        }
        return false;
   }

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(canvasW, canvasH);
	}

	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation,
			int direction) {
		//Get the current position.
		int currentPosition;
		if (orientation == SwingConstants.HORIZONTAL) {
			currentPosition = visibleRect.x;
		} else {
			currentPosition = visibleRect.y;
		}
		//Return the number of pixels between currentPosition
		//and the nearest tick mark in the indicated direction.
		if (direction < 0) {
			int newPosition = currentPosition
					- (currentPosition / maxUnitIncrement)
					* maxUnitIncrement;
			return (newPosition == 0) ? maxUnitIncrement : newPosition;
		} else {
			return ((currentPosition / maxUnitIncrement) + 1)
					* maxUnitIncrement
					- currentPosition;
		}
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation,
			int direction) {
		if (orientation == SwingConstants.HORIZONTAL) {
			return visibleRect.width - maxUnitIncrement;
		} else {
			return visibleRect.height - maxUnitIncrement;
		}
	}

	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	public void setMaxUnitIncrement(int pixels) {
		maxUnitIncrement = pixels;
	}

	public int getCanvasWidth() {
		return canvasW;
	}

	public Rectangle getVisibleArea(){
		return visibleArea;
	}

	public int getCanvasHeight() {
		return canvasH;
	}

	public void mouseClicked(MouseEvent e) {
		calculateMousePosition(e.getX(), e.getY());
		user.mouseClicked(mouseX, mouseY, visibleArea);
	}

	public void mouseEntered(MouseEvent e) {
		isOver = true;
		calculateMousePosition(e.getX(), e.getY());
		user.mouseEntered(mouseX, mouseY, visibleArea);
	}

	public void mouseExited(MouseEvent e) {
		isOver = false;
		calculateMousePosition(e.getX(), e.getY());
		user.mouseExited(mouseX, mouseY, visibleArea);
	}

	public void mousePressed(MouseEvent e) {
		calculateMousePosition(e.getX(), e.getY());
		user.mousePressed(mouseX, mouseY, visibleArea);
	}

	public void mouseReleased(MouseEvent e) {
		calculateMousePosition(e.getX(), e.getY());
		user.mouseReleased(mouseX, mouseY, visibleArea);
	}

	public void mouseDragged(MouseEvent e) {
		calculateMousePosition(e.getX(), e.getY());
		user.mouseDragged(mouseX, mouseY, visibleArea);
		//The user is dragging us, so scroll!
		Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
		scrollRectToVisible(r);
	}

	public void mouseMoved(MouseEvent e) {
		isOver = true;
		calculateMousePosition(e.getX(), e.getY());
		user.mouseMoved(mouseX, mouseY, visibleArea);
	}

	public boolean isMouseOver(){
		return isOver;
	}

	protected void calculateMousePosition(int mx, int my) {
		mouseX = Math.round(mx / scale);
		mouseY = Math.round(my / scale);
	}
}