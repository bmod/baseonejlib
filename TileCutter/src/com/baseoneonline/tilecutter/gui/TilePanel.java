package com.baseoneonline.tilecutter.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


/**
 * Displays one tile.
 *
 */
class TilePanel extends JPanel {

	private final BufferedImage image;

	public TilePanel(final BufferedImage image) {
		this.image = image;
		setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
	}

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}

}