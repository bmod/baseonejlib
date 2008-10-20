package com.baseoneonline.java.blips;

import com.jme.input.MouseInput;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.system.GameSettings;

public class TestBlips extends BasicFixedRateGame {
	
	public static void main(String[] args) {
		TestBlips app = new TestBlips();
		app.start();

	}

	private PlayerNode player;


	@Override
	protected void cleanup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initGame() {
		
		MouseInput.get().setCursorVisible(true);
		
		player = new PlayerNode();
		
		//.attachChild(player);
	}


	

	@Override
	protected void update() {
		if (MouseInput.get().isButtonDown(0)) {
			Vector2f mousePos = new Vector2f(MouseInput.get().getXAbsolute(), MouseInput.get().getYAbsolute());
			Vector3f mousePos3D = display.getWorldCoordinates(mousePos, 0);
			
			Vector3f dif = mousePos3D.subtract(player.getWorldTranslation());
			
			//float angle = player.getWorldTranslation().angleBetween(mousePos3D);
			System.out.println(dif);
			player.getVelocity().addLocal(dif.normalize());
			
		}
		
		player.update();
	}


	@Override
	protected GameSettings getNewSettings() {
		// TODO Auto-generated method stub
		return null;
	}


}
