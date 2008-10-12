package com.baseoneonline.java.mediadb;

import com.baseoneonline.java.mediadb.events.AbstractEventSource;
import com.baseoneonline.java.mediadb.events.Event;
import com.baseoneonline.java.mediadb.events.EventType;

public class Controller extends AbstractEventSource {

	private Controller() {

	}

	public void fireCollectionScanning() {
		final Event evt = new Event(EventType.COLLECTION_SCANNING);
		fireEvent(evt);
	}

	public void fireCollectionScanningDone() {
		final Event evt = new Event(EventType.COLLECTION_SCANNING_DONE);
		fireEvent(evt);
	}


	private static Controller instance;

	public static Controller getController() {
		if (null == Controller.instance) {
			Controller.instance = new Controller();
		}
		return Controller.instance;
	}

}
