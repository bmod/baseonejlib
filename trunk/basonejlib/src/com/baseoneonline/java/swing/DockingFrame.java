package com.baseoneonline.java.swing;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockableState;
import com.vlsolutions.swing.docking.DockingDesktop;

public abstract class DockingFrame extends BaseFrame {

	File layoutFile = new File(getClass().getName() + ".defaultLayout.xml");

	private final DockingDesktop desk = new DockingDesktop();

	public DockingFrame() {

		add(desk);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		initFrame();
		readLayout();

	}

	private void readLayout() {
		try {
			desk.readXML(new FileInputStream(layoutFile));

			for (final DockableState d : desk.getDockables()) {
				if (d.isClosed()) {
					desk.addDockable(d.getDockable());
				}
			}

		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final ParserConfigurationException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final SAXException e) {
			e.printStackTrace();
		} catch (final NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void storeFrame() {
		super.storeFrame();
		saveLayout();
	}

	private void saveLayout() {
		try {
			desk.writeXML(new FileOutputStream(layoutFile));
		} catch (final IOException e1) {
			e1.printStackTrace();
		}
	}

	public void addDockable(final Component comp, final String name) {
		final DockKey key = new DockKey(name);
		key.setFloatEnabled(false);
		key.setCloseEnabled(false);
		key.setAutoHideEnabled(false);

		final Dockable dockable = new com.vlsolutions.swing.docking.Dockable() {

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

}
