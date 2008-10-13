package com.baseoneonline.java.mediadb.events;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.SwingUtilities;

public class AbstractEventSource implements EventSource {

	private final HashMap<EventType, List<EventListener>> listeners = new HashMap<EventType, List<EventListener>>();

	public void addEventListener(final EventType type, final EventListener lst) {
		if (null == listeners.get(type)) {
			listeners.put(type, new Vector<EventListener>());
		}
		if (!listeners.get(type).contains(lst)) {
			listeners.get(type).add(lst);
		}
	}

	public void fireEvent(final EventType type) {
		fireEvent(new Event(type));
	}

	public void fireEvent(final Event evt) {
		if (null != listeners.get(evt.type)) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					for (final EventListener lst : listeners.get(evt.type)) {
						lst.onEvent(evt);
					}
				}
			});
		}
	}

	public void removeEventListener(final EventType type, final EventListener lst) {
		if (null != listeners.get(type)) {
			if (listeners.get(type).contains(lst)) {
				listeners.get(type).remove(lst);
			}
		}
	}

}
