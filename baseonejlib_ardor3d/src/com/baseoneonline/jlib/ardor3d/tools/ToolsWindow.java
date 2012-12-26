package com.baseoneonline.jlib.ardor3d.tools;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.ardor3d.scenegraph.Node;
import com.baseoneonline.java.swing.config.Config;

public class ToolsWindow extends JFrame {

	private static ToolsWindow window;
	private SceneGraphPanel sceneGraphPanel;

	public ToolsWindow() {
		initComponents();
		Config.get().persist(this);
	}

	private void initComponents() {
		setTitle("ToolsWindow");

		final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		sceneGraphPanel = new SceneGraphPanel();
		tabbedPane.addTab("Scene Graph", null, sceneGraphPanel, null);
	}

	public static void show(final Node rootNode) {
		if (window == null) {
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
			window = new ToolsWindow();
		}
		window.setRootNode(rootNode);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				window.setVisible(true);
			}

		});
	}

	private void setRootNode(final Node rootNode) {
		sceneGraphPanel.setRootNode(rootNode);
	}
}
