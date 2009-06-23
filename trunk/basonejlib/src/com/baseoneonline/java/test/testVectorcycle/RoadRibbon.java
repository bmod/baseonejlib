package com.baseoneonline.java.test.testVectorcycle;

import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.TexCoords;
import com.jme.scene.TriMesh;
import com.jme.util.geom.BufferUtils;

public class RoadRibbon extends TriMesh {

	// Width segments
	private final int			segsU		= 3;

	// Length/depth segments
	private final int			segsV		= 16;

	// Total width of road
	private final float			width;

	private final RoadSegment	roadSeg;

	private final Vector2f		uvRepeat	= new Vector2f(1, 3);

	private ITerrain			terrain;

	private final float			terrainOffset;

	public RoadRibbon(RoadSegment seg, float width) {
		roadSeg = seg;
		this.width = width;
		this.terrainOffset = .1f;
		reconstruct();
	}

	public RoadRibbon(RoadSegment seg, float width, ITerrain terrain,
			float terrainOffset) {
		roadSeg = seg;
		this.width = width;
		this.terrain = terrain;
		this.terrainOffset = terrainOffset;
		reconstruct();
	}

	private void reconstruct() {
		float light = .5f;

		ColorRGBA col =
			new ColorRGBA(light + (FastMath.nextRandomFloat() * (1 - light)),
				light + (FastMath.nextRandomFloat() * (1 - light)), light
					+ (FastMath.nextRandomFloat() * (1 - light)), 1);
//		ColorRGBA col = new ColorRGBA();

		int vertsU = segsU + 1;
		int vertsV = segsV + 1;
		int numVerts = vertsU * vertsV;
		int numIndices = segsU * segsV * 6;

		float wstart = -width / 2;

		// Initialize data space
		Vector3f[] vtc = new Vector3f[numVerts];
		Vector3f[] nml = new Vector3f[numVerts];
		ColorRGBA[] cols = new ColorRGBA[numVerts];
		Vector2f[] tex = new Vector2f[numVerts];
		int[] idx = new int[numIndices];

		// Construct vertices
		int i = 0;
		for (int v = 0; v < vertsV; v++) {
			// Percent along the length
			float vt = (float) v / (float) segsV;

			for (int u = 0; u < vertsU; u++) {
				// Percent along width
				float ut = (float) u / (float) segsU;
				// Get point from curve space
				Vector2f p = roadSeg.curve.getPoint(vt, wstart + (ut * width));
				// Set vertex location
				if (null == terrain) {
					vtc[i] = new Vector3f(p.x, 0, p.y);
				} else {
					vtc[i] =
						new Vector3f(p.x, terrain.getHeight(p) + terrainOffset,
							p.y);
				}
				// Add uv coordinates
				tex[i] = new Vector2f(ut * uvRepeat.x, vt * uvRepeat.y);
				// Set normal vector
				nml[i] = Vector3f.UNIT_Y;
				// Put color
				cols[i] = col;
				i++;
			}
		}

		// Calculate indices
		i = 0;
		for (int v = 0; v < segsV; v++) {
			for (int u = 0; u < segsU; u++) {
				int id = v * vertsU + u;
				// First triangle of quad
				idx[i++] = id;
				idx[i++] = id + vertsU;
				idx[i++] = id + 1;
				// Second triangle of quad
				idx[i++] = id + vertsU;
				idx[i++] = id + vertsU + 1;
				idx[i++] = id + 1;
			}
		}

		// Finally put the data in the buffers
		reconstruct(BufferUtils.createFloatBuffer(vtc), BufferUtils
				.createFloatBuffer(nml), BufferUtils.createFloatBuffer(cols),
			TexCoords.makeNew(tex), BufferUtils.createIntBuffer(idx));
		setLightCombineMode(LightCombineMode.Off);
	}

}
