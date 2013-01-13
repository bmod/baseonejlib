package com.baseoneonline.jlib.ardor3d.framework.entities;

import java.io.IOException;

import com.ardor3d.util.export.InputCapsule;
import com.ardor3d.util.export.OutputCapsule;

public class HealthComponent extends Component {

	private double health = 100;

	public double getHealth() {
		return health;
	}

	public void setHealth(double h) {
		health = h;
	}

	public void addHealth(double h) {
		health += h;
	}

	@Override
	public void read(InputCapsule capsule) throws IOException {
		health = capsule.readDouble("health", 100);
	}

	@Override
	public void write(OutputCapsule capsule) throws IOException {
		capsule.write(health, "health", 100);
	}
}
