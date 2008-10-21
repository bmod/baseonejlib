package com.baseoneonline.java.blips;

import com.baseoneonline.java.jme.BasicFixedRateGame;
import com.jme.input.MouseInput;
import com.jme.input.MouseInputListener;
import com.jme.input.controls.binding.MouseButtonBinding;
import com.jme.math.Plane;
import com.jme.math.Vector3f;

public class TestBlips extends BasicFixedRateGame {

	public static void main(final String[] args) {
		final TestBlips app = new TestBlips();
		app.start();

	}

	private PlayerNode player;

	private Plane clickPlane;

	private MousePullController playerController;

	@Override
	protected void initFixedRateGame() {

		MouseInput.get().setCursorVisible(true);

		MouseInput.get().addListener(new MouseInputListener() {
			public void onButton(final int button, final boolean pressed,
					final int x, final int y) {
				if (MouseButtonBinding.RIGHT_BUTTON == button && pressed) {
					player.pulse();
				}
				
			}

			public void onMove(final int delta, final int delta2, final int newX, final int newY) {}

			public void onWheel(final int wheelDelta, final int x, final int y) {}
		});

		player = new PlayerNode();

		clickPlane = new Plane(Vector3f.UNIT_Z, 1);

		playerController = new MousePullController(player, clickPlane);
		player.addController(playerController);

		rootNode.attachChild(player);
	}

	@Override
	protected void update() {

		player.update();
	}

}
