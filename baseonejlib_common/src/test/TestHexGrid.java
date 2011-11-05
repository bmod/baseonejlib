package test;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.baseoneonline.java.swing.config.Config;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.nodes.PPath;

public class TestHexGrid extends JFrame {
	public static void main(final String[] args) {
		final TestHexGrid app = new TestHexGrid();
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				app.setVisible(true);
			}
		});
	}

	public TestHexGrid() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(winAdapter);
		initComponents();
		setSize(300, 300);
		Config.setApplicationClass(getClass());
		Config.get().persist(this);
		// createGrid();
		final double radius = 10;
		generateGrid(4, radius);
	}

	private void createGrid() {
		final PPath hex = new Hexagon(40);
		pCanvas.getLayer().addChild(hex);
	}

	private void generateGrid(final int iter, final double radius) {
		final int[] xyz = { 0, 0, 0 };
		final int[][] deltas = { { 1, 0, -1 }, { 0, 1, -1 }, { -1, 1, 0 },
				{ -1, 0, 1 }, { 0, -1, 1 }, { 1, -1, 0 } };
		for (int i = 0; i < iter; i++) {
			int x = xyz[0];
			int y = xyz[1] - i;
			int z = xyz[2] + i;
			for (int j = 0; j < 6; j++)
				for (int k = 0; k < i; k++) {
					x = x + deltas[j][0];
					y = y + deltas[j][1];
					z = z + deltas[j][2];
					final int[] coords = { x, y, z };
					placeTile(coords, radius);
				}
		}
	}

	private void placeTile(final int[] coords, final double radius) {
		final Point2D pt = IsoHexGrid.hexToCart(coords[1], coords[2],
				radius * 2);
		final Hexagon hx = new Hexagon(radius + 4);
		hx.translate(pt.getX(), pt.getY());
		pCanvas.getLayer().addChild(hx);
	}

	private final WindowAdapter winAdapter = new WindowAdapter() {
		@Override
		public void windowClosing(final java.awt.event.WindowEvent e) {
			Config.get().flush();
			setVisible(false);
			dispose();
		};
	};
	private PCanvas pCanvas;

	private void initComponents() {
		setLayout(new BorderLayout());
		pCanvas = new PCanvas();
		add(pCanvas);
	}

}

class IsoHexGrid {

	private static double sqrt3 = Math.sqrt(3);

	private IsoHexGrid() {
	}

	public static Point2D hexToCart(final int x, final int y,
			final double radius) {
		final double tx = sqrt3 * radius * (y / 2 + x);
		final double ty = 1.5 * radius * y;
		return new Point2D.Double(tx, ty);
	}

	public static Point2D cartToHex(final double x, final double y,
			final double radius) {
		return null;
	}

}

class Hexagon extends PPath {

	public static final double AX = 0.5;
	public static final double AY = Math.sin(Math.PI / 3);
	private static Point2D[] points;
	static {
		points = new Point2D[6];
		points[0] = new Point2D.Double(-1, 0);
		points[1] = new Point2D.Double(-AX, AY);
		points[2] = new Point2D.Double(AX, AY);
		points[3] = new Point2D.Double(1, 0);
		points[4] = new Point2D.Double(AX, -AY);
		points[5] = new Point2D.Double(-AX, -AY);
	}

	public Hexagon(final double radius) {
		final Point2D[] pts = new Point2D[points.length];
		// Calculate angled corners
		// Start left, ccw
		for (int i = 0; i < points.length; i++)
			pts[i] = new Point2D.Double(points[i].getX() * radius,
					points[i].getY() * radius);

		setPathToPolyline(pts);
		closePath();
	}
}