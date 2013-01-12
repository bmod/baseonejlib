package com.baseoneonline.jlib.ardor3d.framework.entities;

import java.io.IOException;

import com.ardor3d.math.type.ReadOnlyTransform;
import com.ardor3d.util.export.InputCapsule;
import com.ardor3d.util.export.OutputCapsule;

public class PhysicsComponent extends Component {

	@Override
	public void update(double t) {
	}

	@Override
	public void onAdded() {

	}

	@Override
	public void onRemoved() {
	}

	public void setTransform(ReadOnlyTransform xf) {
	}

	@Override
	public void write(OutputCapsule capsule) throws IOException {
	}

	@Override
	public void read(InputCapsule capsule) throws IOException {
	}

	@Override
	public Class<?> getClassTag() {
		return null;
	}

}
