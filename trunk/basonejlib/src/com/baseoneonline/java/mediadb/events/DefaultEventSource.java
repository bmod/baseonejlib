package com.baseoneonline.java.mediadb.events;

import java.util.ArrayList;
import java.util.HashMap;

public class DefaultEventSource implements EventSource {

	private final HashMap<EventType, ArrayList<EventListener>> 
	listeners = new HashMap<EventType, ArrayList<EventListener>>();
	
	/**
	 * @param type
	 * @param lst
	 */
	public void addListener(EventType type, EventListener lst) {
		ArrayList<EventListener> lstArray = listeners.get(type);
		if (null == lstArray) {
			lstArray = new ArrayList<EventListener>();
			listeners.put(type, lstArray);
		}
		if (lstArray.contains(lst)) return;
		lstArray.add(lst);
	}
	
	/**
	 * @param type
	 * @param lst
	 */
	public void removeListener(EventType type, EventListener lst) {
		ArrayList<EventListener> lstArray = listeners.get(type);
		if (null == lstArray) return;
		if (lstArray.contains(lst)) lstArray.remove(lst);
	}
	
	/**
	 * @param evt
	 */
	public void fireEvent(Event evt) {
		ArrayList<EventListener> lstArray = listeners.get(evt.getType());
		if (lstArray == null) return;
		for (EventListener l : lstArray) {
			l.eventReceived(evt);
		}
	}
	
}
