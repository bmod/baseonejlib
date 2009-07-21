package com.baseoneonline.java.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import com.baseoneonline.java.swing.BaseFrame;
import com.baseoneonline.java.tools.FileExensionFilter;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.nodes.PImage;

public class TestEdgeTracing extends BaseFrame {

	public static void main(final String[] args) {
		new TestEdgeTracing();
	}

	PCanvas canvas;
	PImage image;

	@Override
	protected void initFrame() {
		final JToolBar toolBar = new JToolBar();
		{
			toolBar.add(new AbstractAction("Open Image") {

				private final String[] fileExtensions = { "jpg", "png" };

				private final FileExensionFilter imageFileFilter = new FileExensionFilter(
						fileExtensions);

				@Override
				public void actionPerformed(final ActionEvent arg0) {
					System.out.println("PERFORMED");
					final JFileChooser fc = new JFileChooser(
							getRememberedPath());
					fc.setFileFilter(imageFileFilter);
					if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(null)) {
						final File f = fc.getSelectedFile();
						rememberPath(f);
						if (null != f) {
							openFile(f);
						}
					}
				}
			});
			toolBar.add(focusAction);
		}
		add(toolBar, BorderLayout.NORTH);

		canvas = new PCanvas();

		add(canvas);

		final FilterPanel filterPanel = new FilterPanel();

		add(filterPanel, BorderLayout.WEST);

	}

	private final Action focusAction = new AbstractAction("Focus") {

		@Override
		public void actionPerformed(final ActionEvent arg0) {
		// TODO Auto-generated method stub

		}
	};

	private void rememberPath(final File f) {
		File dir;
		if (f.isDirectory()) dir = f;
		else
			dir = f.getParentFile();
		getPrefs().put("LastPath", dir.getAbsolutePath());
	}

	private File getRememberedPath() {
		return new File(getPrefs().get("LastPath", ""));
	}

	private void openFile(final File f) {
		try {
			final BufferedImage im = ImageIO.read(f);
			if (null == image) {
				image = new PImage(im);
			} else {
				image.setImage(im);

			}
			canvas.getLayer().addChild(image);
		} catch (final IOException e) {
			java.util.logging.Logger.getLogger(TestEdgeTracing.class.getName())
					.warning(e.getMessage());
		}
	}

}

class FilterPanel extends JPanel {

	DefaultListModel listModel = new DefaultListModel();

	public FilterPanel() {

		setMinimumSize(new Dimension(200, 0));

		final BoxLayout box = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(box);

		listModel.addElement("Blaat");
		listModel.addElement("Test124");
		listModel.addElement("Blaat");
		listModel.addElement("Test124");
		listModel.addElement("Blaat");
		listModel.addElement("Test124");

		final JList list = new JList(listModel);
		add(new JScrollPane(list));

	}
}
