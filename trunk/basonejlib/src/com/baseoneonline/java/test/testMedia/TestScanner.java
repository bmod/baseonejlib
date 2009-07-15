package com.baseoneonline.java.test.testMedia;

import java.awt.BorderLayout;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import com.baseoneonline.java.media.Scanner;
import com.baseoneonline.java.media.ScannerListener;
import com.baseoneonline.java.media.Scanner.State;
import com.baseoneonline.java.media.library.SQLLibrary;
import com.baseoneonline.java.tools.ExecutionTimer;

public class TestScanner extends JFrame {

	public static void main(final String[] args) {
		Logger.getLogger("org.jaudiotagger").setLevel(Level.WARNING);
		final TestScanner app = new TestScanner();
		app.setSize(600, 300);
		app.setDefaultCloseOperation(EXIT_ON_CLOSE);
		app.setVisible(true);
	}

	File[] dirs = {
			new File("D:/temp/Game"),
			new File(
					"C:/Documents and Settings/Administrator/My Documents/My Pictures") };

	final SQLLibrary lib = new SQLLibrary("mediaDB.db");

	ScannerStatusPanel statPanel;

	public TestScanner() {

		setLayout(new BorderLayout());
		statPanel = new ScannerStatusPanel();
		add(statPanel);
		startScanning();
	}

	private void startScanning() {
		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				final ExecutionTimer timer = ExecutionTimer.get();
				final Scanner scanner = new Scanner();
				scanner.addListener(new ScannerListener() {
					@Override
					public void update(final String msg) {
						statPanel.setMessage(msg);
					}

					@Override
					public void stateChanged(final State s) {
						statPanel.setState(s.name());

					}
				});

				scanner.scan(dirs, lib);
				lib.flush();
				timer.stop();
				return null;
			}
		}.execute();
	}

}
