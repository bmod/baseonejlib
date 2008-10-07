package com.baseoneonline.java.mediadb.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class EventDispatcher {
	
	
	private final Vector<ActionListener> listeners = new Vector<ActionListener>(); 
	
	public EventDispatcher() {
		
	}
	
	public void fireAction() {
		for (ActionListener l : listeners) {
			l.actionPerformed(new ActionEvent(this,0,null));
		}
	}
	
	public void addActionListener(ActionListener l) {
		if (listeners.contains(l)) return;
		listeners.add(l);
	}
	
	public void removeActionListener(ActionListener l) {
		listeners.remove(l);
	}
	
}
