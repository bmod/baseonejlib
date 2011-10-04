package com.baseoneonline.java.swing.logview;

import java.awt.BorderLayout;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogView extends JPanel {

	private final JTextArea taLog;

	private LogRecordFormatter formatter;

	private Level level;
	
	public LogView() {
		this(null, new DefaultLogRecordFormatter());
	}
	
	public LogView(String loggerName, final LogRecordFormatter formatter) {
		if (null == loggerName)
			loggerName = "";
		Logger.getLogger(loggerName).addHandler(logHandler);
		taLog = new JTextArea();
		taLog.setEditable(false);
		setLayout(new BorderLayout());
		add(new JScrollPane(taLog));
		setRecordFormatter(formatter);
		Logger.getLogger(getClass().getName()).info("LogView");
	}
	
	public void clear() {
		taLog.setText("");
	}
	
	public void setLevel(final Level level) {
		this.level = level;
	}
	
	public Level getLevel() {
		return level;
	}

	private final Handler logHandler = new Handler() {
		@Override
		public void close() throws SecurityException {
		};

		@Override
		public void flush() {
		};

		@Override
		public void publish(final LogRecord record) {
			taLog.append(formatter.format(record) + "\n");
			taLog.setCaretPosition(taLog.getDocument().getLength());
		};
	};

	public static class DefaultLogRecordFormatter implements LogRecordFormatter {
		@Override
		public String format(final LogRecord r) {
			return r.getLoggerName()
					+ "\n"
					+ r.getLevel().getName()
					+ "\t"
					+ r.getMessage()
					+ "\n";
		}
	}

	public void setRecordFormatter(final LogRecordFormatter formatter) {
		if (null == formatter) {
			this.formatter = new DefaultLogRecordFormatter();
		}
		else {
			this.formatter = formatter;
		}
	}

}