package com.baseoneonline.jlib.ardor3d;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import com.ardor3d.extension.model.collada.jdom.ColladaImporter;
import com.ardor3d.extension.model.collada.jdom.data.ColladaStorage;
import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Vector3;
import com.ardor3d.util.resource.ResourceLocatorTool;

public class AssetManager {

	private ColladaImporter colladaImporter;
	private final URI baseDir;
	private final HashMap<String, Object> assets = new HashMap<>();

	public AssetManager(ClassLoader classLoader) {
		try {
			this.baseDir = classLoader.getResource("").toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		// Logger.getLogger("com.ardor3d.extension.model.collada").setLevel(
		// Level.SEVERE);

	}

	@SuppressWarnings("unchecked")
	public <T> T load(String resource, Class<T> type) {

		if (assets.containsKey(resource))

			if (resource.toLowerCase().endsWith(".dae"))
				return (T) loadCollada(resource);

		throw new RuntimeException("No support for '" + resource + "'.");
	}

	private ColladaStorage loadCollada(String resource) {

		if (colladaImporter == null) {
			colladaImporter = new ColladaImporter();
		}
		try {
			ColladaStorage stor = colladaImporter.load(ResourceLocatorTool
					.locateResource(ResourceLocatorTool.TYPE_MODEL, resource));

			// Correct for Z-up axis
			if (stor.getAssetData().getUpAxis().equals(Vector3.UNIT_Z)) {
				stor.getScene().setRotation(
						new Matrix3().fromAngles(-MathUtils.HALF_PI, 0, 0));
			}

			return stor;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
