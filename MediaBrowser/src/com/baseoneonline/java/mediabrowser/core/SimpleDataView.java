package com.baseoneonline.java.mediabrowser.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.baseoneonline.java.mediabrowser.Settings;

/*
 * Simply show all elements, categorized by type
 */
public class SimpleDataView implements DataView {

	private final Database db;

	private final MediaNode root = new RootNode();

	private final HashMap<TypeNode, ArrayList<MediaFile>> map =
			new HashMap<TypeNode, ArrayList<MediaFile>>();

	private final ArrayList<TypeNode> types = new ArrayList<TypeNode>();

	public SimpleDataView(final Database db, final Settings settings) {
		this.db = db;
		final List<MediaFile> files = db.getMediaFiles();

		for (final FileType t : settings.getMediaFileTypes()) {
			types.add(new TypeNode(t));

		}
	}

	@Override
	public ArrayList<? extends MediaNode> getChildren(final MediaNode parent) {
		if (getRoot() == parent) {
			return types;
		}
		return null;
	}

	@Override
	public MediaNode getRoot() {
		return root;
	}

	@Override
	public void addListener(final DataViewListener l) {

	}

	@Override
	public void removeListener(final DataViewListener l) {

	}

}

class RootNode implements MediaNode {
	@Override
	public String toString() {
		return "Mediaroot";
	}
}

class TypeNode implements MediaNode {
	private final FileType type;

	public TypeNode(final FileType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type.getName();
	}
}
