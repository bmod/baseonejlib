package game;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import com.baseoneonline.java.jme.JMEUtil;
import com.baseoneonline.java.nanoxml.XMLElement;
import com.baseoneonline.java.tools.FileUtils;
import com.jme.scene.Spatial;

/**
 * @author Administrator
 * 
 */
public class ResourceManager {

	private static ResourceManager instance;

	private static final String FILE_DESCRIPTION = "description.lvl";
	private static final String FILE_LEVEL_HANDLES = "handles.txt";
	private static final String PATH_ASSET_ROOT = "assets/";
	private static final String PATH_LEVELS = PATH_ASSET_ROOT + "levels/";

	private final Logger log = Logger.getLogger(getClass().getName());

	private String[] levelHandles;
	private final HashMap<String, LevelResource> levelResources = new HashMap<String, LevelResource>();

	private ResourceManager() {

	}

	public String[] getLevelHandles() {
		if (null == levelHandles) {
			levelHandles = FileUtils.readFile(
					resource(PATH_LEVELS + FILE_LEVEL_HANDLES)).split("\n");

		}
		return levelHandles;
	}

	public LevelResource getLevelResource(final String handle) {
		LevelResource src = levelResources.get(handle);
		if (null == src) {
			src = loadLevelResource(handle);
			levelResources.put(handle, src);
		}
		return src;
	}

	private LevelResource loadLevelResource(final String handle) {
		final String path = PATH_LEVELS + handle + "/" + FILE_DESCRIPTION;
		// LOAD
		final XMLElement xml = new XMLElement();
		try {
			xml.parseFromReader(new InputStreamReader(resource(path)
					.openStream()));
		} catch (final Exception e) {
			throw new RuntimeException("FAIL loading: " + path);
		}

		// PARSE
		final LevelResource lvl = new LevelResource();
		lvl.name = xml.getStringAttribute("NAME");
		lvl.handle = handle;
		for (final XMLElement xGeom : xml.getChildren("MODEL")) {
			lvl.geometryFiles.add(xGeom.getStringAttribute("FILE"));
		}

		return lvl;
	}

	public Level getLevel(final String handle) {
		return loadLevel(getLevelResource(handle));
	}

	private Level loadLevel(final LevelResource src) {
		log.info("Loading level: " + src.handle);

		final Level lvl = new Level();

		// Load geometry
		for (final String gFile : src.geometryFiles) {
			assert (null != gFile);
			final String path = PATH_LEVELS + src.handle + "/" + gFile;
			final Spatial model = JMEUtil.loadObj(resource(path));
			lvl.node.attachChild(model);
		}
		lvl.surface = lvl.node;
		lvl.board = new Board(lvl.surface, new NavGraph());

		return lvl;

	}

	public static ResourceManager get() {
		if (null == instance) instance = new ResourceManager();
		return instance;
	}

	private URL resource(final String path) {
		final URL url = getClass().getClassLoader().getResource(path);
		assert (null != url) : "Path not found: " + path;
		return url;
	}

	public URL getAsset(final String string) {
		return resource(PATH_ASSET_ROOT + string);
	}

	public ArrayList<LevelResource> getLevelResources() {
		return null;
	}

}
