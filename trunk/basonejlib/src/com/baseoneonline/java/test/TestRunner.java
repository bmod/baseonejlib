package com.baseoneonline.java.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class TestRunner {

	public static void main(final String[] args) {
		runRunner();
	}

	private static void testRun() {
		try {
			String line;
			final Process p = Runtime.getRuntime().exec(
					System.getenv("windir") + "\\system32\\" + "tree.com /A");
			final BufferedReader input = new BufferedReader(
					new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				System.out.println(line);
			}
			input.close();
		} catch (final Exception err) {
			err.printStackTrace();
		}
	}

	private static void runRunner() {
		final Runner runner = new Runner("test/potrace -h");
		runner.run();

	}
}

class Runner {

	Process proc;
	String command;

	public Runner(final String command) {
		this.command = command;
	}

	public void run() {
		Logger.getLogger(getClass().getName()).info("Exec command: " + command);
		try {
			proc = Runtime.getRuntime().exec(command);

			final BufferedReader rd = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));

			String line;
			final StringBuffer buf = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
			}

			rd.close();

		} catch (final IOException e) {
			java.util.logging.Logger.getLogger(Runner.class.getName()).warning(
					e.getMessage());
		}
	}
}

interface RunnerListener {

}

interface RunArg {

}
