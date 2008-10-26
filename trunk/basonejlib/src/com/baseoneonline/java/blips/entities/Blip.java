package com.baseoneonline.java.blips.entities;

import java.util.ArrayList;
import java.util.List;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Node;

public abstract class Blip extends Node {

	private Blip parent;

	private int pulseDelay = 1;
	private int pulseQueued = -1;

	protected final List<Blip> children = new ArrayList<Blip>();

	public Blip() {

	}

	public void addChild(final Blip b) {
		children.add(b);
		b.setParent(this);
	}

	@Override
	public Blip getParent() {
		return parent;
	}

	public void setParent(final Blip parent) {
		this.parent = parent;
	}

	public boolean hasParent() {
		return null != parent;
	}

	public float getRadius() {
		return 1;
	}

	public void updateBlip() {
		for (final Blip b : children) {
			solveConstraint(b, this);
			b.updateBlip();
		}
		update();
	}

	private static void solveConstraint(final Blip child, final Blip parent) {
		final float len = child.getRadius() + parent.getRadius();
		final Vector3f ppos = parent.getLocalTranslation();
		final Vector3f pos = child.getLocalTranslation();
		final float dx = pos.x - ppos.x;
		final float dy = pos.y - ppos.y;
		final float a = FastMath.atan2(dy, dx);
		final float x = FastMath.cos(a) * len;
		final float y = FastMath.sin(a) * len;
		child.setLocalTranslation(ppos.x + x, ppos.y + y, 0);
	}

	public abstract void update();

	public void tick() {
		if (pulseQueued > 0) {
			pulseQueued--;
		} else if (pulseQueued == 0) {
			pulseQueued--;

			for (final Blip b : children) {
				b.queuePulse();
			}
			pulse();
		}
	}

	public void queuePulse() {
		pulseQueued = pulseDelay;
	}

	public abstract void pulse();

	@Override
	public String toString() {
		return getName();
	}

	public void setPulseDelay(final int pulseDelay) {
		this.pulseDelay = pulseDelay;
	}

	public int getPulseDelay() {
		return pulseDelay;
	}



}
