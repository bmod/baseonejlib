package test;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.baseoneonline.java.math.IsoCoord;
import com.baseoneonline.java.math.IsoHexGrid;
import com.baseoneonline.java.swing.config.Config;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
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

	private PCanvas pCanvas;
	private final List<Hexagon> hexagons = new ArrayList<Hexagon>();
	private final double hexRadius = 10;

	public TestHexGrid()
	{
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(winAdapter);
		initComponents();
		setSize(300, 300);
		Config.setApplicationClass(getClass());
		Config.get().persist(this);

		createGrid();
		pCanvas.addInputEventListener(mouseAdapter);
	}

	private void createGrid()
	{
		double[] pos = new double[2];
		for (int i = 0; i < 8; i++)
		{
			IsoCoord[] pts = IsoHexGrid.ringCoordinates(i, new IsoCoord());
			for (IsoCoord pt : pts)
			{
				IsoHexGrid.hexToCart(pt, pos);
				final Hexagon hx = new Hexagon(hexRadius);
				hx.translate(pos[0] * hexRadius * 2, pos[1] * hexRadius * 2);
				hexagons.add(hx);
				pCanvas.getLayer().addChild(hx);
			}
		}
	}

	private final PBasicInputEventHandler mouseAdapter = new PBasicInputEventHandler()
	{

		@Override
		public void mouseMoved(PInputEvent e)
		{
			IsoCoord hexPos = IsoHexGrid.cartToHex(e.getPosition().getX()
					/ hexRadius, e.getPosition().getY() / hexRadius, null);
			System.out.println(hexPos);
		}
	};

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

	private void initComponents()
	{
		setLayout(new BorderLayout());
		pCanvas = new PCanvas();
		add(pCanvas);
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