package com.baseoneonline.jlib.ardor3d.framework.entities;

import java.io.IOException;

import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.util.export.InputCapsule;
import com.ardor3d.util.export.OutputCapsule;

public class SpatialComponent extends Component {

	private Spatial spatial;

	public SpatialComponent(final Spatial spatial) {
		this.spatial = spatial;
	}

	@Override
	public void resume() {
		getEntity().getNode().attachChild(spatial);
	}

	@Override
	public void suspend() {
		getEntity().getNode().detachChild(spatial);
	}

	@Override
	public void write(final OutputCapsule capsule) throws IOException {
		capsule.write(spatial, "spatial", null);
	}

	@Override
	public void read(final InputCapsule capsule) throws IOException {
		spatial = (Spatial) capsule.readSavable("spatial", null);
	}

}
