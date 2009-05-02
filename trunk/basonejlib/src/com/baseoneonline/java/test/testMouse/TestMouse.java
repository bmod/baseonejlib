package com.baseoneonline.java.test.testMouse;

import com.baseoneonline.java.jme.BasicFixedRateGame;
import com.baseoneonline.java.jme.JMEUtil;
import com.baseoneonline.java.jme.OrbitCamNode;
import com.baseoneonline.java.test.testMouse.Cursor.CursorState;
import com.baseoneonline.java.test.testMouse.Entity.EntityState;
import com.jme.input.MouseInput;
import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.renderer.Renderer;

public class TestMouse extends BasicFixedRateGame {

	public static void main(final String[] args) {
		new TestMouse().start();
	}

	OrbitCamNode orbitCam;
	BoardCursor boardCursor;
	TileGridNode tileGridNode;

	Vector2f mouse = new Vector2f();
	DragHandler mouseInput;
	PickHandler pickHandler;
	Cursor cursor;

	@Override
	protected void initFixedRateGame() {
		// MouseInput.get().setCursorVisible(true);
		// input.setEnabled(false);

		final int w = 16;
		final int h = 10;
		final float tsize = .5f;

		tileGridNode = new TileGridNode(w, h, tsize);

		rootNode.attachChild(tileGridNode);

		boardCursor = new BoardCursor(tileGridNode);
		boardCursor.getLocalTranslation().y = .1f;
		// rootNode.attachChild(boardCursor);

		orbitCam = new OrbitCamNode(cam);
		orbitCam.setAzimuth(FastMath.HALF_PI * .6f);
		orbitCam.setDistance(6);
		rootNode.attachChild(orbitCam);

		JMEUtil.letThereBeLight(rootNode);

		tileGridNode.addEntity(3, 3, new Entity(.4f));
		tileGridNode.addEntity(5, 7, new Entity(.4f));

		mouseInput = new DragHandler(tileGridNode, boardCursor);
		pickHandler = new PickHandler(rootNode, cam);
		pickHandler.addListener(entListener);
		rootNode.updateRenderState();

		cursor = new Cursor();
		cursor.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		rootNode.attachChild(cursor);

	}

	private final EntityListener entListener = new EntityListener() {
		@Override
		public void onRollOver(final EntityEvent ev) {
			ev.getEntity().setState(EntityState.Hover);
			cursor.setState(CursorState.Open);
		}

		@Override
		public void onRollOut(final EntityEvent ev) {
			ev.getEntity().setState(EntityState.Idle);
			cursor.setState(CursorState.Default);
		}

		@Override
		public void onPress(final EntityEvent ev) {
			if (null != ev.getEntity()) {
				cursor.setState(CursorState.Grab);
			}
		}

		@Override
		public void onRelease(final EntityEvent ev) {
			if (null != ev.getEntity()) {
				cursor.setState(CursorState.Open);
			} else {
				cursor.setState(CursorState.Default);
			}
		}

	};

	@Override
	protected void updateLoop(final float t) {
		mouse.x = MouseInput.get().getXAbsolute();
		mouse.y = MouseInput.get().getYAbsolute();

		updateCamera();
		boardCursor.update(mouse);
		mouseInput.update(t);
		pickHandler.update(t, mouse);
	}

	private void updateCamera() {
		final float mx = (mouse.x / display.getWidth() * 2) - 1;
		final float my = (mouse.y / display.getHeight() * 2) - 1;

		orbitCam.getLocalTranslation().x = mx * 2;
		orbitCam.getLocalTranslation().z = -my;
		orbitCam.getLocalRotation().y = mx * .03f;
	}

}
