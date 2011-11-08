package com.baseoneonline.java.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Functions operating on an 3d isometric hex/triangle grid. The grid assumes
 * that every hexagon has a radius of 1.
 * 
 */
public class IsoHexGrid<T>
{

	/**
	 * Shortest distance from hexagon center to a side, assuming the radius is 1
	 */
	public static final double H = Math.sqrt(3) / 2;

	private final int base = 1024;
	private final List<List<T>> _grid = new ArrayList<List<T>>(base * 2);

	private static final IsoCoord[] deltas =
	{ new IsoCoord(1, 0, -1), new IsoCoord(0, 1, -1), new IsoCoord(-1, 1, 0),
			new IsoCoord(-1, 0, 1), new IsoCoord(0, -1, 1),
			new IsoCoord(1, -1, 0) };

	public IsoHexGrid()
	{

	}

	public T set(IsoCoord c, T element)
	{
		return get(c.x).set(base + c.y, element);
	}

	public T get(IsoCoord c)
	{
		return get(c.x).get(base + c.y);
	}

	private List<T> get(int index)
	{
		List<T> lst = _grid.get(base + index);
		if (null == lst)
			lst = new ArrayList<T>(base * 2);
		return lst;
	}

	/**
	 * Convert from hexagonal grid to cartesian coordinates.
	 * 
	 * @param x
	 * @param y
	 * @return Cartesian coordinates for a specific grid position
	 */
	public static double[] hexToCart(IsoCoord c, double[] out)
	{
		if (null == out)
			out = new double[2];
		out[0] = (H * (c.x - c.y)) / 2;
		out[1] = (c.x + c.y) * .75;
		return out;
	}

	/**
	 * Convert from cartesian to hexagon grid coordinates.
	 * 
	 * Divide x and y by hexagon radius before passing values.
	 * 
	 * @param x
	 * @param y
	 * @return Grid coordinates for a cartesian location.
	 */

	public static IsoCoord cartToHex(final double x, final double y,
			IsoCoord out)
	{

		if (null == out)
			out = new IsoCoord();

		out.x = (int) Math.ceil((x / H) + ((y * 2) / 3));
		out.y = (int) Math.ceil(((2 * y) / 3) - (x / H));

		return out;
	}

	/**
	 * Returns the neighbors of a specified coordinate.
	 * 
	 * @param c
	 * @return
	 */
	public IsoCoord[] neighbors(IsoCoord c)
	{
		return ringCoordinates(1, c);
	}

	/**
	 * @param ring
	 *            The index of the ring, going outward. 0 means the center
	 *            position and returns the starting position.
	 * @param startX
	 *            X-coordinate of the starting position (X advances to the
	 *            right).
	 * @param startY
	 *            Y-coordinate of the starting position (Y advances to 120
	 *            degrees clockwise, more or less bottom left)
	 * @return A list of grid coordinates { {x,y}, {x,y}, .. }
	 */
	public static IsoCoord[] ringCoordinates(int ring, IsoCoord start)
	{
		// Number of coordinates returned
		int num = ringSize(ring);

		int index = 0;
		IsoCoord[] pts = new IsoCoord[num];

		IsoCoord current = new IsoCoord(start.x, start.y - ring);
		int nh = 0;

		pts[index++] = new IsoCoord(current);

		for (int j = 0; j < 6; j++)
		{
			nh = (j == 5) ? ring - 1 : ring;

			for (int i = 0; i < nh; i++)
			{
				current.addLocal(deltas[j]);
				IsoCoord c = new IsoCoord(current);
				pts[index++] = c;
			}
		}

		return pts;
	}

	/**
	 * @return The number of coordinates in the specified ring
	 */
	public static int ringSize(int ring)
	{
		if (ring < 0)
			throw new IllegalArgumentException("There is no negative ring: "
					+ ring);
		return (ring == 0) ? 1 : ring * 6;
	}

}