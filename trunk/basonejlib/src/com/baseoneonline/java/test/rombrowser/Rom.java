package com.baseoneonline.java.rombrowser;
import java.io.File;
import java.io.Serializable;

import com.baseoneonline.java.tools.StringUtils;

public class Rom implements Serializable {

	private final File file;
	private final GameSystem system;

	public Rom(final GameSystem system, final File f) {
		this.file = f;
		this.system = system;
	}

	public GameSystem getSystem() {
		return system;
	}

	public File getFile() {
		return file;
	}

	@Override
	public String toString() {
		return StringUtils.stripExtension(file.getName());
	}

	public String getTitle() {
		return file.getName();
	}

}
