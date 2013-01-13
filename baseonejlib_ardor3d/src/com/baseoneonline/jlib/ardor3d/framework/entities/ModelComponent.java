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

	public void setResource(String resource) {
		this.resource = resource;
	}

	@Override
	public void resume() {
		if (model == null)
			model = ResourceManager.get().getModel(resource);
		getEntity().getNode().attachChild(model);
	}

	@Override
	public void suspend() {
		getEntity().getNode().detachChild(model);
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
