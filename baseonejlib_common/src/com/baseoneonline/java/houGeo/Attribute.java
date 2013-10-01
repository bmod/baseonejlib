package com.baseoneonline.java.houGeo;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.lang.reflect.Array;

abstract class Attribute<T> {

	public String name;
	public int size;
	public String type;
	public T[] defaultValue;
	public T[][] values;

	public Attribute() {
	}

	@SuppressWarnings("unchecked")
	public void initValuesArray(final int numPoints) {
		values = (T[][]) Array.newInstance(valueType(), numPoints, size);
	}

	public void readValues(final StreamTokenizer tk, final int i)
			throws IOException {
		values[i] = readValues(tk);
	}

	public void readDefaultValues(final StreamTokenizer tk) throws IOException {
		defaultValue = readValues(tk);
	}

	public abstract Class<T> valueType();

	public T[] readValues(final StreamTokenizer tk) throws IOException {
		final Class<T> valueType = valueType();
		@SuppressWarnings("unchecked")
		final T[] array = (T[]) Array.newInstance(valueType, size);
		for (int i = 0; i < size; i++) {
			tk.nextToken();
			array[i] = readValue(tk);
		}

		return array;
	}

	protected abstract T readValue(final StreamTokenizer tk);

}