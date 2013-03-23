package com.baseoneonline.jlib.ardor3d.tools;

import java.awt.BorderLayout;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreeCellRenderer;

import com.ardor3d.renderer.state.RenderState;
import com.ardor3d.scenegraph.Node;
import com.baseoneonline.java.swing.config.Config;
import com.baseoneonline.java.swing.extendedTree.ExtendedCellRenderer;
import com.baseoneonline.java.swing.extendedTree.ExtendedNodeAdapter;
import com.baseoneonline.java.swing.extendedTree.ExtendedTreeModel;
import com.baseoneonline.jlib.ardor3d.swing.Icons;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.l2fprod.common.propertysheet.PropertySheetTable;
import com.l2fprod.common.propertysheet.PropertySheetTableModel;

public class SceneGraphPanel extends JPanel {

	private final ExtendedTreeModel model = new ExtendedTreeModel();

	private final TreeCellRenderer cellRenderer = new ExtendedCellRenderer(
			model);
	private PropertySheetPanel table;
	private JTree tree;
	private JSplitPane splitPane;

	public SceneGraphPanel() {
		model.addAdapter(new NodeAdapter());
		model.addAdapter(new RenderStateAdapter());

		initComponents();

		tree.setModel(model);
		tree.setCellRenderer(cellRenderer);
		tree.getSelectionModel().addTreeSelectionListener(
				new TreeSelectionListener() {

					@Override
					public void valueChanged(TreeSelectionEvent e) {
						Object selection = e.getNewLeadSelectionPath()
								.getLastPathComponent();

						table.setBeanInfo(getBeanInfo(selection));
					}
				});
		table.addPropertySheetChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println("Changed " + evt.getNewValue());

			}
		});

		Config.get().persist(getClass().getName() + "SplitPane", splitPane);
	}

	private static BeanInfo getBeanInfo(Object ob) {
		BeanInfo inf;
		try {
			return Introspector.getBeanInfo(ob.getClass());
		} catch (IntrospectionException e1) {
			throw new RuntimeException(e1);
		}
	}

	public void setRootNode(final Node root) {
		model.setRoot(root);
	}

	private void initComponents() {
		setLayout(new BorderLayout(0, 0));
		splitPane = new JSplitPane();
		splitPane.setContinuousLayout(true);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splitPane);

		JScrollPane topScrollPane = new JScrollPane();
		splitPane.setLeftComponent(topScrollPane);

		tree = new JTree();
		topScrollPane.setViewportView(tree);

		JScrollPane bottomScrollPane = new JScrollPane();
		splitPane.setRightComponent(bottomScrollPane);

		table = new PropertySheetPanel();
		bottomScrollPane.setViewportView(table);
	}

}

class RenderStateAdapter extends ExtendedNodeAdapter<RenderState> {

	private static Icon ICON = Icons.load("icons/diamond-orange.png");

	@Override
	public List<Object> getChildren(RenderState node) {
		return null;
	}

	@Override
	public Icon getIcon(RenderState value) {
		return ICON;
	}

	@Override
	public Class<RenderState> getType() {
		return RenderState.class;
	}

	@Override
	public String getLabel(RenderState node) {
		return node.getClass().getSimpleName();
	}

}

class NodeAdapter extends ExtendedNodeAdapter<Node> {

	private static Icon ICON = Icons.load("icons/cube-grey.png");

	@Override
	public List<Object> getChildren(Node parent) {
		List<Object> children = new ArrayList<Object>();
		children.addAll(parent.getChildren());
		children.addAll(parent.getLocalRenderStates().values());
		return children;
	}

	@Override
	public Class<Node> getType() {
		return Node.class;
	}

	@Override
	public String getLabel(Node n) {
		return n.getName();
	}

	@Override
	public Icon getIcon(Node value) {
		return ICON;
	}

}

class PropertyTableModel extends AbstractTableModel {

	private Object object = new Node("Blaat");

	public PropertyTableModel() {

	}

	public void setObject(Object object) {
		this.object = object;
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		if (null == object)
			return 0;

		return getBeanInfo().getPropertyDescriptors().length;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	private BeanInfo getBeanInfo() {
		try {
			return Introspector.getBeanInfo(object.getClass());
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String name = getBeanInfo().getPropertyDescriptors()[rowIndex]
				.getName();
		if (columnIndex == 0) {
			return name;
		} else if (columnIndex == 1) {
			return getBeanInfo().getPropertyDescriptors()[rowIndex]
					.getValue(name);
		}

		return null;
	}
}

class ExtendedTable extends JPanel {

	private final JTable table = new JTable();

	public ExtendedTable() {
		setLayout(new BorderLayout());
		PropertySheetTableModel model = new PropertySheetTableModel();
		PropertySheetTable table = new PropertySheetTable();

	}
}