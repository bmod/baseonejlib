package com.baseoneonline.java.swing.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.vlsolutions.swing.docking.DockingDesktop;

public class DockingDesktopPersistenceFactory implements
		PersistenceFactory<DockingDesktop>
{

	@Override
	public void store(Config conf, String key, DockingDesktop storable)
	{
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			storable.writeXML(baos);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		conf.putBytes(key + "Layout", baos.toByteArray());
	}

	@Override
	public void restore(Config conf, String key, DockingDesktop storable)
	{
		byte[] bytes = conf.getBytes(key + "Layout", null);
		if (null == bytes)
			return;
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		try
		{
			storable.readXML(bais);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public Class<? extends DockingDesktop> getType()
	{
		return DockingDesktop.class;
	}

}
