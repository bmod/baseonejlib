package com.baseoneonline.java.applet;

import java.applet.Applet;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

public class TestAnimApplet extends Applet implements Runnable {

	int frame;
	int delay;
	Thread animator;
	int fps = 360;

	Dimension bufDimension;
	java.awt.Image bufImage;
	Graphics2D bufGraphics;

	/**
	 * Initialize the applet and compute the delay between frames.
	 */

	@Override
	public void init() {
		setBackground(Color.black);
		delay = (fps > 0) ? (1000 / fps) : 100;
	
	}

	/**
	 * Initialize the applet and compute the delay between frames.
	 */
	
	/**
	 * This method is called when the applet becomes visible on the screen.
	 * Create a thread and start it.
	 */
	@Override
	public void start() {
		animator = new Thread(this);
		animator.start();
	}

	/**
	 * This method is called by the thread that was created in the start method.
	 * It does the main animation.
	 */
	public void run() {
		// Remember the starting time
		long tm = System.currentTimeMillis();
		while (Thread.currentThread() == animator) {
			// Display the next frame of animation.
			repaint();

			// Delay depending on how far we are behind.
			try {
				tm += delay;
				Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
			} catch (final InterruptedException e) {
				break;
			}

			// Advance the frame
			frame++;
		}
	}

	private void createGraphics() {
		// Create the offscreen graphics context
		bufDimension = getSize();
		bufImage = createImage(bufDimension.width, bufDimension.height);
		bufGraphics = (Graphics2D) bufImage.getGraphics();
		final RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		bufGraphics.addRenderingHints(rh);
	
	}

	@Override
	public void update(final Graphics g) {

		final Dimension d = getSize();

		if ((bufGraphics == null) || (d.width != bufDimension.width)
				|| (d.height != bufDimension.height)) {
			createGraphics();
		}
		// Erase the previous image
		bufGraphics.setColor(getBackground());
		bufGraphics.fillRect(0, 0, d.width, d.height);
		bufGraphics.setColor(Color.red);

		// Paint the frame into the image
		paintFrame(bufGraphics);

		// Paint the image onto the screen
		g.drawImage(bufImage, 0, 0, null);
		frame++;

	}

	/**
	 * This method is called when the applet is no longer visible. Set the
	 * animator variable to null so that the thread will exit before displaying
	 * the next frame.
	 */
	@Override
	public void stop() {
		animator = null;
	}

	/**
	 * Paint a frame of animation.
	 */
	@Override
	public void paint(final Graphics g) {

	}

	/**
	 * Paint a frame of animation.
	 */
	public void paintFrame(final Graphics2D g) {

		final Dimension d = getSize();
		final float w2 = d.width / 2;
		final float h2 = d.height / 2;
		g.setStroke(new BasicStroke(2));
		final Line2D line = new Line2D.Float(w2 + (float) Math.cos(frame * .001) * w2,
				h2 + (float) Math.cos(frame * .003) * h2, w2 + (float) Math.cos(frame * .005) * w2, h2 + (float) Math.cos(frame * .009) * h2);
		g.draw(line);
	}

}
