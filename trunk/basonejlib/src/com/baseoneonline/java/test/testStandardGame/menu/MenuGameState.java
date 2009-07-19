package com.baseoneonline.java.test.testStandardGame.menu;

import com.baseoneonline.java.test.testStandardGame.global.GlobalCommands;
import com.baseoneonline.java.test.testStandardGame.global.GlobalController.Command;
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

		final SelectionListModel<Command> model = new SelectionListModel<Command>();

		model.addItem(GlobalCommands.EXIT_APP);
		model.addItem(GlobalCommands.LEVEL_SELECT);

		model.setSelectedIndex(0);

		menuNode = new ElasticMenu(model);
		rootNode.attachChild(menuNode);

		// Enable input for menu node
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
