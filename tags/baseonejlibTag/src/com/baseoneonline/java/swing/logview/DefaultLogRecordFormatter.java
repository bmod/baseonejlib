package com.baseoneonline.java.swing.logview;

import java.util.logging.LogRecord;

public class DefaultLogRecordFormatter implements LogRecordFormatter {
	@Override
	public String format(LogRecord rec) {
		return rec.getLevel().getName()+"\t"+rec.getMessage();
	}
}
