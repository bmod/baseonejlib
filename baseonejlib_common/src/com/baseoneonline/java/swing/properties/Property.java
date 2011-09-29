package com.baseoneonline.java.swing.properties;

public abstract class Property<T>
{
	private String name;

	protected T value;

	public Property(String name, T value)
	{
		setName(name);
		setValue(value);
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setValue(T value)
	{
		this.value = value;
	}

	public T getValue()
	{
		return value;
	}

	public abstract String getStringValue();

	public abstract void setStringValue(String value);
}
