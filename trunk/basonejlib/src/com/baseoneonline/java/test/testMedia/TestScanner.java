package com.baseoneonline.java.test.testMedia;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JList;

import com.baseoneonline.java.media.DefaultLibraryView;
import com.baseoneonline.java.media.Library;
import com.baseoneonline.java.media.LibraryListModel;
import com.baseoneonline.java.media.Media;
import com.baseoneonline.java.media.MemoryLibrary;
import com.baseoneonline.java.tools.ExecutionTimer;

public class TestScanner {

	public static void main(final String[] args) {

		final Library lib = new MemoryLibrary();

		final ExecutionTimer timer = ExecutionTimer.get();
		Media.scan(new File("S:/music"), lib);
		timer.stop();

		JFrame frame = new JFrame();
		LibraryListModel model =
			new LibraryListModel(new DefaultLibraryView(lib));
		JList list = new JList(model);
		frame.setLayout(new BorderLayout());
		frame.add(list);
		frame.setSize(800, 600);
		frame.setVisible(true);

	}

}
