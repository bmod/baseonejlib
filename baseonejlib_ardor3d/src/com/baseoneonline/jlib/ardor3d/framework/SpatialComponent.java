package com.baseoneonline.jlib.ardor3d.framework;

import java.io.IOException;

import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.util.export.InputCapsule;
import com.ardor3d.util.export.OutputCapsule;

public class SpatialComponent extends Component {

	private Spatial spatial;

	public SpatialComponent(Spatial s) {
		spatial = s;
	}

	public Spatial getSpatial() {
		return spatial;
	}

	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	@Override
	public void update(double t) {
	}

	@Override
	public void write(OutputCapsule capsule) throws IOException {
		capsule.write(spatial, "spatial", null);
	}

	@Override
	public void read(InputCapsule capsule) throws IOException {
		spatial = (Spatial) capsule.readSavable("spatial", null);
	}

	@Override
	public Class<?> getClassTag() {
		return getClass();
	}

	@Override
	public void onAdded() {
		GameManager.get().getSceneManager().add(spatial);
	}

	@Override
	public void onRemoved() {
		GameManager.get().getSceneManager().remove(spatial);
	}

}
