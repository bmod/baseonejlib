package com.baseoneonline.java.swing.logview;

import java.util.logging.LogRecord;

public class DefaultLogRecordFormatter implements LogRecordFormatter {
	@Override
	public String format(final LogRecord r) {
		return r.getLoggerName() + "\n" + r.getLevel().getName() + "\t"
				+ r.getMessage() + "\n";
	}
}