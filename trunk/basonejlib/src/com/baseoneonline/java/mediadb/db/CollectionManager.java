package com.baseoneonline.java.mediadb.db;

import java.io.File;
import java.io.FileFilter;
import java.util.logging.Logger;

import com.baseoneonline.java.mediadb.util.Conf;
import com.baseoneonline.java.mediadb.util.Const;
import com.baseoneonline.java.mediadb.util.SupportedFilenameFilter;
import com.baseoneonline.java.mediadb.util.SupportedTypes;

import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.exceptions.CannotReadException;

public class CollectionManager implements Const {

	private final Logger log = Logger.getLogger(getClass().getName());

	private static CollectionManager instance;
	private final Collection collection = Collection.getInstance();

	private final FileFilter supportedTypesFilter = new SupportedFilenameFilter();

	private CollectionManager() {

	}

	public void scanCollection(final boolean deepScan) {
		log.info("Scanning collection...");
		final String[] mediaFolders = Conf.getStringArray(MEDIA_FOLDERS);
		Thread t = new Thread() {
			@Override
			public void run() {
				for (String fname : mediaFolders) {
					File f = new File(fname);
					scanFolder(f, deepScan);
				}
			}

		};
		t.start();

	}

	/**
	 * Recursively scan this folder
	 * 
	 * @param dir
	 */
	private void scanFolder(File dir, boolean deepScan) {
		if (!dir.exists()) {
			return;
		}
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				scanFolder(f, deepScan);
			} else {
				if (SupportedTypes.isMovieType(f)) {
					MovieItem movie = new MovieItem(f);
					if (!collection.contains(movie)) {
						Collection.getInstance().add(movie);
					}
				} else if (SupportedTypes.isMusicType(f)) {
					MusicItem movie = new MusicItem(f);
					if (deepScan) scanMusic(movie); 
					if (!collection.contains(movie)) {
					//	log.info("Adding "+f.getName());
						Collection.getInstance().add(movie);
					}
				} else if (SupportedTypes.isImageType(f)) {
					ImageItem movie = new ImageItem(f);
					if (!collection.contains(movie)) {
						Collection.getInstance().add(movie);
					}
				}
			}
		}
	}
	
	private MusicItem scanMusic(MusicItem item) {
		try {
			AudioFile afile = AudioFileIO.read(item.getFile());
			item.setTag(afile.getTag());
			
		} catch (CannotReadException e) {
			log.warning("Could not read: "+item.getFile().getAbsolutePath());
		}
		return item;
	}
	

	public static CollectionManager getManager() {
		if (null == instance) {
			instance = new CollectionManager();
		}
		return instance;
	}
}
