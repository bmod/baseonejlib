package com.baseoneonline.java.tools;

public class ExecutionTimer {

	private long startTime;

	public void start() {
		startTime = System.currentTimeMillis();
	}

	public long stop() {
		final long stopTime = System.currentTimeMillis();
		final long dif = stopTime - startTime;
		final float sec = (float) dif / 1000;
		System.out.println("Execution time: " + dif + " ms. (" + sec + "sec)");
		return dif;
	}

	public static ExecutionTimer get() {
		final ExecutionTimer timer = new ExecutionTimer();
		timer.start();
		return timer;
	}

}
