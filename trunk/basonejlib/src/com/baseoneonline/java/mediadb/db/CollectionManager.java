package com.baseoneonline.java.mediadb.db;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

import com.baseoneonline.java.mediadb.Controller;
import com.baseoneonline.java.mediadb.events.AbstractEventSource;
import com.baseoneonline.java.mediadb.events.EventType;
import com.baseoneonline.java.mediadb.util.Conf;
import com.baseoneonline.java.mediadb.util.Const;
import com.baseoneonline.java.mediadb.util.SupportedTypes;

import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.exceptions.CannotReadException;

/**
 * @author bmod Manages the collection
 */
public class CollectionManager extends AbstractEventSource implements Const {

	final Logger log = Logger.getLogger(getClass().getName());

	static CollectionManager instance;

	private final ScanFolderWorker scanWorker = null;

	private CollectionManager() {

	}

	public boolean getIsScanning() {
		return (null != scanWorker) && !scanWorker.isDone();
	}

	public void scanCollection(final boolean deepScan) {
		log.info("Scanning collection...");

		final ScanFolderWorker worker = new ScanFolderWorker(deepScan);
		worker.execute();

	}

	public static CollectionManager getManager() {
		if (null == CollectionManager.instance) {
			CollectionManager.instance = new CollectionManager();
		}
		return CollectionManager.instance;
	}

	/**
	 * The thread in wich the scanning occurs
	 */
	private class ScanFolderWorker extends SwingWorker<Void, Void> {

		final Logger log = Logger.getLogger(getClass().getName());

		Collection collection = Collection.getInstance();

		String[] mediaFolders = Conf.getStringArray(Const.MEDIA_FOLDERS);

		final boolean deepScan;

		public ScanFolderWorker(final boolean deepScan) {
			this.deepScan = deepScan;
		}

		@Override
		protected Void doInBackground() throws Exception {
			for (final String fname : mediaFolders) {
				final File f = new File(fname);
				scanFolder(f);
			}
			fireEvent(EventType.COLLECTION_SCANNING_DONE);
			return null;
		}
		
		
		private void notifyScanning() {
			//if (--notifyCounter <= 0) {
				fireEvent(EventType.COLLECTION_SCANNING);
			//	notifyCounter = notifyInterval;
			//}
		}

		/**
		 * Recursively scan this folder
		 *
		 * @param dir
		 */
		private void scanFolder(final File dir) {
			if (!dir.exists()) {
				return;
			}
			for (final File f : dir.listFiles()) {
				if (f.isDirectory()) {
					scanFolder(f);
				} else {
					if (SupportedTypes.isMovieType(f)) {

						final MovieItem movie = new MovieItem(f);
						if (!collection.contains(movie)) {
							collection.add(movie);
							notifyScanning();
						}

					} else if (SupportedTypes.isMusicType(f)) {

						final MusicItem music = new MusicItem(f);

						if (deepScan) {
							scanMusic(music);
						}
						if (!collection.contains(music)) {
							collection.add(music);
							notifyScanning();
						}

					} else if (SupportedTypes.isImageType(f)) {

						final ImageItem image = new ImageItem(f);
						if (!collection.contains(image)) {
							collection.add(image);
							notifyScanning();
						}

					}
				}
			}
		}

		private MusicItem scanMusic(final MusicItem item) {
			try {
				final AudioFile afile = AudioFileIO.read(item.getFile());
				item.setTag(afile.getTag());
			} catch (final CannotReadException e) {
				log.warning("Could not read: " + item.getFile().getAbsolutePath());
			} catch (final ArrayIndexOutOfBoundsException e) {
				//
			}
			return item;
		}

		@Override
		protected void process(final List<Void> chunks) {
			Controller.getController().fireCollectionScanning();
		}

		@Override
		protected void done() {
			Controller.getController().fireCollectionScanning();
		}

	}

}

