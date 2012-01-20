package com.baseoneonline.tilecutter.core;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.swing.SwingWorker;

public class CutModel {

	public static interface Listener {
		public void metricsChanged();

		public void imageChanged();
	}

	private final HashSet<Listener> listeners = new HashSet<Listener>();

	private final List<BufferedImage> tiles = new ArrayList<BufferedImage>();

	private BufferedImage image;

	private SwingWorker<Void, BufferedImage> worker;

	private final CutMetrics metrics = new CutMetrics();
	private String filenamePrefix = "tile_";

	public CutModel() {
	}

	public void setFilenamePrefix(final String filenamePrefix) {
		this.filenamePrefix = filenamePrefix;
	}

	public String getFilenamePrefix() {
		return filenamePrefix;
	}

	public void addListener(final Listener l) {
		listeners.add(l);
	}

	public void removeListener(final Listener l) {
		listeners.remove(l);
	}

	public void refresh() {
		if (null != worker) {
			worker.cancel(true);
		}

		if (image == null)
			return;

		worker = new SwingWorker<Void, BufferedImage>() {
			@Override
			protected Void doInBackground() throws Exception {

				final int tw = metrics.getTileSizeX();
				final int th = metrics.getTileSizeY();

				final int[] iArray = new int[(tw * th) * 4];

				for (final Rectangle rect : getRectangles()) {
					if (worker.isCancelled())
						return null;

					final BufferedImage im = new BufferedImage(tw, th,
							image.getType());

					image.getRaster().getPixels(rect.x, rect.y, tw, th, iArray);
					if (!isEmpty(iArray)) {
						im.getRaster().setPixels(0, 0, tw, th, iArray);
						publish(im);
					}
				}
				return null;
			}

			@Override
			protected void process(final List<BufferedImage> chunks) {
				tiles.addAll(chunks);
				fireMetricsChanged();
			}
		};
		worker.execute();
	}

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

	public void setImage(final BufferedImage im) {
		image = im;
		fireImageChanged();
		refresh();
	}

	public Collection<BufferedImage> getTiles() {
		return tiles;
	}

	public void fireMetricsChanged() {
		for (final Listener l : listeners)
			l.metricsChanged();
	}

	public void fireImageChanged() {
		for (final Listener l : listeners)
			l.imageChanged();
	}

	private static boolean isEmpty(final int[] pixels) {

		for (final int v : pixels) {
			if (v != 0)
				return false;
		}
		return true;
	}

	public CutMetrics getMetrics() {
		return metrics;
	}

	public BufferedImage getImage() {
		return image;
	}

}
