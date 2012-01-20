package com.baseoneonline.tilecutter.core;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.swing.SwingWorker;

public class CutModel {

	private final HashSet<CutModelListener> listeners = new HashSet<CutModelListener>();

	private final List<BufferedImage> tiles = new ArrayList<BufferedImage>();

	private BufferedImage image;

	private SwingWorker<List<BufferedImage>, BufferedImage> worker;

	private final CutMetrics metrics = new CutMetrics();
	private String filenamePrefix = "tile_";

	public CutModel() {
	}

	/**
	 * @param filenamePrefix
	 *            Output files will be prefixed with this string. Providing null
	 *            or an empty string will result in #.ext where # is a number.
	 */
	public void setFilenamePrefix(final String filenamePrefix) {
		this.filenamePrefix = filenamePrefix;
	}

	public String getFilenamePrefix() {
		return filenamePrefix;
	}

	public void addListener(final CutModelListener l) {
		listeners.add(l);
	}

	public void removeListener(final CutModelListener l) {
		listeners.remove(l);
	}

	/**
	 * (Re-)slice the input image if it exists. Do it on a background thread to
	 * retain UI responsiveness.
	 */
	public void refresh() {
		if (null != worker) {
			worker.cancel(true);
		}

		if (image == null)
			return;

		tiles.clear();
		fireTilesChanged();
		fireMetricsChanged();

		worker = new SwingWorker<List<BufferedImage>, BufferedImage>() {
			@Override
			protected List<BufferedImage> doInBackground() throws Exception {

				List<BufferedImage> tiles = new ArrayList<BufferedImage>();

				final int tw = metrics.getTileSizeX();
				final int th = metrics.getTileSizeY();

				final int[] iArray = new int[(tw * th) * 4];

				for (final Rectangle rect : getRectangles()) {
					if (worker.isCancelled())
						return null;

					final BufferedImage im = new BufferedImage(tw, th,
							image.getType());

					image.getRaster().getPixels(rect.x, rect.y, tw, th, iArray);
					// Only add tile if it is not empty.
					if (!isEmpty(iArray)) {
						im.getRaster().setPixels(0, 0, tw, th, iArray);
						tiles.add(im);
					}
				}
				return tiles;
			}

			@Override
			protected void done() {
				try {
					tiles.addAll(get());
					fireTilesChanged();
					System.out.println("Tiles Changed");
				} catch (Exception e) {
					// Might happen when the worker has been interrupted while
					// processing. This could be the case when there are fast
					// consecutive updates, so ignore.
					e.printStackTrace();
				}
			}
		};
		worker.execute();
	}

	/**
	 * Calculate the tile metrics and return rectangles.
	 * 
	 * @return The grid of rectangles in pixel sizes.
	 */
	public Rectangle[] getRectangles() {
		final Rectangle[] rects = new Rectangle[metrics.getCountX()
				* metrics.getCountY()];
		int i = 0;
		final int offx = metrics.getxOffset();
		final int offy = metrics.getyOffset();
		final int tw = metrics.getTileSizeX();
		final int th = metrics.getTileSizeY();
		final int sx = metrics.getSpacingX();
		final int sy = metrics.getSpacingY();

		for (int x = 0; x < metrics.getCountX(); x++) {
			for (int y = 0; y < metrics.getCountY(); y++) {
				final int rx = offx + (x * (sx + tw));
				final int ry = offy + (y * (sy + th));
				rects[i++] = new Rectangle(rx, ry, tw, th);
			}
		}
		return rects;
	}

	/**
	 * @param im
	 *            The image to be cut.
	 */
	public void setImage(final BufferedImage im) {
		image = im;
		fireImageChanged();
		refresh();
	}

	/**
	 * @return The slices we want to export.
	 */
	public Collection<BufferedImage> getTiles() {
		return tiles;
	}

	/**
	 * Invoke when the tile size, spacing etc have been changed.
	 */
	public void fireMetricsChanged() {
		for (final CutModelListener l : listeners)
			l.metricsChanged();
	}

	/**
	 * Invoke then the input image has been changed.
	 */
	public void fireImageChanged() {
		for (final CutModelListener l : listeners)
			l.imageChanged();
	}

	/**
	 * Invoked when the tiles have been updated.
	 */
	private void fireTilesChanged() {
		for (CutModelListener l : listeners)
			l.tilesChanged();
	}

	/**
	 * @param pixels
	 *            Pixel data array: 0xRRGGBBAA
	 * @return True when all pixels are fully transparent.
	 */
	private static boolean isEmpty(final int[] pixels) {

		for (final int v : pixels) {
			if (v != 0)
				return false;
		}
		return true;
	}

	/**
	 * @return Object containing the tile size/spacing etc.
	 */
	public CutMetrics getMetrics() {
		return metrics;
	}

	/**
	 * @return The input image to be sliced.
	 */
	public BufferedImage getImage() {
		return image;
	}

}
