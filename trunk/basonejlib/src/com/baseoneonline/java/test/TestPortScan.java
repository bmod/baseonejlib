package com.baseoneonline.java.test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestPortScan {

	String host = "212.182.181.214";
	int[] ports = { 21, 80, 8080, 20124 };

	public static void main(String[] args) {
		new TestPortScan();
	}

	public TestPortScan() {
		for (int p : ports) {
			testPort(p);
		}
	}

	private void testPort(int port) {
		System.out.println("Testing port: " + port);

		try {
			Socket sock = new Socket(host, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
