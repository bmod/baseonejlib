package com.baseoneonline.java.mediadb.events;


public interface EventSource {
	public void addEventListener(EventType type, EventListener lst);
	public void removeEventListener(EventType type, EventListener lst);
	public void fireEvent(Event evt);
}
