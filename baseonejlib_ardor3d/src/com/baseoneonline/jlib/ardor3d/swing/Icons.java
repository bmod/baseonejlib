package com.baseoneonline.jlib.ardor3d.swing;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public final class Icons {

	private Icons() {
	}

	public static Icon load(String resource) {
		java.net.URL imgURL = Icons.class.getClassLoader()
				.getResource(resource);
		if (imgURL == null)
			throw new RuntimeException("Icon not found: " + resource);
		return new ImageIcon(imgURL);
	}
}
