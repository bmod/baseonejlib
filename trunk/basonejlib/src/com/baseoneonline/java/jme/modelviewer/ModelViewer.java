package com.baseoneonline.java.jme.modelviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

import com.baseoneonline.java.jlib.utils.DockAppBase;

public class ModelViewer extends DockAppBase {
	
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ModelViewer();
			}
		});
	}
	
	public ModelViewer() {
		super("Model Viewer", ModelViewer.class.getName() + ".cfg",
				ModelViewer.class.getName() + ".layout.xml");
		
		final TreePanel treePanel = new TreePanel();
		addDockable(treePanel, "Tree");
	
		final ViewPanel viewPanel = new ViewPanel();
		addDockable(viewPanel,"View");
		
		final PropertiesPanel propsPanel = new PropertiesPanel();
		addDockable(propsPanel, "Properties");
		
		final LogPanel logPanel = new LogPanel();
		addDockable(logPanel, "Log");
		
		readLayout();
	}

	@Override
	protected boolean closeRequested() {
		return true;
	}
}

class LogPanel extends JPanel {
	public LogPanel() {
		setLayout(new BorderLayout());
		final JTextArea taLog = new JTextArea();
		add(new JScrollPane(taLog));
		
		Logger.getLogger("").addHandler(new Handler() {
			@Override
			public void close() throws SecurityException {
			}
			@Override
			public void flush() {
			}
			@Override
			public void publish(final LogRecord record) {
				taLog.append(record.getMessage()+"\n");
			}
		});
	}
}

class PropertiesPanel extends JPanel {
	public PropertiesPanel() {
		
	}
}

class ViewPanel extends JPanel {
	public ViewPanel() {
		setBackground(Color.BLACK);
	}
}

class TreePanel extends JPanel {
	public TreePanel() {
		setLayout(new BorderLayout());
		final JTree tree = new JTree();
		add(new JScrollPane(tree));
	}
}
