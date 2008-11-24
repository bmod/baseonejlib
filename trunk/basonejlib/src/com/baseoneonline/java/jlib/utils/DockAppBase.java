package com.baseoneonline.java.jlib.utils;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.baseoneonline.java.jme.modelviewer.util.Const;
import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockingDesktop;

public abstract class DockAppBase extends JFrame implements Const {

	private final Logger log = Logger.getLogger(getClass().getName());
	
	private final Config conf = Config.getConfig();

	final DockingDesktop desk = new DockingDesktop();
	
	private final String layoutFilename;

	public DockAppBase(final String title, final String configFileName,
			final String layoutFileName) {
		this.layoutFilename = layoutFileName;
		
		conf.setFile(getClass().getName() + ".cfg");

		setTitle(conf.get(APP_TITLE, "ModelViewer"));
		setSize(conf.get(APP_WIDTH, 500), conf.get(APP_HEIGHT, 500));
		setLocation(conf.get(APP_X, 20), conf.get(APP_Y, 20));

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				conf.set(APP_WIDTH, getWidth());
				conf.set(APP_HEIGHT, getHeight());
				conf.set(APP_X, getX());
				conf.set(APP_Y, getY());
				Config.getConfig().write();
				try {
					log.info("Writing layout...");
					desk.writeXML(new FileOutputStream(new File(layoutFileName)));
				} catch (final FileNotFoundException e1) {
					log.warning("File not found: "+layoutFileName);
				} catch (final IOException e1) {
					log.warning("IOError writing to file: "+layoutFileName);
				}
				
				System.exit(0);
				
			}
		});

		add(desk);
		
		
		setVisible(true);
	}
	
	protected void readLayout() {
		log.info("Reading layout: "+layoutFilename);
		try {
			desk.readXML(new FileInputStream(new File(layoutFilename)));
		} catch (final FileNotFoundException e) {
			log.warning("Layout file not found: "+layoutFilename);
		} catch (final ParserConfigurationException e) {
			log.warning("Layout parsing failed, removing file: "+layoutFilename);
			deleteLayoutFile();
		} catch (final IOException e) {
			log.warning("IO Error reading: "+layoutFilename);
		} catch (final SAXException e) {
			log.warning("Layout SAX parsing failed, removing file: "+layoutFilename);
			deleteLayoutFile();
		}
	}
	
	private void deleteLayoutFile() {
		final File file = new File(layoutFilename);
		file.delete();
	}

	protected void addDockable(final JPanel comp, final String name) {
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
	
	protected abstract boolean closeRequested();

}
