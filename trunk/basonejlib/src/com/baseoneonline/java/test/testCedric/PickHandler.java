package com.baseoneonline.java.test.testCedric;

import java.util.ArrayList;
import java.util.List;

import com.jme.input.MouseInput;
import com.jme.intersection.PickData;
import com.jme.intersection.PickResults;
import com.jme.intersection.TrianglePickResults;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Node;

public class PickHandler {

	private final Node node;
	private final Camera cam;
	private Entity hoverEntity;
	private float pickDist;
	private final boolean[] buttonsDown;

	private final List<MouseListener> listeners = new ArrayList<MouseListener>();

	public PickHandler(final Node node, final Camera cam) {
		this.node = node;
		this.cam = cam;
		buttonsDown = new boolean[MouseInput.get().getButtonCount()];
	}

	public void addListener(final MouseListener l) {
		if (!listeners.contains(l))
			listeners.add(l);
	}

	public void removeListener(final MouseListener l) {
		listeners.remove(l);
	}

	public void update(final float t, final Vector2f mousePos) {
		final MouseInput mouseIn = MouseInput.get();
		final Vector3f p1 = cam.getWorldCoordinates(mousePos, 0);
		final Vector3f p2 = cam.getWorldCoordinates(mousePos, 1);

		final Ray ray = new Ray(p1, p2.subtractLocal(p1).normalizeLocal());
		final PickResults pr = new TrianglePickResults();
		node.findPick(ray, pr);

		Entity ent = null; // Candidate for picked entity
		pickDist = Float.MAX_VALUE; // Shortest pick distance
		float d; // Current pick distance
		final int len = pr.getNumber();
		for (int i = 0; i < len; i++) {
			final PickData pd = pr.getPickData(i);
			final Node parent = pd.getTargetMesh().getParent();
			if (parent instanceof Entity) {
				d = pd.getDistance();
				if (d < pickDist) {
					pickDist = d;
					ent = (Entity) parent;
				}
			}
		}

		/* Handle mouse buttons */

		for (int i = 0; i < buttonsDown.length; i++) {
			if (mouseIn.isButtonDown(i)) {
				if (!buttonsDown[i]) {
					buttonsDown[i] = true;
					firePress(ent, i);
				}
			} else {
				if (buttonsDown[i]) {
					buttonsDown[i] = false;
					fireRelease(ent, i);
				}
			}
		}

		/* Handle hovers */
		if (null != ent) {
			if (ent != hoverEntity) {
				hoverEntity = ent;
				fireRollOver(hoverEntity);
			}
		} else {
			if (null != hoverEntity) {
				fireRollOut(hoverEntity);
				hoverEntity = null;
			}
		}

	}

	private void fireRelease(final Entity e, final int button) {
		final MouseEvent ev = new MouseEvent(e, button);
		for (final MouseListener l : listeners) {
			l.onRelease(ev);
		}
	}

	private void fireRollOver(final Entity e) {
		final MouseEvent ev = new MouseEvent(e);
		for (final MouseListener l : listeners) {
			l.onRollOver(ev);
		}
	}

	private void fireRollOut(final Entity e) {
		final MouseEvent ev = new MouseEvent(e);
		for (final MouseListener l : listeners) {
			l.onRollOut(ev);
		}
	}

	private void firePress(final Entity e, final int button) {
		final MouseEvent ev = new MouseEvent(e, button);
		for (final MouseListener l : listeners) {
			l.onPress(ev);
		}
	}

}
