package com.baseoneonline.jlib.ardor3d;

import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;

public class Spring3 {

	private final Vector3 currentValue = new Vector3();
	private final Vector3 restValue = new Vector3();
	private final Vector3 velocity = new Vector3();
	private final Vector3 force = new Vector3();
	private double damp = .95;
	private double K = .01;

	public Spring3(final ReadOnlyVector3 translation) {
		setRestValue(translation);
		setCurrentValue(translation);
	}

	public void setVelocity(final ReadOnlyVector3 velocity) {
		this.velocity.set(velocity);
	}

	public Vector3 getVelocity() {
		return velocity;
	}

	public void setRestValue(final ReadOnlyVector3 value) {
		restValue.set(value);
	}

	public Vector3 getRestValue() {
		return restValue;
	}

	public void addValue(final ReadOnlyVector3 v) {
		currentValue.addLocal(v);
	}

	public void setCurrentValue(final ReadOnlyVector3 currentValue) {
		this.currentValue.set(currentValue);
	}

	public Vector3 getCurrentValue() {
		return currentValue;
	}

	public void setForce(final ReadOnlyVector3 force) {
		this.force.set(force);
	}

	public Vector3 getForce() {
		return force;
	}

	public double getDamp() {
		return damp;
	}

	public void setDamp(final double damp) {
		this.damp = damp;
	}

	public double getK() {
		return K;
	}

	public void setK(final double k) {
		K = k;
	}

	public void update() {
		restValue.subtract(currentValue, force);
		force.multiplyLocal(K);
		velocity.addLocal(force);
		velocity.multiplyLocal(damp);
		currentValue.addLocal(velocity);
	}

}
