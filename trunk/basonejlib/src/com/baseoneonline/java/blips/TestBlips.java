package com.baseoneonline.java.blips;




import java.util.logging.Level;
import java.util.logging.Logger;

import com.baseoneonline.java.blips.core.MousePullController;
import com.baseoneonline.java.blips.core.PlayerNode;
import com.baseoneonline.java.blips.sequencer.Sequencer;
import com.baseoneonline.java.blips.sequencer.SequencerEvent;
import com.baseoneonline.java.blips.sequencer.SequencerListener;
import com.baseoneonline.java.jme.BasicFixedRateGame;
import com.jme.input.MouseInput;
import com.jme.input.MouseInputListener;
import com.jme.input.controls.binding.MouseButtonBinding;
import com.jme.math.Plane;
import com.jme.math.Vector3f;

public class TestBlips extends BasicFixedRateGame {

	String basePath = "com/baseoneonline/java/blips/data/";
	
	public static void main(final String[] args) {
		Logger.getLogger("com.jme").setLevel(Level.WARNING);
		final TestBlips app = new TestBlips();
		app.start();

	}

	private PlayerNode player;

	private Plane clickPlane;
	
	private final Sequencer sequencer = new Sequencer();

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
		
		sequencer.addListener(new SequencerListener() {
			public void onEvent(final SequencerEvent ev) {
				player.pulse();
			}
		});
		sequencer.start();
		
	}

	@Override
	protected void update() {

		player.update();
	}

}
