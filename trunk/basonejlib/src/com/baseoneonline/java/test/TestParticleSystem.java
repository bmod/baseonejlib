package com.baseoneonline.java.test;

import java.awt.Dimension;
import java.awt.Graphics2D;

import com.golden.gamedev.GameLoader;

public class TestParticleSystem extends com.golden.gamedev.Game {

	public static void main(final String[] args) {
		final GameLoader ldr = new GameLoader();
		ldr.setup(new TestParticleSystem(), new Dimension(640, 480), false);
		ldr.start();
	}

	@Override
	public void initResources() {

	}

	@Override
	public void render(final Graphics2D g) {

	}

	@Override
	public void update(final long g) {
		System.out.println(g);
	}

}
