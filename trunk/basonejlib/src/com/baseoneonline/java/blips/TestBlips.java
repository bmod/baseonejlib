package com.baseoneonline.java.blips;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.baseoneonline.java.blips.core.BlipSystem;
import com.baseoneonline.java.blips.core.MousePullController;
import com.baseoneonline.java.blips.entities.Blip;
import com.baseoneonline.java.blips.entities.DefaultBlip;
import com.baseoneonline.java.blips.entities.PlayerBlip;
import com.baseoneonline.java.blips.sequencer.Sequencer;
import com.baseoneonline.java.blips.sequencer.SequencerListener;
import com.baseoneonline.java.jlib.utils.JMEMath;
import com.baseoneonline.java.jme.BasicFixedRateGame;
import com.jme.input.MouseInput;
import com.jme.math.Plane;
import com.jme.math.Vector3f;

class TestBlips extends BasicFixedRateGame {

	String basePath = "com/baseoneonline/java/blips/data/";

	public static void main(final String[] args) {
		Logger.getLogger("com.jme").setLevel(Level.WARNING);
		final TestBlips app = new TestBlips();
		app.start();

	}

	private PlayerBlip player;

	private Plane clickPlane;

	private FollowCam camNode;

	private final Sequencer sequencer = new Sequencer();

	private MousePullController playerController;
	StarField starField;

	private final BlipSystem blipSystem = new BlipSystem();

	@Override
	protected void initFixedRateGame() {

		MouseInput.get().setCursorVisible(true);


		starField = new StarField();
		rootNode.attachChild(starField);

		clickPlane = new Plane(Vector3f.UNIT_Z, 1);
		player = new PlayerBlip();

		playerController = new MousePullController(player, clickPlane);

		blipSystem.addBlip(player, null);

		Blip b1 = player;

		for (int i=0; i<20; i++) {
			final Blip b2 = getBlip();
			blipSystem.addBlip(b2, b1);
			b1 = b2;
		}

		rootNode.attachChild(blipSystem);

		sequencer.addListener(new SequencerListener() {
			public void onBeat() {
				player.queuePulse();
			}
			public void onTick() {
				blipSystem.tick();
			}
		});
		sequencer.start();


		camNode = new FollowCam(cam, player);

	}

	int blipCounter = 0;

	private Blip getBlip() {
		final DefaultBlip blip = new DefaultBlip();
		blip.setName("Blip " + blipCounter++);
		final Vector3f loc = JMEMath.nextRandomVector3f(-4, 4);
		loc.z = 0;
		blip.setLocalTranslation(loc);
		return blip;
	}

	@Override
	protected void update() {

		playerController.update(1);

		camNode.update();

		blipSystem.update();

		starField.getBounds().setCenter(player.getWorldTranslation());
		starField.update();



	}

}
