package com.baseoneonline.java.mediadb.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.baseoneonline.java.mediadb.db.Collection;
import com.baseoneonline.java.mediadb.db.CollectionManager;
import com.baseoneonline.java.mediadb.db.ImageItem;
import com.baseoneonline.java.mediadb.db.Item;
import com.baseoneonline.java.mediadb.db.MovieItem;
import com.baseoneonline.java.mediadb.db.MusicItem;
import com.baseoneonline.java.mediadb.events.Event;
import com.baseoneonline.java.mediadb.events.EventListener;
import com.baseoneonline.java.mediadb.events.EventType;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class FilteredListPanel extends JPanel {

	private final static int FILE_NAME = 0, COL_TITLE = 1, COL_ARTIST = 2,
			COL_ALBUM = 3, COL_TYPE = 4;
	private final String[] columnNames = { "File", "Title", "Artist", "Album",
	"Type" };

	/**
	 *
	 */
	private static final long serialVersionUID = -3320299330064839131L;
	private final GridModel gridModel = new GridModel();
	private JTable table;
	private JLabel lbStatus;
	private JScrollPane spList;
	private TableSearchFilter searchFilter;
	private final TableRowSorter<GridModel> sorter = new TableRowSorter<GridModel>(
			gridModel);

	public FilteredListPanel() {
		initGUI();
		initFilter();
	}

	private void initFilter() {
		final int[] fields = { FILE_NAME, COL_TITLE, COL_ARTIST, COL_ALBUM };

		searchFilter = new TableSearchFilter(fields);
		sorter.setRowFilter(searchFilter);
		table.setRowSorter(sorter);

	}

	private void initGUI() {
		setLayout(new BorderLayout());

		final JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		final JTextField tfFilter = new JTextField();
		tfFilter.setPreferredSize(new Dimension(200, 22));
		tfFilter.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(final KeyEvent e) {}

			@Override
			public void keyReleased(final KeyEvent e) {}

			@Override
			public void keyTyped(final KeyEvent e) {
				// if (KeyEvent.VK_ENTER == e.getKeyCode()) {
				searchFilter.setSearchString(tfFilter.getText().trim());
				gridModel.fireTableDataChanged();
				// }
			}
		});
		topPanel.add(tfFilter);

		add(topPanel, BorderLayout.NORTH);

		CollectionManager.getManager().addEventListener(
				EventType.COLLECTION_SCANNING, new EventListener() {
					@Override
					public void onEvent(final Event e) {
						gridModel.fireTableDataChanged();
						updateStatusLabel();
						table.invalidate();
						spList.scrollRectToVisible(table.getCellRect(gridModel
								.getRowCount() - 1, 0, true));
					}
				});

		table = new JTable(gridModel);
		spList = new JScrollPane(table);
		add(spList, BorderLayout.CENTER);

		lbStatus = new JLabel("Status");
		add(lbStatus, BorderLayout.SOUTH);

	}

	private void updateStatusLabel() {
		lbStatus.setText("Number of items: " + gridModel.getRowCount());
	}

	private class GridModel extends DefaultTableModel {


		/**
		 *
		 */
		private static final long serialVersionUID = -1194024000572769101L;

		public GridModel() {}

		@Override
		public Object getValueAt(final int row, final int column) {
			final Item item = Collection.getInstance().get(row);
			if (item instanceof MusicItem) {
				final MusicItem music = (MusicItem) item;
				switch (column) {
					case FILE_NAME:
						return music.getFile().getName();
					case COL_TITLE:
						return music.getTag().getTitle();
					case COL_ARTIST:
						return music.getTag().getArtist();
					case COL_ALBUM:
						return music.getTag().getAlbum();
					case COL_TYPE:
						return "music";
					default:
						return "";
				}
			}
			if (item instanceof MovieItem) {
				final MovieItem movie = (MovieItem) item;
				switch (column) {
					case FILE_NAME:
						return movie.getFile().getName();
					case COL_TITLE:
						return movie.getFile().getName();
					case COL_TYPE:
						return "movie";
					default:
						return "";
				}
			}
			if (item instanceof ImageItem) {
				final ImageItem image = (ImageItem) item;
				switch (column) {
					case FILE_NAME:
						return image.getFile().getName();
					case COL_TITLE:
						return image.getFile().getName();
					case COL_TYPE:
						return "image";
					default:
						return "";
				}

			}
			return item.getFile().getName();
		}

		@Override
		public int getRowCount() {
			return Collection.getInstance().size();
		}

		@Override
		public String getColumnName(final int column) {
			return columnNames[column];
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

	}

}
