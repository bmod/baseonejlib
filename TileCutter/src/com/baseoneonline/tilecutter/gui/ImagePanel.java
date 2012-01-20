package com.baseoneonline.tilecutter.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.baseoneonline.tilecutter.core.CutModel;
import com.baseoneonline.tilecutter.core.CutModel.Listener;

public class ImagePanel extends JPanel implements Listener {

	private BufferedImage image;

	private final Color lineColor = new Color(1f, 0f, 0f);

	private final AffineTransform imageTransform = new AffineTransform();

	private CutModel model;

	public ImagePanel() {
		addMouseMotionListener(mouseAdapter);
	}

	public void setModel(final CutModel model) {
		if (null != model)
			model.removeListener(this);
		model.addListener(this);
	}

	@Override
	protected void paintComponent(final Graphics g1) {
		super.paintComponent(g1);
		final Graphics2D g = (Graphics2D) g1;
		try {
			if (null != image) {
				// final AffineTransform original = g.getTransform();
				// g.setTransform(imageTransform);
				g.drawImage(image, 0, 0, null);

				g.setColor(lineColor);
				// Draw grid
				for (final Rectangle rect : model.getRectangles()) {
					g.drawRect(rect.x, rect.y, rect.width, rect.height);
				}

				// g.setTransform(original);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private final MouseAdapter mouseAdapter = new MouseAdapter() {

		private int prevX = -1;
		private int prevY = -1;
		private boolean first = true;

		@Override
		public void mouseDragged(final java.awt.event.MouseEvent e) {
			if (!first) {
				final float tx = e.getX() - prevX;
				final float ty = e.getY() - prevY;
				imageTransform.translate(tx, ty);
				repaint();
			}
			prevX = e.getX();
			prevY = e.getY();
			first = false;
		}

		@Override
		public void mouseReleased(final java.awt.event.MouseEvent arg0) {

		}
	};

	@Override
	public void metricsChanged() {

	}

	@Override
	public void imageChanged() {
		image = model.getImage();
		repaint();
	}

}
