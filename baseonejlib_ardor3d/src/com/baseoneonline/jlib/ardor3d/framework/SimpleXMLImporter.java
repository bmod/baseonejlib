package com.baseoneonline.jlib.ardor3d.framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.ardor3d.util.export.Savable;
import com.ardor3d.util.export.xml.XMLImporter;
import com.baseoneonline.jlib.ardor3d.framework.entities.Entity;
import com.baseoneonline.jlib.ardor3d.framework.entities.ModelComponent;
import com.baseoneonline.jlib.ardor3d.framework.entities.PhysicsComponent;
import com.baseoneonline.jlib.ardor3d.framework.entities.RandomRotationComponent;
import com.baseoneonline.jlib.ardor3d.framework.entities.SpatialComponent;
import com.baseoneonline.jlib.ardor3d.framework.entities.TriggerComponent;

public class SimpleXMLImporter extends XMLImporter {

	private final HashMap<String, Class<? extends Savable>> tagMap = new HashMap<String, Class<? extends Savable>>();

	public SimpleXMLImporter() {
		simplifyTag(Entity.class);
		simplifyTag(ModelComponent.class);
		simplifyTag(PhysicsComponent.class);
		simplifyTag(SpatialComponent.class);
		simplifyTag(TriggerComponent.class);
		simplifyTag(RandomRotationComponent.class);
	}

	public void simplifyTag(Class<? extends Savable> clazz) {
		String name = clazz.getSimpleName().toLowerCase();
		setTag(name, clazz);
	}

	private void setTag(String name, Class<? extends Savable> clazz) {
		if (tagMap.containsKey(name))
			throw new RuntimeException("The tag is already defined: " + name);
		tagMap.put(name, clazz);
	}

	@Override
	public Savable load(final InputStream is) throws IOException {
		try {
			final DOMInputCapsule _domIn = new DOMInputCapsule(
					DocumentBuilderFactory.newInstance().newDocumentBuilder()
							.parse(is), tagMap);
			return _domIn.readSavable(null, null);
		} catch (final SAXException e) {
			final IOException ex = new IOException();
			ex.initCause(e);
			throw ex;
		} catch (final ParserConfigurationException e) {
			final IOException ex = new IOException();
			ex.initCause(e);
			throw ex;
		}
	}
}
