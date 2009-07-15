package com.baseoneonline.java.test;

import java.net.URL;

import com.jme.app.SimpleGame;
import com.jme.math.FastMath;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.scene.state.FogState.DensityFunction;
import com.jme.util.TextureManager;

public class TestTextures extends SimpleGame {

	public static void main(final String[] args) {
		final TestTextures game = new TestTextures();
		game.start();
	}

	@Override
	protected void simpleInitGame() {

		display.getRenderer().setBackgroundColor(ColorRGBA.darkGray);

		final int len = 10;
		final float spacing = 5;
		for (int i = 0; i < len; i++) {
			final TestQuad q = new TestQuad();
			q.getLocalTranslation().z = spacing * i;
			q.setLightCombineMode(LightCombineMode.Off);
			applyTexture(q, "assets/images/testText.png");
			rootNode.attachChild(q);
		}

		final FogState fs = display.getRenderer().createFogState();
		fs.setColor(display.getRenderer().getBackgroundColor());
		fs.setStart(0);
		fs.setEnd(50);
		fs.setDensityFunction(DensityFunction.Linear);
		fs.setEnabled(true);
		rootNode.setRenderState(fs);
	}

	private void applyTexture(final Spatial spatial, final String textureFile) {
		final URL textureURL = getClass().getClassLoader().getResource(
				textureFile);
		final TextureState ts = display.getRenderer().createTextureState();
		ts.setTexture(TextureManager.loadTexture(textureURL));

		final BlendState bs = display.getRenderer().createBlendState();
		bs.setBlendEnabled(true);
		bs.setSourceFunction(SourceFunction.SourceAlpha);
		bs.setDestinationFunction(DestinationFunction.OneMinusSourceAlpha);
		spatial.setRenderState(bs);
		spatial.setRenderState(ts);
		spatial.updateRenderState();
	}

}

class TestQuad extends Quad {

	float rotSpeed = FastMath.nextRandomFloat() * .01f * -.5f;
	float angle = 0;

	public TestQuad() {
		super("Quad", 10, 10);
	}

	@Override
	public void updateGeometricState(final float time, final boolean initiator) {
		angle += rotSpeed;
		getLocalRotation().fromAngles(0, 0, angle);
		super.updateGeometricState(time, initiator);
	}
}
