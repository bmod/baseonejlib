package com.baseoneonline.java.blips.sequencer;

public class SequencerEvent {
	public static enum Type {
		TICK,
		BEAT,
		NOTE
	}
	
	public final Type type;
	
	public SequencerEvent(final Type t) {
		type = t;
	}
	
}
