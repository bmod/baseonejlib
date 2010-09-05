package com.baseoneonline.java.mediabrowser.core;

import java.util.ArrayList;

public interface DataView {

	public ArrayList<? extends MediaNode> getChildren(MediaNode parent);

	public MediaNode getRoot();

	public void addListener(DataViewListener l);

	public void removeListener(DataViewListener l);

}
