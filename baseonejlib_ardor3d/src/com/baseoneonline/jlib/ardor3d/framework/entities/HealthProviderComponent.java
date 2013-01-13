package com.baseoneonline.jlib.ardor3d.framework.entities;

import java.io.IOException;

import com.ardor3d.util.export.InputCapsule;
import com.ardor3d.util.export.OutputCapsule;

public class HealthProviderComponent extends Component {

	private double charge = 30;

	public void setCharge(double charge) {
		this.charge = charge;
	}

	public double getCharge() {
		return charge;
	}

	@Override
	public void write(OutputCapsule capsule) throws IOException {
		capsule.write(charge, "charge", 30);
	}

	@Override
	public void read(InputCapsule capsule) throws IOException {
		charge = capsule.readDouble("charge", 30);
	}

	@Override
	protected void onCollide(Entity other) {
		if (other.hasComponent(HealthComponent.class))
			other.getComponent(HealthComponent.class).addHealth(charge);
		getEntity().destroy();

	}
}
