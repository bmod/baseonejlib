package com.baseoneonline.java.slickTest;

import java.awt.Font;
import java.io.File;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

public class SlickTest implements Game {

	public static void main(String[] args) {
		try {
			AppGameContainer container = new AppGameContainer(new SlickTest());
			container.setMinimumLogicUpdateInterval(20);
			container.setDisplayMode(800, 600, false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public boolean closeRequested() {
		return true;
	}

	public String getTitle() {
		return "SlickTest";
	}

	Font font;
	TrueTypeFont ttf;
	File[] files;

	public void init(GameContainer gc) throws SlickException {
		
		files = new File("C:/Documents and Settings/bask/Desktop/lwjgl-2.0rc2/lwjgl-2.0rc2/jar").listFiles();
		font = new Font("Verdana", Font.BOLD, 20);
		ttf = new TrueTypeFont(font, true);
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		float spacing = 30;
		for (int i=0; i<files.length; i++) {
			String n = files[i].getName();
			ttf.drawString(30f, 30f+(spacing*i), n);
		}
	}

	public void update(GameContainer gc, int t) throws SlickException {
		
	}
}
