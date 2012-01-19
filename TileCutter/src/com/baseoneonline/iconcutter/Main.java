package com.baseoneonline.iconcutter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import com.baseoneonline.java.swing.WrapLayout;
import com.baseoneonline.java.swing.config.Config;
import com.baseoneonline.java.swing.propertySheet.BeanListener;
import com.baseoneonline.java.swing.propertySheet.ObjectInspectorJPanel;
import com.baseoneonline.java.tools.FileExensionFilter;

public class Main extends JFrame {

	private static final String LAST_SELECTED_OUTPUT_DIR = "lastSelectedOutputDir";
	private static final String LAST_OPENED_DIRECTORY = "lastOpenedDirectory";
	private ObjectInspectorJPanel objectInspectorJPanel;
	private ImagePanel imagePanel;

	private BufferedImage image;

	private final CutConfig cutProps;

	private final ArrayList<BufferedImage> tiles = new ArrayList<BufferedImage>();

	public static void main(final String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final Exception e) {

		}

		Config.setApplicationClass(Main.class);
		final Main frame = new Main();
		frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
	}

	public Main() {
		initComponents();
		addWindowListener(winAdapter);
		Config.get().persist(this);

		cutProps = imagePanel.getCutProps();

		objectInspectorJPanel.addBeanListener(beanListener);
		objectInspectorJPanel.setBean(cutProps);
	}

	private final BeanListener beanListener = new BeanListener() {

		@Override
		public void beanChanged(final Object bean) {
			updateTiles();
		}
	};

	private final WindowAdapter winAdapter = new WindowAdapter() {
		@Override
		public void windowClosing(final java.awt.event.WindowEvent e) {
			Config.get().flush();
			Main.this.dispose();
		}
	};
	private JPanel tilePanel;

	private void initComponents() {
		setMinimumSize(new Dimension(300, 300));

		final JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		final JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		final JMenuItem mntmOpenImage = new JMenuItem("Open Image");
		mntmOpenImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				final JFileChooser fc = new JFileChooser(Config.get().getFile(
						LAST_OPENED_DIRECTORY));

				fc.setFileFilter(new FileExensionFilter(
						new String[] { "jpg", "png", "gif", "jpeg" }));
				if (JFileChooser.APPROVE_OPTION == fc
						.showOpenDialog(Main.this)) {
					Config.get().setFile(LAST_OPENED_DIRECTORY,
							fc.getSelectedFile().getParentFile());
					loadImage(fc.getSelectedFile());
				}
			}
		});
		mnFile.add(mntmOpenImage);

		final JMenuItem mntmSaveTiles = new JMenuItem("Save Tiles...");
		mntmSaveTiles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				final JFileChooser fc = new JFileChooser(
						LAST_SELECTED_OUTPUT_DIR);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (JFileChooser.APPROVE_OPTION == fc
						.showOpenDialog(Main.this)) {
					Config.get().setFile(LAST_SELECTED_OUTPUT_DIR,
							fc.getSelectedFile());
					saveTiles(fc.getSelectedFile());
				}
			}

		});
		mnFile.add(mntmSaveTiles);

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

	private void saveTiles(final File dir) {
		int i = 0;
		for (final BufferedImage im : tiles) {
			final File outFile = new File(dir.getAbsolutePath() + "/"
					+ cutProps.getFilenamePrefix() + i + ".png");
			try {
				ImageIO.write(im, "PNG", outFile);
			} catch (final Exception e) {
				showWarning("Error saving file: " + outFile.getAbsolutePath());
			}
			i++;
		}
		JOptionPane.showMessageDialog(this, "Files saved!");
	}

	private void loadImage(final File f) {
		try {
			image = ImageIO.read(f);
			imagePanel.setImage(image);

		} catch (final Exception e) {
			showWarning("Could not open file: " + f);
		}
		updateTiles();
	}

	private void showWarning(final String msg) {
		JOptionPane.showMessageDialog(Main.this, msg, "Warning",
				JOptionPane.ERROR_MESSAGE);
	}

	private void updateTiles() {
		if (null != worker) {
			worker.cancel(true);
		}

		imagePanel.repaint();

		tiles.clear();
		tilePanel.removeAll();

		worker = new SwingWorker<Void, BufferedImage>() {
			@Override
			protected Void doInBackground() throws Exception {
				final int nx = cutProps.getCountX();
				final int ny = cutProps.getCountY();
				final int tw = cutProps.getTileSizeX();
				final int th = cutProps.getTileSizeY();

				final int[] iArray = new int[(tw * th) * 4];

				for (final Rectangle rect : imagePanel.getRectangles()) {
					if (worker.isCancelled())
						return null;

					final BufferedImage im = new BufferedImage(tw, th,
							image.getType());

					image.getRaster().getPixels(rect.x, rect.y, tw, th, iArray);
					if (!isEmpty(iArray)) {
						im.getRaster().setPixels(0, 0, tw, th, iArray);
						publish(im);
					}
				}
				return null;
			}

			@Override
			protected void process(final List<BufferedImage> chunks) {
				tiles.addAll(chunks);
				for (final BufferedImage im : chunks) {
					tilePanel.add(new TilePanel(im));
				}
				tilePanel.revalidate();
			}
		};
		worker.execute();
	}

	private static boolean isEmpty(final int[] pixels) {

		for (final int v : pixels) {
			if (v != 0)
				return false;
		}
		return true;
	}

	private SwingWorker<Void, BufferedImage> worker;

}

class TilePanel extends JPanel {

	private final BufferedImage image;

	public TilePanel(final BufferedImage image) {
		this.image = image;
		setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
	}

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}
}