package com.baseoneonline.tilecutter;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.baseoneonline.java.swing.config.Config;
import com.baseoneonline.tilecutter.gui.MainFrame;

public class Application {

	public static void main(final String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final Exception e) {

		}

		Config.setApplicationClass(MainFrame.class);
		Application.get().getMainFrame().setVisible(true);
	}

	// For use with Config
	public static final String APPLICATION_TITLE = "Tile Cutter";
	public static final String LAST_SELECTED_OUTPUT_DIR = "lastSelectedOutputDir";
	public static final String LAST_OPENED_DIRECTORY = "lastOpenedDirectory";
	public static final String LAST_OPENED_IMAGE = "lastOpenedImage";

	public static final String[] ALLOWED_IMAGE_EXTENSIONS = { "jpg", "png",
			"gif", "jpeg" };
	public static final String EXPORT_FILE_FORMAT = "png";

	private static Application instance;

	private final MainFrame frame;

	private Application() {
		frame = new MainFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(winAdapter);

		Logger.getLogger("").addHandler(logHandler);
	}

	public static Application get() {
		if (null == instance)
			instance = new Application();
		return instance;
	}

	private final WindowAdapter winAdapter = new WindowAdapter() {
		@Override
		public void windowClosing(final java.awt.event.WindowEvent e) {
			Config.get().flush();
			frame.dispose();
		}
	};

	public Component getMainFrame() {
		return frame;
	}

	private final Handler logHandler = new Handler() {

		@Override
		public void publish(final LogRecord record) {
			// Show a warning dialog when severity => WARNING
			if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
				JOptionPane.showMessageDialog(getMainFrame(), record.getMessage(),
						"Warning", JOptionPane.ERROR_MESSAGE);
			}
		}

		@Override
		public void flush() {
			// Unused
		}

		@Override
		public void close() throws SecurityException {
			// Unused
		}
	};

}
