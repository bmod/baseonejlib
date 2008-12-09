package com.baseoneonline.java.jme.modelviewer.gui;

import java.awt.BorderLayout;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogPanel extends JPanel {
	public LogPanel() {
		setLayout(new BorderLayout());
		final JTextArea taLog = new JTextArea();
		add(new JScrollPane(taLog));
		
		Logger.getLogger("").addHandler(new Handler() {
			@Override
			public void close() throws SecurityException {
			}
			@Override
			public void flush() {
			}
			@Override
			public void publish(final LogRecord record) {
				taLog.append(record.getMessage()+"\n");
			}
		});
	}
}
