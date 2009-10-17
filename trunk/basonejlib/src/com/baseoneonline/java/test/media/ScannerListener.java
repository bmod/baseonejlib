package com.baseoneonline.java.test.media;

import com.baseoneonline.java.test.media.Scanner.State;


public interface ScannerListener {
	
	public void stateChanged(State s);
	
	public void update(String msg);
}
