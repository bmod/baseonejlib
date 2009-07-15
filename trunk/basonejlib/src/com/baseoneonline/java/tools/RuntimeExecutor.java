package com.baseoneonline.java.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class RuntimeExecutor {

	public static void exec(final String cmd, final OutputStream os) {
		try {

			final Process proc = new ProcessBuilder(cmd).start();
			// any error message?
			final StreamGobbler errorGobbler = new StreamGobbler(proc
					.getErrorStream(), "ERROR", os);

			// any output?
			final StreamGobbler outputGobbler = new StreamGobbler(proc
					.getInputStream(), "OUTPUT", os);

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			// any error???
			final int exitVal = proc.waitFor();
			System.out.println("ExitValue: " + exitVal);
			os.flush();
			os.close();
		} catch (final Throwable t) {
			t.printStackTrace();
		}
	}
}

class StreamGobbler extends Thread {

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
		this.os = redirect;
	}

	@Override
	public void run() {
		try {
			PrintWriter pw = null;
			if (os != null) pw = new PrintWriter(os);

			final InputStreamReader isr = new InputStreamReader(is);
			final BufferedReader br = new BufferedReader(isr);
			System.out.println(type + ">");
			String line = null;
			while ((line = br.readLine()) != null) {
				if (pw != null) pw.println(line);
			}
			if (pw != null) pw.flush();
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
	}
}