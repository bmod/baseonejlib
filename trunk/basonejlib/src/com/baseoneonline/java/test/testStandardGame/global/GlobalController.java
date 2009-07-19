package com.baseoneonline.java.test.testStandardGame.global;

import java.util.ArrayList;

public class GlobalController {

	private static GlobalController instance;

	public static class Command {

		private final String name;

		public Command(final String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public static interface Listener {

		public void command(Command c);
	}

	private final ArrayList<Listener> listeners = new ArrayList<Listener>();

	private GlobalController() {}

	public void addListener(final Listener l) {
		if (listeners.contains(l)) return;
		listeners.add(l);
	}

	public void removeListener(final Listener l) {
		listeners.remove(l);
	}

	public void fireCommand(final Command cmd) {
		for (final Listener l : listeners) {
			l.command(cmd);
		}
	}

	public static GlobalController get() {
		if (null == instance) instance = new GlobalController();
		return instance;
	}
}
