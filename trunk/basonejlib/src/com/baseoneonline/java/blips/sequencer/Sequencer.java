package com.baseoneonline.java.blips.sequencer;

import java.util.ArrayList;
import java.util.List;

public class Sequencer {

	private final List<SequencerListener> listeners = new ArrayList<SequencerListener>();

	int MS_PER_MINUTE = 60000;

	long delay = 50;
	private int ticksPerBeat = 1;
	private int currentTick = 1;

	Thread timer = new Thread() {

		@Override
		public void run() {

			while (true) {

				try {
					final long startTime = System.currentTimeMillis();

					if (currentTick > ticksPerBeat) {
						fireBeat();
						currentTick = 0;
					}
					currentTick++;
					fireTick();

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
		setSpeed(80,4);
	}

	public void start() {
		timer.start();
	}

	public void setSpeed(final float bpm, final int ticksPerBeat) {
		delay = (long) (MS_PER_MINUTE / (bpm*ticksPerBeat));
		this.ticksPerBeat = ticksPerBeat;
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

	public void fireTick() {
		for (final SequencerListener l : listeners) {
			l.onTick();
		}
	}

	public void fireBeat() {
		for (final SequencerListener l : listeners) {
			l.onBeat();
		}
	}

}
