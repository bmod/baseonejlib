package com.baseoneonline.java.swing.docking;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;

import com.baseoneonline.java.swing.config.Config;
import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockingDesktop;

public class VLDockingFrame extends DockingFrame
{

	private final JFrame frame;
	private final DockingDesktop desktop;

	public VLDockingFrame(String title)
	{
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setMinimumSize(new Dimension(400, 400));

		frame.setLayout(new BorderLayout());
		desktop = new DockingDesktop();
		frame.add(desktop);
	}

	@Override
	public void addDockable(final Component comp, String title)
	{
		final DockKey key = new DockKey(title);
		key.setFloatEnabled(true);
		key.setCloseEnabled(false);
		key.setAutoHideEnabled(false);

		final Dockable dockable = new com.vlsolutions.swing.docking.Dockable()
		{

			@Override
			public Component getComponent()
			{
				return comp;
			}

			@Override
			public DockKey getDockKey()
			{
				return key;
			}
		};
		desktop.addDockable(dockable);
	}

	@Override
	public void restoreLayout()
	{
		Config.get().persist(desktop);
	}

	@Override
	public void addWindowListener(WindowAdapter l)
	{
		frame.addWindowListener(l);
	}

	@Override
	public void setVisible(boolean b)
	{
		frame.setVisible(b);
	}

	@Override
	public void dispose()
	{
		frame.dispose();
	}

}
