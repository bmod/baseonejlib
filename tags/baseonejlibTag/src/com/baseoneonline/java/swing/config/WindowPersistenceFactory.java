package com.baseoneonline.java.swing.config;

import java.awt.Rectangle;
import java.awt.Window;

public class WindowPersistenceFactory implements PersistenceFactory
{

	@Override
	public void store(Config conf, String key, Object value)
	{
		conf.putRectangle(key + "Bounds", ((Window) value).getBounds());
	}

	@Override
	public void restore(Config conf, String key, Object value)
	{
		Rectangle rect = conf.getRectangle(key + "Bounds",
				((Window) value).getBounds());
		if (null != rect)
			((Window) value).setBounds(rect);
	}

	@Override
	public Class<?> getType()
	{
		return Window.class;
	}

}
