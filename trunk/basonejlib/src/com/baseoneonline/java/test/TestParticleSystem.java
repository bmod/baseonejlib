package com.baseoneonline.java.test;

import com.baseoneonline.java.particleSystem.ParticleSystem;
import com.jme.app.SimpleGame;
import com.jme.renderer.ColorRGBA;

public class TestParticleSystem extends SimpleGame {

	public static void main(final String[] args) {

		new TestParticleSystem().start();
	}

	ParticleSystem psys = new ParticleSystem();

	@Override
	protected void simpleInitGame() {
		display.getRenderer().setBackgroundColor(ColorRGBA.blue);

	}

	@Override
	protected void simpleUpdate() {
		psys.step();

	}

}
