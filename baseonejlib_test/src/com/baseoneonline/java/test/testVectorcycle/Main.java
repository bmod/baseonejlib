package com.baseoneonline.java.test.testVectorcycle;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.baseoneonline.java.nanoxml.XMLElement;
import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.image.Texture.WrapMode;
import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.FaultFractalHeightMap;

public class Main extends SimpleGame {

	public static void main(String[] args) {
		Main app = new Main();
		app.samples = 3;
		app.start();
	}

	List<Car> cars = new ArrayList<Car>();

	Car followCar;

	Road road;

	@Override
	protected void simpleInitGame() {
		input.setEnabled(false);
		cam.setFrustumPerspective(90, (float) display.getWidth()
				/ (float) display.getHeight(), 1, 1000);
		cam.setLocation(new Vector3f(5, 200, 5));
		cam.lookAt(new Vector3f(), Vector3f.UNIT_Y);

		URL im = getClass().getClassLoader().getResource(
				"assets/images/max_track_road.jpg");
		Texture tex = TextureManager.loadTexture(im,
				MinificationFilter.BilinearNearestMipMap,
				MagnificationFilter.Bilinear);
		tex.setWrap(WrapMode.Repeat);
		TextureState ts = display.getRenderer().createTextureState();
		ts.setTexture(tex);

		final TerrainPage terrain = getTerrain();
		ITerrain iterrain = new ITerrain() {
			@Override
			public float getHeight(Vector2f p) {
				return terrain.getHeight(p);
			}
		};

		road = loadRoad("assets/vcRoad.xml", iterrain);

		// Create cars
		for (int i = 0; i < 10; i++) {
			Car c = new Car(1.5f, 1, 3);

			c.speed = FastMath.nextRandomFloat() * 2;
			c.pos.x = FastMath.nextRandomFloat() * road.getWidth()
					- road.getWidth() / 2;
			c.pos.y = FastMath.nextRandomFloat() * road.numSegments();
			cars.add(c);
			followCar = c;
			rootNode.attachChild(c);
		}
		followCar.speed = 1;

		Node ribbon = road.getRibbons();
		ribbon.setRenderState(ts);
		ribbon.updateRenderState();

		rootNode.attachChild(ribbon);
		rootNode.attachChild(road.getLines());
		// rootNode.attachChild(terrain);
	}

	private Road loadRoad(String file, ITerrain terrain) {
		XMLElement x = new XMLElement();
		Road r = new Road(10, terrain, 5f);
		try {
			x.parseFromReader(new InputStreamReader(getClass().getClassLoader()
					.getResourceAsStream(file)));

			for (XMLElement xSeg : x.getChild("road").getChildren()) {
				r.addSegment(xSeg.getFloatAttribute("distance"), xSeg
						.getFloatAttribute("curve")
						* FastMath.DEG_TO_RAD);
			}

			return r;
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).warning(e.getMessage());
		}
		return null;
	}

	@Override
	protected void simpleUpdate() {
		for (Car c : cars) {
			c.pos.y += c.speed * tpf;
			// System.out.println(c.pos);
			if (c.pos.y > road.numSegments()) {
				c.pos.y -= road.numSegments();
			}
			c.setLocalTranslation(road.getPoint(c.pos));
			// c.setLocalRotation(road.getOrientation(c.pos, Vector3f.UNIT_Z));
		}

		Vector3f behindCarPos = road.getPoint(
				followCar.pos.add(new Vector2f(0, -.1f))).add(0, 5, 0);
		Vector3f carPos = road.getPoint(followCar.pos);
		cam.setLocation(behindCarPos);
		cam.lookAt(carPos, Vector3f.UNIT_Y);

	}

	private TerrainPage getTerrain() {
		FaultFractalHeightMap heightMap = new FaultFractalHeightMap(257, 1000,
				1, 2, .8f);
		heightMap.setHeightScale(.1f);
		Vector3f terrainScale = new Vector3f(15, 1, 15);

		TerrainPage tb = new TerrainPage("TerrainPage", 33,
				heightMap.getSize(), terrainScale, heightMap.getHeightMap());
		tb.setModelBound(new BoundingBox());
		tb.updateModelBound();
		return tb;
	}
}

class Car extends Box {

	public float speed = 1;

	public Vector2f pos = new Vector2f();

	public Car(float w, float h, float d) {
		super("Car", new Vector3f(0, h / 2, 0), w, h, d);
	}
}
