package com.baseoneonline.jlib.ardor3d.framework;

import java.util.HashMap;

import com.ardor3d.input.logical.InputTrigger;
import com.ardor3d.input.logical.LogicalLayer;
import com.ardor3d.input.logical.TriggerAction;
import com.ardor3d.input.logical.TwoInputStates;
import com.google.common.base.Predicate;

public class InputManager {

	private static InputManager instance;

	public static InputManager get() {
		if (instance == null)
			instance = new InputManager();
		return instance;
	}

	private HashMap<String, Predicate<TwoInputStates>> inputMap;
	private LogicalLayer logicalLayer;

	private InputManager() {

	}

	public void setLogicalLayer(LogicalLayer logicalLayer) {
		this.logicalLayer = logicalLayer;
	}

	public LogicalLayer getLogicalLayer() {
		return logicalLayer;
	}

	private void setInputMap(HashMap<String, Predicate<TwoInputStates>> inputMap) {
		this.inputMap = inputMap;
	}

	public InputTrigger register(String key, TriggerAction action) {
		if (null == inputMap)
			throw new RuntimeException("Please provide an inputmap first");
		if (!inputMap.containsKey(key))
			throw new RuntimeException("Key not found in inputmap: " + key);
		InputTrigger trigger = new InputTrigger(inputMap.get(key), action);
		logicalLayer.registerTrigger(trigger);
		return trigger;
	}

	private HashMap<String, Predicate<TwoInputStates>> getMap() {
		if (null == inputMap)
			inputMap = new HashMap<String, Predicate<TwoInputStates>>();
		return inputMap;
	}

	public void put(String id, Predicate<TwoInputStates> predicate) {
		getMap().put(id, predicate);
	}
}
