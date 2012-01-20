package com.baseoneonline.tilecutter.actions;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import com.baseoneonline.java.swing.config.Config;
import com.baseoneonline.tilecutter.Application;
import com.baseoneonline.tilecutter.core.CutModel;

public class SaveTilesAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2431488700060179711L;
	private final CutModel model;

	public SaveTilesAction(final CutModel model) {
		super("Save Tiles");
		this.model = model;
	}

	@Override
	public void actionPerformed(final ActionEvent arg0) {

		final JFileChooser fc = new JFileChooser(
				Application.LAST_SELECTED_OUTPUT_DIR);

		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		if (JFileChooser.APPROVE_OPTION != fc.showOpenDialog(Application.get()
				.getMainFrame()))
			return;

		Config.get().setFile(Application.LAST_SELECTED_OUTPUT_DIR,
				fc.getSelectedFile());

		final File dir = fc.getSelectedFile();

		int i = 0;

		for (final BufferedImage im : model.getTiles()) {

			final String path = String.format("%s/%s%s.%s",
					dir.getAbsolutePath(), model.getFilenamePrefix(), i,
					Application.EXPORT_FILE_FORMAT);

			final File outFile = new File(path);

			try {
				ImageIO.write(im, "PNG", outFile);
			} catch (final Exception e) {
				Logger.getLogger(getClass().getName()).severe(
						"Could not save image: " + outFile);
			}
			i++;
		}
	}
}
