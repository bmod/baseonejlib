package game.states;

import game.Cursor;
import game.Level;
import game.managers.ResourceManager;

import java.util.ArrayList;

import com.baseoneonline.java.jme.OrbitCamNode;
import com.baseoneonline.java.math.Vec2i;
import com.jme.input.MouseInput;
import com.jme.intersection.PickData;
import com.jme.intersection.TrianglePickResults;
import com.jme.math.FastMath;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.ZBufferState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameState;

public class MainGameState extends GameState {

	private Node rootNode;
	private OrbitCamNode camNode;
	MouseToSurfaceTracker mouseScreenTracker;
	VerticalSurfaceTracker cursorTracker;
	Cursor pin;
	private final float tileSize = 1;
	private Level lvl;

	public MainGameState() {
		initInput();

		initScene();

		initCamera();
		update(0);
	}

	private void initScene() {
		rootNode = new Node();
		initBlendMode();
		initZBuffer();

		lvl = ResourceManager.get().loadLevel("level1");
		rootNode.attachChild(lvl.node);

		mouseScreenTracker = new MouseToSurfaceTracker(lvl.node);
		cursorTracker = new VerticalSurfaceTracker(lvl.node);

		pin = new Cursor(ResourceManager.get().loadObj("models/mdl_pin.obj"));
		rootNode.attachChild(pin);

		rootNode.updateRenderState();

	}

	private void initBlendMode() {
		final BlendState bs = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();

		bs.setBlendEnabled(true);
		bs.setEnabled(true);
		bs.setSourceFunction(SourceFunction.SourceAlpha);
		bs.setDestinationFunction(DestinationFunction.OneMinusSourceAlpha);
		// bs.setTestEnabled(true);
		// bs.setTestFunction(TestFunction.LessThan);
		rootNode.setRenderState(bs);
		rootNode.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
	}

	private void initCamera() {
		camNode = new OrbitCamNode(DisplaySystem.getDisplaySystem()
				.getRenderer().getCamera());
		camNode.setAzimuth(50 * FastMath.DEG_TO_RAD);
		camNode.setDistance(10);
		rootNode.attachChild(camNode);

	}

	private void initZBuffer() {
		final ZBufferState buf = DisplaySystem.getDisplaySystem().getRenderer()
				.createZBufferState();
		buf.setEnabled(true);
		buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
		rootNode.setRenderState(buf);
	}

	@Override
	public void cleanup() {}

	@Override
	public void render(final float tpf) {
		final Renderer r = DisplaySystem.getDisplaySystem().getRenderer();
		r.draw(rootNode);
	}

	@Override
	public void update(final float tpf) {
		handleInput();

		mouseScreenTracker.update();
		if (mouseScreenTracker.isIntersecting()) {
			pin.setCullHint(CullHint.Dynamic);
			final Vec2i gridPos = getBoardPosition(mouseScreenTracker
					.getIntersection());
			cursorTracker.setRayPos(getRealPosition(gridPos));
			cursorTracker.update();
			if (cursorTracker.isIntersecting()) {
				pin.moveTo(cursorTracker.getIntersection());
			}
			// System.out.println(tracker.getSurfPoint());
		} else {
			pin.setCullHint(CullHint.Always);
		}

		rootNode.updateGeometricState(tpf, true);
	};

	private Vector2f getRealPosition(final Vec2i v) {
		return new Vector2f(v.x * tileSize + tileSize / 2, v.y * tileSize
				+ tileSize / 2);
	}

	private Vec2i getBoardPosition(final Vector3f vec) {
		return new Vec2i((int) Math.floor(vec.x / tileSize), (int) Math
				.floor(vec.z / tileSize));
	}

	private void initInput() {}

	private void handleInput() {

	}

}

class VerticalSurfaceTracker extends SurfaceTracker {

	public VerticalSurfaceTracker(final Spatial surface) {
		super(surface);
		ray.direction = new Vector3f(0, -1, 0);
		ray.origin.y = 10;
	}

	public void setRayPos(final Vector2f pos) {
		ray.origin.x = pos.x;
		ray.origin.z = pos.y;
	}

}

abstract class SurfaceTracker {

	protected Ray ray = new Ray();
	protected TrianglePickResults results = new TrianglePickResults();
	protected Spatial surface;
	protected Vector3f intersection = new Vector3f();

	public SurfaceTracker(final Spatial surface) {
		this.surface = surface;
	}

	public boolean isIntersecting() {
		return intersection != null;
	}

	public Vector3f getIntersection() {
		return intersection;
	}

	public void update() {
		surface.findPick(ray, results);
		final Vector3f[] tri = new Vector3f[3];

		for (int i = 0; i < results.getNumber(); i++) {

			final PickData pd = results.getPickData(i);
			final TriMesh geo = (TriMesh) pd.getTargetMesh();
			final ArrayList<Integer> tris = pd.getTargetTris();
			if (tris.size() > 0) {
				geo.getTriangle(tris.get(0), tri);
				if (ray.intersectWhere(tri[0], tri[1], tri[2], intersection)) break;
			}
		}
	}
}

class MouseToSurfaceTracker extends SurfaceTracker {

	private final Vector2f mpos = new Vector2f();

	public MouseToSurfaceTracker(final Node surface) {
		super(surface);
	}

	@Override
	public void update() {
		mpos.x = MouseInput.get().getXAbsolute();
		mpos.y = MouseInput.get().getYAbsolute();
		ray.origin = DisplaySystem.getDisplaySystem().getWorldCoordinates(mpos,
				0);
		final Vector3f point2 = DisplaySystem.getDisplaySystem()
				.getWorldCoordinates(mpos, 1);
		ray.direction = point2.subtractLocal(ray.origin).normalizeLocal();
		super.update();
	}
}
