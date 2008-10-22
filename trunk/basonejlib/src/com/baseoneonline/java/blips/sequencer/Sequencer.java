package com.baseoneonline.java.blips.sequencer;

import java.util.ArrayList;
import java.util.List;

import com.baseoneonline.java.blips.sequencer.SequencerEvent.Type;

public class Sequencer {

	private final List<SequencerListener> listeners = new ArrayList<SequencerListener>();

	int MS_PER_MINUTE = 60000;

	long delay = 50;

	Thread timer = new Thread() {

		@Override
		public void run() {

			while (true) {

				try {
					final long startTime = System.currentTimeMillis();
					fireEvent(Type.BEAT);

					final long timeTaken = System.currentTimeMillis()
							- startTime;

					if (timeTaken < delay) {
						synchronized (this) {
							wait(delay - timeTaken);
						}
					} 

				} catch (final InterruptedException e) {}

			}
		}

	};

	public Sequencer() {
		setSpeed(80);
	}

	public void start() {
		timer.start();
	}

	public void setSpeed(final float bpm) {
		delay = (long) (MS_PER_MINUTE / bpm);
	}

	public void addListener(final SequencerListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	public void removeListener(final SequencerListener l) {
		if (listeners.contains(l)) {
			listeners.remove(l);
		}
	}

	public void fireEvent(final Type type) {
	final SequencerEvent e = new SequencerEvent(Type.TICK);
		for (final SequencerListener l : listeners) {
			l.onEvent(e);
		}
	}

}
