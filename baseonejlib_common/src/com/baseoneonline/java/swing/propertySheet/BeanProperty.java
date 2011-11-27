package com.baseoneonline.java.swing.propertySheet;

import java.lang.reflect.Method;

class BeanProperty
{

	private final String name;
	private final Method readMethod;
	private final Method writeMethod;
	private final Class<?> type;
	private final Class<?> declaringClass;

	public BeanProperty(String name, Class<?> exactType, Method readMethod,
			Method writeMethod)
	{
		this.name = name;
		this.declaringClass = exactType;
		this.readMethod = readMethod;
		this.writeMethod = writeMethod;
		this.type = readMethod.getReturnType();
	}

	public String getName()
	{
		return name;
	}

	public Method getReadMethod()
	{
		return readMethod;
	}

	public Method getWriteMethod()
	{
		return writeMethod;
	}

	public Class<?> getType()
	{
		return type;
	}

	public Class<?> getDeclaringClass()
	{
		return declaringClass;
	}

}