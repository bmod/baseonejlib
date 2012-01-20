package com.baseoneonline.tilecutter.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

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
import com.baseoneonline.tilecutter.core.CutModelListener;
import java.awt.FlowLayout;

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

	/**
	 * Most of this is automatically generated
	 */
	private void initComponents() {
		setMinimumSize(new Dimension(300, 300));

		final JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		final JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mnFile.add(new OpenImageAction(model));

		mnFile.add(new SaveTilesAction(model));

		final JSplitPane mainSplitPane = new JSplitPane();
		mainSplitPane.setContinuousLayout(true);
		getContentPane().add(mainSplitPane, BorderLayout.CENTER);

		objectInspectorJPanel = new ObjectInspectorJPanel();
		mainSplitPane.setLeftComponent(objectInspectorJPanel);
		mainSplitPane.setDividerLocation(150);

		final JSplitPane rightSplitPane = new JSplitPane();
		rightSplitPane.setContinuousLayout(true);
		rightSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mainSplitPane.setRightComponent(rightSplitPane);

		final JScrollPane scrollPaneCutter = new JScrollPane();
		rightSplitPane.setLeftComponent(scrollPaneCutter);

		imagePanel = new ImagePanel();
		scrollPaneCutter.setViewportView(imagePanel);

		final JScrollPane scrollPaneTiles = new JScrollPane();
		rightSplitPane.setRightComponent(scrollPaneTiles);

		WrapLayout wl_tilePanel = new WrapLayout();
		wl_tilePanel.setAlignment(FlowLayout.LEFT);
		tilePanel = new JPanel(wl_tilePanel);

		scrollPaneTiles.setViewportView(tilePanel);
		rightSplitPane.setDividerLocation(250);

		Config.get().persist("HSplitPane", mainSplitPane);
		Config.get().persist("VSplitPane", rightSplitPane);
	}

	private final CutModelListener modelListener = new CutModelListener() {

		@Override
		public void metricsChanged() {
			objectInspectorJPanel.setBean(model.getMetrics());
		}

		@Override
		public void imageChanged() {
			// unused
		}

		@Override
		public void tilesChanged() {
			updateTilePanel();
		}
	};
	
	private void updateTilePanel() {
		tilePanel.removeAll();
		tilePanel.revalidate();

		for (BufferedImage im : model.getTiles()) {
			tilePanel.add(new TilePanel(im));
		}
		// Force validation
		tilePanel.revalidate();
		tilePanel.repaint();
	}

	/**
	 * When our tile cutting properties have been changed
	 */
	private final BeanListener beanListener = new BeanListener() {

		@Override
		public void beanChanged(final Object bean) {
			model.refresh();
		}
	};

}