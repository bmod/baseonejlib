package com.baseoneonline.java.rombrowser;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MediaCache {

	private static MediaCache instance;

	private final HashMap<GameSystem, ArrayList<Rom>> romMap = new HashMap<GameSystem, ArrayList<Rom>>();

	private MediaCache() {

	}

	public ArrayList<Rom> getRoms(final GameSystem sys) {
		ArrayList<Rom> roms = romMap.get(sys);
		if (null == roms) {
			roms = new ArrayList<Rom>();
			for (final File f : sys.getPath().listFiles(sys.getFileFilter())) {
				roms.add(new Rom(sys, f));
			}
			romMap.put(sys, roms);
		}
		return roms;
	}

	public void store() {

	}

	public static MediaCache get() {
		if (null == instance) instance = new MediaCache();
		return instance;
	}

}
