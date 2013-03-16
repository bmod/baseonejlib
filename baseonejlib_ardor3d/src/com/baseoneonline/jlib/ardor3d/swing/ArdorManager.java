package com.baseoneonline.jlib.ardor3d.swing;

import com.ardor3d.annotation.MainThread;
import com.ardor3d.framework.FrameHandler;
import com.ardor3d.image.util.AWTImageLoader;
import com.ardor3d.input.logical.LogicalLayer;
import com.ardor3d.util.Timer;

public class ArdorManager {
	private static ArdorManager instance;
	final Timer timer = new Timer();
	final FrameHandler frameWork = new FrameHandler(timer);
	final LogicalLayer logicalLayer = new LogicalLayer();

	private volatile boolean exit = false;

	private ArdorManager() {
		System.setProperty("ardor3d.useMultipleContexts", "true");
		AWTImageLoader.registerLoader();
	}

	public LogicalLayer getLogicalLayer() {
		return logicalLayer;
	}

	public void shutdown() {
		exit = true;
	}

	@MainThread
	public void start() {
		while (!exit) {
			logicalLayer.checkTriggers(0);
			frameWork.updateFrame();
			Thread.yield();
		}
	}

	public void update() {
		frameWork.updateFrame();
	}

	public FrameHandler getFrameWork() {
		return frameWork;
	}

	public static ArdorManager get() {
		if (null == instance)
			instance = new ArdorManager();
		return instance;
	}
}