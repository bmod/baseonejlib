package com.baseoneonline.jlib.ardor3d.framework;

public interface ObjectLoader<T> {
	T load(String resource);

	Class<T> getContentType();
}