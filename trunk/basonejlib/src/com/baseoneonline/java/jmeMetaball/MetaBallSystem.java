package com.baseoneonline.java.jmeMetaball;

import com.jme.image.Texture;
import com.jme.image.Texture.EnvironmentalMapMode;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;
import com.jme.scene.state.CullState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.WireframeState;
import com.jme.scene.state.CullState.Face;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class MetaBallSystem extends Node {

	private final MetaBall[] balls;
	private final Vector3f boxSize = new Vector3f(5, 5, 5);
	private final TriMesh mesh = new TriMesh("mesh");
	private final Polygonisator poly = new Polygonisator(boxSize.mult(2), 0.5f);

	public MetaBallSystem() {
		// RenderState material = getMaterialState(ColorRGBA.white);
		// RenderState texture = getTextureState("YOUR TEXTURE HERE");
		// RenderState cullState = getCullState();
		float maxWeight = 10f;
		float maxSpeed = .1f;
		balls = new MetaBall[] {
				MetaBall.getRandomBall(boxSize, maxWeight, maxSpeed),
				MetaBall.getRandomBall(boxSize, maxWeight, maxSpeed),
				MetaBall.getRandomBall(boxSize, maxWeight, maxSpeed),
				MetaBall.getRandomBall(boxSize, maxWeight, maxSpeed) };

		WireframeState wireState = DisplaySystem.getDisplaySystem()
				.getRenderer().createWireframeState();
		// mesh.setRenderState(material);
		// mesh.setRenderState(texture);
		// mesh.setRenderState(cullState);
		mesh.setDefaultColor(new ColorRGBA(1, 1, 1, .5f));
		mesh.setRenderState(wireState);
		mesh.updateRenderState();
		attachChild(mesh);

		updateRenderState();
	}

	public void update(float t) {
		ScalarField field = new MetaBallScalarField(balls);
		for (MetaBall ball : balls) {
			ball.getPosition().addLocal(ball.getSpeed().mult(t));
			if (ball.getPosition().x < -boxSize.x
					|| ball.getPosition().x > boxSize.x) {
				ball.getSpeed().x = -ball.getSpeed().x;
			}
			if (ball.getPosition().y < -boxSize.y
					|| ball.getPosition().y > boxSize.y) {
				ball.getSpeed().y = -ball.getSpeed().y;
			}
			if (ball.getPosition().z < -boxSize.z
					|| ball.getPosition().z > boxSize.z) {
				ball.getSpeed().z = -ball.getSpeed().z;
			}
		}
		poly.calculate(mesh, field, 1f);
	}
	
	private static MaterialState getMaterialState(ColorRGBA color) {
		MaterialState mat = DisplaySystem.getDisplaySystem().getRenderer()
				.createMaterialState();
		mat.setEmissive(color == null ? new ColorRGBA(0.5f, 0.5f, 0.5f, 1)
				: color);
		return mat;
	}

	private static TextureState getTextureState(String texture) {
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		Texture t = TextureManager.loadTexture(texture,
				MinificationFilter.NearestNeighborLinearMipMap,
				MagnificationFilter.Bilinear, ts.getMaxAnisotropic(), false);
		t.setEnvironmentalMapMode(EnvironmentalMapMode.ReflectionMap);
		ts.setTexture(t);
		return ts;
	}

	private static CullState getCullState() {
		CullState cs = DisplaySystem.getDisplaySystem().getRenderer()
				.createCullState();
		cs.setCullFace(Face.Back); // THAT IS STRANGE, but CS_BACK looks just wrong
								// and the normals are pointing in the right
								// direction
		cs.setEnabled(true);
		return cs;
	}

}
