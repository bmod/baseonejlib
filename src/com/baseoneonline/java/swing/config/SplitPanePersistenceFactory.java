package com.baseoneonline.java.swing.config;

import javax.swing.JSplitPane;

public class SplitPanePersistenceFactory implements PersistenceFactory
{

	@Override
	public void store(Config conf, String key, Object value)
	{
		JSplitPane splitPane = ((JSplitPane) value);
		conf.putInt(key + "DividerLocation", splitPane.getDividerLocation());
	}

	@Override
	public void restore(Config conf, String key, Object value)
	{
		JSplitPane splitPane = ((JSplitPane) value);
		splitPane.setDividerLocation(conf.getInt(key + "DividerLocation",
				splitPane.getDividerLocation()));
	}

	@Override
	public Class<?> getType()
	{
		return JSplitPane.class;
	}

}
