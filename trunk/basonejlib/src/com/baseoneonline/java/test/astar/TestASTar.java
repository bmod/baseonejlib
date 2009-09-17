package com.baseoneonline.java.test.astar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestASTar extends JFrame {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final TestASTar app = new TestASTar();
		app.setSize(800, 600);
		app.setDefaultCloseOperation(EXIT_ON_CLOSE);
		app.setVisible(true);
	}

	public TestASTar() {
		final Graph graph = new Graph();
		graph.init(30, 20);
		final GraphView view = new GraphView(graph);
		add(view);
	}

}

class GraphView extends JPanel {

	private final Graph graph;
	private final int tileSize = 10;
	private final int tileSpacing = 1;
	private final int space = tileSize + tileSpacing;

	private final Color col_bg = new Color(.9f, .9f, .9f);
	private final Color col_wall = new Color(.5f, .5f, .5f);

	private final Color col_start = Color.ORANGE;
	private final Color col_goal = Color.GREEN;

	private final int cellMargin = 1;

	private final Vec2i start = new Vec2i();
	private final Vec2i goal = new Vec2i();

	public GraphView(final Graph g) {
		this.graph = g;

		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
	}

	private final MouseAdapter mouseAdapter = new MouseAdapter() {

		int button;

		@Override
		public void mousePressed(final MouseEvent e) {
			button = e.getButton();
			mouseDragged(e);
		}

		@Override
		public void mouseDragged(final MouseEvent e) {

			final Vec2i pos = toGrid(e.getX(), e.getY());
			switch (button) {
			case MouseEvent.BUTTON1:
				start.set(pos);
				break;
			case MouseEvent.BUTTON2:
				System.out.println(2);
				break;
			case MouseEvent.BUTTON3:
				goal.set(pos);
				break;

			default:
				break;
			}
			repaint();
		}
	};

	private Vec2i toGrid(final int x, final int y) {
		return new Vec2i(x / space, y / space);
	}

	@Override
	public void paint(final Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());

		for (int x = 0; x < graph.getW(); x++) {
			for (int y = 0; y < graph.getH(); y++) {
				if (graph.get(x, y) == 0) {
					g.setColor(col_bg);
				} else {
					g.setColor(col_wall);
				}
				g.fillRect(x * space, y * space, tileSize, tileSize);
			}
		}

		g.setColor(Color.ORANGE);
		g.fillOval((start.x * space) + cellMargin, (start.y * space)
				+ cellMargin, tileSize - cellMargin * 2, tileSize - cellMargin
				* 2);
		g.setColor(Color.GREEN);
		g.fillOval((goal.x * space) + cellMargin,
				(goal.y * space) + cellMargin, tileSize - cellMargin * 2,
				tileSize - cellMargin * 2);

	}

}
