package com.baseoneonline.java.test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class TestMayaCommander {
	public static void main(String[] args) {
		new TestMayaCommander();
	}

	Connector	con;

	public TestMayaCommander() {
		con = new Connector();
		con.sendProc("return `ls -type camera`;");
		System.out.println("Result: "+con.receive());
	}

}

class Connector {

	private final int	port	= 20123;
	Socket				sock;
	private static char END_OF_STREAM = "\n".charAt(0);
	
	
	public Connector() {

	}
	
	public void sendProc(String string) {
		StringBuffer buf = new StringBuffer();
		buf.append("proc string[] myProc() { ");
		buf.append(string.replaceAll("'", "\""));
		buf.append(" } myProc();");
		send(buf.toString());
	}

	public void send(String string) {
		Logger.getLogger(getClass().getName()).info("Sending...");
		open();
		try {
			OutputStreamWriter writer =
				new OutputStreamWriter(sock.getOutputStream());
			writer.write(string);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String receive() {
		Logger.getLogger(getClass().getName()).info("Receiving...");
		open();
		try {
			InputStreamReader reader =
				new InputStreamReader(sock.getInputStream());
			StringBuffer buf = new StringBuffer();
			int c;
			while ((c = reader.read()) != END_OF_STREAM) {
				buf.append((char) c);
			}
			return buf.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void open() {
		if (null == sock) {
			try {
				sock = new Socket("localhost", port);
				Logger.getLogger(getClass().getName()).info(
					"Connected to maya on port " + port);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (sock.isConnected()) {
			System.out.println("SOCK IS CONNECTED");
		} else if (sock.isClosed()) {
			System.out.println("Sock is closed");

		}
	}

}