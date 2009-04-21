package com.baseoneonline.java.mediadb.events;

public class Event {
	
	public EventType type;
	
	public Event(EventType type) {
		this.type = type;
	}
	
	public EventType getType() {
		return type;
	}
	
}
