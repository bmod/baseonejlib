package com.baseoneonline.java.media;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import com.baseoneonline.java.media.library.SQLLibrary;
import com.baseoneonline.java.media.library.items.MediaItem;
import com.baseoneonline.java.tools.FileUtils;
import com.baseoneonline.java.tools.FileVisitor;

public class Scanner {
	public static enum State {
		READING_LIBRARY, ADDING_FILES, FINDING_GHOSTS, IDLE
	}

	public int timerDelay = 100;
	private final Timer timer;
	private File currentFile;

	private State state = State.IDLE;

	private final List<ScannerListener> listeners = new ArrayList<ScannerListener>();

	public Scanner() {
		timer = new Timer(timerDelay, timerListener);
	}

	public void addListener(final ScannerListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	private final ActionListener timerListener = new ActionListener() {
		@Override
		public void actionPerformed(final ActionEvent e) {
			switch (state) {
			case READING_LIBRARY:
				fireUpdate("Please wait..");
				break;
			case FINDING_GHOSTS:
				fireUpdate("Finding ghost: " + currentFile);
				break;
			case ADDING_FILES:
				fireUpdate("Scanning file: " + currentFile);
				break;
			case IDLE:
				fireUpdate("Idle");
				break;
			default:
				break;
			}
		}
	};

	public void scan(final File[] dirs, final SQLLibrary lib) {
		scan(dirs, lib, true, null);
	}

	public void scan(final File[] dirs, final SQLLibrary lib,
			final FileFilter fileFilter) {
		scan(dirs, lib, true, fileFilter);
	}

	public void scan(final File[] dirs, final SQLLibrary lib,
			final boolean removeGhosts, final FileFilter filter) {

		if (!timer.isRunning()) timer.start();

		setState(State.READING_LIBRARY);
		final List<MediaItem> items = lib.getItems();

		setState(State.FINDING_GHOSTS);
		for (final MediaItem m : items) {
			currentFile = m.file.value;
			if (!currentFile.exists()) {
				lib.remove(m);
			}
		}

		setState(State.ADDING_FILES);

		for (final File dir : dirs) {
			FileUtils.visitFiles(dir, FileVisitor.FILES_ONLY, true,
					new FileVisitor() {
						@Override
						public void visit(final File f) {
							currentFile = f;
							final MediaItem m = MediaItem.create(f);
							m.readMetadata();
							lib.add(m);
						}
					}, MediaItem.FILE_FILTER);
		}

		currentFile = null;

		setState(State.IDLE);

		if (timer.isRunning()) timer.stop();

	}

	private void setState(final State s) {
		state = s;
		fireStateChanged(s);
	}

	public void removeListener(final ScannerListener l) {
		listeners.remove(l);
	}

	protected void fireStateChanged(final State s) {
		for (final ScannerListener l : listeners) {
			l.stateChanged(s);
		}
	}

	protected void fireUpdate(final String msg) {
		for (final ScannerListener l : listeners) {
			l.update(msg);
		}
	}

}
