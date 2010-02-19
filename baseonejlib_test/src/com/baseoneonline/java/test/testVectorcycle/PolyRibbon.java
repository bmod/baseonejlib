package com.baseoneonline.java.test.testVectorcycle;

import com.jme.curve.BezierCurve;
import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.TexCoords;
import com.jme.scene.TriMesh;
import com.jme.util.geom.BufferUtils;


/**
 * @author "B. Korsmit"
 *	 
 */
public class PolyRibbon extends TriMesh {
	Node pnode = new Node();
	Node cnode = new Node();
	
	public PolyRibbon(BezierCurve bz, float width) {
		this(bz, width, 2,6);
	}
	
	public PolyRibbon(BezierCurve bz, float width, int segsWidth, int segsLength) {
		super("PolyRibbon");
		int len = (segsLength + 1) * 2;


		pnode.attachChild(cnode);
		
		float light = .5f;
		
		ColorRGBA col = new ColorRGBA(light+(FastMath.nextRandomFloat()*(1-light)),
			light+(FastMath.nextRandomFloat()*(1-light)),
			light+(FastMath.nextRandomFloat()*(1-light)),1);

		Vector3f[] vtc = new Vector3f[len];
		Vector3f[] nml = new Vector3f[len];
		ColorRGBA[] cols = new ColorRGBA[len];
		Vector2f[] tex = new Vector2f[len];
		int[] idx = new int[segsLength * 6];
		
		float rotPrecision = .001f;

		for (int i = 0; i <= segsLength; i++) {
			int id = i * 2;

			nml[id] = new Vector3f(0, 1, 0);
			nml[id + 1] = new Vector3f(0, 1, 0);

			cols[id] = col;
			cols[id + 1] = col;

			tex[id] = new Vector2f();
			tex[id + 1] = new Vector2f();

			float t = (float) i / (float) segsLength;

			Vector3f pt = bz.getPoint(t);

			Matrix3f mtx;
			if (t < 1) {
				mtx = bz.getOrientation(t, .001f, Vector3f.UNIT_Y);
			} else {
				mtx = bz.getOrientation(t-rotPrecision, .001f, Vector3f.UNIT_Y);
			}
	
			
			pnode.setLocalTranslation(pt);
			

			pnode.setLocalRotation(mtx);
			cnode.setLocalTranslation(0, 0, -width/2);
			pnode.updateGeometricState(0, true);


			vtc[id] = cnode.getWorldTranslation().clone();
			
			cnode.setLocalTranslation(0, 0, width/2);
			pnode.updateGeometricState(0, true);	
			
			vtc[id + 1] = cnode.getWorldTranslation().clone();

		}

		for (int i = 0; i < segsLength; i++) {
			int id = i * 6;
			idx[id++] = i * 2;
			idx[id++] = i * 2 + 1;
			idx[id++] = i * 2 + 3;

			idx[id++] = i * 2;
			idx[id++] = i * 2 + 3;
			idx[id++] = i * 2 + 2;
		}

		reconstruct(BufferUtils.createFloatBuffer(vtc), BufferUtils
				.createFloatBuffer(nml), BufferUtils.createFloatBuffer(cols),
			TexCoords.makeNew(tex), BufferUtils.createIntBuffer(idx));
		setLightCombineMode(LightCombineMode.Off);
	}
}
