package com.baseoneonline.java.test;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.FaultFractalHeightMap;

public class TestAdaptiveTerrain extends SimpleGame {
	public static void main(String[] args) {
		new TestAdaptiveTerrain().start();
	}

	@Override
	protected void simpleInitGame() {

		cam.setLocation(new Vector3f(250, 50, 250));
		cam.lookAt(new Vector3f(), Vector3f.UNIT_Y);

		
		
		rootNode.attachChild(getTerrain());

	}
	
	
	private Spatial getTerrain() {
		FaultFractalHeightMap heightMap =
			new FaultFractalHeightMap(257, 1000, 1, 2, .8f);
		heightMap.setHeightScale(.1f);
		Vector3f terrainScale = new Vector3f(5, .5f, 5);

		TerrainPage tb =
			new TerrainPage("TerrainPage", 33, heightMap.getSize(),
				terrainScale, heightMap.getHeightMap());
		tb.setModelBound(new BoundingBox());
		tb.updateModelBound();
		return tb;
	}
	
}
