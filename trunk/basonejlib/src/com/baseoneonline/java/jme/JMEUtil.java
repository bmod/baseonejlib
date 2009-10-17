package com.baseoneonline.java.jme;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

import com.baseoneonline.java.tools.StringUtils;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.input.MouseInput;
import com.jme.light.PointLight;
import com.jme.math.Plane;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Line.Mode;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.ObjToJme;

public class JMEUtil {

	public static void letThereBeLight(final Node rootNode) {
		final PointLight light = new PointLight();
		light.setLocation(new Vector3f(200, 300, 100));
		light.setEnabled(true);
		light.setDiffuse(ColorRGBA.white);
		final LightState ls =
			DisplaySystem.getDisplaySystem().getRenderer().createLightState();
		ls.attach(light);
		rootNode.setRenderState(ls);
		rootNode.updateRenderState();

	}

	/**
	 * @return The position where the mouse pointer intersects with a plane
	 */
	public static Vector3f getMousePosition3D(final Plane p) {
		final Vector2f mousePos =
			new Vector2f(MouseInput.get().getXAbsolute(), MouseInput.get()
					.getYAbsolute());
		return getPosition3D(mousePos, p);
	}

	public static Vector3f getPosition3D(Vector2f pos2d, Plane p) {
		final Vector3f point1 =
			DisplaySystem.getDisplaySystem().getWorldCoordinates(pos2d, 0);
		final Vector3f point2 =
			DisplaySystem.getDisplaySystem().getWorldCoordinates(pos2d, 1);
		final Vector3f direction =
			point2.subtractLocal(point1).normalizeLocal();
		final Ray ray = new Ray(point1, direction);
		final Vector3f loc = new Vector3f();
		ray.intersectsWherePlane(p, loc);
		return loc;
	}

	public static void applyTexture(final Spatial spatial, final String fname) {
		final TextureState ts =
			DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setTexture(TextureManager.loadTexture(fname,
			MinificationFilter.Trilinear, MagnificationFilter.Bilinear));
		ts.setEnabled(true);
		spatial.setRenderState(ts);

	}

	public static void applyBlendState(final Spatial q) {
		final BlendState bs =
			DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		bs.setEnabled(true);
		bs.setSourceFunction(SourceFunction.SourceAlpha);
		bs.setDestinationFunction(DestinationFunction.OneMinusSourceAlpha);
		q.setRenderState(bs);
	}
	
	public static Spatial loadObj(final URL fileurl) {
		return loadObj(fileurl, null);
	}

public static Spatial loadObj(URL fileurl, URL pathurl) {
		Logger.getLogger(JMEUtil.class.getName()).info("Loading "+fileurl);
		final ObjToJme converter = new ObjToJme();

		final ByteArrayOutputStream BO = new ByteArrayOutputStream();
		try {
			final InputStream is = fileurl.openStream();
			if (null == pathurl) pathurl = StringUtils.parentOf(fileurl.toURI()).toURL();
			converter.setProperty("mtllib", pathurl);
			converter.convert(is, BO);

		} catch (final Exception e) {
			throw new RuntimeException(e.getMessage());
		}

		Spatial s = null;
		try {
			s =
				(Spatial) BinaryImporter.getInstance().load(
					new ByteArrayInputStream(BO.toByteArray()));
		} catch (final IOException e) {
			Logger.getLogger(JMEUtil.class.getName()).warning(e.getMessage());
		}
		s.setModelBound(new BoundingBox());
		s.updateModelBound();
		return s;

	}


	public static Line createLine(final Vector3f[] vtc, final ColorRGBA col) {
		final Vector3f[] nml = new Vector3f[vtc.length];
		final ColorRGBA[] cols = new ColorRGBA[vtc.length];
		final Vector2f[] tex = new Vector2f[vtc.length];
		final Vector2f tx = new Vector2f();
		for (int i = 0; i < vtc.length; i++) {
			nml[i] = Vector3f.UNIT_Y;
			cols[i] = col;
			tex[i] = tx;
		}
		final Line ln = new Line("Line", vtc, nml, cols, tex);
		ln.setMode(Mode.Connected);
		return ln;
	}

	public static URL getResource(final Object resourceClass,
			final String string) {
		return resourceClass.getClass().getClassLoader().getResource(string);
	}

}
