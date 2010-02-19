package com.baseoneonline.java.test;

import com.baseoneonline.java.jme.springParticles.Particle;
import com.baseoneonline.java.jme.springParticles.ParticleSystem;
import com.jme.app.SimpleGame;
import com.jme.input.MouseInput;
import com.jme.math.FastMath;
import com.jme.renderer.Renderer;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.shape.Quad;

public class TestSpringParticles extends SimpleGame {
	public static void main(String[] args) {
		
		TestSpringParticles app = new TestSpringParticles();
		
		app.samples = 3;
		app.start();
	}

	private ParticleSystem	psys;

	private Quad[] quads;
	private Particle[] particles;
	
	/* (non-Javadoc)
	 * @see com.jme.app.BaseSimpleGame#simpleInitGame()
	 */
	@Override
	protected void simpleInitGame() {
		MouseInput.get().setCursorVisible(true);
		
		psys = new ParticleSystem();
		
		rootNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		rootNode.setLightCombineMode(LightCombineMode.Off);
		
		int len = 1000;
		quads = new Quad[len];
		particles = new Particle[len];
		for (int i = 0; i < len; i++) {
			Particle p =
				psys.createParticle(FastMath.nextRandomFloat() * 640, FastMath
						.nextRandomFloat() * 480);
			Quad q = new Quad("q"+i, 2,2);
			quads[i] = q;
			particles[i] = p;
			rootNode.attachChild(q);
		}
	}
	
	@Override
	protected void simpleUpdate() {
		updateParticles(tpf);
	}
	
	private void updateParticles(float t) {
		psys.update(tpf);
		Particle p;
		for (int i =0; i<quads.length; i++) {
			p = particles[i];
			quads[i].setLocalTranslation(p.x, p.y, 0);
		}
	}
	
}
