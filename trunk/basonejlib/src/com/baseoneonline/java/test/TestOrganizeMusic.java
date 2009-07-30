package com.baseoneonline.java.test;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.baseoneonline.java.test.testMusicOrganizer.MusicOrganizer;

public class TestOrganizeMusic {

	public static void main(String[] args) {
		new TestOrganizeMusic();
	}


	String path = "s:/music";

	public TestOrganizeMusic() {
		Logger.getLogger("org.jaudiotagger").setLevel(Level.SEVERE);

		MusicOrganizer.get().organize(new File(path));

	}

}
