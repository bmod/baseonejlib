package com.baseoneonline.java.rombrowser;
import java.io.IOException;
import java.util.logging.Logger;

public class Launcher {

	private static Launcher instance;

	private Launcher() {

	}

	public void launch(final Rom rom) {
		final String command = rom.getSystem().getEmulator() + " \""
				+ rom.getFile().getAbsolutePath() + "\" "
				+ rom.getSystem().getParameters();
		Logger.getLogger(getClass().getName()).info(command);

		try {
			Runtime.getRuntime().exec(command);
		} catch (final IOException e) {
			java.util.logging.Logger.getLogger(Launcher.class.getName())
					.warning(e.getMessage());
		}
	}

	public static Launcher get() {
		if (null == instance) instance = new Launcher();
		return instance;
	}
}
