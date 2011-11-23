package com.baseoneonline.java.swing.config;

import java.awt.Rectangle;
import java.awt.Window;

public class WindowPersistenceFactory implements PersistenceFactory<Window>
{

	@Override
	public void store(Config conf, String key, Window value)
	{
		conf.putRectangle(key + "Bounds", (value).getBounds());
	}

	@Override
	public void restore(Config conf, String key, Window value)
	{
		Rectangle rect = conf.getRectangle(key + "Bounds", (value).getBounds());
		if (null != rect)
			(value).setBounds(rect);
	}

	@Override
	public Class<? extends Window> getType()
	{
		return Window.class;
	}

}
