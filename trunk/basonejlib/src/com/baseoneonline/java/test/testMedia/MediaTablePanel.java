package com.baseoneonline.java.test.testMedia;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.baseoneonline.java.test.media.SQLLibModel;
import com.baseoneonline.java.test.media.library.SQLLibrary;



public class MediaTablePanel extends JPanel {

	private final int timerDelay = 400;
	private final JTable table;
	private final JTextField tfFilter;
	private final SQLLibModel model;
	private final Timer timer;

	public MediaTablePanel(final SQLLibrary lib) {
		setLayout(new BorderLayout());
		model = new SQLLibModel(lib);
		timer = new Timer(timerDelay, new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				model.filter(tfFilter.getText().trim());
			}
		});
		timer.setRepeats(false);

		tfFilter = new JTextField();
		tfFilter.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(final DocumentEvent e) {
				timer.restart();
			}

			@Override
			public void insertUpdate(final DocumentEvent e) {
				timer.restart();
			}

			@Override
			public void removeUpdate(final DocumentEvent e) {
				timer.restart();
			}

		});

		add(tfFilter, BorderLayout.NORTH);

		table = new JTable(model);
		final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
				model);
		table.setRowSorter(sorter);

		add(new JScrollPane(table));
	}
}
