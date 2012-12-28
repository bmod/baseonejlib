package com.baseoneonline.jlib.ardor3d;

import com.ardor3d.input.logical.LogicalLayer;
import com.ardor3d.renderer.Camera;

public interface IGame {

	Camera getCamera();

	LogicalLayer getLogicalLayer();

}
