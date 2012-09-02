package com.baseoneonline.java.math;

/**
 * @author bmod
 * 
 * @param <T>
 *            Control point type
 * @param <U>
 *            Storable control point type
 */
public abstract class Curve<T, U extends T>
{

	public static final int Open = 0;
	public static final int Loop = 1;
	public static final int Clamped = 2;

	protected T[] cvs;

	protected int mode = Open;

	public Curve(T[] cvs)
	{
		setCVs(cvs);
	}

	public int getMode()
	{
		return mode;
	}

	public void setMode(int mode)
	{
		this.mode = mode;
	}

	/**
	 * Replace this curve's control vertices.
	 * 
	 * @param cvs
	 *            The new set of control vertices.
	 */
	public void setCVs(T[] cvs)
	{
		this.cvs = cvs;
	}

	/**
	 * @param i
	 *            Index of the control vertex to retrieve.
	 * @return Instance of a control vertex used by this curve.
	 */
	public T getCV(int i)
	{
		return cvs[i];
	}

	/**
	 * @return The number of control vertices in this curve.
	 */
	public int getCVCount()
	{
		return cvs.length;
	}

	/**
	 * @param t
	 *            A value between 0 and 1.
	 * @param store
	 *            Will contain the result of the evaluation.
	 * @return
	 */
	public abstract T getPoint(double t, U store);

}
