package com.baseoneonline.tilecutter.core;

public interface CutModelListener {

	/**
	 * Invoked when the tile size, spacing, etc have changed, tiles will be
	 * processed and updated later.
	 */
	public void metricsChanged();

	/**
	 * Is invoked when a new source image has been loaded or the image has
	 * been removed
	 */
	public void imageChanged();
	
	/**
	 * Tiles have been cut and are ready for display or saving.
	 */
	public void tilesChanged();
}