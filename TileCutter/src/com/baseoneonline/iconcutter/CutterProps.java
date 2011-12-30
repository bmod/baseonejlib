package com.baseoneonline.iconcutter;

public class CutterProps {
	private int xOffset = 4;
	private int yOffset = 4;
	private int tileSizeX = 16;
	private int tileSizeY = 16;
	private int countX = 8;
	private int countY = 8;
	private int spacingX = 3;
	private int spacingY = 3;

	private String filenamePrefix = "tile_";

	public void setFilenamePrefix(final String filenamePrefix) {
		this.filenamePrefix = filenamePrefix;
	}

	public String getFilenamePrefix() {
		return filenamePrefix;
	}

	public int getSpacingX() {
		return spacingX;
	}

	public int getSpacingY() {
		return spacingY;
	}

	public void setSpacingX(final int spacingX) {
		this.spacingX = spacingX;
	}

	public void setSpacingY(final int spacingY) {
		this.spacingY = spacingY;
	}

	public void setxOffset(final int xOffset) {
		this.xOffset = xOffset;
	}

	public int getxOffset() {
		return xOffset;
	}

	public void setyOffset(final int yOffset) {
		this.yOffset = yOffset;
	}

	public int getyOffset() {
		return yOffset;
	}

	public void setTileSizeX(final int tileSizeX) {
		this.tileSizeX = tileSizeX;
	}

	public int getTileSizeX() {
		return tileSizeX;
	}

	public void setTileSizeY(final int tileSizeY) {
		this.tileSizeY = tileSizeY;
	}

	public int getTileSizeY() {
		return tileSizeY;
	}

	public int getCountX() {
		return countX;
	}

	public void setCountX(final int countX) {
		this.countX = countX;
	}

	public void setCountY(final int countY) {
		this.countY = countY;
	}

	public int getCountY() {
		return countY;
	}
}
