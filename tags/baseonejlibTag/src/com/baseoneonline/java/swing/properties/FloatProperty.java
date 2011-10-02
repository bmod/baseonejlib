package com.baseoneonline.java.swing.properties;

public class FloatProperty extends Property<Float>
{

	public FloatProperty(String name, Float value)
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
		this.value = Float.parseFloat(value);
	}

}
