package com.baseoneonline.tilecutter.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import com.baseoneonline.java.swing.WrapLayout;
import com.baseoneonline.java.swing.config.Config;
import com.baseoneonline.java.swing.propertySheet.BeanListener;
import com.baseoneonline.java.swing.propertySheet.ObjectInspectorJPanel;
import com.baseoneonline.tilecutter.actions.OpenImageAction;
import com.baseoneonline.tilecutter.actions.SaveTilesAction;
import com.baseoneonline.tilecutter.core.CutModel;
import com.baseoneonline.tilecutter.core.CutModel.Listener;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1074859149654708331L;

	private ObjectInspectorJPanel objectInspectorJPanel;
	private ImagePanel imagePanel;
	private JPanel tilePanel;
	private final CutModel model = new CutModel();

	public MainFrame() {
		initComponents();
		Config.get().persist(this);

		// Model stuff
		objectInspectorJPanel.addBeanListener(beanListener);
		objectInspectorJPanel.setBean(model.getMetrics());
		model.addListener(modelListener);
		imagePanel.setModel(model);
	}

	private void initComponents() {
		setMinimumSize(new Dimension(300, 300));

		final JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		final JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mnFile.add(new OpenImageAction(model));

		mnFile.add(new SaveTilesAction(model));

		final JSplitPane splitPane = new JSplitPane();
		splitPane.setContinuousLayout(true);
		getContentPane().add(splitPane, BorderLayout.CENTER);

		objectInspectorJPanel = new ObjectInspectorJPanel();
		splitPane.setLeftComponent(objectInspectorJPanel);
		splitPane.setDividerLocation(150);

		final JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setContinuousLayout(true);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_1);

		final JScrollPane scrollPane_1 = new JScrollPane();
		splitPane_1.setLeftComponent(scrollPane_1);

		imagePanel = new ImagePanel();
		scrollPane_1.setViewportView(imagePanel);

		final JScrollPane scrollPane_2 = new JScrollPane();
		splitPane_1.setRightComponent(scrollPane_2);

		tilePanel = new JPanel(new WrapLayout());

		scrollPane_2.setViewportView(tilePanel);
		splitPane_1.setDividerLocation(250);

		Config.get().persist("HSplitPane", splitPane);
		Config.get().persist("VSplitPane", splitPane_1);
	}

	private final CutModel.Listener modelListener = new Listener() {

		@Override
		public void metricsChanged() {
			objectInspectorJPanel.setBean(model.getMetrics());
		}

		@Override
		public void imageChanged() {
		}
	};

	private final BeanListener beanListener = new BeanListener() {

		@Override
		public void beanChanged(final Object bean) {
			model.refresh();
		}
	};

}