package game;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.BlendState;
import com.jme.system.DisplaySystem;

public class Cursor extends Node {

	private final float K = .3f;
	private final float damp = .6f;
	private final Vector3f vel = new Vector3f();
	private final BlendState blendState;

	private final Vector3f targetPos = new Vector3f();

	public Cursor(final Spatial model) {
		super("Cursor");
		setModel(model);
		blendState = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		blendState.setConstantColor(new ColorRGBA(1, 0, 0, .5f));
		setRenderState(blendState);
		updateRenderState();
	}

	public void moveTo(final Vector3f p) {
		targetPos.set(p);
	}

	private void setModel(final Spatial model) {
		attachChild(model);
	}

	@Override
	public void updateGeometricState(final float time, final boolean initiator) {
		vel.addLocal(targetPos.subtractLocal(getLocalTranslation())
				.multLocal(K));
		vel.multLocal(damp);
		getLocalTranslation().addLocal(vel);
		super.updateGeometricState(time, initiator);
	}

}
