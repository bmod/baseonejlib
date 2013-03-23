package com.baseoneonline.jlib.ardor3d.tools;

import com.ardor3d.util.ReadOnlyTimer;
import com.baseoneonline.java.swing.config.Config;
import com.baseoneonline.jlib.ardor3d.GameBase;

public class ModelViewer extends GameBase {

	public static void main(String[] args) {
		Config.setApplicationClass(ModelViewer.class);
		new ModelViewer().start();
	}

	@Override
	protected void init() {
		ToolsWindow.show(root);
	}

	@Override
	protected void update(ReadOnlyTimer timer) {
	}

}
