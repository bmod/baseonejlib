package com.baseoneonline.java.test.testStandardGame;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameState;

public class StatusState extends GameState {

	Node root;

	public StatusState() {
		root = new Node();
		root.setRenderQueueMode(Renderer.QUEUE_ORTHO);

		root.attachChild(new CameraStatus());
	}

	@Override
	public void cleanup() {}

	@Override
	public void render(final float tpf) {
		DisplaySystem.getDisplaySystem().getRenderer().draw(root);
	}

	@Override
	public void update(final float tpf) {
		root.updateGeometricState(tpf, true);
	}

}

class CameraStatus extends Node {

	Text txt;

	public CameraStatus() {
		// final BitmapFont fnt = BitmapFontLoader.loadDefaultFont();
		// txt = new BitmapText(fnt, false);
		// txt.setLocalTranslation(100, 100, 0);
		txt = Text.createDefaultTextLabel("");
		txt.setRenderState(Text.getFontBlend());
		txt.updateRenderState();

		attachChild(txt);
	}

	@Override
	public void updateGeometricState(final float time, final boolean initiator) {
		final Vector3f p = DisplaySystem.getDisplaySystem().getRenderer()
				.getCamera().getLocation();
		final NumberFormat fmt = new DecimalFormat("000.00");
		txt.print(fmt.format(p.x) + ", " + fmt.format(p.y) + ", "
				+ fmt.format(p.z));

		super.updateGeometricState(time, initiator);

	}
}
