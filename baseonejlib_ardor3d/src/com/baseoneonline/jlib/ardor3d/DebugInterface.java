package com.baseoneonline.jlib.ardor3d;

import java.util.ArrayList;
import java.util.List;

import com.ardor3d.annotation.MainThread;
import com.ardor3d.bounding.BoundingSphere;
import com.ardor3d.bounding.BoundingVolume;
import com.ardor3d.framework.Canvas;
import com.ardor3d.image.TextureStoreFormat;
import com.ardor3d.input.Key;
import com.ardor3d.input.MouseButton;
import com.ardor3d.input.MouseState;
import com.ardor3d.input.logical.InputTrigger;
import com.ardor3d.input.logical.KeyHeldCondition;
import com.ardor3d.input.logical.KeyPressedCondition;
import com.ardor3d.input.logical.MouseButtonClickedCondition;
import com.ardor3d.input.logical.MouseButtonPressedCondition;
import com.ardor3d.input.logical.TriggerAction;
import com.ardor3d.input.logical.TwoInputStates;
import com.ardor3d.intersection.PickData;
import com.ardor3d.intersection.PickResults;
import com.ardor3d.intersection.Pickable;
import com.ardor3d.intersection.PickingUtil;
import com.ardor3d.intersection.PrimitivePickResults;
import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Ray3;
import com.ardor3d.math.Vector2;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.renderer.Camera;
import com.ardor3d.renderer.Renderer;
import com.ardor3d.renderer.queue.RenderBucketType;
import com.ardor3d.renderer.state.BlendState;
import com.ardor3d.renderer.state.WireframeState;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.ui.text.BMText;
import com.ardor3d.ui.text.BasicText;
import com.ardor3d.util.ReadOnlyTimer;
import com.ardor3d.util.geom.Debugger;
import com.ardor3d.util.stat.StatCollector;
import com.ardor3d.util.stat.StatListener;
import com.baseoneonline.jlib.ardor3d.controllers.EditorCameraController;
import com.google.common.base.Predicates;

public class DebugInterface {

	private boolean enabled = false;

	private Node uiNode;
	private final IGame game;
	private final IGameContainer gameContainer;

	private boolean showNormals = false;
	private boolean showBounds = false;
	private boolean showDepth = false;
	private boolean showDebugCamera = false;

	private WireframeState wireframeState;
	private List<InputTrigger> inputMap;
	private EditorCameraController camCtrl;
	private BMText txtLabel;
	private final List<Spatial> selection = new ArrayList<Spatial>();

	/**
	 * @warning Please initialize in {@link #start()}
	 * 
	 * @param game
	 * @param gameContainer
	 */
	public DebugInterface(final IGame game, final IGameContainer gameContainer) {
		this.game = game;
		this.gameContainer = gameContainer;
	}

	public void toggle() {
		setEnabled(!enabled);
	}

	public void setEnabled(final boolean enabled) {
		if (this.enabled == enabled)
			return;
		this.enabled = enabled;

		if (enabled)
			start();
		else
			stop();
	}

	public boolean isEnabled() {
		return enabled;
	}

	private void start() {
		StatCollector.addStatListener(statListener);

		uiNode = new Node("UI");
		final BlendState bs = new BlendState();
		bs.setEnabled(true);
		bs.setBlendEnabled(true);
		uiNode.setRenderState(bs);
		uiNode.getSceneHints().setRenderBucketType(RenderBucketType.Ortho);

		txtLabel = addText();
		txtLabel.setTranslation(8, 8, 0);

		uiNode.updateWorldRenderStates(true);

		registerInput();

		if (null == camCtrl)
			camCtrl = new EditorCameraController(game.getLogicalLayer(),
					game.getMainCamera());

		if (wireframeState == null) {
			wireframeState = new WireframeState();
			wireframeState.setEnabled(false);
			gameContainer.getSceneRoot().setRenderState(wireframeState);
		}

		checkInfinityBounds(gameContainer.getSceneRoot());
		focusAll();
	}

