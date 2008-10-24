package com.baseoneonline.java.jme.menuTest;

import java.awt.Color;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.input.MouseInput;
import com.jme.math.Vector3f;
import com.jme.scene.BillboardNode;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;

public class TextLabel2DTest extends SimpleGame {
	public static void main(String[] args) {
		TextLabel2DTest game = new TextLabel2DTest();
		game.start();
	}
	@Override
	protected void simpleInitGame() {
		MouseInput.get().setCursorVisible(true);
		input.setEnabled(false);
		
		
		
	}
	
	
	private void createBoxes() {
		for (int i = 0; i < 5; i++) {
			Node n = new Node("Node " +i);
			Box b = new Box("b" +i, new Vector3f(), 0.5f, 0.2f, 0.5f);
			b.setModelBound(new BoundingBox());
			b.updateModelBound();
			n.attachChild(b);
			rootNode.attachChild(n);
			
			TextLabel2D label = new TextLabel2D("Box:" +i);
			label.setBackground(Color.blue);
			BillboardNode bNode = label.getBillboard(0.5f);
			bNode.getLocalTranslation().y += 1;
			n.attachChild(bNode);
			n.setLocalTranslation(i, i, i);
		}
	}
}