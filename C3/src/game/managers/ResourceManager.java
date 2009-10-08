package game.managers;

import game.resources.LevelResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

import com.baseoneonline.java.nanoxml.XMLElement;
import com.baseoneonline.java.nanoxml.XMLParseException;
import com.jme.scene.Spatial;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.ObjToJme;

public class ResourceManager {

	private static ResourceManager instance;

	private static final String FILE_DESCRIPTION = "description.xml";
	private static final String PATH_ASSET_ROOT = "assets/";
	private static final String PATH_LEVELS = "levels/";

	private final Logger log = Logger.getLogger(getClass().getName());

	private ResourceManager() {

	}

	public LevelResource getLevelDescriptor(final String id)
			throws XMLParseException, IOException {
		final XMLElement xml = new XMLElement();
		final String path = PATH_ASSET_ROOT + PATH_LEVELS + id + "/"
				+ FILE_DESCRIPTION;
		xml.parseFromReader(new InputStreamReader(getStream(path)));
		return parseLevelResource(xml);
	}

	private LevelResource parseLevelResource(final XMLElement xml) {
		final LevelResource lvl = new LevelResource();
		lvl.name = xml.getStringAttribute("NAME");
		return lvl;
	}

	public Spatial getLevel(final String id) {
		log.info("Loading level: " + id);
		final URL url = resource(id);
		final ObjToJme objToJME = new ObjToJme();
		final File temp = new File("temp");

		try {
			log.finer("Converting " + id + " to JME Binary.");
			objToJME.convert(getStream(id), new FileOutputStream(temp));
			log.finer("Loading JME Binary: " + temp.getAbsolutePath());
			final BinaryImporter importer = new BinaryImporter();
			return (Spatial) importer.load(temp);
		} catch (final Exception e) {
			java.util.logging.Logger.getLogger(ResourceManager.class.getName())
					.severe("FAIL! Cannot load: " + id + ". " + e.getMessage());
		}
		return null;
	}

	public InputStream getStream(final String src) {
		return getClass().getClassLoader().getResourceAsStream(src);
	}

	public URL resource(final String src) {
		return getClass().getClassLoader().getResource(PATH_ASSET_ROOT + src);
	}

	public static ResourceManager get() {
		if (null == instance) instance = new ResourceManager();
		return instance;
	}
}
