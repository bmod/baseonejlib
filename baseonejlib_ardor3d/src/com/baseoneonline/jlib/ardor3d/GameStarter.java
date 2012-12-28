package com.baseoneonline.jlib.ardor3d;

import com.ardor3d.extension.shadow.map.ParallelSplitShadowMapPass;
import com.ardor3d.extension.shadow.map.ParallelSplitShadowMapPass.Filter;
import com.ardor3d.framework.Canvas;
import com.ardor3d.framework.CanvasRenderer;
import com.ardor3d.framework.DisplaySettings;
import com.ardor3d.framework.FrameHandler;
import com.ardor3d.framework.NativeCanvas;
import com.ardor3d.framework.Scene;
import com.ardor3d.framework.Updater;
import com.ardor3d.framework.lwjgl.LwjglCanvas;
import com.ardor3d.framework.lwjgl.LwjglCanvasRenderer;
import com.ardor3d.image.TextureStoreFormat;
import com.ardor3d.image.util.AWTImageLoader;
import com.ardor3d.image.util.ScreenShotImageExporter;
import com.ardor3d.input.Key;
import com.ardor3d.input.MouseButton;
import com.ardor3d.input.MouseManager;
import com.ardor3d.input.PhysicalLayer;
import com.ardor3d.input.logical.InputTrigger;
import com.ardor3d.input.logical.KeyPressedCondition;
import com.ardor3d.input.logical.LogicalLayer;
import com.ardor3d.input.logical.MouseButtonClickedCondition;
import com.ardor3d.input.logical.TriggerAction;
import com.ardor3d.input.logical.TwoInputStates;
import com.ardor3d.input.lwjgl.LwjglControllerWrapper;
import com.ardor3d.input.lwjgl.LwjglKeyboardWrapper;
import com.ardor3d.input.lwjgl.LwjglMouseManager;
import com.ardor3d.input.lwjgl.LwjglMouseWrapper;
import com.ardor3d.intersection.PickResults;
import com.ardor3d.light.DirectionalLight;
import com.ardor3d.light.Light;
import com.ardor3d.light.PointLight;
import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Ray3;
import com.ardor3d.math.Vector3;
import com.ardor3d.renderer.Camera;
import com.ardor3d.renderer.Renderer;
import com.ardor3d.renderer.TextureRendererFactory;
import com.ardor3d.renderer.lwjgl.LwjglTextureRendererProvider;
import com.ardor3d.renderer.pass.BasicPassManager;
import com.ardor3d.renderer.pass.RenderPass;
import com.ardor3d.renderer.queue.RenderBucketType;
import com.ardor3d.renderer.state.CullState;
import com.ardor3d.renderer.state.LightState;
import com.ardor3d.renderer.state.WireframeState;
import com.ardor3d.renderer.state.ZBufferState;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.event.DirtyType;
import com.ardor3d.scenegraph.visitor.UpdateModelBoundVisitor;
import com.ardor3d.util.Constants;
import com.ardor3d.util.ContextGarbageCollector;
import com.ardor3d.util.GameTaskQueue;
import com.ardor3d.util.GameTaskQueueManager;
import com.ardor3d.util.ReadOnlyTimer;
import com.ardor3d.util.Timer;
import com.ardor3d.util.geom.Debugger;
import com.ardor3d.util.screen.ScreenExporter;
import com.ardor3d.util.stat.StatCollector;
import com.google.common.base.Predicates;

public class GameStarter implements Runnable, IGame {
	private ParallelSplitShadowMapPass shadowPass;
	private BasicPassManager passManager;

	private final Vector3 lightPosition = new Vector3(10000, 5000, 10000);

	private final boolean updateLight = false;

	/** Temp vec for updating light pos. */

	public static boolean QUIT_VM_ON_EXIT = true;

	protected final LogicalLayer logicalLayer = new LogicalLayer();

	protected PhysicalLayer physicalLayer;

	protected final Timer timer = new Timer();
	protected final FrameHandler frameHandler = new FrameHandler(timer);

	protected DisplaySettings displaySettings;

