package com.baseoneonline.java.resourceMapper;

public abstract class ResourceTree
{
	public abstract int getChildCount(Object parent);

	public abstract Object getChild(Object parent, int index);

	public abstract String getName(Object node);

	public abstract String getAttribute(Object node, String name);

	public abstract Object addChild(Object parent, String name);

	public abstract Object getRoot();

	public Integer getIntAttribute(final Object node, final String name)
			throws Exception
	{
		final String value = getAttribute(node, name);
		if (null == value)
			return null;
		try
		{
			return Integer.parseInt(value);
		} catch (final NumberFormatException e)
		{
			throw new Exception(
					String.format(
							"Could not convert attribute '%s' value '%s' to int on node '%s'.",
							name, value, getName(node)));
		}
	}

	public Float getFloatAttribute(final Object node, final String name)
			throws Exception
	{
		final String value = getAttribute(node, name);
		if (null == value)
			return null;
		try
		{
			return Float.parseFloat(value);
		} catch (final NumberFormatException e)
		{
			throw new Exception(
					String.format(
							"Could not convert attribute '%s' value '%s' to float on node '%s'.",
							name, value, getName(node)));
		}
	}

	public abstract Object getChild(Object node, String name);

	public Double getDoubleAttribute(final Object node, final String name)
			throws Exception
	{
		final String value = getAttribute(node, name);
		if (null == value)
			return null;

		try
		{
			return Double.parseDouble(value);
		} catch (final NumberFormatException e)
		{
			throw new Exception(
					String.format(
							"Could not convert attribute '%s' value '%s' to double on node '%s'.",
							name, value, getName(node)));
		}
	}

	public Boolean getBooleanAttribute(final Object node, final String name)
			throws Exception
	{
		final String value = getAttribute(node, name);
		if (null == value)
			return null;

		try
		{
			return Boolean.parseBoolean(value);
		} catch (final NumberFormatException e)
		{
			throw new Exception(
					String.format(
							"Could not convert attribute '%s' value '%s' to boolean on node '%s'.",
							name, value, getName(node)));
		}
	}

	public String[] getArrayAttribute(final Object node, final String name)
	{
		final String value = getAttribute(node, name);
		if (null == value)
			return null;

		final String[] values = value.split(",");
		for (int i = 0; i < values.length; i++)
			values[i] = values[i].trim();
		return values;
	}

	public float[] getFloatArrayAttribute(final Object node, final String name)
			throws Exception
	{
		final String[] value = getArrayAttribute(node, name);
		if (null == value)
			return null;

		final float[] re = new float[value.length];
		try
		{
			for (int i = 0; i < re.length; i++)
			{
				re[i] = Float.parseFloat(value[i]);
			}
		} catch (final NumberFormatException e)
		{
			throw new Exception(
					String.format(
							"Could not convert attribute '%s' value '%s' to float on node '%s'.",
							name, value, getName(node)));
		}
		return re;
	}

	public double[] getDoubleArrayAttribute(final Object node, final String name)
			throws Exception
	{
		final String[] value = getArrayAttribute(node, name);
		if (null == value)
			return null;

		final double[] re = new double[value.length];
		try
		{
			for (int i = 0; i < re.length; i++)
			{
				re[i] = Double.parseDouble(value[i]);
			}
		} catch (final NumberFormatException e)
		{
			throw new Exception(
					String.format(
							"Could not convert attribute '%s' value '%s' to double on node '%s'.",
							name, value, getName(node)));
		}
		return re;
	}

	public int[] getIntArrayAttribute(final Object node, final String name)
			throws Exception
	{
		final String[] value = getArrayAttribute(node, name);
		if (null == value)
			return null;

		final int[] re = new int[value.length];
		try
		{
			for (int i = 0; i < re.length; i++)
			{
				re[i] = Integer.parseInt(value[i]);
			}
		} catch (final NumberFormatException e)
		{
			throw new Exception(
					String.format(
							"Could not convert attribute '%s' value '%s' to int on node '%s'.",
							name, value, getName(node)));
		}
		return re;
	}

	public boolean[] getBooleanArrayAttribute(final Object node,
			final String name) throws Exception
	{
		final String[] value = getArrayAttribute(node, name);
		if (null == value)
			return null;

		final boolean[] re = new boolean[value.length];
		try
		{
			for (int i = 0; i < re.length; i++)
			{
				re[i] = Boolean.parseBoolean(value[i]);
			}
		} catch (final NumberFormatException e)
		{
			throw new Exception(
					String.format(
							"Could not convert attribute '%s' value '%s' to boolean on node '%s'.",
							name, value, getName(node)));
		}
		return re;
	}

}
