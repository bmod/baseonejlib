package com.baseoneonline.java.swing.propertySheet;

import java.lang.reflect.Method;

import com.l2fprod.common.propertysheet.AbstractProperty;

public class BeanPropertyAdapter extends AbstractProperty
{

	private final BeanProperty prop;

	public BeanPropertyAdapter(BeanProperty prop)
	{
		this.prop = prop;
	}

	@Override
	public String getName()
	{
		return prop.getName();
	}

	@Override
	public String getDisplayName()
	{
		return getName();
	}

	@Override
	public String getShortDescription()
	{
		return null;
	}

	@Override
	public Class<?> getType()
	{
		return prop.getType();
	}

	@Override
	public boolean isEditable()
	{
		return prop.getWriteMethod() != null;
	}

	@Override
	public String getCategory()
	{
		return prop.getDeclaringClass().getSimpleName();
	}

	@Override
	public void readFromObject(Object object)
	{
		try
		{
			this.setValue(prop.getReadMethod().invoke(object));
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeToObject(Object object)
	{
		Method writeMethod = prop.getWriteMethod();
		if (null == writeMethod)
			return;
		try
		{
			writeMethod.invoke(object, this.getValue());
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public BeanProperty getInfo()
	{
		return prop;
	}
}