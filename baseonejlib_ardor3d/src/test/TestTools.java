package test;

import com.ardor3d.annotation.MainThread;
import com.ardor3d.framework.Canvas;
import com.ardor3d.input.Key;
import com.ardor3d.input.logical.InputTrigger;
import com.ardor3d.input.logical.KeyPressedCondition;
import com.ardor3d.input.logical.TriggerAction;
import com.ardor3d.input.logical.TwoInputStates;
import com.ardor3d.math.Vector3;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.shape.Box;
import com.ardor3d.util.ReadOnlyTimer;
import com.baseoneonline.java.swing.config.Config;
import com.baseoneonline.jlib.ardor3d.GameBase;
import com.baseoneonline.jlib.ardor3d.tools.ToolsWindow;

public class TestTools extends GameBase {

	public static void main(final String[] args) {
		Config.setApplicationClass(TestTools.class);
		new TestTools().start();
	}

	@Override
	protected void init() {
		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.GRAVE), showToolsAction));

		final Node childNode = new Node("Child Node");
		root.attachChild(childNode);
		final Box b = new Box("Kak", Vector3.ZERO, 1, 1, 1);
		root.attachChild(b);
	}

	@Override
	protected void update(final ReadOnlyTimer timer) {

	}

	@Override
	public void exit() {
		Config.get().flush();
		super.exit();
	}

	private final TriggerAction showToolsAction = new TriggerAction() {

		@Override
		@MainThread
		public void perform(final Canvas source,
				final TwoInputStates inputStates, final double tpf) {
			ToolsWindow.show(root);
		}
	};

}
