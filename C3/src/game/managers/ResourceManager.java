package game.managers;

import game.Level;
import game.resources.LevelResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Logger;

import com.baseoneonline.java.nanoxml.XMLElement;
import com.jme.scene.Spatial;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.ObjToJme;

public class ResourceManager {

	private static ResourceManager instance;

	private static final String FILE_DESCRIPTION = "description.xml";
	private static final String PATH_ASSET_ROOT = "assets/";
	private static final String PATH_LEVELS = "levels/";

	private final Logger log = Logger.getLogger(getClass().getName());

	private final HashMap<String, LevelResource> levelResources =
		new HashMap<String, LevelResource>();

	private ResourceManager() {

	}

	public LevelResource getLevelResource(final String id) {
		LevelResource src = levelResources.get(id);
		if (null == src) {
			src = loadLevelResource(levelPath(id) + FILE_DESCRIPTION);
		}

		return src;
	}

	private LevelResource loadLevelResource(final String path) {
		// LOAD
		final XMLElement xml = new XMLElement();
		try {
			xml.parseFromReader(new InputStreamReader(getInStream(path)));
		} catch (Exception e) {
			throw new NullPointerException("FAIL loading: " + path);
		}

		// PARSE
		final LevelResource lvl = new LevelResource();
		lvl.name = xml.getStringAttribute("NAME");
		for (XMLElement xGeom : xml.getChildren("MODEL")) {
			lvl.geometryFiles.add(xGeom.getContent());
		}

		return lvl;
	}

	public Level loadLevel(final String id) {
		log.info("Loading level: " + id);
		LevelResource src = getLevelResource(id);

		Level lvl = new Level();
		for (String gFile : src.geometryFiles) {
			lvl.node.attachChild(loadGeometry(levelPath(id) + gFile));
		}

		return lvl;

	}

	/**
	 * @param path
	 * @return
	 */
	private Spatial loadGeometry(String path) {
		log.info("Loading geometry: " + path);
		final File temp = new File("temp");
		final ObjToJme objToJME = new ObjToJme();
		try {
			log.finer("Converting " + path + " to JME Binary.");
			objToJME.convert(getInStream(path), new FileOutputStream(temp));
			log.finer("Loading JME Binary: " + temp.getAbsolutePath());
			temp.delete();
			final BinaryImporter importer = new BinaryImporter();
			return (Spatial) importer.load(temp);
		} catch (final Exception e) {
			throw new NullPointerException("FAIL! Cannot load: '" + path + "'.");
		}
	}

	public InputStream getInStream(final String src) {
		return getClass().getClassLoader().getResourceAsStream(src);
	}

	private String levelPath(String id) {
		return PATH_ASSET_ROOT + PATH_LEVELS + id + "/";
	}

	public static ResourceManager get() {
		if (null == instance)
			instance = new ResourceManager();
		return instance;
	}
}
