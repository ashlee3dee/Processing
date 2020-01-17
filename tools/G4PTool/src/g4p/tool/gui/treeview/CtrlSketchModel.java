package g4p.tool.gui.treeview;

import java.io.Serializable;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;


@SuppressWarnings("serial")
public class CtrlSketchModel extends DefaultTreeModel implements Serializable {

	/**
	 * Ctor creates a DataModel object with the given root
	 * 
	 * @param root the node that is the top of a tree
	 */
	public CtrlSketchModel(TreeNode root) {
		super(root);
	}

	/**
	 * Ctor for an empty DataModel
	 *
	 */
	public CtrlSketchModel() {
		super(null);
	}

	/**
	 * Determines whether a given node is the root node of the 
	 * data model
	 * 
	 * @param node
	 * @return true if node is the root else false
	 */
	public boolean isRoot(DefaultMutableTreeNode node){
		return (node == root);
	}

}
