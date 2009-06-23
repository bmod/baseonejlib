package com.baseoneonline.java.media;

import com.baseoneonline.java.media.Scanner.State;

public interface ScannerListener {
	
	public void stateChanged(State s);
	
	public void update(String msg);
}
