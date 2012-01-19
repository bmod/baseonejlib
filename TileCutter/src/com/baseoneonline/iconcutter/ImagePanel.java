package com.baseoneonline.iconcutter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private BufferedImage image;
	private CutConfig cutProps = new CutConfig();

	private final Color lineColor = new Color(1f, 0f, 0f);

	private final AffineTransform imageTransform = new AffineTransform();

	public ImagePanel() {
		addMouseMotionListener(mouseAdapter);
	}

	public void setImage(final BufferedImage image) {
		this.image = image;
		setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		repaint();
	}

	public void setCutProps(final CutConfig cutProps) {
		this.cutProps = cutProps;
	}

	public CutConfig getCutProps() {
		return cutProps;
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
				for (final Rectangle rect : getRectangles()) {
					g.drawRect(rect.x, rect.y, rect.width, rect.height);
				}

				// g.setTransform(original);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public Rectangle[] getRectangles() {
		final Rectangle[] rects = new Rectangle[cutProps.getCountX()
				* cutProps.getCountY()];
		int i = 0;
		final int offx = cutProps.getxOffset();
		final int offy = cutProps.getyOffset();
		final int tw = cutProps.getTileSizeX();
		final int th = cutProps.getTileSizeY();
		final int sx = cutProps.getSpacingX();
		final int sy = cutProps.getSpacingY();

		for (int x = 0; x < cutProps.getCountX(); x++) {
			for (int y = 0; y < cutProps.getCountY(); y++) {
				final int rx = offx + (x * (sx + tw));
				final int ry = offy + (y * (sy + th));
				rects[i++] = new Rectangle(rx, ry, tw, th);
			}
		}
		return rects;
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

}
