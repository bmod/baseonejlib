package com.baseoneonline.java.houGeo;

import java.io.StreamTokenizer;

class FloatAttribute extends Attribute<Double> {
	public FloatAttribute() {
	}

	@Override
	public Class<Double> valueType() {
		return Double.class;
	}

	@Override
	protected Double readValue(final StreamTokenizer tk) {
		return tk.nval;
	}

}