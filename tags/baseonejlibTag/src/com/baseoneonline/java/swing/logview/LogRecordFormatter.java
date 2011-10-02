package com.baseoneonline.java.swing.logview;

import java.util.logging.LogRecord;

public interface LogRecordFormatter {
	public String format(LogRecord rec) ;
}
