package test;


import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.vlsolutions.swing.docking.DockingDesktop;

public class DockTest extends JFrame
{

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e)
		{
			// TODO: handle exception
		}

		DockTest app = new DockTest();
		app.setVisible(true);
	}

	public DockTest()
	{
		initComponents();
	}

	private void initComponents()
	{
		setLayout(new BorderLayout());
		DockingDesktop desk = new DockingDesktop();
		add(desk);
	}

}