	private BMText addText() {
		final BMText label = BasicText.createDefaultTextLabel("MyText",
				"Ohay!", 12);
		uiNode.attachChild(label);
		return label;
	}

	private void stop() {
		StatCollector.removeStatListener(statListener);

		uiNode = null;
		deregisterInput();
	}

	private void registerInput() {
		inputMap = new ArrayList<InputTrigger>();
		inputMap.add(new InputTrigger(new KeyPressedCondition(Key.F1),
				new TriggerAction() {

					@Override
					@MainThread
					public void perform(final Canvas source,
							final TwoInputStates inputStates, final double tpf) {
						showBounds = !showBounds;
					}
				}));
		inputMap.add(new InputTrigger(new KeyPressedCondition(Key.F2),
				new TriggerAction() {

					@Override
					@MainThread
					public void perform(final Canvas source,
							final TwoInputStates inputStates, final double tpf) {
						showDepth = !showDepth;
					}
				}));
		inputMap.add(new InputTrigger(new KeyPressedCondition(Key.F3),
				new TriggerAction() {

					@Override
					@MainThread
					public void perform(final Canvas source,
							final TwoInputStates inputStates, final double tpf) {
						showNormals = !showNormals;
					}
				}));

		inputMap.add(new InputTrigger(new KeyPressedCondition(Key.F4),
				new TriggerAction() {

					@Override
					@MainThread
					public void perform(final Canvas source,
							final TwoInputStates inputStates, final double tpf) {
						wireframeState.setEnabled(!wireframeState.isEnabled());
					}
				}));
		inputMap.add(new InputTrigger(Predicates.and(
				Predicates.not(new KeyHeldCondition(Key.LSHIFT)),
				new MouseButtonClickedCondition(MouseButton.LEFT)),
				new TriggerAction() {

					@Override
					@MainThread
					public void perform(final Canvas source,
							final TwoInputStates inputStates, final double tpf) {
						final Spatial s = findSpatialUnderMouse(inputStates
								.getCurrent().getMouseState());
						clearSelection();
						if (s != null)
							addSelection(s);
					}
				}));
		inputMap.add(new InputTrigger(
				Predicates.and(new KeyHeldCondition(Key.LSHIFT),
						new MouseButtonPressedCondition(MouseButton.LEFT)),
				new TriggerAction() {

					@Override
					@MainThread
					public void perform(final Canvas source,
							final TwoInputStates inputStates, final double tpf) {
						final Spatial s = findSpatialUnderMouse(inputStates
								.getCurrent().getMouseState());
						if (s != null)
							addSelection(s);
					}
				}));

		inputMap.add(new InputTrigger(new KeyPressedCondition(Key.F),
				new TriggerAction() {

					@Override
					@MainThread
					public void perform(final Canvas source,
							final TwoInputStates inputStates, final double tpf) {
						focusSelection();
					}
				}));
		inputMap.add(new InputTrigger(new KeyPressedCondition(Key.A),
				new TriggerAction() {

					@Override
					@MainThread
					public void perform(final Canvas source,
							final TwoInputStates inputStates, final double tpf) {
						focusAll();
					}
				}));
		inputMap.add(new InputTrigger(new KeyPressedCondition(Key.ONE),
				new TriggerAction() {

					@Override
					@MainThread
					public void perform(final Canvas source,
							final TwoInputStates inputStates, final double tpf) {
						showDebugCamera = !showDebugCamera;
						if (showDebugCamera)
							focusSelection();
					}
				}));

		for (final InputTrigger t : inputMap)
			game.getLogicalLayer().registerTrigger(t);
	}

	private void deregisterInput() {
		for (final InputTrigger t : inputMap)
			game.getLogicalLayer().deregisterTrigger(t);
		inputMap.clear();
	}

