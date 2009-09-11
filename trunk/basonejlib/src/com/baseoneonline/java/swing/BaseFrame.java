package com.baseoneonline.java.swing;

import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JFrame;

/**
 * Basic JFrame that stores its size and position on close. Extend this class
 * and instantiate it to show the frame.
 * 
 * @author B. Korsmit
 * 
 */
public abstract class BaseFrame extends JFrame {

	private final Preferences prefs;
	private final String APP_WIDTH = "appWidth", APP_HEIGHT = "appHeight",
			APP_X = "appX", APP_Y = "appY";

	public BaseFrame() {
		prefs = Preferences.userNodeForPackage(getClass());
		setTitle(getClass().getSimpleName());
		setVisible(true);

		restoreFrame();
		initFrame();
	}

	public Preferences getPrefs() {
		return prefs;
	}

	protected void restoreFrame() {
		int w = prefs.getInt(APP_WIDTH, 800);
		int h = prefs.getInt(APP_HEIGHT, 600);
		int x = prefs.getInt(APP_X, 0);
		int y = prefs.getInt(APP_Y, 0);
		final int sw = Toolkit.getDefaultToolkit().getScreenSize().width;
		final int sh = Toolkit.getDefaultToolkit().getScreenSize().height;
		if (w > sw) w = sw;
		if (h > sh) h = sh;

		if (x < 0) x = 0;
		if (x + w > sw) x = sw - w;
		if (y < 0) y = 0;
		if (y + h > sh) y = sh - h;

		setSize(w, h);
		setLocation(x, y);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(final WindowEvent e) {
				quit();
			}
		});
	}

	protected void quit() {
		storeFrame();
		System.exit(0);
	}

	protected void storeFrame() {
		prefs.putInt(APP_WIDTH, getWidth());
		prefs.putInt(APP_HEIGHT, getHeight());
		prefs.putInt(APP_X, getX());
		prefs.putInt(APP_Y, getY());
		try {
			prefs.flush();
		} catch (final BackingStoreException e) {
			java.util.logging.Logger.getLogger(BaseFrame.class.getName())
					.warning(e.getMessage());
		}
	}

	protected abstract void frameClosing();

	protected abstract void initFrame();
}
