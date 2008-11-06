package com.baseoneonline.java.jme.menuTest;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;


public class DirectoryModel implements MenuModel<File> {
	private final Logger log = Logger.getLogger(getClass().getName());

	private File dir;

	
	
	private final ArrayList<File> files = new ArrayList<File>();

	private int selectedIndex;

	public DirectoryModel(final File root) {
		
		setDirectory(root);
	}


	public void setDirectory(final File d) {
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
		final File parent = dir.getParentFile();
		if (null != parent) {
			files.add(parent);
		}
		for (final File f : dir.listFiles()) {
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
	
	public void changeDirectory(final File dir) {
	}
	
	public File getSelectedItem() {
		return files.get(selectedIndex);
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(final int index) {
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






	public File get(final int index) {
		return files.get(index);
	}



	public void add(final File element) {
		// TODO Auto-generated method stub

	}



	public String getLabel(final File element) {
		return element.getName();
	}

}
