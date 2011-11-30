package com.baseoneonline.java.swing.propertySheet;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BeanPropertyInfo
{

	private static final HashMap<Class<?>, BeanPropertyInfo> cache = new HashMap<Class<?>, BeanPropertyInfo>();

	private final Class<?> beanClass;

	private final BeanProperty[] properties;

	private BeanPropertyAdapter[] adapters;

	private BeanPropertyInfo(Class<?> beanClass)
	{
		this.beanClass = beanClass;

		List<BeanProperty> props = readProps(beanClass);
		properties = props.toArray(new BeanProperty[props.size()]);
	}

	public Class<?> getBeanClass()
	{
		return beanClass;
	}

	public BeanProperty[] getProperties()
	{
		return properties;
	}

	public BeanPropertyAdapter[] getAdaptedProperties()
	{
		if (null == adapters)
		{
			adapters = new BeanPropertyAdapter[properties.length];
			for (int i = 0; i < adapters.length; i++)
				adapters[i] = new BeanPropertyAdapter(properties[i]);
		}
		return adapters;
	}

	private Method getWriteMethod(Class<?> type, String propertyName,
			Class<?> propertyType)
	{
		Method writeMethod = null;
		try
		{
			writeMethod = type.getDeclaredMethod("set" + propertyName,
					propertyType);

			if (!isAccessibleMethod(writeMethod))
				return null;

			Class<?>[] paramTypes = writeMethod.getParameterTypes();
			if (paramTypes.length != 1)
				return null;
			if (paramTypes[0] != (propertyType))
				return null;

		} catch (Exception e)
		{
		}

		return writeMethod;
	}

	private boolean isAccessibleMethod(Method m)
	{
		int mods = m.getModifiers();
		if (!Modifier.isPublic(mods))
			return false;
		if (Modifier.isStatic(mods))
			return false;
		if (Modifier.isPrivate(mods))
			return false;
		if (Modifier.isProtected(mods))
			return false;
		return true;
	}

	private List<BeanProperty> readProps(Class<?> exactType)
	{
		List<BeanProperty> properties = new ArrayList<BeanProperty>();
		for (Method readMethod : exactType.getDeclaredMethods())
		{
			if (readMethod.getParameterTypes().length != 0)
				continue;

			if (isAccessibleMethod(readMethod))
			{

				String methodName = readMethod.getName();
				String propertyName = null;
				Class<?> propertyType = null;

				boolean isGetter = false;
				if (methodName.startsWith("get") && methodName.length() > 3)
				{
					propertyName = methodName.substring(3);
					propertyType = readMethod.getReturnType();
					if (propertyType != boolean.class
							|| propertyType != Boolean.class)
						isGetter = true;
				} else if (methodName.startsWith("is")
						&& methodName.length() > 2)
				{
					propertyName = methodName.substring(2);
					propertyType = readMethod.getReturnType();
					if (propertyType == boolean.class
							|| propertyType == Boolean.class)
						isGetter = true;
				}

				if (isGetter)
				{
					Method writeMethod = getWriteMethod(exactType,
							propertyName, propertyType);

					String adjustedName = decapitalizeFirstChar(propertyName);

					properties.add(new BeanProperty(adjustedName, exactType,
							readMethod, writeMethod));

				}
			}
		}

		Class<?> superType = exactType.getSuperclass();
		if (null != superType && superType != Object.class)
			properties.addAll(readProps(superType));

		return properties;
	}

	private static String decapitalizeFirstChar(String name)
	{
		if (name.length() == 1)
			return name.toLowerCase();

		return Character.toLowerCase(name.charAt(0)) + name.substring(1);
	}

	public static BeanPropertyInfo getInfo(Class<?> class1)
	{
		BeanPropertyInfo info = cache.get(class1);
		if (null == info)
		{
			info = new BeanPropertyInfo(class1);
			cache.put(class1, info);
		}
		return info;
	}
}