	protected final Node root = new Node("RootNode");

	protected LightState lightState;
	protected WireframeState wireframeState;

	protected volatile boolean _exit = false;

	protected boolean showBounds = false;
	protected boolean showNormals = false;
	protected boolean showDepth = false;

	protected boolean paused = false;
	protected boolean stepping = false;

	protected boolean doShot = false;

	protected NativeCanvas canvas;

	protected ScreenShotImageExporter screenShotExp = new ScreenShotImageExporter();

	protected MouseManager mouseManager;

	protected PointLight light;

	public static int framerate = 60;
	public static int colorDepth = 32;

	protected Camera camera;
	private final IGameContainer game;

	public GameStarter(IGameContainer game) {
		this.game = game;
	}

	public float getDisplayRatio() {
		return (float) displaySettings.getWidth()
				/ (float) displaySettings.getHeight();
	}

	@Override
	public void run() {
		try {
			frameHandler.init();

			while (!_exit) {
				frameHandler.updateFrame();
				Thread.yield();
			}
			// grab the graphics context so cleanup will work out.
			final CanvasRenderer cr = canvas.getCanvasRenderer();
			cr.makeCurrentContext();
			quit(cr.getRenderer());
			cr.releaseCurrentContext();
			if (QUIT_VM_ON_EXIT) {
				System.exit(0);
			}
		} catch (final Throwable t) {
			System.err.println("Throwable caught in MainThread - exiting");
			t.printStackTrace(System.err);
		}
	}

	public void exit() {
		_exit = true;
	}

	protected void setShadowsEnabled(final boolean b) {
		shadowPass.setEnabled(b);
	}

	protected final void initExample() {
		// Setup main camera.
		canvas.setTitle(getClass().getSimpleName());

		camera = canvas.getCanvasRenderer().getCamera();
		camera.setLocation(new Vector3(250, 200, -250));

		camera.setFrustumPerspective(45.0, (float) canvas.getCanvasRenderer()
				.getCamera().getWidth()
				/ (float) canvas.getCanvasRenderer().getCamera().getHeight(),
				1.0, 10000);

		camera.lookAt(Vector3.ZERO, Vector3.UNIT_Y);

		// Setup some standard states for the scene.
		final CullState cullFrontFace = new CullState();
		cullFrontFace.setEnabled(true);
		cullFrontFace.setCullFace(CullState.Face.Back);
		root.setRenderState(cullFrontFace);

		// final TextureState ts = new TextureState();
		// ts.setEnabled(true);
		// ts.setTexture(TextureManager.load("images/ardor3d_white_256.jpg",
		// Texture.MinificationFilter.Trilinear, true));
		// root.setRenderState(ts);

		// final MaterialState ms = new MaterialState();
		// ms.setColorMaterial(ColorMaterial.Diffuse);
		// root.setRenderState(ms);

		passManager = new BasicPassManager();

		final RenderPass rootPass = new RenderPass();
		rootPass.add(root);

		lightState.detachAll();
		final DirectionalLight light = new DirectionalLight();
		// final PointLight light = new PointLight();

		light.setEnabled(true);
		lightState.attach(light);

		// Create pssm pass
		shadowPass = new ParallelSplitShadowMapPass(light, 1024, 3);
		shadowPass.setFiltering(Filter.Pcf);
		shadowPass.setMaxShadowDistance(500);
		shadowPass.add(root);
		shadowPass.setUseSceneTexturing(true);
		shadowPass.setUseObjectCullFace(true);

		// Outline pass
		// outlinePass = new OutlinePass(true);
		// outlinePass.setOutlineColor(new ColorRGBA(10, 10, 10, 1));
		// outlinePass.add(root);

		shadowPass.addOccluder(root);

		passManager.add(rootPass);
		// passManager.add(outlinePass);
		// passManager.add(shadowPass);

		root.acceptVisitor(new UpdateModelBoundVisitor(), false);

	}

