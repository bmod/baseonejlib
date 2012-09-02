package com.baseoneonline.jlib.ardor3d.math;

import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.baseoneonline.java.math.Curve;

public abstract class Curve3 extends Curve<ReadOnlyVector3, Vector3>
{

	public Curve3(ReadOnlyVector3[] cvs)
	{
		super(cvs);
	}

}
