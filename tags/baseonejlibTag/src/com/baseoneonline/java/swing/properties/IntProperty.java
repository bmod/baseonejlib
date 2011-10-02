package com.baseoneonline.java.swing.properties;

public class IntProperty extends Property<Integer>
{

	public IntProperty(String name, int value)
	{
		super(name, value);

	}

	@Override
	public String getStringValue()
	{
		return value.toString();
	}

	@Override
	public void setStringValue(String value)
	{
		this.value = Integer.parseInt(value);
	}

}
