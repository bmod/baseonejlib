package com.baseoneonline.java.test.testStandardGame.menu;

import java.util.ArrayList;
import java.util.HashMap;

import com.jme.input.KeyInput;
import com.jme.input.KeyInputListener;

public class MenuInput {

	public enum Trigger {
		Up, Down, Left, Right, Accept, Back
	}

	private static MenuInput instance;

	private final HashMap<Integer, Trigger> keyMap = new HashMap<Integer, Trigger>();

	private final ArrayList<IMenuInputListener> listeners = new ArrayList<IMenuInputListener>();

	private MenuInput() {

		KeyInput.get().addListener(new KeyInputListener() {

			@Override
			public void onKey(final char character, final int keyCode,
					final boolean pressed) {
				if (pressed && keyMap.containsKey(keyCode)) {
					fireTrigger(keyMap.get(keyCode));
				}

			}
		});
	}

	public void mapKey(final int key, final Trigger trigger) {
		keyMap.put(key, trigger);
	}

	private void fireTrigger(final Trigger trigger) {
		for (final IMenuInputListener l : listeners) {
			l.onMenuInput(trigger);
		}
	}

	public void addListener(final IMenuInputListener l) {
		listeners.add(l);
	}

	public void removeListener(final IMenuInputListener l) {
		listeners.remove(l);
	}

	public static MenuInput get() {
		if (null == instance) instance = new MenuInput();
		return instance;
	}
}
