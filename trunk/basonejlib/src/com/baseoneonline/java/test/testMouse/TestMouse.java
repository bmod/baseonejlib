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

	private final MouseListener entListener = new MouseListener() {

		private DragController ctrl;
		private Vector2f grabPos;
		private boolean isDragging = false;

		@Override
		public void onRollOver(final MouseEvent ev) {
			if (!isDragging) {
				ev.getEntity().setState(EntityState.Hover);
				cursor.setState(CursorState.Open);
			}
		}

		@Override
		public void onRollOut(final MouseEvent ev) {
			if (!isDragging) {
				ev.getEntity().setState(EntityState.Idle);
				cursor.setState(CursorState.Default);
			}
		}

		@Override
		public void onPress(final MouseEvent ev) {
			if (null != ev.getEntity()) {
				cursor.setState(CursorState.Grab);
				ctrl = new DragController(ev.getEntity(), boardCursor
						.mouseOnPlanePosition());
				grabPos = boardCursor.getGridPosition();
				ev.getEntity().addController(ctrl);
				isDragging = true;
			}
		}

		@Override
		public void onRelease(final MouseEvent ev) {
			if (null != ev.getEntity()) {
				cursor.setState(CursorState.Open);
			} else {
				cursor.setState(CursorState.Default);
			}
			if (isDragging) {
				final Vector2f gridPos = boardCursor.getGridPosition();
				System.out.println(gridPos);
				final Entity ent = tileGridNode.getEntity(gridPos);
				if (ent != null) {
					ctrl.drop(tileGridNode.realPosition(gridPos));
					tileGridNode.moveEntity(grabPos, gridPos);
				} else {
					System.out.println("OCCUPIED");
					ctrl.drop(tileGridNode.realPosition(grabPos));
				}
				isDragging = false;
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