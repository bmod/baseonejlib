package com.baseoneonline.java.math;

public class IsoCoord
{
	public int x = 0;
	public int y = 0;
	public int z = 0;

	public IsoCoord()
	{
	}

	public IsoCoord(IsoCoord source)
	{
		set(source);
	}

	public IsoCoord(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.z = -x - y;
	}

	public IsoCoord(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void set(IsoCoord c)
	{
		this.x = c.x;
		this.y = c.y;
		this.z = c.z;
	}

	public boolean isValid()
	{
		return x + y + z == 0;
	}

	@Override
	public String toString()
	{
		return getClass().getName() + "[x=" + x + ", y=" + y + ", z=" + z + "]";
	}

	public void addLocal(IsoCoord c)
	{
		x += c.x;
		y += c.y;
		z += c.z;
	}

}