package com.baseoneonline.java.test.testStandardGame.menu;

import com.baseoneonline.java.tools.SelectionListModel;
import com.jme.input.InputHandler;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameState;

public class MenuGameState extends GameState {

	Node rootNode;
	ElasticMenu menuNode;

	InputHandler handler;

	public MenuGameState() {
		rootNode = new Node();
		rootNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);

		final SelectionListModel<String> model = new SelectionListModel<String>();
		model.addItem("Base One");
		model.addItem("Tea for Two");
		model.addItem("Three Trees");
		model.addItem("Four You");
		model.addItem("Take Five");
		model.addItem("Sixth Sense");

		model.setSelectedIndex(3);

		menuNode = new ElasticMenu(model);
		rootNode.attachChild(menuNode);

		MenuInput.get().addListener(menuNode);
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
		rootNode.updateGeometricState(tpf, true);
	}

	public static interface MenuListener {

		void itemSelected(Object item);

		void itemSelectionConfirmed(Object item);
	}

}
