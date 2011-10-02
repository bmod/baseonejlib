package com.baseoneonline.java.applet;

import java.applet.Applet;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class Paint extends Applet {


	private Point currPos;
	private Point prevPos;



	private int paintUpdates = 0;
	private int mouseUpdates = 0;

	private final Stroke currentStroke = new BasicStroke(100,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private final Color currentColor = new Color(.4f,.5f,.8f,.1f);
	private BufferedImage image;
	private Graphics2D graphics;

	private final List<Line2D> pointList = new LinkedList<Line2D>();

	@Override
	public void init() {

		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		image = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_INT_RGB);
		graphics = image.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, getWidth(), getHeight());

		repaint();
	}

	@Override
	public void update(final Graphics g) {
		// g.setColor(getBackground());
		// g.fillRect(0,0, getWidth(), getHeight());
		g.drawImage(image, 0, 0, null);
		drawDebug(g);

		graphics.setColor(currentColor);
		graphics.setStroke(currentStroke);
		while (pointList.size() > 0) {
			graphics.draw(pointList.remove(0));
		}

		paintUpdates++;
	}

	private void drawDebug(final Graphics g) {
		g.drawString("PaintUpdates: " + paintUpdates, 10, 10);
		g.drawString("MouseUpdates: " + mouseUpdates, 10, 30);
	}

	private void paintLine(final Point p1, final Point p2) {
		pointList.add(new Line2D.Double(p1, p2));

	}

	private final MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mousePressed(final MouseEvent e) {
			
			currPos = e.getPoint();
		}

		@Override
		public void mouseDragged(final MouseEvent e) {
			prevPos = currPos;
			currPos = e.getPoint();
			paintLine(prevPos, currPos);
			mouseUpdates++;
			repaint();
		}

		@Override
		public void mouseReleased(final MouseEvent e) {
			
		}
	};

}
