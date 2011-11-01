package com.baseoneonline.java.resourceMapper;

import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import com.baseoneonline.java.nanoxml.XMLElement;

public class XMLResourceTree extends ResourceTree {

	private static final String LINK = "link";

	private final ResourceLocator locator;

	public XMLResourceTree(final ResourceLocator locator) throws Exception {
		this.locator = locator;

	}

	private void saveElement(Object node, String path) throws Exception {
		XMLElement xml = new XMLElement(getName(node));

	}

	private XMLElement loadElement(final String path) throws Exception {
		final XMLElement root = new XMLElement();
		root.parseFromReader(new InputStreamReader(locator.getInputStream(path)));
		return root;
	}

	private List<XMLElement> children(final XMLElement parent) {

		for (final XMLElement xml : parent.getChildren()) {
			if (xml.getName().equals(LINK)) {
				parent.removeChild(xml);
				try {
					final XMLElement replacement = loadElement(xml
							.getStringAttribute("src"));
					parent.addChild(replacement);
				} catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return parent.getChildren();
	}

	@Override
	public int getChildCount(final Object parent) {
		return children((XMLElement) parent).size();
	}

	@Override
	public Object getChild(final Object parent, final int index) {
		return children((XMLElement) parent).get(index);
	}

	@Override
	public Object getChild(final Object parent, final String name) {
		return ((XMLElement) parent).getChild(name);
	}

	@Override
	public String getName(final Object node) {
		return ((XMLElement) node).getName();
	}

	@Override
	public String getString(final Object node, final String name) {
		return ((XMLElement) node).getStringAttribute(name);
	}

	@Override
	public Object addChild(final Object parent, final String name) {
		final XMLElement xml = new XMLElement(name);
		((XMLElement) parent).addChild(xml);
		return xml;
	}

	@Override
	public Object load(String path) throws Exception {
		return loadElement(path);
	}

	@Override
	public void save(Object node, String path) {
		throw new NotImplementedException();
	}

	@Override
	public void putString(Object node, String name, String value) {
		((XMLElement) node).setAttribute(name, value);
	}

}
