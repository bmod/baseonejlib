package test;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.baseoneonline.java.swing.config.Config;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.nodes.PPath;

public class TestHexGrid extends JFrame
{
	public static void main(final String[] args)
	{
		final TestHexGrid app = new TestHexGrid();
		SwingUtilities.invokeLater(new Runnable()
		{

			@Override
			public void run()
			{
				app.setVisible(true);
			}
		});
	}

	public TestHexGrid()
	{
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(winAdapter);
		initComponents();
		setSize(300, 300);
		Config.setApplicationClass(getClass());
		Config.get().persist(this);
		// createGrid();
		final double radius = 10;
		generateGrid(3, radius);
	}

	private void createGrid()
	{
		final PPath hex = new Hexagon(40);
		pCanvas.getLayer().addChild(hex);
	}

	private void generateGrid(final int iter, final double radius)
	{
		int[] coords = new int[3];
		final int[] xyz =
		{ -2, 2, 0 };
		final int[][] deltas =
		{
		{ 1, 0, -1 },
		{ 0, 1, -1 },
		{ -1, 1, 0 },
		{ -1, 0, 1 },
		{ 0, -1, 1 },
		{ 1, -1, 0 } };
		for (int r = 0; r < iter; r++)
		{
			int x = 0;
			int y = -r;
			int z = r;
			int nh = 0;

			coords[0] = x;
			coords[1] = y;
			coords[2] = z;
			placeTile(coords, radius);

			for (int j = 0; j < 6; j++)
			{

				if (j == 5)
					nh = r - 1;
				else
					nh = r;
				for (int i = 0; i < nh; i++)
				{
					x += deltas[j][0];
					y += deltas[j][1];
					z += deltas[j][2];

					coords[0] = x;
					coords[1] = y;
					coords[2] = z;
					placeTile(coords, radius);
				}
			}
		}
	}

	private void placeTile(final int[] coords, final double radius)
	{
		final Point2D pt = IsoHexGrid.hexToCart(coords[0], coords[1],
				radius * 2);
		System.out.println(coords[0] + "\t" + coords[1] + "\t" + coords[2]);
		final Hexagon hx = new Hexagon(radius + 4);
		hx.translate(pt.getX(), pt.getY());
		pCanvas.getLayer().addChild(hx);
	}

	private final WindowAdapter winAdapter = new WindowAdapter()
	{
		@Override
		public void windowClosing(final java.awt.event.WindowEvent e)
		{
			Config.get().flush();
			setVisible(false);
			dispose();
		};
	};
	private PCanvas pCanvas;

	private void initComponents()
	{
		setLayout(new BorderLayout());
		pCanvas = new PCanvas();
		add(pCanvas);
	}

}

class IsoHexGrid
{

	private static double offXX = Hexagon.HEIGHT;
	private static double offYX = 1.5;
	private static double offXY = -Hexagon.HEIGHT;
	private static double offYY = 1.5;

	private IsoHexGrid()
	{
	}

	public static Point2D hexToCart(final int x, final int y,
			final double radius)
	{
		final double tx = (x * offXX) + (y * offXY);
		final double ty = (x * offYX) + (y * offYY);
		return new Point2D.Double(tx * radius, ty * radius);
	}

	public static Point2D cartToHex(final double x, final double y,
			final double radius)
	{
		return null;
	}

}

class Hexagon extends PPath
{

	public static final double HEIGHT = Math.sin(Math.PI / 3);
	private static Point2D[] points;
	static
	{
		points = new Point2D[6];
		points[0] = new Point2D.Double(0, -1);
		points[1] = new Point2D.Double(HEIGHT, -.5);
		points[2] = new Point2D.Double(HEIGHT, .5);
		points[3] = new Point2D.Double(0, 1);
		points[4] = new Point2D.Double(-HEIGHT, .5);
		points[5] = new Point2D.Double(-HEIGHT, -.5);
	}

	public Hexagon(final double radius)
	{
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