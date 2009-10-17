package com.baseoneonline.java.media;


public class Attribute<T> {

	public String name;
	public T value;

	public Attribute(final String name) {
		this(name, null);
	}

	public Attribute(final String name, final T value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String toString() {
		if (null == value) return "?";
		return value.toString();
	}

}
