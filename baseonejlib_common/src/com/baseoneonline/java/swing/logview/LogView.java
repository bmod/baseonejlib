package com.baseoneonline.java.swing.logview;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import com.baseoneonline.java.swing.SwingUtils;

public class LogView extends JPanel
{

	public static final Level[] LOG_LEVELS = { Level.ALL, Level.CONFIG,
			Level.FINE, Level.FINER, Level.FINEST, Level.INFO, Level.SEVERE,
			Level.WARNING };

	private JTable table;

	private final LogRecordModel model = new LogRecordModel();

	private Logger logger;

	private final JPopupMenu bottomContextMenu = createContextMenu();

	public LogView(final Logger logger)
	{

		initComponents();
		updateStatus();
		model.addTableModelListener(tableModelListener);

		SwingUtils.setContextMenu(statusPanel, bottomContextMenu);
		setLogger(logger);
		setLevel(Level.INFO);

	}

	public LogView()
	{
		this(Logger.getLogger(""));
	}

	private void initComponents()
	{
		table = new JTable(model);
		table.setBorder(null);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.setShowGrid(false);
		table.setTableHeader(null);

		table.getColumnModel().getColumn(0).setMaxWidth(80);

		setLayout(new BorderLayout());
		scrollPane = new JScrollPane(table);
		scrollPane.setBorder(null);
		add(scrollPane);
		statusPanel = new JPanel();
		statusPanel.setLayout(new BorderLayout(0, 0));
		lblStatus = new JLabel("Status");
		statusPanel.add(lblStatus);
		add(statusPanel, BorderLayout.SOUTH);
	}

	public void setLogger(final Logger logger)
	{
		if (null == this.logger)
		{
			logger.removeHandler(logHandler);
		}
		this.logger = logger;
		logger.addHandler(logHandler);
	}

	public Logger getLogger()
	{
		return logger;
	}

	private final TableModelListener tableModelListener = new TableModelListener()
	{

		@Override
		public void tableChanged(final TableModelEvent e)
		{
			scrollPane.getVerticalScrollBar().setValue(
					scrollPane.getVerticalScrollBar().getMaximum());
		}
	};

	private void updateStatus()
	{
		if (null == lblStatus || logger == null)
			return;
		final StringBuffer buf = new StringBuffer();
		buf.append("Level: " + logger.getLevel().getName());

		lblStatus.setText(buf.toString());
	}

	public void clear()
	{
		model.clear();
	}

	public void setLevel(final Level level)
	{
		logger.setLevel(level);
		updateStatus();
	}

	public Level getLevel()
	{
		return logger.getLevel();
	}

	private final Handler logHandler = new Handler()
	{
		@Override
		public void close() throws SecurityException
		{
		};

		@Override
		public void flush()
		{
		};

		@Override
		public void publish(final LogRecord record)
		{
			model.add(record);
		};
	};
	private JLabel lblStatus;
	private JScrollPane scrollPane;
	private JPanel statusPanel;

	public void setRecordFormatter(final Formatter formatter)
	{
		model.setFormatter(formatter);
	}

	private JPopupMenu createContextMenu()
	{
		final JPopupMenu menu = new JPopupMenu();

		for (final Level lvl : LOG_LEVELS)
		{
			menu.add(new AbstractAction(lvl.getName())
			{

				@Override
				public void actionPerformed(final ActionEvent e)
				{
					setLevel(lvl);
				}
			});
		}

		return menu;
	}
}

class LogCellRenderer extends DefaultTableCellRenderer
{
	@Override
	public Component getTableCellRendererComponent(final JTable table,
			final Object value, final boolean isSelected,
			final boolean hasFocus, final int row, final int column)
	{
		final Component comp = super.getTableCellRendererComponent(table,
				value, isSelected, hasFocus, row, column);
		if (row == 0)
		{

		}
		return comp;
	}
}

class LogRecordModel extends AbstractTableModel
{

	private final LinkedList<LogRecord> records = new LinkedList<LogRecord>();
	private Formatter formatter = new DefaultLogRecordFormatter();

	public void add(final LogRecord record)
	{
		records.add(record);
		final int last = records.size() - 1;
		fireTableRowsInserted(last, last);
	}

	public void setFormatter(final Formatter formatter)
	{
		this.formatter = formatter;
	}

	public Formatter getFormatter()
	{
		return formatter;
	}

	public void clear()
	{
		records.clear();
	}

	@Override
	public int getRowCount()
	{
		return records.size();
	}

	@Override
	public int getColumnCount()
	{
		return 2;
	}

	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex)
	{
		switch (columnIndex) {
		case 0:
			return records.get(rowIndex).getLevel();
		case 1:
			return records.get(rowIndex).getMessage();

		default:
			break;
		}
		return null;
	}

}
