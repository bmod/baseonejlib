package com.baseoneonline.java.swing.config;

import javax.swing.JTree;

public class JTreePersistenceFactory implements
		PersistenceFactory<JTree>
{

	@Override
	public void store(Config conf, String key, JTree tree)
	{
		// TODO: Implement tree expansion storage
	}

	@Override
	public void restore(Config conf, String key, JTree tree)
	{

		// TODO: Implement tree expansion storage
	}

	@Override
	public Class<? extends JTree> getType()
	{
		return JTree.class;
	}

}
