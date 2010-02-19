package com.baseoneonline.java.tools;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class MayaLink {

	public enum ReturnType {
		StringArray, String, Void
	}

	private static HashMap<ReturnType, String> returnTypeMap;

	private static MayaLink instance;

	private final int port = 20124;

	private static final String RE_ERROR = "// ERROR:";
	private static final String RE_WARN = "// WARNING:";

	private Socket sock;
	private InetSocketAddress host;
	private PrintWriter writer;

	public MayaLink(final int port) {
		host = new InetSocketAddress("localhost", port);
		returnTypeMap = getReturnTypeMap();
	}

	private static HashMap<ReturnType, String> getReturnTypeMap() {
		final HashMap<ReturnType, String> map = new HashMap<ReturnType, String>();
		map.put(ReturnType.Void, "");
		map.put(ReturnType.StringArray, "string[]");
		map.put(ReturnType.String, "string");
		return map;
	}

	public int getPort() {
		return port;
	}

	public void setPort(final int port) {
		disconnect();
		host = new InetSocketAddress(host.getHostName(), port);
	}

	public String send(final String cmd) {
		connect();
		getPrintWriter().print(cmd);
		getPrintWriter().flush();
		final String re = readFromSocket();
		if (null != re) return re.trim();
		return null;
	}

	private void disconnect() {
		if (null == sock) return;
		if (sock.isConnected()) {
			try {
				sock.close();
			} catch (final IOException e) {
				// Don't care
			}
		}
	}

	private String readFromSocket() {
		try {
			final InputStreamReader reader = new InputStreamReader(sock
					.getInputStream());
			final StringBuffer buf = new StringBuffer();
			int c;
			while (0 != (c = reader.read())) {
				buf.append((char) c);
			}
			final String re = buf.toString();
			if (re.startsWith(RE_ERROR)) {
				// Logger.getLogger(getClass().getName()).severe(
				// "Maya says: " + re);
			} else if (re.startsWith(RE_WARN)) {
				// Logger.getLogger(getClass().getName()).warning(
				// "Maya says: " + re);
			} else {
				return re;
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	private PrintWriter getPrintWriter() {
		try {
			if (null == writer) {
				writer = new PrintWriter(new OutputStreamWriter(sock
						.getOutputStream()));
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return writer;
	}

	private void connect() {
		if (null == sock) {
			sock = new Socket();
		}
		if (!sock.isConnected()) {
			try {
				sock.connect(host);
			} catch (final IOException e) {
				JOptionPane.showMessageDialog(null, "Error connecting to maya, try: commandPort -n \"localhost:"
								+ port + "\";");
			}
		}
	}

}
