package com.baseoneonline.java.tools;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.baseoneonline.java.swing.config.Config;

public class ResourceEditor extends JFrame {
	public static void main(String[] args) {
		Config.setApplicationClass(ResourceEditor.class);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		new ResourceEditor().setVisible(true);
	}

	public ResourceEditor() {
		Config.get().persist(this);
	}
}
