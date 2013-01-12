package com.baseoneonline.jlib.ardor3d.framework.entities;

import java.io.IOException;

import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Vector3;
import com.ardor3d.util.export.InputCapsule;
import com.ardor3d.util.export.OutputCapsule;
import com.baseoneonline.jlib.ardor3d.math.Simplex;

public class RandomRotationComponent extends Component {

	private final Matrix3 orient = new Matrix3();
	private double time = MathUtils.rand.nextDouble() * 10;
	private double speed = 1;
	private double amplitude = 1;

	private final Vector3 tmp = new Vector3();
	private final Vector3 angles = new Vector3();
	private final Simplex noise = new Simplex();

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getSpeed() {
		return speed;
	}

	@Override
	public void update(double t) {
		noise.noise(time * .01, tmp);
		tmp.multiplyLocal(.1);
		angles.addLocal(tmp);
		orient.fromAngles(angles.getX(), angles.getY(), angles.getZ());

		getOwner().getNode().setRotation(orient);
		time += t;
	}

	@Override
	public void onAdded() {
		angles.set(MathUtils.TWO_PI * MathUtils.rand.nextDouble(),
				MathUtils.TWO_PI * MathUtils.rand.nextDouble(),
				MathUtils.TWO_PI * MathUtils.rand.nextDouble());
	}

	@Override
	public void onRemoved() {}

	@Override
	public void write(OutputCapsule capsule) throws IOException {
		capsule.write(speed, "speed", 1);
		capsule.write(amplitude, "amplitude", 1);
	}

	@Override
	public void read(InputCapsule capsule) throws IOException {
		speed = capsule.readDouble("speed", 1);
		amplitude = capsule.readDouble("amplitude", 1);
	}
}
