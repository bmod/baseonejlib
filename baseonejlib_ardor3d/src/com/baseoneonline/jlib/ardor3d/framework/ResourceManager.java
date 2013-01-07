package com.baseoneonline.jlib.ardor3d.framework;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import javax.swing.filechooser.FileFilter;

import com.ardor3d.extension.model.collada.jdom.ColladaImporter;
import com.ardor3d.extension.model.collada.jdom.data.ColladaStorage;
import com.ardor3d.extension.model.collada.jdom.data.MaterialInfo;
import com.ardor3d.extension.model.obj.ObjImporter;
import com.ardor3d.image.Texture;
import com.ardor3d.image.Texture.MinificationFilter;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.util.export.Ardor3dImporter;
import com.ardor3d.util.export.xml.XMLImporter;
import com.ardor3d.util.resource.ResourceLocatorTool;
import com.ardor3d.util.resource.SimpleResourceLocator;
import com.baseoneonline.java.tools.FileExensionFilter;

public class ResourceManager {

	private static ResourceManager instance;

	private final Ardor3dImporter importer = new XMLImporter();
	private final HashMap<String, Entity> entities = new HashMap<String, Entity>();
	private final HashMap<String, Node> models = new HashMap<String, Node>();

	private static final HashMap<String, ModelLoader> loaders = new HashMap<String, ModelLoader>();

	public static FileFilter MODEL_FILE_FILTER;
	static {
		loaders.put("dae", new ColladaLoader());
		loaders.put("obj", new ObjLoader());

		MODEL_FILE_FILTER = new FileExensionFilter(loaders.keySet().toArray(
				new String[loaders.keySet().size()]));
	}

	public static ResourceManager get() {
		if (null == instance)
			instance = new ResourceManager();
		return instance;
	}

	private Class<?> referenceClass;

	private ResourceManager() {}

	public void setReferenceClass(Class<?> clazz) {
		this.referenceClass = clazz;
	}

	public void addTextureLocator(String path) {// Add the classpathloader as
												// texture dir
		SimpleResourceLocator srl;
		try {
			srl = new SimpleResourceLocator(
					ResourceLocatorTool.getClassPathResource(referenceClass,
							path));

		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		ResourceLocatorTool.addResourceLocator(
				ResourceLocatorTool.TYPE_TEXTURE, srl);
	}

	public Entity getEntity(String resource) {
		// TODO: Caching
		// if (!entities.containsKey(resource))
		// entities.put(resource, loadEntity(resource));
		// return new Entity(entities.get(resource));
		return loadEntity(resource);
	}

	private Entity loadEntity(String resource) {
		URL url = referenceClass.getClassLoader().getResource(resource);
		if (url == null)
			throw new RuntimeException("Resource not found: " + resource);
		try {
			return (Entity) importer.load(url);
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
	}

	public Node getModel(String resource) {
		Node model;
		if (models.containsKey(resource)) {
			model = models.get(resource);
		} else {
			model = loadModel(resource);
			models.put(resource, model);
		}
		return model.makeCopy(false);
	}

	private Node loadModel(String resource) {
		for (String ext : loaders.keySet()) {
			if (resource.toLowerCase().endsWith("." + ext)) {
				Node model = loaders.get(ext).load(resource);
				if (model == null)
					throw new RuntimeException("Model returned null: "
							+ resource);
				return model;
			}
		}
		throw new RuntimeException("No support for model resource: " + resource);
	}

	public Spatial getModel(File f) {
		try {
			return getModel(toResourcePath(f.toURI().toURL()));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	private String toResourcePath(URL url) {
		String relPath = referenceClass.getClassLoader().getResource("")
				.toString();
		String absPath = url.toString();
		return absPath.replace(relPath, "");
	}

}

interface ModelLoader {
	Node load(String resource);
}

class ColladaLoader implements ModelLoader {

	private final ColladaImporter importer = new ColladaImporter();

	@Override
	public Node load(String resource) {
		ColladaStorage store = null;

		try {
			store = importer.load(resource);

			for (MaterialInfo inf : store.getMaterialMap().values())
				for (Texture t : inf.getTextures().values())
					t.setMinificationFilter(MinificationFilter.Trilinear);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return store.getScene();
	}
}

class ObjLoader implements ModelLoader {

	private final ObjImporter importer = new ObjImporter();

	public ObjLoader() {
		importer.setMinificationFilter(MinificationFilter.Trilinear);
	}

	@Override
	public Node load(String resource) {
		return importer.load(resource).getScene();
	}
}
