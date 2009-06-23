package com.baseoneonline.java.media;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import com.baseoneonline.java.media.library.MediaItem;
import com.baseoneonline.java.media.library.SQLLibrary;
import com.baseoneonline.java.tools.FileUtils;
import com.baseoneonline.java.tools.FileVisitor;

public class Scanner {
	public static enum State {
		ADDING_FILES, FINDING_GHOSTS, IDLE
	}

	public int timerDelay = 100;
	private final Timer timer;
	private File currentFile;

	private State state = State.IDLE;

	private final List<ScannerListener> listeners =
		new ArrayList<ScannerListener>();

	public Scanner() {
		timer = new Timer(timerDelay, timerListener);
	}

	public void addListener(ScannerListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	private final ActionListener timerListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (state == State.ADDING_FILES) {
				fireUpdate("Scanning file: " + currentFile.getAbsolutePath());
			}
		}
	};
	
	public void scan(File dir, SQLLibrary lib) {
		scan(dir, lib, true);
	}

	public void scan(final File dir, final SQLLibrary lib, boolean removeGhosts) {

		if (!timer.isRunning())
			timer.start();
		
		setState(State.FINDING_GHOSTS);
		for (MediaItem m : lib.getItems()) {
			if (!m.file.exists()) lib.remove(m);
		}
		

		setState(State.ADDING_FILES);
		FileUtils.visitFiles(dir, FileVisitor.FILES_ONLY, new FileVisitor() {
			@Override
			public void visit(final File f) {
				currentFile = f;
				lib.add(new MediaItem(f));
			}
		});
		
		
		
		setState(State.IDLE);

		if (timer.isRunning())
			timer.stop();

	}

	private void setState(State s) {
		state = s;
		fireStateChanged(s);
	}

	public void removeListener(ScannerListener l) {
		listeners.remove(l);
	}

	protected void fireStateChanged(State s) {
		for (ScannerListener l : listeners) {
			l.stateChanged(s);
		}
	}

	protected void fireUpdate(String msg) {
		for (ScannerListener l : listeners) {
			l.update(msg);
		}
	}

}
