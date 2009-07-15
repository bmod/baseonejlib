package com.baseoneonline.java.test.testMedia;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import com.baseoneonline.java.media.library.SQLLibrary;
import com.baseoneonline.java.media.library.items.MediaItem;

public class TestViewLibrary extends JFrame {

	public static void main(final String[] args) {
		final TestViewLibrary app = new TestViewLibrary();
		app.setSize(800, 600);
		app.setDefaultCloseOperation(EXIT_ON_CLOSE);
		app.setVisible(true);
	}

	SQLLibrary lib;

	public TestViewLibrary() {
		lib = new SQLLibrary("mediaDB.db");
		showTable();
	}

	private void showTable() {
		setLayout(new BorderLayout());
		final MediaTablePanel panel = new MediaTablePanel(lib);
		add(panel);
	}

	private void showList() {
		final ListModel mdl = new AbstractListModel() {

			private final List<MediaItem> items = lib.getItems();

			@Override
			public Object getElementAt(final int index) {
				return items.get(index);
			}

			@Override
			public int getSize() {
				return items.size();
			}

		};
		add(new JScrollPane(new JList(mdl)));
	}
}
