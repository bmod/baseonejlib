package test.util;

import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyTransform;
import com.ardor3d.renderer.Camera;
import com.ardor3d.scenegraph.Node;

public class CameraOrbitNode extends Node
{

	private double distance = 5;
	private final Camera cam;

	private final Vector3 camOffset = new Vector3();

	public CameraOrbitNode(final Camera cam)
	{
		this.cam = cam;
	}

	private void updateCamera()
	{
		camOffset.set(0, 0, -distance);
		final ReadOnlyTransform worldXF = getWorldTransform();
		worldXF.applyForward(camOffset);
		cam.setFrame(camOffset, worldXF.getMatrix());

	}

	public void setDistance(final double distance)
	{
		this.distance = distance;
	}

	public double getDistance()
	{
		return distance;
	}

	@Override
	public void updateGeometricState(final double time, final boolean initiator)
	{
		super.updateGeometricState(time, initiator);
		updateCamera();
	}

}