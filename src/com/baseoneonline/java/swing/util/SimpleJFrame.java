package com.baseoneonline.java.swing.util;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public abstract class SimpleJFrame extends JFrame {

	private final Preferences prefs =
		Preferences.userNodeForPackage(getClass());
	
	public SimpleJFrame() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {


				setSize(prefs.getInt("frameWidth", 400), prefs.getInt(
						"frameHeight",
						300));
				setLocation(prefs.getInt("frameX", 0), prefs.getInt("frameY", 0));
				setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				setVisible(true);
				addWindowListener(windowAdapter);
				
			}
		});
		initialize();
	}
	
	private final WindowAdapter windowAdapter = new WindowAdapter() {
		@Override
		public void windowClosing(final WindowEvent e) {
			prepareWindowClose();
		}
	};
	
	protected abstract void initialize();

	private void prepareWindowClose() {
		prefs.putInt("frameWidth", getWidth());
		prefs.putInt("frameHeight", getHeight());
		prefs.putInt("frameX", getLocation().x);
		prefs.putInt("frameY", getLocation().y);
		windowClosing();
	}
	
	protected abstract void windowClosing();
	
}
