package com.baseoneonline.jlib.ardor3d;

import com.ardor3d.renderer.Renderer;

public interface IGameContainer {

	public void init(IGame game);

	public void update(double t);

	public void render(Renderer r);

}
