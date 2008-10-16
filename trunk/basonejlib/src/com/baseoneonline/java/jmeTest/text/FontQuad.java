package com.baseoneonline.java.jmeTest.text;

import java.nio.FloatBuffer;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.TexCoords;
import com.jme.scene.TriMesh;
import com.jme.util.geom.BufferUtils;

public class FontQuad extends TriMesh {
	private float width;
	private float height;

	public FontQuad(String name, float width, float height) {
		super("FQuad");
		this.width = width;
		this.height = height;
		initialize();

	}

	public void setColor(ColorRGBA col) {

	}

	public void resize(float width, float height) {
		this.width = width;
		this.height = height;
		updateGeometry();
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	private void updateGeometry() {
		getVertexBuffer().clear();
		getVertexBuffer().put(0).put(0).put(0);
		getVertexBuffer().put(0).put(-height).put(0);
		getVertexBuffer().put(width).put(-height).put(0);
		getVertexBuffer().put(width).put(0).put(0);

	}

	/**
	 * <code>initialize</code> builds the data for the <code>Quad</code>
	 * object.
	 * 
	 * @param width
	 *            the width of the <code>Quad</code>.
	 * @param height
	 *            the height of the <code>Quad</code>.
	 */
	public void initialize() {

		setVertexCount(4);
		setVertexBuffer(BufferUtils.createVector3Buffer(getVertexCount()));
		setNormalBuffer(BufferUtils.createVector3Buffer(getVertexCount()));
		FloatBuffer tbuf = BufferUtils.createVector2Buffer(getVertexCount());
		setTextureCoords(new TexCoords(tbuf));
		setTriangleQuantity(2);
		setIndexBuffer(BufferUtils.createIntBuffer(getTriangleCount() * 3));

		updateGeometry();

		getNormalBuffer().put(0).put(0).put(1);
		getNormalBuffer().put(0).put(0).put(1);
		getNormalBuffer().put(0).put(0).put(1);
		getNormalBuffer().put(0).put(0).put(1);

		tbuf.put(0).put(1);
		tbuf.put(0).put(0);
		tbuf.put(1).put(0);
		tbuf.put(1).put(1);

		getIndexBuffer().put(0);
		getIndexBuffer().put(1);
		getIndexBuffer().put(2);
		getIndexBuffer().put(0);
		getIndexBuffer().put(2);
		getIndexBuffer().put(3);

	}

}
