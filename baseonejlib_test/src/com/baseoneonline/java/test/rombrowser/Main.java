package com.baseoneonline.java.test.rombrowser;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.baseoneonline.java.nanoxml.XMLElement;

public class Main extends JFrame {

	private final URL settingsFile = getClass().getResource("settings.xml");
	private final DefaultListModel systemListModel = new DefaultListModel();
	private final DefaultListModel romListModel = new DefaultListModel();

	private GameSystem currentSystem;
	private RomDetailPane detailPane;

	public static void main(final String[] args) {

		final Main app = new Main();
		app.setSize(800, 600);
		app.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		app.setVisible(true);
	}

	private final WindowAdapter windowAdapter = new WindowAdapter() {

		@Override
		public void windowClosing(final WindowEvent e) {
			System.exit(0);
		}
	};

	public Main() {
		addWindowListener(windowAdapter);

		createUI();
		reload();
	}

	private void createUI() {
		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		{
			final JList romList;
			final JList systemList = new JList(systemListModel);
			systemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			splitPane.add(new JScrollPane(systemList), JSplitPane.LEFT);

			final JSplitPane subSplit = new JSplitPane(
					JSplitPane.VERTICAL_SPLIT);
			{
				romList = new JList(romListModel);
				subSplit.add(new JScrollPane(romList), JSplitPane.TOP);

				detailPane = new RomDetailPane();
				subSplit.add(detailPane);
			}
			splitPane.add(subSplit, JSplitPane.RIGHT);

			//
			systemList.addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(final ListSelectionEvent e) {
					final GameSystem sys = (GameSystem) systemList
							.getSelectedValue();
					populateRomList(sys);
				}

			});

			romList.addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(final ListSelectionEvent e) {
					final Rom rom = (Rom) romList.getSelectedValue();
					if (null != rom) detailPane.setRom(rom);
				}
			});

		}
		add(splitPane);
	}

	private void populateRomList(final GameSystem sys) {
		if (currentSystem == sys) return;
		currentSystem = sys;

		romListModel.clear();
		new SwingWorker<Void, Void>() {

			ArrayList<Rom> roms;

			@Override
			protected Void doInBackground() throws Exception {
				roms = MediaCache.get().getRoms(sys);
				return null;
			}

			@Override
			protected void done() {
				for (final Rom rom : roms) {
					romListModel.addElement(rom);
				}
				// mon.setProgress(100);
				super.done();
			}
		}.execute();
	}

	private void reload() {
		systemListModel.clear();
		final XMLElement xml = new XMLElement();
		try {
			xml
					.parseFromReader(new InputStreamReader(settingsFile
							.openStream()));
			for (final XMLElement xGameSystem : xml.getChildren("romlist")) {
				final GameSystem sys = new GameSystem();
				sys.deSerialize(xGameSystem);
				systemListModel.addElement(sys);
			}

		} catch (final Exception e) {
			java.util.logging.Logger.getLogger(Main.class.getName()).warning(
					e.getMessage());
		}

	}

}
