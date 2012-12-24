package com.baseoneonline.jlib.ardor3d;

import java.io.IOException;

import com.ardor3d.extension.model.collada.jdom.ColladaImporter;
import com.ardor3d.extension.model.collada.jdom.data.ColladaStorage;
import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Vector3;

public class AssetManager {

	private final Class<?> refClass;
	private ColladaImporter colladaImporter;

	private static final Matrix3 Z_UP_CORRECTION = new Matrix3().fromAngles(
			-MathUtils.PI / 2, 0, 0);

	/**
	 * @param classLoader
	 *            Will be used for path reference.
	 */
	public AssetManager(Class<?> refClass) {
		this.refClass = refClass;
	}

	@SuppressWarnings("unchecked")
	public <T> T load(String resource, Class<T> returnType) {

		if (resource.toLowerCase().endsWith(".dae"))
			return (T) loadCollada(resource);

		throw new UnsupportedOperationException(String.format(
				"No loader for resource '%s' of type '%s'", resource,
				returnType.getName()));
	}

	private ColladaStorage loadCollada(String resource) {
		if (colladaImporter == null) {
			colladaImporter = new ColladaImporter();

		}
		try {
			ColladaStorage stor = colladaImporter.load(resource);

			// Correct for Z-up axis
			if (stor.getAssetData().getUpAxis().equals(Vector3.UNIT_Z))
				stor.getScene().setRotation(Z_UP_CORRECTION);
			return stor;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
