package com.baseoneonline.java.mediabrowser.test;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

import com.baseoneonline.java.mediabrowser.Database;
import com.baseoneonline.java.mediabrowser.MediaFile;
import com.baseoneonline.java.mediabrowser.MediaScanner;
import com.baseoneonline.java.mediabrowser.SQLiteDatabase;
import com.baseoneonline.java.mediabrowser.Settings;
import com.baseoneonline.java.mediabrowser.SimpleListModel;

public class TestScanner extends JFrame {

	public static void main(final String[] args) {
		new TestScanner();
	}

	JList list;
	SimpleListModel listModel;
	JScrollPane listScroll;
	JButton btStart;

	public TestScanner() {

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Scanner");
		setSize(800, 600);
		setVisible(true);

		listModel = new SimpleListModel();
		list = new JList(listModel);
		listScroll = new JScrollPane(list);
		add(listScroll);

		final Database db = new SQLiteDatabase("media.db");
		final MediaScanner scanner = new MediaScanner(db);
		scanner.addListener(new MediaScanner.Listener() {

			@Override
			public void process(final List<MediaFile> files) {
				listModel.addItems(files);
				final int idx = listModel.getSize();
				if (idx > 0)
					list.ensureIndexIsVisible(idx - 1);
			}

			@Override
			public void done(final List<MediaFile> files) {
				System.out.println("Done");
			}
		});

		scanner.scan(Settings.get().getMediaSources());

	}
}
