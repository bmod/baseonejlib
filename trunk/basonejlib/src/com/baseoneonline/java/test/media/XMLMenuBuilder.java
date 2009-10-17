package com.baseoneonline.java.test.media;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.baseoneonline.java.nanoxml.XMLElement;
import com.baseoneonline.java.test.media.nodes.DefaultNode;
import com.baseoneonline.java.test.media.nodes.FileNode;
import com.baseoneonline.java.test.media.nodes.INode;

public class XMLMenuBuilder {

	private static final String NODE_LAUNCHER = "LAUNCHER";
	private static final String NODETYPE_FILENODE = "FILENODE";
	private static final String NODETYPE_NODE = "NODE";
	private static final String ATT_NAME = "LABEL";
	private static final String ATT_PATH = "PATH";
	private static final String ATT_EXTENSIONS = "EXTENSIONS";

	private static Logger Log = Logger
			.getLogger(XMLMenuBuilder.class.getName());

	public static INode buildFromFile(final String filename, final String name) {
		final XMLElement xml = new XMLElement();
		try {
			xml.parseFromReader(new FileReader(filename));
			checkForProblems(xml);
			final DefaultNode root = new DefaultNode(name);
			parse(xml, root);
			return root;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return new DefaultNode("Unable to read xml menu");
	}

	private static void parse(final XMLElement xml, final DefaultNode parent) {
		for (final XMLElement xChild : xml.getChildren()) {
			final String name = xChild.getStringAttribute(ATT_NAME, "Unnamed");

			if (xChild.getName().equalsIgnoreCase(NODETYPE_NODE)) {
				final DefaultNode node = new DefaultNode(name);
				parent.add(node);
				parse(xChild, node);
			} else if (xChild.getName().equalsIgnoreCase(NODETYPE_FILENODE)) {
				final String path = xChild.getStringAttribute(ATT_PATH);
				final XMLElement xLauncher = xChild.getChild(NODE_LAUNCHER);
				if (null != xLauncher) {
					final Launcher launcher = createLauncher(xLauncher);
					parent.add(new FileNode(path, name, launcher));
				}
			}
		}
	}

	private static Launcher createLauncher(final XMLElement xl) {
		final Launcher ln = new Launcher();
		final String extString = xl.getStringAttribute(ATT_EXTENSIONS);
		if (null != extString) {
			final String[] exts = extString.split(",");
			final ArrayList<String> extensions = new ArrayList<String>();
			for (final String ext : exts) {
				if (!ext.equals("")) extensions.add(ext.trim());
			}
			ln.extensions = extensions.toArray(new String[extensions.size()]);
		}
		return ln;
	}

	private static void checkForProblems(final XMLElement xml) {
		final ArrayList<String> paths = new ArrayList<String>();
		for (final XMLElement x : xml.getChildren(true)) {
			if (x.getName().equalsIgnoreCase(NODETYPE_FILENODE)) {
				final String path = x.getStringAttribute(ATT_PATH, "");
				final File f = new File(path);
				if (!f.exists()) {
					Log.warning("Path does not exist: " + path);
				}
				if (null != path && !path.equals("")) {
					if (containsString(paths, path, false)) {
						Log.warning("Path is present twice or more: " + path);
					} else {
						paths.add(path);
					}
				}
			}
		}
	}

	private static boolean containsString(final List<String> list,
			final String string, final boolean caseSensitive) {
		for (final String str : list) {
			if (caseSensitive) {
				if (str.equals(string)) return true;
			} else {
				if (str.equalsIgnoreCase(string)) return true;
			}
		}
		return false;
	}

}
