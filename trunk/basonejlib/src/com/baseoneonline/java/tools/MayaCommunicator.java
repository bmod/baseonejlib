package com.baseoneonline.java.tools;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Logger;

public class MayaCommunicator {

	public enum ReturnType {
		StringArray, String, Void
	}

	private static HashMap<ReturnType, String> returnTypeMap;

	private static MayaCommunicator instance;

	private final int port = 20124;

	private final Logger log;;
	
	private static final String RE_ERROR = "// ERROR:";

	private Socket sock;
	private InetSocketAddress host;
	private PrintWriter writer;

	private MayaCommunicator() {
		log = Logger.getLogger(getClass().getName());
		host = new InetSocketAddress("localhost", 20124);
		returnTypeMap = getReturnTypeMap();
	}

	private static HashMap<ReturnType, String> getReturnTypeMap() {
		HashMap<ReturnType, String> map = new HashMap<ReturnType, String>();
		map.put(ReturnType.Void, "");
		map.put(ReturnType.StringArray, "string[]");
		map.put(ReturnType.String, "string");
		return map;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		disconnect();
		host = new InetSocketAddress(host.getHostName(), port);
	}

	public void sendCommand(String cmd) {
		connect();
		log.info("Sending command: " + cmd);
		getPrintWriter().print(cmd);
		getPrintWriter().flush();
	}
	
	public Object sendCommand(Command<?> cmd) {
		sendCommand(cmd.getCommand());
		return readFromSocket();
	}
	
	public String sendAsProc(String cmd, ReturnType retype) {
		sendCommand(asProc(cmd, retype));
		return readFromSocket();
	}

	private void disconnect() {
		if (null == sock)
			return;
		if (sock.isConnected()) {
			try {
				sock.close();
			} catch (IOException e) {
				// Don't care
			}
		}
	}

	private String readFromSocket() {
		try {
			InputStreamReader reader =
				new InputStreamReader(sock.getInputStream());
			StringBuffer buf = new StringBuffer();
			int c;
			while (0 != (c = reader.read())) {
				buf.append((char) c);
			}
			String re = buf.toString();
			if (re.startsWith(RE_ERROR)) {
				throw new UnknownError("Maya says \""+re+"\"");
			}
			return re;
		} catch (IOException e) {
			log.warning("Could not get inputstream");
		}
		return null;
	}

	private PrintWriter getPrintWriter() {
		try {
			if (null == writer) {
				writer =
					new PrintWriter(new OutputStreamWriter(sock
							.getOutputStream()));
			}
		} catch (IOException e) {
			log.warning("Could not get outputstream.");
		}
		return writer;
	}

	private void connect() {
		if (null == sock) {
			sock = new Socket();
		}
		if (!sock.isConnected()) {
			log.info("Connecting to maya on " + host);
			try {
				sock.connect(host);
			} catch (IOException e) {
				log.warning("Could not connect to " + host);
			}
		}
	}

	public static MayaCommunicator get() {
		if (null == instance)
			instance = new MayaCommunicator();
		return instance;
	}
	
	public static String asProc(String cmd, ReturnType retype) {
		return "proc " + returnTypeMap.get(retype) + " temp() {" + cmd
		+ ";} temp();";
	}
	
	public static abstract class Command<T> {
		
		public abstract String getCommand();

		@SuppressWarnings("unchecked")
		public T send() {
			Object re = MayaCommunicator.get().sendCommand(this);
			if (null == re) return null;
			if (re.getClass() == String[].class) {
				return (T) ((String)re).split("\t");
			}
			if (re.getClass() == String.class) {
				return (T) re;
			}
			return null;
		}
		
		

	}

}
