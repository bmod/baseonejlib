package com.baseoneonline.java.jme.swingtests;

import javax.swing.JFrame;

import com.jme.system.canvas.JMECanvasImplementor;

public class SwingTest extends JFrame {
	public static void main(final String[] args) {
		final SwingTest app = new SwingTest();
		app.setTitle("SwingTest");
		app.setSize(500, 400);
		app.setVisible(true);
	}

	public SwingTest() {
	}

}

class CanvasImplementation extends JMECanvasImplementor {

	@Override
	public void doRender() {
	// TODO Auto-generated method stub

	}

	@Override
	public void doUpdate() {
	// TODO Auto-generated method stub

	}

}