package com.baseoneonline.java.test.testStandardGame.global;

import com.baseoneonline.java.test.testStandardGame.global.GlobalController.Command;

public class GlobalCommands {

	private GlobalCommands() {}

	public static Command EXIT_APP = new Command("Exit");
	public static Command LEVEL_SELECT = new Command("Level Select");
}
