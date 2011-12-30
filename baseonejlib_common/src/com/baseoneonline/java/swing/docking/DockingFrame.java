package com.baseoneonline.java.swing.docking;

import java.awt.Component;
import java.awt.event.WindowAdapter;

public abstract class DockingFrame
{

	public abstract void addDockable(Component comp, String title);

	public abstract void restoreLayout();

	public abstract void addWindowListener(WindowAdapter l);

	public abstract void setVisible(boolean b);

	public abstract void dispose();

}
