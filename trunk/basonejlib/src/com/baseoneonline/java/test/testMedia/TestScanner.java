package com.baseoneonline.java.test.testMedia;

import java.io.File;

import com.baseoneonline.java.media.Library;
import com.baseoneonline.java.media.Media;
import com.baseoneonline.java.media.MemoryLibrary;
import com.baseoneonline.java.tools.ExecutionTimer;

public class TestScanner {

	public static void main(final String[] args) {

		final Library lib = new MemoryLibrary();

		final ExecutionTimer timer = ExecutionTimer.get();
		Media.scan(new File("S:/music"), lib);
		timer.stop();

	}

}
