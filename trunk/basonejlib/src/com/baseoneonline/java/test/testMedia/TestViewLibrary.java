package com.baseoneonline.java.test.testMedia;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.baseoneonline.java.test.media.library.SQLLibrary;



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
}
