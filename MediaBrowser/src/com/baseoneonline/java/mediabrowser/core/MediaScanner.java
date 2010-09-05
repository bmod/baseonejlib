package com.baseoneonline.java.mediabrowser.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

public class MediaScanner {

	private final ArrayList<Listener> listeners =
			new ArrayList<MediaScanner.Listener>();

	private SwingWorker<ArrayList<MediaFile>, MediaFile> worker;
	private ArrayList<MediaFile> mediaFiles;

	private final Database db;

	public MediaScanner(final Database db) {
		this.db = db;
	}

	public void scan(final String[] dirs) {
		final File[] files = new File[dirs.length];
		for (int i = 0; i < files.length; i++) {
			files[i] = new File(dirs[i]);
		}
		scan(files);
	}

	public void scan(final File[] dirs) {
		// validateDatabase();
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

	private void scanFilesFromDisk(final File[] dirs) {
		// Validate
		fireStatusChanged(Status.GatherFiles);

		worker = new SwingWorker<ArrayList<MediaFile>, MediaFile>() {

			@Override
			protected ArrayList<MediaFile> doInBackground() throws Exception {

				mediaFiles = new ArrayList<MediaFile>();
				for (final File dir : dirs) {
					if (dir.exists()) {
						scanDir(dir);
					} else {
						Logger.getLogger(getClass().getName()).warning(
								"Dir not found: " + dir.getAbsolutePath());
					}
				}
				return mediaFiles;
			}

			private void scanDir(final File dir) {
				for (final File f : dir.listFiles()) {
					if (f.isDirectory()) {
						scanDir(f);
					} else {
						final MediaFile mf = new MediaFile(f.getAbsolutePath());
						publish(mf);
					}
				}
			}

			@Override
			protected void process(final List<MediaFile> chunks) {
				mediaFiles.addAll(chunks);
				fireProgress(chunks);
			}

			@Override
			protected void done() {
				fireStatusChanged(Status.Done);
			};

		};
		worker.execute();

	}

	private void fireStatusChanged(final Status msg) {
		for (final Listener l : listeners) {
			l.statusChanged(msg);
		}
	}

	private void fireProgress(final List<MediaFile> chunk) {
		for (final Listener l : listeners) {
			l.process(chunk);
		}
	}

	public ArrayList<MediaFile> getMediaFiles() {
		return mediaFiles;
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
		public void process(List<MediaFile> files);

		public void statusChanged(Status msg);

	}

}
