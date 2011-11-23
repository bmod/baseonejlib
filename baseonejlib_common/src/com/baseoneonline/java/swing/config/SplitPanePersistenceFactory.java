package com.baseoneonline.java.swing.config;

import javax.swing.JSplitPane;

public class SplitPanePersistenceFactory implements
		PersistenceFactory<JSplitPane>
{

	@Override
	public void store(Config conf, String key, JSplitPane value)
	{

		conf.putInt(key + "DividerLocation", value.getDividerLocation());
	}

	@Override
	public void restore(Config conf, String key, JSplitPane value)
	{

		value.setDividerLocation(conf.getInt(key + "DividerLocation",
				value.getDividerLocation()));

	}

	@Override
	public Class<? extends JSplitPane> getType()
	{
		return JSplitPane.class;
	}

}
