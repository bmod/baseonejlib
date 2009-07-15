package com.baseoneonline.java.test.testStandardGame.gameStates;

import com.baseoneonline.java.test.testStandardGame.nodes.MenuNode;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameState;

public class MenuState extends GameState {

	Node rootNode;
	MenuNode menuNode;

	public MenuState() {
		rootNode = new Node();
		rootNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);

		menuNode = new MenuNode();
		rootNode.attachChild(menuNode);

	}

	@Override
	public void cleanup() {

	}

	@Override
	public void render(final float tpf) {

		DisplaySystem.getDisplaySystem().getRenderer().draw(rootNode);

	}

	@Override
	public void update(final float tpf) {

	}

	public static interface MenuListener {

		void itemSelected(Object item);

		void itemSelectionConfirmed(Object item);
	}

}
