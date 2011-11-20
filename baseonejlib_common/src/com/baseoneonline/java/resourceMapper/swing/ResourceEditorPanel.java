package com.baseoneonline.java.resourceMapper.swing;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.baseoneonline.java.resourceMapper.Resource;
import com.baseoneonline.java.resourceMapper.ResourceNode;
import com.baseoneonline.java.swing.config.Config;

public class ResourceEditorPanel extends JPanel implements
		TreeSelectionListener
{
	private JSplitPane splitPane;
	private JTree tree;
	private final ResourcePropertyEditor resourcePropertyEditor;

	public ResourceEditorPanel(Resource rootResource)
	{
		initComponents();

		Config.get().persist(getClass().getName() + "Splitpane", splitPane);

		tree.setModel(new ResourceTreeModel(rootResource));
		tree.setCellRenderer(new ResourceTreeCellRenderer());
		tree.addTreeSelectionListener(this);

		resourcePropertyEditor = new ResourcePropertyEditor();
		resourcePropertyEditor.setBorder(new EmptyBorder(8, 0, 0, 0));
		splitPane.setRightComponent(resourcePropertyEditor);
	}

	private void initComponents()
	{
		setLayout(new BorderLayout(0, 0));

		splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);

		tree = new JTree();
		tree.setShowsRootHandles(true);
		tree.setRootVisible(false);
		scrollPane.setViewportView(tree);
		splitPane.setDividerLocation(200);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		ResourceNode resNode = (ResourceNode) e.getPath()
				.getLastPathComponent();
		resourcePropertyEditor.setResource(resNode);
	}

}