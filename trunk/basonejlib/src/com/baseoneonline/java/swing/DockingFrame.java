package com.baseoneonline.java.swing;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockableState;
import com.vlsolutions.swing.docking.DockingDesktop;

public abstract class DockingFrame extends JFrame {

	private final Preferences prefs;
	private static final String APP_WIDTH = "appWidth",
			APP_HEIGHT = "appHeight", APP_X = "appX", APP_Y = "appY";
	File layoutFile = new File(getClass().getName() + ".defaultLayout.xml");

	private DockingDesktop desk = new DockingDesktop();

	public DockingFrame() {

		Class<?> id = getClass();

		prefs = Preferences.userNodeForPackage(id);
		setTitle(id.getSimpleName());
		add(desk);
		
		restoreFrame();

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				saveLayout();
				quit();
			}
		});

		initFrame();
		readLayout();

		setVisible(true);

	}

	private void restoreFrame() {
		int w = prefs.getInt(APP_WIDTH, 800);
		int h = prefs.getInt(APP_HEIGHT, 600);
		int x = prefs.getInt(APP_X, 0);
		int y = prefs.getInt(APP_Y, 0);
		final int sw = Toolkit.getDefaultToolkit().getScreenSize().width;
		final int sh = Toolkit.getDefaultToolkit().getScreenSize().height;
		if (w > sw)
			w = sw;
		if (h > sh)
			h = sh;

		if (x < 0)
			x = 0;
		if (x + w > sw)
			x = sw - w;
		if (y < 0)
			y = 0;
		if (y + h > sh)
			y = sh - h;

		setSize(w, h);
		setLocation(x, y);

	}

	private void readLayout() {
		try {
			desk.readXML(new FileInputStream(layoutFile));

			for (DockableState d : desk.getDockables()) {
				if (d.isClosed()) {
					desk.addDockable(d.getDockable());
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	private void saveLayout() {
		try {
			desk.writeXML(new FileOutputStream(layoutFile));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void quit() {
		saveLayout();
		System.exit(0);
	}

	public void addDockable(final Component comp, String name) {
		final DockKey key = new DockKey(name);
		key.setFloatEnabled(false);
		key.setCloseEnabled(false);
		key.setAutoHideEnabled(false);

		Dockable dockable = new com.vlsolutions.swing.docking.Dockable() {
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

	protected abstract void initFrame();

}
