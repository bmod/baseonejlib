package com.baseoneonline.java.resourceMapper;

import java.util.ArrayList;
import java.util.List;

import com.baseoneonline.java.nanoxml.XMLElement;

public class XMLResourceNode implements ResourceNode {

	private List<ResourceNode> children;

	XMLElement xml;

	public XMLResourceNode(final String name) {
		xml = new XMLElement(name);
		xml.ignoreCase = false;
	}

	public XMLResourceNode(final XMLElement xml) {
		this.xml = xml;
	}

	@Override
	public void addChild(final ResourceNode xChild) {
		xml.addChild(((XMLResourceNode) xChild).getXML());
	}

	@Override
	public void setAttribute(final String key, final Object value) {
		xml.setAttribute(key, value);
	}

	@Override
	public ResourceNode create(final String name) {
		return new XMLResourceNode(name);
	}

	@Override
	public ResourceNode getChild(final String name) {
		for (final ResourceNode node : getChildren()) {
			if (node.getName().equals(name))
				return node;
		}
		return null;
	}

	@Override
	public String getName() {
		return xml.getName();
	}

	@Override
	public String getAttribute(final String name) {
		return xml.getStringAttribute(name);
	}

	@Override
	public List<ResourceNode> getChildren() {
		if (null == children) {
			children = new ArrayList<ResourceNode>();
			for (final XMLElement xChild : xml.getChildren()) {
				children.add(new XMLResourceNode(xChild));
			}
		}
		return children;
	}

	@Override
	public List<ResourceNode> getChildren(final String name) {
		final List<ResourceNode> children = new ArrayList<ResourceNode>();
		for (final ResourceNode node : getChildren()) {
			if (node.getName().equals(name))
				children.add(node);
		}
		return children;
	}

	public XMLElement getXML() {
		return xml;
	}
}
