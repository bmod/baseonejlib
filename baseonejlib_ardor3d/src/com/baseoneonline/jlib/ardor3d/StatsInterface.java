package com.baseoneonline.jlib.ardor3d;

import java.util.ArrayList;
import java.util.List;

import com.ardor3d.annotation.MainThread;
import com.ardor3d.framework.Canvas;
import com.ardor3d.image.TextureStoreFormat;
import com.ardor3d.input.Key;
import com.ardor3d.input.logical.InputTrigger;
import com.ardor3d.input.logical.KeyPressedCondition;
import com.ardor3d.input.logical.TriggerAction;
import com.ardor3d.input.logical.TwoInputStates;
import com.ardor3d.renderer.Renderer;
import com.ardor3d.renderer.queue.RenderBucketType;
import com.ardor3d.renderer.state.BlendState;
import com.ardor3d.renderer.state.WireframeState;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.ui.text.BasicText;
import com.ardor3d.util.ReadOnlyTimer;
import com.ardor3d.util.geom.Debugger;
import com.ardor3d.util.stat.StatCollector;
import com.ardor3d.util.stat.StatListener;

public class StatsInterface {

	private boolean enabled = false;

	private Node root;
	private final IGame game;
	private final IGameContainer gameContainer;

	private boolean showNormals = false;
	private boolean showBounds = false;
	private boolean showDepth = false;
	private WireframeState wireframeState;

	private List<InputTrigger> inputMap;

	public StatsInterface(final IGame game, final IGameContainer gameContainer) {
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

	private void start() {
		StatCollector.addStatListener(statListener);

		root = new Node("Root");
		final BlendState bs = new BlendState();
		bs.setEnabled(true);
		bs.setBlendEnabled(true);
		root.setRenderState(bs);
		root.getSceneHints().setRenderBucketType(RenderBucketType.Ortho);

		final BasicText text = BasicText.createDefaultTextLabel("MyText",
				"Ohay!");
		root.attachChild(text);

		root.updateWorldRenderStates(true);

		wireframeState = new WireframeState();
		wireframeState.setEnabled(false);

		registerInput();
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
						showNormals = !showNormals;
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

	private void stop() {
		StatCollector.removeStatListener(statListener);

		root = null;
		deregisterInput();
	}

	public void render(final Renderer renderer) {
		if (!enabled)
			return;

		final Node scene = gameContainer.getSceneRoot();

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

		root.onDraw(renderer);
	}

	public void update(final ReadOnlyTimer timer) {
		if (!enabled)
			return;

	}

	private final StatListener statListener = new StatListener() {

		@Override
		public void statsUpdated() {
		}
	};

}
