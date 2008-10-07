package com.baseoneonline.java.mediadb.events;


public interface EventSource {
	public void addListener(EventType type, EventListener lst);
	public void removeListener(EventType type, EventListener lst);
	public void fireEvent(Event evt);
}
