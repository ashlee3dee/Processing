/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package g4p.tool.gui.tabview;

import g4p.tool.controls.DBase;
import g4p.tool.controls.DWindow;

import java.awt.Rectangle;

/**
 *
 * @author peter
 */
public interface IScrollAreaUser {

	public DWindow getWindowComponent();

	public DBase getSelected();
	
	public void setSelected(DBase comp, Rectangle compRect);

	public void mouseClicked(int mouseX, int mouseY, Rectangle visibleArea);

	public void mousePressed(int mouseX, int mouseY, Rectangle visibleArea);

	public void mouseReleased(int mouseX, int mouseY, Rectangle visibleArea);

	public void mouseMoved(int mouseX, int mouseY, Rectangle visibleArea);

	public void mouseDragged(int mouseX, int mouseY, Rectangle visibleArea);

	public void mouseEntered(int mouseX, int mouseY, Rectangle visibleArea);

	public void mouseExited(int mouseX, int mouseY, Rectangle visibleArea);

	public Rectangle getVisibleArea();
	
	public float getScale();
	
	// Scale as percentage
	public void setScale(int scale);

	// Scale as a fraction
	public void setScale(float scale);
}
