package test;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JTree;

import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public class OpenIDETest
{
	public static void main(String[] args)
	{
		WindowManager man = WindowManager.getDefault();
		Frame frame = man.getMainWindow();

		frame.setVisible(true);
		frame.add(new MyComponent());

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