package game;

import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameState;

public class DebugState extends GameState {

	Node rootNode;
	Text tFPS;

	private final int interval = 60;
	private int ivalCount = 0;
	private float time = 0;

	public DebugState() {
		rootNode = new Node();
		rootNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);

		tFPS = Text.createDefaultTextLabel("FPSTEXT");
		rootNode.attachChild(tFPS);
		rootNode.updateRenderState();
		tFPS.setLocalTranslation(10, 10, 0);
	}

	@Override
	public void update(final float t) {
		if (--ivalCount <= 0) {
			ivalCount = interval;
			updateInterval();
			time = 0;
		}
		time += t;
	}

	private void updateInterval() {
		tFPS.print("FPS: " + interval / time);
		rootNode.updateGeometricState(1, true);
	}

	@Override
	public void cleanup() {

	}

	@Override
	public void render(final float tpf) {
		DisplaySystem.getDisplaySystem().getRenderer().draw(rootNode);
	}
}
