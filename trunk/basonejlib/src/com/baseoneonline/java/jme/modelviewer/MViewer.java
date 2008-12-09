package com.baseoneonline.java.jme.modelviewer;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.jme.scene.Node;
import com.jmex.model.collada.ColladaImporter;
import com.jmex.xml.xml.XmlException;


public class MViewer extends JFrame {
	
	private final Logger log = Logger.getLogger(getClass().getName());
	
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final MViewer app = new MViewer();
				app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				app.setSize(800,600);
				app.setVisible(true);
			}
		});
	}
	
	public MViewer() {
		final String file = "C:/Documents and Settings/bask/My Documents/scene.dae";
		
		setLayout(new BorderLayout());
		
		final SceneModel treeModel = new SceneModel();
		final JTree tree = new JTree(treeModel);
		
		add(tree);
		
		final Node model = loadCollada(file);
		treeModel.setNode(model);
	}
	
	
	private Node loadCollada(final String filename) {
		try {
			ColladaImporter.load(new FileInputStream(new File(filename)), filename);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final XmlException e) {
			log.warning("XML parse error");
		} catch (final NullPointerException e) {
			log.warning("Null pointer exception");
		}
		return ColladaImporter.getModel();
	}
	
	
	
	
}

class SceneModel implements TreeModel {
	
	private final List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();

	
	private Node node;
	
	public void setNode(final Node n) {
		node = n;
		fireContentsChanged();
	}
	
	@Override
	public void addTreeModelListener(final TreeModelListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	@Override
	public Object getChild(final Object parent, final int index) {
		return ((Node)parent).getChildren().get(index);
	}

	@Override
	public int getChildCount(final Object parent) {
		if (null == ((Node)parent).getChildren()) {
			return 0;
		}
		return ((Node)parent).getChildren().size();
	}

	@Override
	public int getIndexOfChild(final Object parent, final Object child) {
		return ((Node)parent).getChildren().indexOf(child);
	}

	@Override
	public Object getRoot() {
		return node;
	}

	@Override
	public boolean isLeaf(final Object node) {
		return getChildCount(node) < 1;
	}

	@Override
	public void removeTreeModelListener(final TreeModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void valueForPathChanged(final TreePath path, final Object newValue) {
	}
	
	private void fireContentsChanged() {
		final Object[] path = {node};
		final TreeModelEvent e = new TreeModelEvent(this, path);
		for (final TreeModelListener l : listeners) {
			l.treeStructureChanged(e);
		}
	}
	
}
