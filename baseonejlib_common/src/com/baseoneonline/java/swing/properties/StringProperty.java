package com.baseoneonline.java.swing.properties;

public class StringProperty extends Property<String>
{

	public StringProperty(String name, String value)
	{
		super(name, value);
	}

	@Override
	public String getStringValue()
	{
		return value;
	}

	@Override
	public void setStringValue(String value)
	{
		this.value = value;
	}

}
