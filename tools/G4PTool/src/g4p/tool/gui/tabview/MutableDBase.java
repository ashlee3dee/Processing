package g4p.tool.gui.tabview;

import g4p.tool.TGuiConstants;
import g4p.tool.ToolMessages;
import g4p.tool.controls.DBase;

public class MutableDBase implements TGuiConstants {

	public DBase comp = null;
	public int area = Integer.MAX_VALUE;
	public int selID = OVER_NONE;
	public int orgX,orgY,orgW,orgH;

	public MutableDBase(){	}

	public void reset(){
		comp = null;
		area = Integer.MAX_VALUE;
		selID = OVER_NONE;
	}

	public void reset(DBase comp){
		this.comp = comp;
		area = comp.getSize();
		selID = OVER_NONE;
	}

	public String toString(){
		return ToolMessages.build("{0}   over  {1}", comp, selID);
	}
}
