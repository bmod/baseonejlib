package com.baseoneonline.java.applet;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Graphics2D;

public abstract class AnimApplet extends Applet implements Runnable {
	int delay;
	Thread animator;

	@Override
	public void init() {
		setFramesPerSecond(60);
		initAnim();
	}

	@Override
	public void start() {
		animator = new Thread(this);
		animator.start();
	}

	public void run() {
		while (Thread.currentThread() == animator) {
			// Display the next frame of animation.
			repaint();
			long tm = System.currentTimeMillis();

			// Delay for a while
			try {
				tm += delay;
				Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));

			} catch (InterruptedException e) {
				break;
			}

		}
	}
	@Override
	public void update(Graphics g) {
		updateAnim(1);
	}
	
	@Override
	public void paint(Graphics g) {
		paintAnim((Graphics2D)g);
	}
	
	@Override
	public void stop() {
		animator = null;
	}

	public void setFramesPerSecond(int fps) {
		delay = (fps > 0) ? (1000 / fps) : 100;
	}

	protected abstract void initAnim();
	protected abstract void updateAnim(float t);
	protected abstract void paintAnim(Graphics2D g);

}
