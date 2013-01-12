package com.baseoneonline.jlib.ardor3d.framework;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.ardor3d.util.export.Savable;
import com.ardor3d.util.export.xml.XMLImporter;
import com.baseoneonline.java.nanoxml.XMLElement;
import com.baseoneonline.java.nanoxml.XMLParseException;
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

	public void setTag(String name, Class<? extends Savable> clazz) {
		if (tagMap.containsKey(name))
			throw new RuntimeException("The tag is already defined: " + name);
		tagMap.put(name, clazz);
	}

	/**
	 * Replace all supported tags with the class names
	 * 
	 * @param resource
	 * @return The
	 */
	@Override
	public Savable load(InputStream is) throws IOException {

		XMLElement xml = new XMLElement();
		xml.ignoreCase = false;
		try {
			xml.parseFromReader(new InputStreamReader(is));
		} catch (XMLParseException e) {
			throw new RuntimeException(e);
		}
		transformTag(xml);
		System.out.println(xml.toString());
		InputStream is2 = new ByteArrayInputStream(xml.toString().getBytes());
		return super.load(is2);
	}

	private void transformTag(XMLElement xml) {
		String tag = xml.getName().toLowerCase();

		// Simple tagname
		if (tagMap.containsKey(tag)) {
			Class<? extends Savable> type = tagMap.get(tag);
			xml.setName(type.getName());
		}

		for (XMLElement xChild : xml.getChildren())
			transformTag(xChild);
	}
}
