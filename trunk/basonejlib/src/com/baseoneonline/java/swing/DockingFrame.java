package com.baseoneonline.java.swing;

import java.awt.Component;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockableState;
import com.vlsolutions.swing.docking.DockingDesktop;

public abstract class DockingFrame {

	File layoutFile = new File(getClass().getName() + ".defaultLayout.xml");

	private static final String LAYOUT = "layout";

	private final DockingDesktop desk = new DockingDesktop();

	public JFrame getFrame() {
		return baseFrame;
	}

	BaseFrame baseFrame = new BaseFrame() {

		@Override
		protected void frameClosing() {
			frameClosingProxy();
		}

		@Override
		protected void initFrame() {
			initFrameProxy();
		}

		@Override
		protected void storeFrame() {
			super.storeFrame();
			saveLayout();
		}
	};

	public DockingFrame() {

		baseFrame.add(desk);

		baseFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		readLayout();

	}

	private void readLayout() {
		final String layoutData = baseFrame.getPrefs().get(LAYOUT, "");
		if (layoutData.length() > 0) {
			try {
				final ByteArrayInputStream bais =
					new ByteArrayInputStream(layoutData.getBytes());
				desk.readXML(bais);

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
	}

	private void saveLayout() {
		try {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			desk.writeXML(baos);
			baseFrame.getPrefs().put(LAYOUT, baos.toString());
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

	private void frameClosingProxy() {
		frameClosing();
	}

	private void initFrameProxy() {
		initFrame();
	}

	protected abstract void initFrame();

	protected abstract void frameClosing();

}
