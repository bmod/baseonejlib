package com.baseoneonline.jlib.ardor3d;

import com.ardor3d.renderer.Renderer;
import com.ardor3d.scenegraph.Node;

public interface IGameContainer {

	public void init(IGame game);

	public void update(double t);

	public void postUpdate(double t);

	public void render(Renderer r);

	public Node getSceneRoot();

}
