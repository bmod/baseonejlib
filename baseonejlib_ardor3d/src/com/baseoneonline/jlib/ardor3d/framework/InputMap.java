package com.baseoneonline.jlib.ardor3d.framework;

import java.util.HashMap;

import com.ardor3d.input.logical.TwoInputStates;
import com.google.common.base.Predicate;

public class InputMap {

	private final HashMap<String, Predicate<TwoInputStates>> map = new HashMap<String, Predicate<TwoInputStates>>();

	public InputMap() {

	}

	public Predicate<TwoInputStates> get(String key) {
		return map.get(key);
	}

	public void put(String key, Predicate<TwoInputStates> condition) {
		map.put(key, condition);
	}

}
