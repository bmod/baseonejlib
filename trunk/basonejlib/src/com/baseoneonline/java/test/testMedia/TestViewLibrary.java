package com.baseoneonline.java.test.testMedia;

import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import com.baseoneonline.java.media.library.MediaItem;
import com.baseoneonline.java.media.library.SQLLibrary;

public class TestViewLibrary extends JFrame {
	
	public static void main(String[] args) {
		TestViewLibrary app = new TestViewLibrary();
		app.setSize(800,600);
		app.setDefaultCloseOperation(EXIT_ON_CLOSE);
		app.setVisible(true);
	}
	
	public TestViewLibrary() {
		final SQLLibrary lib = new SQLLibrary("mediaDB.db");
		ListModel mdl = new AbstractListModel() {
			
			private final List<MediaItem> items = lib.getItems();

			@Override
			public Object getElementAt(int index) {
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
