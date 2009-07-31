package com.baseoneonline.java.test;

import com.baseoneonline.java.tools.MayaCommunicator;
import com.baseoneonline.java.tools.MayaCommunicator.Command;

public class TestMayaCommander {
	public static void main(String[] args) {
		new TestMayaCommander();
	}

	public TestMayaCommander() {
		// MayaCommunicator.get().sendCommand("print \"Hello world!\"\\n");
		String[] re =
			CmdGetCameras.send();
		System.out.println(re);
	}

	Command<String[]> CmdGetCameras = new MayaCommunicator.Command<String[]>() {
		@Override
		public String getCommand() {
			//return asProc("return `ls -type camera`");
			return null;
		}
	};

}
