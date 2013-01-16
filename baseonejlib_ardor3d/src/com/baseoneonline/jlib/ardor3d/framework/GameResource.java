package com.baseoneonline.jlib.ardor3d.framework;

import java.io.IOException;

import com.ardor3d.util.export.InputCapsule;
import com.ardor3d.util.export.OutputCapsule;
import com.ardor3d.util.export.Savable;

public class GameResource implements Savable {

	@Override
	public void write(final OutputCapsule capsule) throws IOException {
	}

	@Override
	public void read(final InputCapsule capsule) throws IOException {
	}

	@Override
	public Class<?> getClassTag() {
		return getClass();
	}

}
