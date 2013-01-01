package com.baseoneonline.jlib.ardor3d;

import com.ardor3d.renderer.Renderer;
import com.ardor3d.renderer.state.BlendState;
import com.ardor3d.renderer.state.CullState;
import com.ardor3d.renderer.state.ZBufferState;
import com.ardor3d.scenegraph.Node;

public abstract class BaseGameContainer implements IGameContainer {

	protected Node root = new Node("Root");
	protected IGame game;

	@Override
	public void init(final IGame game) {
		this.game = game;

		final ZBufferState buf = new ZBufferState();
		buf.setEnabled(true);
		buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
		root.setRenderState(buf);
		final BlendState bs = new BlendState();
		bs.setBlendEnabled(true);
		root.setRenderState(bs);
		final CullState cullFrontFace = new CullState();
		cullFrontFace.setEnabled(true);
		cullFrontFace.setCullFace(CullState.Face.Back);
		root.setRenderState(cullFrontFace);
		root.updateWorldRenderStates(true);
	}

	@Override
	public void update(final double t) {
		root.updateGeometricState(t, true);
	}

	@Override
	public void postUpdate(final double t) {
	}

	@Override
	public void render(final Renderer r) {
		root.onDraw(r);
		r.renderBuckets();
	}

	@Override
	public Node getSceneRoot() {
		return root;
	}

}
