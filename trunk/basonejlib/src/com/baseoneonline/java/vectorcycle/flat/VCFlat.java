package com.baseoneonline.java.vectorcycle.flat;

import java.awt.Font;

import com.baseoneonline.java.blips.FollowCam;
import com.baseoneonline.java.jme.BasicFixedRateGame;
import com.baseoneonline.java.jme.menuTest.text.TTFont;
import com.baseoneonline.java.jme.menuTest.text.TextNode;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;

public class VCFlat extends BasicFixedRateGame implements Command {

	public static void main(final String[] args) {
		final VCFlat app = new VCFlat();
		app.start();
	}
	
	private FollowCam followCam;
	private Player player;
	private Road road;
	
	private StatsNode statNode;
	
	@Override
	protected void initFixedRateGame() {
		MouseInput.get().setCursorVisible(true);
		
		player = new Player();
		rootNode.attachChild(player);
		
		followCam = new FollowCam(cam, player);
		followCam.setDistance(5);
		
		road = new Road();
		
		statNode = new StatsNode();
		statNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		rootNode.attachChild(statNode);
		
		key.add(ACCELLERATE, KeyInput.KEY_W);
		key.add(BRAKE, KeyInput.KEY_S);
		key.add(LEFT, KeyInput.KEY_A);
		key.add(RIGHT, KeyInput.KEY_D);
		

	}

	@Override
	protected void update() {
		
		if (key.isValidCommand(ACCELLERATE)) {
			player.accellerate();
		}
		if (key.isValidCommand(BRAKE)) {
			player.brake();
		}
		if (key.isValidCommand(LEFT, false)) {
			player.left();
		}
		if (key.isValidCommand(RIGHT, false)) {
			player.right();
		}
		
		player.setLocalTranslation(road.translatePosition(player.getPosition()));
		followCam.update();
	}

}


class StatsNode extends Node {
	
	private final TTFont font = new TTFont(new Font("sans", Font.PLAIN, 20));
	public StatsNode() {
		final TextNode text = new TextNode(font);
		text.setText("Braap");
		attachChild(text);
		updateRenderState();
	}
}

interface Command {
	public static String ACCELLERATE = "accellerate";
	public static String BRAKE = "brake";
	public static String LEFT = "left";
	public static String RIGHT = "right";
}

class Biker extends Node {

	
	private Vector2f position = new Vector2f();
	
	public void accellerate() {
		
	}
	
	public void brake() {
		
	}
	
	public void left() {
		
	}
	
	public void right() {
		
	}
	
	public void setPosition(final Vector2f pos) {
		position = pos;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
}

class Player extends Biker {
	public Player() {
		final Box b = new Box("player", new Vector3f(), 1f,1f,1f);
		attachChild(b);
		
	}
}

class Road extends Node {
	
	public Road() {
		
	}
	
	public Vector3f translatePosition(final Vector2f pos) {
		return new Vector3f(pos.x, 0, pos.y);
	}
	
	
}