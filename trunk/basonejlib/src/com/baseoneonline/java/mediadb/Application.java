package com.baseoneonline.java.mediadb;

import com.baseoneonline.java.mediadb.db.AbstractDatabase;
import com.baseoneonline.java.mediadb.events.DefaultEventSource;
import com.baseoneonline.java.mediadb.events.Event;
import com.baseoneonline.java.mediadb.events.EventListener;
import com.baseoneonline.java.mediadb.events.EventSource;
import com.baseoneonline.java.mediadb.events.EventType;

public class Application {
	private static Application instance;

	final String[][] exts = { 
			{ "jpg", "jpeg", "png", "tif", "tiff", "gif" },
			{ "mp3", "mpa" }, 
			{ "mov", "wmv", "avi" },
			{ "sfc", "smc", "nes" } 
	};
	private final EventSource eventSource = new DefaultEventSource();

	private Application() {

	}

	public void initialize(AbstractDatabase db) {
	}


	public static Application getInstance() {
		if (instance == null)
			instance = new Application();
		return instance;
	}

	public void addListener(EventType type, EventListener lst) {
		eventSource.addEventListener(type, lst);
	}

	public void fireEvent(Event evt) {
		eventSource.fireEvent(evt);
	}

	public void removeListener(EventType type, EventListener lst) {
		eventSource.removeEventListener(type, lst);
	}
}
