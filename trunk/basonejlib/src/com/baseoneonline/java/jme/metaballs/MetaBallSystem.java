package com.baseoneonline.java.jme.metaballs;

import java.net.URL;

import com.jme.image.Texture;
import com.jme.image.Texture.EnvironmentalMapMode;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;
import com.jme.scene.state.CullState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class MetaBallSystem extends Node {

	private final MetaBall[] balls;
	private final Vector3f boxSize;
	private final TriMesh mesh = new TriMesh("mesh");
	private final Polygonisator poly;

	public MetaBallSystem() {
		this(5);
	}

	public MetaBallSystem(final float bSize) {

		boxSize = new Vector3f(bSize, bSize, bSize);
		poly = new Polygonisator(boxSize.mult(2), 1f);

		final float maxWeight = 10f;
		final float maxSpeed = .1f;
		balls = new MetaBall[] {
				MetaBall.getRandomBall(boxSize, maxWeight, maxSpeed),
				MetaBall.getRandomBall(boxSize, maxWeight, maxSpeed),
				MetaBall.getRandomBall(boxSize, maxWeight, maxSpeed),
				MetaBall.getRandomBall(boxSize, maxWeight, maxSpeed),
				MetaBall.getRandomBall(boxSize, maxWeight, maxSpeed),
				MetaBall.getRandomBall(boxSize, maxWeight, maxSpeed) };

		// WireframeState wireState = DisplaySystem.getDisplaySystem()
		// .getRenderer().createWireframeState();
		// mesh.setRenderState(wireState);

		final URL tex = getClass().getClassLoader().getResource(
				"com/baseoneonline/java/data/refmap.jpg");
		mesh.setRenderState(getTextureState(tex));

		final CullState cs = DisplaySystem.getDisplaySystem().getRenderer()
				.createCullState();
		cs.setCullFace(com.jme.scene.state.CullState.Face.Front);
		mesh.setRenderState(cs);

		mesh.setLightCombineMode(LightCombineMode.Off);

		mesh.updateRenderState();
		attachChild(mesh);

		updateRenderState();
	}

	private static TextureState getTextureState(final URL texture) {
		final TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		final Texture t = TextureManager.loadTexture(texture,
				MinificationFilter.BilinearNearestMipMap,
				MagnificationFilter.Bilinear, ts.getMaxAnisotropic(), true);
		t.setEnvironmentalMapMode(EnvironmentalMapMode.SphereMap);
		ts.setTexture(t);
		return ts;
	}

	public void update(final float t) {
		final ScalarField field = new MetaBallScalarField(balls);
		for (final MetaBall ball : balls) {
			ball.getPosition().addLocal(ball.getSpeed().mult(t));
			if ((ball.getPosition().x < -boxSize.x)
					|| (ball.getPosition().x > boxSize.x)) {
				ball.getSpeed().x = -ball.getSpeed().x;
			}
			if ((ball.getPosition().y < -boxSize.y)
					|| (ball.getPosition().y > boxSize.y)) {
				ball.getSpeed().y = -ball.getSpeed().y;
			}
			if ((ball.getPosition().z < -boxSize.z)
					|| (ball.getPosition().z > boxSize.z)) {
				ball.getSpeed().z = -ball.getSpeed().z;
			}
		}
		poly.calculate(mesh, field, 1f);

		setLocalRotation(getLocalRotation().add(quat.mult(t)));

	}

	private final Quaternion quat = new Quaternion().fromAngles(800f, 4f, 3f);
}
