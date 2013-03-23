package com.baseoneonline.jlib.ardor3d.tools;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.ardor3d.scenegraph.Node;
import com.baseoneonline.java.swing.config.Config;

public class ToolsWindow extends JFrame {

	private static ToolsWindow window;
	private SceneGraphPanel sceneGraphPanel;

	public ToolsWindow() {
		initComponents();
		Config.get().persist(this);
		addWindowListener(winAdapter);
	}

	private void initComponents() {
		setTitle("ToolsWindow");

		final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		sceneGraphPanel = new SceneGraphPanel();

		tabbedPane.addTab("Scene Graph", null, sceneGraphPanel, null);

	}

	private final WindowAdapter winAdapter = new WindowAdapter() {
		@Override
		public void windowClosing(java.awt.event.WindowEvent e) {
			Config.get().flush();
		}
	};

	public static void show(final Node rootNode) {
		if (window == null) {
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception e) {
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
