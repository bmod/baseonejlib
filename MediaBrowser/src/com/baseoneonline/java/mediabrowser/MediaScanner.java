package com.baseoneonline.java.mediabrowser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

public class MediaScanner {

	private final ArrayList<Listener> listeners = new ArrayList<MediaScanner.Listener>();

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
		validateDatabase();
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
		for (final File dir : dirs) {
			if (!dir.exists())
				throw new RuntimeException("Directory doesn't exists: "
						+ dir.getAbsolutePath());
			if (!dir.isDirectory())
				throw new RuntimeException("Must be directory: "
						+ dir.getAbsolutePath());
		}

		worker = new SwingWorker<ArrayList<MediaFile>, MediaFile>() {

			@Override
			protected ArrayList<MediaFile> doInBackground() throws Exception {
				mediaFiles = new ArrayList<MediaFile>();
				for (final File dir : dirs) {
					scanDir(dir);
				}
				return mediaFiles;
			}

			private void scanDir(final File dir) {

				for (final File f : dir.listFiles()) {
					if (f.isDirectory()) {
						scanDir(f);
					} else {
						publish(addMediaFile(f));
					}
				}
			}

			@Override
			protected void process(final List<MediaFile> chunks) {
				for (final Listener l : listeners) {
					l.process(chunks);
				}
			}

		};
		worker.execute();

	}

	private MediaFile addMediaFile(final File f) {
		final MediaFile mf = new MediaFile(f.getAbsolutePath());
		mediaFiles.add(mf);
		return mf;
	}

	public void addListener(final Listener l) {
		if (!listeners.contains(l))
			listeners.add(l);
	}

	public void removeListener(final Listener l) {
		listeners.remove(l);
	}

	public static interface Listener {
		public void process(List<MediaFile> files);

		public void done(List<MediaFile> files);
	}

}
