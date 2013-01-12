package com.baseoneonline.jlib.ardor3d.framework.entities;

import java.io.IOException;

import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.util.export.InputCapsule;
import com.ardor3d.util.export.OutputCapsule;
import com.baseoneonline.jlib.ardor3d.framework.ResourceManager;

public class ModelComponent extends Component {

	private String resource;
	private Spatial model;

	public ModelComponent() {

	}

	public ModelComponent(String resource) {
		this.resource = resource;
	}

	@Override
	public void update(double t) {
	}

	@Override
	public void onAdded() {
		if (model == null)
			model = ResourceManager.get().getModel(resource);
		getOwner().getNode().attachChild(model);
	}

	@Override
	public void onRemoved() {
		getOwner().getNode().detachChild(model);
	}

	@Override
	public void write(OutputCapsule capsule) throws IOException {
		capsule.write(resource, "resource", null);
	}

	@Override
	public void read(InputCapsule capsule) throws IOException {
		resource = capsule.readString("resource", null);

	}

}
