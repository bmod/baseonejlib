package test;

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
import com.ardor3d.input.GrabbedState;
import com.ardor3d.input.Key;
import com.ardor3d.input.MouseButton;
import com.ardor3d.input.MouseManager;
import com.ardor3d.input.PhysicalLayer;
import com.ardor3d.input.logical.AnyKeyCondition;
import com.ardor3d.input.logical.InputTrigger;
import com.ardor3d.input.logical.KeyPressedCondition;
import com.ardor3d.input.logical.LogicalLayer;
import com.ardor3d.input.logical.MouseButtonClickedCondition;
import com.ardor3d.input.logical.MouseButtonPressedCondition;
import com.ardor3d.input.logical.MouseButtonReleasedCondition;
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
import com.ardor3d.renderer.pass.OutlinePass;
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
import com.ardor3d.util.stat.StatCollector;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public abstract class TestBase implements Runnable
{
	private ParallelSplitShadowMapPass shadowPass;
	private OutlinePass outlinePass;
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

	protected final Node root = new Node();

	protected LightState lightState;
	protected WireframeState wireframeState;

	protected volatile boolean _exit = false;

	protected boolean showBounds = false;
	protected boolean showNormals = false;
	protected boolean showDepth = false;

	protected boolean doShot = false;

	protected NativeCanvas canvas;

	protected MouseManager mouseManager;

	protected PointLight light;

	public static int displayWidth = 720;
	public static int displayHeight = 406;
	public static int framerate = 60;
	public static int colorDepth = 32;

	protected Camera camera;

	@Override
	public void run()
	{
		try
		{
			frameHandler.init();

			while (!_exit)
			{
				frameHandler.updateFrame();
				Thread.yield();
			}
			// grab the graphics context so cleanup will work out.
			final CanvasRenderer cr = canvas.getCanvasRenderer();
			cr.makeCurrentContext();
			quit(cr.getRenderer());
			cr.releaseCurrentContext();
			if (QUIT_VM_ON_EXIT)
			{
				System.exit(0);
			}
		} catch (final Throwable t)
		{
			System.err.println("Throwable caught in MainThread - exiting");
			t.printStackTrace(System.err);
		}
	}

	public void exit()
	{
		_exit = true;
	}

	protected void setShadowDistance(final double dist)
	{
		shadowPass.setMaxShadowDistance(dist);
	}

	protected void setShadowsEnabled(final boolean b)
	{
		shadowPass.setEnabled(b);
	}

	protected abstract void init();

	protected final void initExample()
	{
		// Setup main camera.
		canvas.setTitle("Parallel Split Shadow Maps - Example");

		camera = canvas.getCanvasRenderer().getCamera();
		camera.setLocation(new Vector3(250, 200, -250));

		camera.setFrustumPerspective(45.0, (float) canvas.getCanvasRenderer()
				.getCamera().getWidth()
				/ (float) canvas.getCanvasRenderer().getCamera().getHeight(),
				1.0, 10000);

		camera.lookAt(new Vector3(0, 0, 0), Vector3.UNIT_Y);

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
		shadowPass.setEnabled(false);

		// Outline pass
		// outlinePass = new OutlinePass(true);
		// outlinePass.setOutlineColor(new ColorRGBA(10, 10, 10, 1));
		// outlinePass.add(root);

		shadowPass.addOccluder(root);

		passManager.add(rootPass);
		// passManager.add(outlinePass);
		passManager.add(shadowPass);

		root.acceptVisitor(new UpdateModelBoundVisitor(), false);

	}

	protected abstract void update(final ReadOnlyTimer timer);

	private final Scene scene = new Scene()
	{

		@Override
		public boolean renderUnto(final Renderer renderer)
		{
			// Execute renderQueue item
			GameTaskQueueManager
					.getManager(canvas.getCanvasRenderer().getRenderContext())
					.getQueue(GameTaskQueue.RENDER).execute(renderer);

			// Clean up card garbage such as textures, vbos, etc.
			ContextGarbageCollector.doRuntimeCleanup(renderer);

			/** Draw the rootNode and all its children. */
			if (!canvas.isClosing())
			{
				/** Call renderExample in any derived classes. */
				renderExample(renderer);
				renderDebug(renderer);

				return true;
			} else
			{
				return false;
			}
		}

		@Override
		public PickResults doPick(final Ray3 pickRay)
		{
			return null;
		}
	};

	protected void renderExample(final Renderer renderer)
	{
		if (!shadowPass.isInitialised())
		{
			shadowPass.init(renderer);
		}

		// Update the shadowpass "light" position. Iow it's camera.
		final Light light = lightState.get(0);
		if (light instanceof PointLight)
		{
			((PointLight) light).setLocation(lightPosition);
		} else if (light instanceof DirectionalLight)
		{
			((DirectionalLight) light).setDirection(lightPosition.normalize(
					null).negateLocal());
		}

		passManager.renderPasses(renderer);

	}

	protected void renderDebug(final Renderer renderer)
	{
		if (showBounds)
		{
			Debugger.drawBounds(root, renderer, true);
		}

		if (showNormals)
		{
			Debugger.drawNormals(root, renderer);
			Debugger.drawTangents(root, renderer);
		}

		if (showDepth)
		{
			renderer.renderBuckets();
			Debugger.drawBuffer(TextureStoreFormat.Depth16, Debugger.NORTHEAST,
					renderer);
		}
	}

	private final Updater updater = new Updater()
	{

		@Override
		public void update(final ReadOnlyTimer timer)
		{
			if (canvas.isClosing())
			{
				exit();
			}

			if (Constants.stats)
			{
				StatCollector.update();
			}

			logicalLayer.checkTriggers(timer.getTimePerFrame());

			// Execute updateQueue item
			GameTaskQueueManager
					.getManager(canvas.getCanvasRenderer().getRenderContext())
					.getQueue(GameTaskQueue.UPDATE).execute();

			/** Call simpleUpdate in any derived classes of ExampleBase. */
			TestBase.this.update(timer);

			passManager.updatePasses(timer.getTimePerFrame());

			if (updateLight)
			{
				final double time = timer.getTimeInSeconds() * 0.2;
				lightPosition.set(Math.sin(time) * 10000.0, 5000.0,
						Math.cos(time) * 10000.0);
			}

			/** Update controllers/render states/transforms/bounds for rootNode. */
			root.updateGeometricState(timer.getTimePerFrame(), true);
		}

		private void initStates()
		{
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
		public void init()
		{

			registerInputTriggers();

			initStates();

			initExample();

			TestBase.this.init();

			root.updateGeometricState(0);
		}
	};

	protected void quit(final Renderer renderer)
	{
		ContextGarbageCollector.doFinalCleanup(renderer);
		canvas.close();
	}

	public void start()
	{

		final DisplaySettings settings = new DisplaySettings(displayWidth,
				displayHeight, colorDepth, framerate, 1, 8, 0, 1, false, false);

		displaySettings = settings;

		final LwjglCanvasRenderer canvasRenderer = new LwjglCanvasRenderer(
				scene);
		canvas = new LwjglCanvas(canvasRenderer, settings);
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

	protected void registerInputTriggers()
	{

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.ESCAPE), new TriggerAction()
		{
			@Override
			public void perform(final Canvas source,
					final TwoInputStates inputState, final double tpf)
			{
				exit();
			}
		}));

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.L), new TriggerAction()
		{
			@Override
			public void perform(final Canvas source,
					final TwoInputStates inputState, final double tpf)
			{
				lightState.setEnabled(!lightState.isEnabled());
				// Either an update or a markDirty is needed here since we did
				// not touch the affected spatial directly.
				root.markDirty(DirtyType.RenderState);
			}
		}));

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.F4), new TriggerAction()
		{
			@Override
			public void perform(final Canvas source,
					final TwoInputStates inputState, final double tpf)
			{
				showDepth = !showDepth;
			}
		}));

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.T), new TriggerAction()
		{
			@Override
			public void perform(final Canvas source,
					final TwoInputStates inputState, final double tpf)
			{
				wireframeState.setEnabled(!wireframeState.isEnabled());
				// Either an update or a markDirty is needed here since we did
				// not touch the affected spatial directly.
				root.markDirty(DirtyType.RenderState);
			}
		}));

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.B), new TriggerAction()
		{
			@Override
			public void perform(final Canvas source,
					final TwoInputStates inputState, final double tpf)
			{
				showBounds = !showBounds;
			}
		}));

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.C), new TriggerAction()
		{
			@Override
			public void perform(final Canvas source,
					final TwoInputStates inputState, final double tpf)
			{
				System.out.println("Camera: "
						+ canvas.getCanvasRenderer().getCamera());
			}
		}));

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.N), new TriggerAction()
		{
			@Override
			public void perform(final Canvas source,
					final TwoInputStates inputState, final double tpf)
			{
				showNormals = !showNormals;
			}
		}));

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.F1), new TriggerAction()
		{
			@Override
			public void perform(final Canvas source,
					final TwoInputStates inputState, final double tpf)
			{
				doShot = true;
			}
		}));

		final Predicate<TwoInputStates> clickLeftOrRight = Predicates.or(
				new MouseButtonClickedCondition(MouseButton.LEFT),
				new MouseButtonClickedCondition(MouseButton.RIGHT));

		logicalLayer.registerTrigger(new InputTrigger(clickLeftOrRight,
				new TriggerAction()
				{
					@Override
					public void perform(final Canvas source,
							final TwoInputStates inputStates, final double tpf)
					{
						System.err.println("clicked: "
								+ inputStates.getCurrent().getMouseState()
										.getClickCounts());
					}
				}));

		logicalLayer.registerTrigger(new InputTrigger(
				new MouseButtonPressedCondition(MouseButton.LEFT),
				new TriggerAction()
				{
					@Override
					public void perform(final Canvas source,
							final TwoInputStates inputState, final double tpf)
					{
						if (mouseManager.isSetGrabbedSupported())
						{
							mouseManager.setGrabbed(GrabbedState.GRABBED);
						}
					}
				}));
		logicalLayer.registerTrigger(new InputTrigger(
				new MouseButtonReleasedCondition(MouseButton.LEFT),
				new TriggerAction()
				{
					@Override
					public void perform(final Canvas source,
							final TwoInputStates inputState, final double tpf)
					{
						if (mouseManager.isSetGrabbedSupported())
						{
							mouseManager.setGrabbed(GrabbedState.NOT_GRABBED);
						}
					}
				}));

		logicalLayer.registerTrigger(new InputTrigger(new AnyKeyCondition(),
				new TriggerAction()
				{
					@Override
					public void perform(final Canvas source,
							final TwoInputStates inputState, final double tpf)
					{
						System.out.println("Key character pressed: "
								+ inputState.getCurrent().getKeyboardState()
										.getKeyEvent().getKeyChar());
					}
				}));

	}
}