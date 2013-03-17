package com.baseoneonline.java.swing.extendedTree;

import java.util.List;

import javax.swing.Icon;

public abstract class ExtendedNodeAdapter<T> {

	public abstract List<Object> getChildren(T node);

	public abstract Icon getIcon(T value);

	public abstract Class<T> getType();

	public abstract String getLabel(T node);

}