	public void render(final Renderer renderer) {
		if (!enabled)
			return;

		final Node scene = gameContainer.getSceneRoot();

		Debugger.setBoundsColor(ColorRGBA.BLUE);
		if (showBounds) {
			Debugger.drawBounds(scene, renderer, true);
		}

		if (showNormals) {
			Debugger.drawNormals(scene, renderer);
			Debugger.drawTangents(scene, renderer);
		}

		if (showDepth) {
			renderer.renderBuckets();
			Debugger.drawBuffer(TextureStoreFormat.Depth16, Debugger.NORTHEAST,
					renderer);
		}

		Debugger.setBoundsColor(ColorRGBA.YELLOW);
		for (final Spatial s : selection) {
			Debugger.drawBounds(s, renderer, true);
			Debugger.drawAxis(s, renderer);
		}

		uiNode.onDraw(renderer);
	}

	public void update(final ReadOnlyTimer timer) {

		uiNode.updateGeometricState(timer.getTimePerFrame(), true);

		StringBuffer buf = new StringBuffer();
		// Selection
		if (selection.size() > 0) {
			buf.append("Selection:\n");
			for (Spatial s : selection)
				buf.append("\t" + s.toString() + '\n');
			buf.append("\n");
		}

		// Camera
		final Camera cam = game.getMainCamera();
		final ReadOnlyVector3 pos = cam.getLocation();
		final ReadOnlyVector3 aim = camCtrl.getCenter();
		buf.append(String.format(
				"Camera: (%.2f, %.2f, %.2f) Target:  (%.2f, %.2f, %.2f)",
				pos.getX(), pos.getY(), pos.getZ(), aim.getX(), aim.getY(),
				aim.getZ()));

		txtLabel.setText(buf.toString());
	}

	public void postUpdate(final double tpf) {
		if (showDebugCamera)
			camCtrl.update();
	}

	private final StatListener statListener = new StatListener() {

		@Override
		public void statsUpdated() {

		}
	};

	private void clearSelection() {
		selection.clear();
	}

	private void addSelection(final Spatial s) {
		selection.add(s);
	}

	private Spatial findSpatialUnderMouse(final MouseState ms) {
		final Camera cam = game.getMainCamera();
		final Ray3 ray = new Ray3();
		cam.getPickRay(new Vector2(ms.getX(), ms.getY()), false, ray);
		final PickResults results = new PrimitivePickResults();
		PickingUtil.findPick(gameContainer.getSceneRoot(), ray, results);
		final PickData pdata = results.findFirstIntersectingPickData();
		if (pdata != null) {
			final Pickable pickable = pdata.getTarget();
			if (pickable instanceof Spatial) {
				return (Spatial) pickable;
			}
		}
		return null;
	}

	private void focusCamera(final Spatial... spatials) {
		if (spatials.length < 1)
			return;
		BoundingVolume bounds = null;
		for (final Spatial s : spatials) {
			if (bounds == null)
				bounds = s.getWorldBound().clone(new BoundingSphere());
			else
				bounds.mergeLocal(s.getWorldBound());
		}
		camCtrl.setCenter(bounds.getCenter());
		camCtrl.setDistance(bounds.getRadius() * 1.5);
	}

	private void focusAll() {
		focusCamera(gameContainer.getSceneRoot());
	}

	private void focusSelection() {
		if (selection.size() == 0)
			focusAll();
		else
			focusCamera(selection.toArray(new Spatial[selection.size()]));
	}

	private void checkInfinityBounds(final Spatial spatial) {

		if (spatial instanceof Node)
			for (final Spatial s : ((Node) spatial).getChildren())
				checkInfinityBounds(s);

		final BoundingVolume bounds = spatial.getWorldBound();

		if (bounds == null)
			throw new RuntimeException("Spatial has no bounds: " + spatial);

		if (Double.isInfinite(bounds.getRadius()))
			throw new RuntimeException("Spatial has inifintely large bounds: "
					+ spatial);
	}
}
