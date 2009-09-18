package com.baseoneonline.java.test.astar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class GraphView extends JPanel {

	private final Graph graph;
	private final int tileSize = 10;
	private final int tileSpacing = 1;
	private final int space = tileSize + tileSpacing;

	private final Color col_bg = new Color(.9f, .9f, .9f);
	private final Color col_wall = new Color(.5f, .5f, .5f);

	private final Color col_start = Color.ORANGE;
	private final Color col_goal = Color.GREEN;
	private final Color col_mark = Color.BLUE;

	private final int cellMargin = 1;

	private final Vec2i start = new Vec2i();
	private final Vec2i goal = new Vec2i();

	private Vec2i[] markNodes;

	public GraphView(final Graph g) {
		this.graph = g;

		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
	}

	public Vec2i getStart() {
		return start;
	}

	public Vec2i getGoal() {
		return goal;
	}

	public void markNodes(Vec2i[] nodes) {
		this.markNodes = nodes;
		repaint();
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
			if (pos.x < 0)
				pos.x = 0;
			if (pos.x > graph.getW() - 1)
				pos.x = graph.getW() - 1;
			if (pos.y < 0)
				pos.y = 0;
			if (pos.y > graph.getH() - 1)
				pos.y = graph.getH() - 1;

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
			fireMapChanged();
		}
	};

	private void fireMapChanged() {
		firePropertyChange("mapChange", null, null);
	}

	private Vec2i toGrid(final int x, final int y) {
		return new Vec2i(x / space, y / space);
	}

	@Override
	public void paint(final Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());

		// Grid bg
		for (int x = 0; x < graph.getW(); x++) {
			for (int y = 0; y < graph.getH(); y++) {
				if (graph.getCost(x, y) == 0) {
					g.setColor(col_bg);
				} else {
					g.setColor(col_wall);
				}
				g.fillRect(x * space, y * space, tileSize, tileSize);
			}
		}

		// StartNode
		g.setColor(col_start);
		g.fillOval((start.x * space) + cellMargin, (start.y * space)
			+ cellMargin, tileSize - cellMargin * 2, tileSize - cellMargin * 2);

		// GoalNode
		g.setColor(col_goal);
		g.fillOval((goal.x * space) + cellMargin,
			(goal.y * space) + cellMargin, tileSize - cellMargin * 2, tileSize
				- cellMargin * 2);

		// Marked path
		if (null != markNodes) {
			g.setColor(col_mark);
			for (Vec2i m : markNodes) {
				g.drawRect(m.x * space, m.y * space, tileSize, tileSize);
			}
		}

	}

}
