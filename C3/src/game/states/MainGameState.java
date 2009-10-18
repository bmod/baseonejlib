package game.states;

import game.Board;
import game.Cursor;
import game.Level;
import game.PlayerController;
import game.SurfMouseInputHandler;
import game.managers.ResourceManager;

import com.baseoneonline.java.astar.TileGraph;
import com.baseoneonline.java.jme.OrbitCamNode;
import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.FastMath;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.ZBufferState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameState;

public class MainGameState extends GameState {

	private Node rootNode;
	private OrbitCamNode camNode;
	private Cursor pin;
	private Level lvl;
	SurfMouseInputHandler mouseHandler;
	private Board board;

	private InputHandler input;

	private PlayerController playerController;

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

		board = new Board(lvl.node, new TileGraph(10, 10));

		mouseHandler = new SurfMouseInputHandler(lvl.node, board);

		pin = new Cursor(ResourceManager.get().loadObj("models/mdl_pin.obj"));
		rootNode.attachChild(pin);

		rootNode.updateRenderState();

		createPlayer();

	}

	private void createPlayer() {
		final float r = .2f;
		final Sphere sphere = new Sphere("sphere", 8, 8, r);
		final Node nty = new Node();
		sphere.setLocalTranslation(0, r, 0);
		nty.attachChild(sphere);
		rootNode.attachChild(nty);
		nty.setModelBound(new BoundingBox());
		nty.updateModelBound();
		final MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer()
				.createMaterialState();
		nty.setRenderState(ms);
		nty.updateRenderState();
		playerController = new PlayerController(nty, board);
		nty.addController(playerController);
		// n.setLocalTranslation(board.getSurfPos(new Vector2f(0, 0)));

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

		mouseHandler.update(tpf);
		movePin();

		rootNode.updateGeometricState(tpf, true);
	};

	private void movePin() {
		if (mouseHandler.isMouseOnSurface()) {
			pin.moveTo(board.getSurfPos(mouseHandler.getGridPos()));
			pin.setCullHint(CullHint.Dynamic);
		} else {
			pin.setCullHint(CullHint.Always);
		}

	}

	private void initInput() {
		input = new InputHandler();
		input.addAction(new InputAction() {

			@Override
			public void performAction(final InputActionEvent evt) {
				if (evt.getTriggerPressed()) {
					playerController.moveTo(mouseHandler.getGridPos());
				}
			}
		}, InputHandler.DEVICE_MOUSE, 0, InputHandler.AXIS_NONE, false);
	}

	private void handleInput() {
		input.update(0);
	}

}
