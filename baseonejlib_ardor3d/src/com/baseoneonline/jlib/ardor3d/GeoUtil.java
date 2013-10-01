package com.baseoneonline.jlib.ardor3d;

import com.ardor3d.math.Vector3;
import com.baseoneonline.java.houGeo.Geo;
import com.baseoneonline.java.houGeo.Nurbs;
import com.baseoneonline.java.houGeo.Point;
import com.baseoneonline.jlib.ardor3d.math.Nurbs3;

public class GeoUtil {

	private GeoUtil() {
	}

	public static Nurbs3[] createNurbs(Geo geo) {
		final Nurbs3[] curves = new Nurbs3[geo.nurbCurves.length];
		for (int i = 0; i < curves.length; i++) {
			final Nurbs nc = geo.nurbCurves[i];

			final Vector3[] vtc = new Vector3[nc.vertices.length];
			for (int j = 0; j < nc.vertices.length; j++) {
				final int pointIndex = nc.vertices[j];
				vtc[j] = toVector3(geo.points[pointIndex]);
			}
			Nurbs3 n = new Nurbs3(vtc);
			n.setClamped(nc.clamped);
			curves[i] = n;

		}
		return curves;
	}

	public static Vector3 toVector3(Point p) {
		return new Vector3(p.x, p.y, p.z);
	}

}
