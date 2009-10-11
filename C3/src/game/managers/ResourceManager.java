package game.managers;

import game.Level;
import game.resources.LevelResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Logger;

import com.baseoneonline.java.nanoxml.XMLElement;
import com.jme.bounding.BoundingBox;
import com.jme.scene.Spatial;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.model.converters.ObjToJme;

/**
 * @author Administrator
 * 
 */
public class ResourceManager {

	private static ResourceManager instance;

	private static final String FILE_DESCRIPTION = "description.xml";
	private static final String PATH_ASSET_ROOT = "assets/";
	private static final String PATH_LEVELS = "levels/";

	private final Logger log = Logger.getLogger(getClass().getName());

	private final HashMap<String, LevelResource> levelResources = new HashMap<String, LevelResource>();

	private ResourceManager() {

	}

	public LevelResource getLevelResource(final String id) {
		LevelResource src = levelResources.get(id);
		if (null == src) {
			src = loadLevelResource(getLevelAsset(id, FILE_DESCRIPTION));
		}

		return src;
	}

	private LevelResource loadLevelResource(final URL url) {
		// LOAD
		final XMLElement xml = new XMLElement();
		try {
			xml.parseFromReader(new InputStreamReader(url.openStream()));
		} catch (final Exception e) {
			throw new NullPointerException("FAIL loading: " + url);
		}

		// PARSE
		final LevelResource lvl = new LevelResource();
		lvl.name = xml.getStringAttribute("NAME");
		for (final XMLElement xGeom : xml.getChildren("MODEL")) {
			lvl.geometryFiles.add(xGeom.getContent());
		}

		return lvl;
	}

	public Level loadLevel(final String id) {
		log.info("Loading level: " + id);
		final LevelResource src = getLevelResource(id);

		final Level lvl = new Level();
		for (final String gFile : src.geometryFiles) {
			lvl.node.attachChild(loadGeometry(getLevelAsset(id, gFile)));
		}

		return lvl;

	}

	/**
	 * @param path
	 * @return
	 */
	private Spatial loadGeometry(final URL url) {
		log.info("Loading geometry: " + url);

		try {
			return loadObj(url);
		} catch (final Exception e) {
			throw new NullPointerException("FAIL! Cannot load: " + url);
		}
	}

	private Spatial loadObj(final URL model) {

		final ObjToJme converter = new ObjToJme();

		final ByteArrayOutputStream BO = new ByteArrayOutputStream();
		try {
			final InputStream is = model.openStream();
			converter.setProperty("mtllib", model);
			final URI texDir = new File(model.toURI()).getParentFile().toURI();
			ResourceLocatorTool.addResourceLocator(
					ResourceLocatorTool.TYPE_TEXTURE,
					new SimpleResourceLocator(texDir));
			converter.convert(is, BO);

		} catch (final IOException e) {
			throw new NullPointerException(e.getMessage());
		} catch (final URISyntaxException e) {
			java.util.logging.Logger.getLogger(ResourceManager.class.getName())
					.warning(e.getMessage());
		}

		Spatial s = null;
		try {
			s = (Spatial) BinaryImporter.getInstance().load(
					new ByteArrayInputStream(BO.toByteArray()));
		} catch (final IOException e) {
			log.warning(e.getMessage());
		}
		s.setModelBound(new BoundingBox());
		s.updateModelBound();
		return s;

	}

	public URL getAsset(final String src) {
		final URL url = getClass().getClassLoader().getResource(
				PATH_ASSET_ROOT + src);
		if (null == url) { throw new NullPointerException(
				"Failed to locate asset: " + src); }
		return url;
	}

	public URL getLevelAsset(final String id, final String asset) {
		return getAsset(PATH_LEVELS + id + "/" + asset);
	}

	public static ResourceManager get() {
		if (null == instance) instance = new ResourceManager();
		return instance;
	}

	/**
	 * @param path
	 *            Path to obj relative to assets/
	 * @return
	 */
	public Spatial loadObj(final String path) {
		return loadObj(getAsset(path));
	}
}
