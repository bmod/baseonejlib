package com.baseoneonline.tilecutter.actions;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import com.baseoneonline.java.swing.config.Config;
import com.baseoneonline.java.tools.FileExensionFilter;
import com.baseoneonline.tilecutter.Application;
import com.baseoneonline.tilecutter.core.CutModel;

public class OpenImageAction extends AbstractAction {

	private final CutModel model;

	public OpenImageAction(final CutModel model) {
		super("Load Image");
		this.model = model;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		final JFileChooser fc = new JFileChooser(Config.get().getFile(
				Application.LAST_OPENED_DIRECTORY));

		fc.setFileFilter(new FileExensionFilter(
				Application.ALLOWED_IMAGE_EXTENSIONS));

		if (JFileChooser.APPROVE_OPTION != fc.showOpenDialog(Application.get()
				.getMainFrame())) {
			return;
		}

		Config.get().setFile(Application.LAST_OPENED_DIRECTORY,
				fc.getSelectedFile().getParentFile());
		final File f = fc.getSelectedFile();

		BufferedImage image = null;
		try {
			image = ImageIO.read(f);

		} catch (final Exception ex) {
			final String msg = String.format("Could not load image: '%s'\n%s",
					f, ex.getMessage());
			Logger.getLogger(getClass().getName()).severe(msg);

		}
		model.setImage(image);
	}
}
