package com.baseoneonline.jlib.ardor3d.framework;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.filechooser.FileFilter;

import com.ardor3d.extension.model.collada.jdom.ColladaAnimUtils;
import com.ardor3d.extension.model.collada.jdom.ColladaImporter;
import com.ardor3d.extension.model.collada.jdom.data.ColladaStorage;
import com.ardor3d.extension.model.collada.jdom.data.MaterialInfo;
import com.ardor3d.extension.model.obj.ObjImporter;
import com.ardor3d.image.Texture;
import com.ardor3d.image.Texture.MinificationFilter;
import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Matrix3;
import com.ardor3d.math.Vector3;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.util.export.Savable;
import com.ardor3d.util.resource.ResourceLocatorTool;
import com.ardor3d.util.resource.SimpleResourceLocator;
import com.baseoneonline.java.tools.FileExensionFilter;
import com.baseoneonline.jlib.ardor3d.framework.entities.Entity;

public class ResourceManager {

	private static ResourceManager instance;

	private final HashMap<String, Entity> entities = new HashMap<String, Entity>();
	private final HashMap<String, Node> models = new HashMap<String, Node>();

	private static final HashMap<String, ModelLoader> loaders = new HashMap<String, ModelLoader>();
	private final HashMap<Class<?>, ObjectLoader<?>> objectLoaders = new HashMap<Class<?>, ObjectLoader<?>>();
	private final XMLImporter importer = new XMLImporter();

	public static FileFilter MODEL_FILE_FILTER;
	static {
		loaders.put("dae", new ColladaLoader());
		loaders.put("obj", new ObjLoader());
		loaders.put("blend", new BlenderLoader());

		MODEL_FILE_FILTER = new FileExensionFilter(loaders.keySet().toArray(
				new String[loaders.keySet().size()]));

	}

	public static ResourceManager get() {
		if (null == instance)
			instance = new ResourceManager();
		return instance;
	}

	private Class<?> referenceClass;

	private ResourceManager() {
	}

	public XMLImporter getTagTransformer() {
		return importer;
	}

	public void setReferenceClass(final Class<?> clazz) {
		referenceClass = clazz;
	}

	public void addTextureLocator(final String path) {
		SimpleResourceLocator srl;
		try {
			srl = new SimpleResourceLocator(
					ResourceLocatorTool.getClassPathResource(referenceClass,
							path));

		} catch (final URISyntaxException e) {
			throw new RuntimeException(e);
		}
		ResourceLocatorTool.addResourceLocator(
				ResourceLocatorTool.TYPE_TEXTURE, srl);
	}

	public Entity getEntity(final String resource) {
		// TODO: Caching
		// if (!entities.containsKey(resource))
		// entities.put(resource, loadEntity(resource));
		// return new Entity(entities.get(resource));
		return loadEntity(resource);
	}

	private Entity loadEntity(final String resource) {
		return (Entity) loadSavableXML(resource);
	}

	private Savable loadSavableXML(final String resource) {
		final URL url = referenceClass.getClassLoader().getResource(resource);

		InputStream is = null;
		try {
			is = ResourceManager.get().getResourceURL(resource).openStream();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}

		if (url == null)
			throw new RuntimeException("Resource not found: " + resource);

		try {
			return importer.load(is);
		} catch (final IOException e1) {
			throw new RuntimeException(e1);
		}
	}

	public Node getModel(final String resource) {
		Node model;
		if (models.containsKey(resource)) {
			model = models.get(resource);
		} else {
			model = loadModel(resource);
			models.put(resource, model);
		}
		return model.makeCopy(false);
	}

	private Node loadModel(final String resource) {
		for (final String ext : loaders.keySet()) {
			if (resource.toLowerCase().endsWith("." + ext)) {
				final Node model = loaders.get(ext).load(resource);
				if (model == null)
					throw new RuntimeException("Model returned null: "
							+ resource);
				return model;
			}
		}
		throw new RuntimeException("No support for model resource: " + resource);
	}

	public Spatial getModel(final File f) {
		try {
			return getModel(toResourcePath(f.toURI().toURL()));
		} catch (final MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	private String toResourcePath(final URL url) {
		final String relPath = referenceClass.getClassLoader().getResource("")
				.toString();
		final String absPath = url.toString();
		return absPath.replace(relPath, "");
	}

	private URL getResourceURL(final String resource) {
		final URL url = referenceClass.getClassLoader().getResource(resource);
		if (url == null)
			throw new RuntimeException("Resource could not be resolved: "
					+ resource);
		return url;
	}

	@SuppressWarnings({ "unchecked" })
	private <T> ObjectLoader<T> getObjectLoader(Class<T> type) {
		if (objectLoaders.containsKey(type))
			return (ObjectLoader<T>) objectLoaders.get(type);
		throw new RuntimeException("No objectloader for type " + type.getName());
	}

	public <T> T getObject(Class<T> type, String resource) {
		return getObjectLoader(type).load(resource);
	}

	public void addObjectLoader(ObjectLoader<?> loader) {
		objectLoaders.put(loader.getContentType(), loader);
	}

}

interface ModelLoader {
	Node load(String resource);
}

class ColladaLoader implements ModelLoader {

	private final ColladaImporter importer = new ColladaImporter();

	@Override
	public Node load(final String resource) {
		ColladaStorage store = null;
		Logger.getLogger(ColladaAnimUtils.class.getName()).setLevel(
				Level.SEVERE);
		try {
			store = importer.load(resource);

			if (store.getAssetData().getUpAxis() == Vector3.UNIT_Z) {
				store.getScene().setRotation(
						new Matrix3().fromAngles(-MathUtils.HALF_PI, 0, 0));
			}

			for (final MaterialInfo inf : store.getMaterialMap().values())
				for (final Texture t : inf.getTextures().values())
					t.setMinificationFilter(MinificationFilter.Trilinear);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return store.getScene();
	}
}

class BlenderLoader implements ModelLoader {

	private final String blenderExe = "C:/Program Files/Blender Foundation/Blender/blender.exe";
	private final String scriptFile = new File("test.py").getAbsolutePath();

	@Override
	public Node load(String resource) {
		String cmd = String.format("\"%s\" -P \"%s\"", blenderExe, scriptFile);
		System.out.println(cmd);
		ProcessBuilder builder = new ProcessBuilder(cmd);

		Process proc = null;
		try {
			proc = builder.start();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}
}

class ObjLoader implements ModelLoader {

	private final ObjImporter importer = new ObjImporter();

	public ObjLoader() {
		importer.setMinificationFilter(MinificationFilter.Trilinear);
	}

	@Override
	public Node load(final String resource) {
		return importer.load(resource).getScene();
	}
}
