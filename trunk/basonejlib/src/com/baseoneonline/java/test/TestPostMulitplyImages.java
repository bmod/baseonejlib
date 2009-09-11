package com.baseoneonline.java.test;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.baseoneonline.java.swing.DockingFrame;
import com.baseoneonline.java.swing.FileDropListPanel;

public class TestPostMulitplyImages extends DockingFrame {

	public static void main(final String[] args) {
		new TestPostMulitplyImages();
	}

	private FileDropListPanel dropPanel;

	@Override
	protected void initFrame() {
		dropPanel = new FileDropListPanel();
		addDockable(dropPanel, "Source Files");
	}

	public void convert() {

		final String inPath = "D:/temp";
		final String outPath = "d:/temp/outImages/";
		int i = 0;
		for (final File f : new File(inPath).listFiles(imageFileFilter)) {

			final BufferedImage im = readImage(f);

			if (null != im) {

				ImageOperaton.MultiplyByAlpha.process(im);

				final File outFile = new File(outPath + f.getName());
				writeImage(im, outFile);

				System.out.println("Written: " + outFile);
				i++;
			}
		}
		System.out.println("Written " + i + " files!");

	}

	private BufferedImage readImage(final File file) {
		try {
			return ImageIO.read(file);
			// System.out.println("Read: " + f);
		} catch (final IOException e1) {
			Logger.getLogger(getClass().getName()).warning(
					"Failed to read: " + file);
		}
		return null;
	}

	private void writeImage(final BufferedImage im, final File f) {
		try {
			ImageIO.write(im, "png", f);
		} catch (final IOException e) {
			Logger.getLogger(getClass().getName()).warning(
					"Failed to write: " + f);
		}
	}

	private final FileFilter imageFileFilter = new FileFilter() {

		@Override
		public boolean accept(final File pathname) {
			final String n = pathname.getName().toLowerCase();
			if (n.endsWith("png")) return true;
			return false;
		}
	};

	@Override
	protected void frameClosing() {
	// TODO Auto-generated method stub

	}

}

interface ImageOperaton {

	void process(final BufferedImage im);

	public static ImageOperaton MultiplyByAlpha = new ImageOperaton() {

		@Override
		public void process(final BufferedImage im) {
			final WritableRaster r = im.getRaster();
			if (r.getNumBands() != 4) throw new UnsupportedOperationException(
					"Only 4 channels per pixel are supported.");
			final int w = im.getWidth();
			final int h = im.getHeight();
			final float[] px = new float[4];
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					r.getPixel(x, y, px);

					px[0] *= px[3] / 0xFF;
					px[1] *= px[3] / 0xFF;
					px[2] *= px[3] / 0xFF;

					r.setPixel(x, y, px);
				}
			}
		}
	};

}
