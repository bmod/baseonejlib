package com.baseoneonline.jlib.ardor3d.framework;

import com.ardor3d.input.logical.InputTrigger;
import com.ardor3d.input.logical.LogicalLayer;
import com.ardor3d.input.logical.TriggerAction;

public class InputManager {

	private final LogicalLayer logicalLayer;
	private final InputMap map;

	public InputManager(LogicalLayer logicalLayer, InputMap map) {
		this.logicalLayer = logicalLayer;
		this.map = map;
	}

	public void update(double t) {
		logicalLayer.checkTriggers(t);
	}

	public void registerTrigger(String key, TriggerAction action) {
		logicalLayer.registerTrigger(new InputTrigger(map.get(key), action));
	}
}
