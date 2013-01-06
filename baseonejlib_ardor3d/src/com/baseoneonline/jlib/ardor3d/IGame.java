package com.baseoneonline.jlib.ardor3d;

import com.ardor3d.input.logical.LogicalLayer;
import com.ardor3d.renderer.Camera;
import com.baseoneonline.jlib.ardor3d.jbullet.PhysicsWorld;

public interface IGame {

	Camera getMainCamera();

	LogicalLayer getLogicalLayer();

	PhysicsWorld getPhysicsWorld();
}
