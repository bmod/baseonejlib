package com.baseoneonline.java.jmeTest;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;


public class DirectoryModel {
	private final Logger log = Logger.getLogger(getClass().getName());
	
	private File dir;
	
	private final ArrayList<File> files = new ArrayList<File>();
	
	private int selectedIndex;
	
	public DirectoryModel(File startDir) {
		setDirectory(startDir);
	}
	
	
	public void setDirectory(File d) {
		if (!d.exists()) {
			log.severe("Directory does not exist: "+d.getAbsolutePath());
			return;
		}
		if (!d.isDirectory()) {
			log.severe("Is not a directory: "+d.getAbsolutePath());
			return;
		}
		
		dir = d;
		files.clear();
		File parent = dir.getParentFile();
		if (null != parent) {
			files.add(parent);
		}
		for (File f : dir.listFiles()) {
			files.add(f);
		}

		
		if (files.size() > 0) {
			if (files.size() > 1) {
				selectedIndex = 1;
			} else {
				selectedIndex = 0;
			}
		} else {
			selectedIndex = -1;
		}
		
	}
	
	public int getSelectedIndex() {
		return selectedIndex;
	}
	
	public void setSelectedIndex(int index) {
		selectedIndex = index;
	}
	
	public File[] getFiles() {
		return files.toArray(new File[files.size()]);
	}
	
	public File getDirectory() {
		return dir;
	}
	
	public int size() {
		if (null != files) {
			return files.size();
		}
		return 0;
	}
	
}
