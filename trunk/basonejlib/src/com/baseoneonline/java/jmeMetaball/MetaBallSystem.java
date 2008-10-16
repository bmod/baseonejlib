package com.baseoneonline.java.jmeMetaball;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;

public class MetaBallSystem extends Node {

	private final MetaBall[] balls;
	private final Vector3f boxSize;
	private final TriMesh mesh = new TriMesh("mesh");
	private final Polygonisator poly;
	
	public MetaBallSystem() {
		this(5);
	}

	public MetaBallSystem(float bSize) {
		
		boxSize = new Vector3f(bSize, bSize, bSize);
		poly =  new Polygonisator(boxSize.mult(2), 1f);
		
		float maxWeight = 10f;
		float maxSpeed = .1f;
		balls = new MetaBall[] {
				MetaBall.getRandomBall(boxSize, maxWeight, maxSpeed),
				MetaBall.getRandomBall(boxSize, maxWeight, maxSpeed),
				MetaBall.getRandomBall(boxSize, maxWeight, maxSpeed),
				MetaBall.getRandomBall(boxSize, maxWeight, maxSpeed) };

		//WireframeState wireState = DisplaySystem.getDisplaySystem()
		//		.getRenderer().createWireframeState();
		//mesh.setRenderState(wireState);
		
		MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
		ms.setAmbient(ColorRGBA.red);
		ms.setDiffuse(new ColorRGBA(1,0,0,1));
		ms.setShininess(1);
		ms.setEnabled(true);
		mesh.setRenderState(ms);
		
	
		//CullState cs = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
		//cs.setCullFace(Face.Back);
		//mesh.setRenderState(cs);
		
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
	


}
