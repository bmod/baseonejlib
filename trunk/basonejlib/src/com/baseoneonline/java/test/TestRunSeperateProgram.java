package com.baseoneonline.java.test;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.baseoneonline.java.nanoxml.XMLElement;
import com.baseoneonline.java.swing.mediaTree.FileNode;
import com.baseoneonline.java.swing.mediaTree.MediaNode;
import com.baseoneonline.java.swing.mediaTree.MediaTreeModel;

public class TestRunSeperateProgram {

	public static void main(final String[] args) {
		FileType.fromXMLFile("FileTypes.xml");
		final GUI app = new GUI();
		// app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setSize(500, 400);
		app.setVisible(true);
	}
}

class MenuItem {

	private final String name;
	private final String path;
	private TreeModel treeModel;

	public MenuItem(final String name, final String path) {
		this.name = name;
		this.path = path;
	}

	public TreeModel getTreeModel() {
		if (null == treeModel) {
			treeModel = new MediaTreeModel(new FileNode(path));
		}
		return treeModel;
	}

	@Override
	public String toString() {
		return name;
	}
}

class GUI extends JFrame {

	DefaultListModel menuModel = new DefaultListModel();
	JTree tree;
	final MediaNode rootNode = new MediaNode() {

		MediaNode[] items = { new FileNode("M:/music"),
				new FileNode("M:/video"), new FileNode("M:/games"),
				new FileNode("M:/Pictures") };

		@Override
		public MediaNode[] getChildren() {
			return items;
		}

		@Override
		public String toString() {
			return "Library";
		}

		@Override
		public boolean isLeaf() {
			return false;
		}
	};

	public GUI() {
		addWindowListener(winAdapter);
		tree = new JTree(new MediaTreeModel(rootNode));
		tree.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getClickCount() == 2) {
					final TreePath path = tree.getClosestPathForLocation(e
							.getX(), e.getY());
					if (null != path) {
						showNode((MediaNode) path.getLastPathComponent());
					}
				}
			}
		});
		add(new JScrollPane(tree));

	}

	Process proc;

	String zsnes = "m:/games/Emulators/ZSNES/zsnesw.exe -m";

	private void showNode(final MediaNode node) {
		if (node instanceof FileNode) {
			final FileNode fnode = (FileNode) node;
			if (fnode.isFile()) {
				final FileType type = FileType.findType(fnode);
				if (null != type) {
					runProgram(type.getPlayer(), fnode);
				}
			}
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

abstract class FileType {

	public abstract String getPlayer();

	public abstract String[] getExtensions();

	private static FileType[] types;

	public static void fromXMLFile(final String xmlFile) {
		final XMLElement xml = new XMLElement();
		final List<FileType> ftypes = new ArrayList<FileType>();
		try {
			xml.parseFromReader(new FileReader(xmlFile));
			for (final XMLElement xType : xml.getChildren("filetype")) {
				final String xPlayer = xType.getChild("player").getContent()
						.trim();
				final String xExt = xType.getChild("extensions").getContent();
				final String[] exts = xExt.split(",");
				for (int i = 0; i < exts.length; i++) {
					exts[i] = exts[i].trim().toLowerCase();
				}
				ftypes.add(new FileType() {

					@Override
					public String[] getExtensions() {
						return exts;
					}

					@Override
					public String getPlayer() {
						return xPlayer;
					}
				});
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		types = ftypes.toArray(new FileType[ftypes.size()]);
	}

	public static FileType findType(final File f) {
		final String name = f.getName().toLowerCase();
		for (final FileType type : types) {
			for (final String ext : type.getExtensions()) {
				if (name.endsWith(ext)) { return type; }
			}
		}
		return null;
	}
}
