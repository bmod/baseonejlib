package com.baseoneonline.java.jme.modelviewer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EventObject;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.xml.parsers.ParserConfigurationException;

import org.jdesktop.application.Action;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;
import org.xml.sax.SAXException;

import com.baseoneonline.java.jme.modelviewer.action.FileOpenAction;
import com.baseoneonline.java.jme.modelviewer.action.FileOpenTask;
import com.baseoneonline.java.jme.modelviewer.action.QuitAction;
import com.baseoneonline.java.jme.modelviewer.gui.LogPanel;
import com.baseoneonline.java.jme.modelviewer.gui.PropertiesPanel;
import com.baseoneonline.java.jme.modelviewer.gui.TreePanel;
import com.baseoneonline.java.jme.modelviewer.gui.ViewPanel;
import com.baseoneonline.java.jme.modelviewer.util.Const;
import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockingDesktop;

public class ModelViewer extends SingleFrameApplication implements Const {

	private final Logger log = Logger.getLogger(getClass().getName());

	private static ModelViewer instance;

	private String layoutFilename;

	private ResourceMap resource;

	private final DockingDesktop desk = new DockingDesktop();

	public static void main(final String[] args) {
		launch(ModelViewer.class, args);
	}

	@Override
	protected void initialize(final String[] args) {
		final ApplicationContext ctx = getContext();
		final org.jdesktop.application.ResourceManager mgr = ctx
				.getResourceManager();
		resource = mgr.getResourceMap(getClass());
	}

	@Override
	protected void startup() {
		final JPanel mainPanel = new JPanel(new BorderLayout());
		final JMenu fileMenu = new JMenu("File");
		fileMenu.add(new JMenuItem(new FileOpenAction()));
		fileMenu.add(new JSeparator());
		fileMenu.add(new JMenuItem(new QuitAction()));

		final JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);

		layoutFilename = resource.getString(LAYOUT_FILE, "layout.xml");
		final TreePanel treePanel = new TreePanel();
		addDockable(treePanel, "Tree");

		final ViewPanel viewPanel = new ViewPanel();
		addDockable(viewPanel, "View");

		final PropertiesPanel propsPanel = new PropertiesPanel();
		addDockable(propsPanel, "Properties");

		final LogPanel logPanel = new LogPanel();
		addDockable(logPanel, "Log");

		readLayout();
		mainPanel.add(menuBar, BorderLayout.NORTH);
		mainPanel.add(desk);
		show(mainPanel);
	}

	@Override
	public void exit(final EventObject e) {
		if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null,
				"Quit?", "Quit?", JOptionPane.YES_NO_OPTION)) {
			writeLayout();
			super.exit(e);
		}
	}

	private void addDockable(final JPanel comp, final String name) {
		final DockKey key = new DockKey(name);
		key.setCloseEnabled(false);
		key.setFloatEnabled(false);
		key.setAutoHideEnabled(false);
		final Dockable dockable = new Dockable() {
			@Override
			public Component getComponent() {
				return comp;
			}

			@Override
			public DockKey getDockKey() {
				return key;
			}
		};
		desk.addDockable(dockable);
	}

	private void readLayout() {
		try {
			desk.readXML(new FileInputStream(new File(layoutFilename)));
			log.info("Layout read: " + layoutFilename);
		} catch (final FileNotFoundException e) {
			log.warning("Layout file not found: " + layoutFilename);
		} catch (final ParserConfigurationException e) {
			log.warning("Layout parsing failed, removing file: "
					+ layoutFilename);
		} catch (final IOException e) {
			log.warning("IO Error reading: " + layoutFilename);
		} catch (final SAXException e) {
			log.warning("Layout SAX parsing failed, removing file: "
					+ layoutFilename);
		}
	}

	private void writeLayout() {
		try {
			desk.writeXML(new FileOutputStream(new File(layoutFilename)));
			log.info("Layout written: " + layoutFilename);
		} catch (final FileNotFoundException e1) {
			log.warning("File not found: " + layoutFilename);
		} catch (final IOException e1) {
			log.warning("IOError writing to file: " + layoutFilename);
		}
	}
	
	@Action Task<Void, Void> openFile() {
		
		return new FileOpenTask();
	}

	public static ModelViewer getModelViewer() {
		return (ModelViewer) getInstance();
	}
}
