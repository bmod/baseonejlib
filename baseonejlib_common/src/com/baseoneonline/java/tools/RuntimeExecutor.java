package com.baseoneonline.java.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Logger;

public class RuntimeExecutor {

	private static Logger LOG = Logger.getLogger(RuntimeExecutor.class
			.getName());

	public static void exec(final String cmd, final OutputStream os) {
		LOG.info("RUN: " + cmd);
		try {

			final Process proc = new ProcessBuilder(cmd).start();
			// any error message?
			final StreamGobbler errorGobbler = new StreamGobbler(
					proc.getErrorStream(), "ERROR", os);

			// any output?
			final StreamGobbler outputGobbler = new StreamGobbler(
					proc.getInputStream(), "OUTPUT", os);

			// kick them off
			outputGobbler.start();
			errorGobbler.start();

			// any error???
			final int exitVal = proc.waitFor();
			LOG.info("EXIT: " + exitVal);
			os.flush();
			os.close();
		} catch (final Throwable t) {
			t.printStackTrace();
		}
	}
}

class StreamGobbler extends Thread {

	private static Logger LOG = Logger.getLogger(StreamGobbler.class.getName());

	InputStream is;
	String type;
	OutputStream os;

	StreamGobbler(final InputStream is, final String type) {
		this(is, type, null);
	}

	StreamGobbler(final InputStream is, final String type,
			final OutputStream redirect) {
		this.is = is;
		this.type = type;
		os = redirect;
	}

	@Override
	public void run() {
		try {
			PrintWriter pw = null;
			boolean hasRun = false;
			if (os != null)
				pw = new PrintWriter(os);

			final InputStreamReader isr = new InputStreamReader(is);
			final BufferedReader br = new BufferedReader(isr);

			if (pw != null) {
				int c;
				while (-1 != (c = br.read())) {
					if (!hasRun) {
						hasRun = true;
						LOG.info(type + ": ");
					}
					pw.append((char) c);
				}
				pw.flush();
			}
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
	}
}