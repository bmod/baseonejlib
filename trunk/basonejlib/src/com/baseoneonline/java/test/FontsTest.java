package com.baseoneonline.java.test;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.net.URL;

import javax.swing.JFrame;

public class FontsTest extends JFrame {

	public static void main(String[] args) {
		FontsTest app = new FontsTest();
		app.setSize(600,500);
		app.setDefaultCloseOperation(EXIT_ON_CLOSE);
		app.setVisible(true);
	}
	
	Font font;
	BufferedImage im;
	
	public FontsTest() {
		URL fontURL = getClass().getClassLoader().getResource(
				"com/baseoneonline/java/assets/DejaVuSerif.ttf");
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, fontURL.openStream());
		//	font = font.deriveFont(20);
			im = new BufferedImage(1024, 1024,BufferedImage.TYPE_INT_ARGB);
			
			
			
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setFont(font);
		g.scale(100, 100);
		g.drawString("Hello DejaVu", 5, 5);
	}

}
