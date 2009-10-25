package com.baseoneonline.java.jme;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.jme.scene.Node;

public class SceneOutliner extends JPanel {

    private static final long serialVersionUID = 1L;
    private JPanel northPanel = null;
    private JButton btRefresh = null;
    private JTree sceneTree = null;
    private JScrollPane jScrollPane = null;
    private final SceneGraphTreeModel sceneModel;
    private JSplitPane jSplitPane = null;
    private JPanel jPanel = null;
    private JTextPane jTextPane = null;

    /**
     * This is the default constructor
     */
    public SceneOutliner() {
	super();
	sceneModel = new SceneGraphTreeModel();
	getSceneTree().setModel(sceneModel);
	getSceneTree().setCellRenderer(new SceneGraphCellRenderer());
	initialize();
    }

    public void setRootNode(final Node root) {
	sceneModel.setRootNode(root);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	this.setSize(300, 200);
	this.setLayout(new BorderLayout());
	this.add(getNorthPanel(), BorderLayout.NORTH);
	this.add(getJSplitPane(), BorderLayout.CENTER);
    }

    /**
     * This method initializes northPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getNorthPanel() {
	if (northPanel == null) {
	    final FlowLayout flowLayout = new FlowLayout();
	    flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
	    northPanel = new JPanel();
	    northPanel.setLayout(flowLayout);
	    northPanel.add(getBtRefresh(), null);
	}
	return northPanel;
    }

    /**
     * This method initializes btRefresh
     * 
     * @return javax.swing.JButton
     */
    private JButton getBtRefresh() {
	if (btRefresh == null) {
	    btRefresh = new JButton();
	    btRefresh.setText("Rerfresh");
	}
	return btRefresh;
    }

    /**
     * This method initializes sceneTree
     * 
     * @return javax.swing.JTree
     */
    private JTree getSceneTree() {
	if (sceneTree == null) {
	    sceneTree = new JTree();
	}
	return sceneTree;
    }

    /**
     * This method initializes jScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
	if (jScrollPane == null) {
	    jScrollPane = new JScrollPane();
	    jScrollPane.setViewportView(getSceneTree());
	}
	return jScrollPane;
    }

    /**
     * This method initializes jSplitPane
     * 
     * @return javax.swing.JSplitPane
     */
    private JSplitPane getJSplitPane() {
	if (jSplitPane == null) {
	    jSplitPane = new JSplitPane();
	    jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
	    jSplitPane.setBottomComponent(getJPanel());
	    jSplitPane.setTopComponent(getJScrollPane());
	}
	return jSplitPane;
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
	if (jPanel == null) {
	    jPanel = new JPanel();
	    jPanel.setLayout(new BorderLayout());
	    jPanel.add(getJTextPane(), BorderLayout.CENTER);
	}
	return jPanel;
    }

    /**
     * This method initializes jTextPane
     * 
     * @return javax.swing.JTextPane
     */
    private JTextPane getJTextPane() {
	if (jTextPane == null) {
	    jTextPane = new JTextPane();
	}
	return jTextPane;
    }

} // @jve:decl-index=0:visual-constraint="-1,0"

class SceneGraphCellRenderer extends DefaultTreeCellRenderer {

    private static Icon nodeIcon = getIcon("nodeIcon.png");

    private static Icon getIcon(final String name) {
	final Icon icon = new ImageIcon(SceneGraphCellRenderer.class
		.getClassLoader().getResource("assets/sceneGraphTree/" + name));
	return icon;
    }

    @Override
    public Component getTreeCellRendererComponent(final JTree tree,
	    final Object value, final boolean sel, final boolean expanded,
	    final boolean leaf, final int row, final boolean hasFocus) {
	super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
		row, hasFocus);
	if (value instanceof Node) {
	    setIcon(nodeIcon);
	}
	return this;
    }

}
