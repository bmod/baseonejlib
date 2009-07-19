package com.baseoneonline.java.media;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTree;

import com.baseoneonline.java.media.nodes.FileNode;
import com.baseoneonline.java.media.nodes.INode;
import com.baseoneonline.java.media.ui.InfoPanel;
import com.baseoneonline.java.media.ui.MediaTreePanel;
import com.baseoneonline.java.media.ui.MediaTreePanel.TreePanelListener;

public class TestRunSeperateProgram {

	public static void main(final String[] args) {
		final GUI app = new GUI();
		// app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setSize(1024, 800);
		app.setVisible(true);
	}
}

class GUI extends JFrame {

	DefaultListModel menuModel = new DefaultListModel();
	JTree tree;

	public GUI() {
		addWindowListener(winAdapter);
		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		{
			final InfoPanel infoPanel = new InfoPanel();
			final MediaTreePanel treePanel = new MediaTreePanel(getRootNode());
			treePanel.addListener(new TreePanelListener() {

				@Override
				public void nodeClicked(final INode node) {
					infoPanel.setNode(node);
				}

				@Override
				public void nodeDoubleClicked(final INode node) {}

			});
			splitPane.add(treePanel, JSplitPane.LEFT);
			splitPane.add(infoPanel);
		}
		add(splitPane);
	}

	private INode getRootNode() {
		final INode root = XMLMenuBuilder.buildFromFile("mediaMenu.xml",
				"MEDIA");
		return root;
	}

	Process proc;

	private void showNode(final INode node) {
		if (node instanceof FileNode) {
			final FileNode fnode = (FileNode) node;
			System.out.println("Showing node: " + fnode);
		}
	}

	private void runProgram(final String program, final File file) {
		runProgram(program, file.getAbsolutePath());
	}

	private void runProgram(final String program, final String file) {

		final String command = program + " \"" + file + "\"";

		Logger.getLogger(getClass().getName()).info(
				"Running process: " + command);

		if (null != proc) {
			proc.destroy();
		}
		// get the current run time
		final Runtime rt = Runtime.getRuntime();
		// command that needs to be executed.
		try {
			// execute the command as a separate process
			proc = rt.exec(command);

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	WindowAdapter winAdapter = new WindowAdapter() {

		@Override
		public void windowClosing(final WindowEvent e) {
			if (null != proc) proc.destroy();
			System.exit(0);
		}
	};
}