	private final Scene scene = new Scene() {

		@Override
		public boolean renderUnto(final Renderer renderer) {
			// Execute renderQueue item
			GameTaskQueueManager
					.getManager(canvas.getCanvasRenderer().getRenderContext())
					.getQueue(GameTaskQueue.RENDER).execute(renderer);

			// Clean up card garbage such as textures, vbos, etc.
			ContextGarbageCollector.doRuntimeCleanup(renderer);

			/** Draw the rootNode and all its children. */
			if (!canvas.isClosing()) {
				/** Call renderExample in any derived classes. */
				game.render(renderer);
				// renderBase(renderer);
				renderDebug(renderer);

				if (doShot) {
					// force any waiting scene elements to be renderer.
					renderer.renderBuckets();
					ScreenExporter.exportCurrentScreen(canvas
							.getCanvasRenderer().getRenderer(), screenShotExp);
					doShot = false;
				}
				return true;
			} else {
				return false;
			}
		}

		@Override
		public PickResults doPick(final Ray3 pickRay) {
			return null;
		}
	};

	protected void renderBase(final Renderer renderer) {
		if (!shadowPass.isInitialised()) {
			shadowPass.init(renderer);
		}

		// Update the shadowpass "light" position. Iow it's camera.
		final Light light = lightState.get(0);
		if (light instanceof PointLight) {
			((PointLight) light).setLocation(lightPosition);
		} else if (light instanceof DirectionalLight) {
			final Vector3 tmp = Vector3.fetchTempInstance();
			lightPosition.normalize(tmp).negateLocal();
			((DirectionalLight) light).setDirection(tmp);
			Vector3.releaseTempInstance(tmp);
		}

		passManager.renderPasses(renderer);

	}

	protected void renderDebug(final Renderer renderer) {
		if (showBounds) {
			Debugger.drawBounds(root, renderer, true);
		}

		if (showNormals) {
			Debugger.drawNormals(root, renderer);
			Debugger.drawTangents(root, renderer);
		}

		if (showDepth) {
			renderer.renderBuckets();
			Debugger.drawBuffer(TextureStoreFormat.Depth16, Debugger.NORTHEAST,
					renderer);
		}
	}

	private final Updater updater = new Updater() {

		@Override
		public void update(final ReadOnlyTimer timer) {
			if (canvas.isClosing()) {
				exit();
			}

			if (Constants.stats) {
				StatCollector.update();
			}

			logicalLayer.checkTriggers(timer.getTimePerFrame());

			// Execute updateQueue item
			if (!paused || stepping) {
				GameTaskQueueManager
						.getManager(
								canvas.getCanvasRenderer().getRenderContext())
						.getQueue(GameTaskQueue.UPDATE).execute();

				/** Call simpleUpdate in any derived classes of ExampleBase. */
				game.update(timer.getTimePerFrame());
			}

			passManager.updatePasses(timer.getTimePerFrame());

			if (updateLight) {
				final double time = timer.getTimeInSeconds() * 0.2;
				lightPosition.set(Math.sin(time) * 10000.0, 5000.0,
						Math.cos(time) * 10000.0);
			}

			if (!paused || stepping) {
				root.updateGeometricState(timer.getTimePerFrame(), true);
				game.lateUpdate(timer.getTimePerFrame());
			}

			stepping = false;
		}

		private void initStates() {
			final ZBufferState buf = new ZBufferState();
			buf.setEnabled(true);
			buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
			root.setRenderState(buf);

			// ---- LIGHTS
			/** Set up a basic, default light. */
			light = new PointLight();
			light.setDiffuse(new ColorRGBA(0.75f, 0.75f, 0.75f, 0.75f));
			light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
			light.setLocation(new Vector3(100, 100, 100));
			light.setEnabled(true);

			/** Attach the light to a lightState and the lightState to rootNode. */
			lightState = new LightState();
			lightState.setEnabled(true);
			lightState.attach(light);
			root.setRenderState(lightState);

			wireframeState = new WireframeState();
			wireframeState.setEnabled(false);
			root.setRenderState(wireframeState);

			root.getSceneHints().setRenderBucketType(RenderBucketType.Opaque);
		}

		@Override
		public void init() {

			registerInputTriggers();

			AWTImageLoader.registerLoader();

			initStates();

			initExample();

			game.init(GameStarter.this);

			root.updateGeometricState(0);
		}
	};

