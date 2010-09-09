package com.baseoneonline.java.mediabrowser.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

import com.baseoneonline.java.mediabrowser.util.Util;

public class MediaScanner {

	private final ArrayList<Listener> listeners =
			new ArrayList<MediaScanner.Listener>();

	private SwingWorker<Void, MediaFile> worker;

	private final Database db;
	
	private int totalFiles = 0;
	// Files to read before storing in the database, batch storing is faster
	private final int storeInterval = 1500;

	public MediaScanner(final Database db) {
		this.db = db;
	}

	public void scan(final ArrayList<File> dirs) {
		scanFilesFromDisk(dirs);
	}

	private void validateDatabase() {
		final ArrayList<MediaFile> removeFiles = new ArrayList<MediaFile>();
		final ArrayList<MediaFile> updateFiles = new ArrayList<MediaFile>();
		for (final MediaFile mf : db.getMediaFiles()) {
			final File f = new File(mf.file);
			if (!f.exists()) {
				removeFiles.add(mf);
				continue;
			}
			if (f.lastModified() != mf.lastModified) {
				updateFiles.add(mf);
				continue;
			}
		}
	}

	private void scanFilesFromDisk(final ArrayList<File> dirs) {
		// Validate
		fireStatusChanged(Status.GatherFiles);
		
		final ArrayList<MediaFile> storeCache = new ArrayList<MediaFile>();

		worker = new SwingWorker<Void, MediaFile>() {

			@Override
			protected Void doInBackground() throws Exception {
				totalFiles = 0;
				for (final File dir : dirs) {
					if (dir.exists()) {
						scanDir(dir);
					} else {
						Logger.getLogger(getClass().getName()).warning(
								"Dir not found: " + dir.getAbsolutePath());
					}
				}
				if (storeCache.size() > 0) {
					db.storeMediaFiles(storeCache);
				}
				
				return null;
			}

			private void scanDir(final File dir) {
				for (final File f : dir.listFiles()) {
					if (f.isDirectory()) {
						scanDir(f);
					} else {

						final MediaFile mf = new MediaFile(f.getAbsolutePath());
						mf.type = getTypeByExtension(f.getName());
						if (mf.type != null) {
							totalFiles++;
							storeCache.add(mf);
							if (storeCache.size() > storeInterval) {
								//db.storeMediaFiles(storeCache);
								storeCache.clear();
							}
							
							publish(mf);
						}
						
					}
				}
			}

			@Override
			protected void process(final List<MediaFile> files) {
				fireProgress(files);
			}

			@Override
			protected void done() {
				fireStatusChanged(Status.Done);
			};

		};
		worker.execute();

	}

	private FileType getTypeByExtension(final String filename) {

		final String extension = Util.extension(filename);

		for (final FileType t : db.getFileTypes()) {
			if (Util.contains(t.extensions, extension))
				return t;
		}
		return null;
	}

	private void fireStatusChanged(final Status msg) {
		for (final Listener l : listeners) {
			l.statusChanged(msg, totalFiles);
		}
	}

	private void fireProgress(final List<MediaFile> chunk) {
		for (final Listener l : listeners) {
			l.process(chunk, totalFiles);
		}
	}


	public void addListener(final Listener l) {
		if (!listeners.contains(l))
			listeners.add(l);
	}

	public void removeListener(final Listener l) {
		listeners.remove(l);
	}

	public static enum Status {
		GatherFiles, Done
	}

	public static interface Listener {
		public void process(List<MediaFile> files, int totalScanned);

		public void statusChanged(final Status msg, int totalScanned);

	}

}
