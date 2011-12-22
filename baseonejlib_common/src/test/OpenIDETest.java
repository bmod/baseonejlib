package test;

import java.awt.BorderLayout;

import javax.swing.JTree;

import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public class OpenIDETest
{
	public static void main(final String[] args)
	{
		final WindowManager man = WindowManager.getDefault();
		man.getMainWindow().setVisible(true);

		final MyComponent comp = new MyComponent();
		comp.requestActive();
	}

}

class MyComponent extends TopComponent
{
	public MyComponent()
	{
		setLayout(new BorderLayout());
		add(new JTree());
	}

}