	protected void quit(final Renderer renderer) {
		ContextGarbageCollector.doFinalCleanup(renderer);
		canvas.close();
	}

	public void start(final int width, final int height,
			final boolean fullscreen) {
		start(new DisplaySettings(width, height, colorDepth, framerate, 1, 8,
				0, 1, fullscreen, false));
	}

	public void start() {
		start(new DisplaySettings(720, 406, colorDepth, framerate, 1, 8, 0, 1,
				false, false));
	}

	public void start(final DisplaySettings settings) {

		displaySettings = settings;

		final LwjglCanvasRenderer canvasRenderer = new LwjglCanvasRenderer(
				scene);
		canvas = new LwjglCanvas(settings, canvasRenderer);
		canvas.setVSyncEnabled(true);
		physicalLayer = new PhysicalLayer(new LwjglKeyboardWrapper(),
				new LwjglMouseWrapper(), new LwjglControllerWrapper(),
				(LwjglCanvas) canvas);
		mouseManager = new LwjglMouseManager();
		TextureRendererFactory.INSTANCE
				.setProvider(new LwjglTextureRendererProvider());

		logicalLayer.registerInput(canvas, physicalLayer);

		// Register our example as an updater.
		frameHandler.addUpdater(updater);

		// register our native canvas
		frameHandler.addCanvas(canvas);

		new Thread(this).start();
	}

	protected void registerInputTriggers() {

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.ESCAPE), new TriggerAction() {
			@Override
			public void perform(final Canvas source,
					final TwoInputStates inputState, final double tpf) {
				exit();
			}
		}));

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.L), new TriggerAction() {
			@Override
			public void perform(final Canvas source,
					final TwoInputStates inputState, final double tpf) {
				lightState.setEnabled(!lightState.isEnabled());
				// Either an update or a markDirty is needed here since we did
				// not touch the affected spatial directly.
				root.markDirty(DirtyType.RenderState);
			}
		}));

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.F4), new TriggerAction() {
			@Override
			public void perform(final Canvas source,
					final TwoInputStates inputState, final double tpf) {
				showDepth = !showDepth;
			}
		}));

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.T), new TriggerAction() {
			@Override
			public void perform(final Canvas source,
					final TwoInputStates inputState, final double tpf) {
				wireframeState.setEnabled(!wireframeState.isEnabled());
				// Either an update or a markDirty is needed here since we did
				// not touch the affected spatial directly.
				root.markDirty(DirtyType.RenderState);
			}
		}));

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.B), new TriggerAction() {
			@Override
			public void perform(final Canvas source,
					final TwoInputStates inputState, final double tpf) {
				showBounds = !showBounds;
			}
		}));

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.C), new TriggerAction() {
			@Override
			public void perform(final Canvas source,
					final TwoInputStates inputState, final double tpf) {
				System.out.println("Camera: "
						+ canvas.getCanvasRenderer().getCamera());
			}
		}));

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.N), new TriggerAction() {
			@Override
			public void perform(final Canvas source,
					final TwoInputStates inputState, final double tpf) {
				showNormals = !showNormals;
			}
		}));

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.F1), new TriggerAction() {
			@Override
			public void perform(final Canvas source,
					final TwoInputStates inputState, final double tpf) {
				doShot = true;
			}
		}));

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.P), new TriggerAction() {

			@Override
			public void perform(final Canvas source,
					final TwoInputStates inputStates, final double tpf) {
				paused = !paused;
			}
		}));

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.O), new TriggerAction() {

			@Override
			public void perform(final Canvas source,
					final TwoInputStates inputStates, final double tpf) {
				stepping = true;
			}
		}));

		Predicates.or(new MouseButtonClickedCondition(MouseButton.LEFT),
				new MouseButtonClickedCondition(MouseButton.RIGHT));

	}

	@Override
	public Camera getCamera() {
		return camera;
	}

	@Override
	public LogicalLayer getLogicalLayer() {
		return logicalLayer;
	}
}
