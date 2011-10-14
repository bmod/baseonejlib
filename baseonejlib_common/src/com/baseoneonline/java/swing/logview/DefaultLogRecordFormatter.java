package com.baseoneonline.java.swing.logview;

import java.awt.Color;
import java.util.HashMap;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class DefaultLogRecordFormatter extends Formatter {

	private static HashMap<Level, Color> colorMap = new HashMap<Level, Color>();

	static {
		colorMap.put(Level.INFO, Color.GREEN);
		colorMap.put(Level.WARNING, Color.ORANGE);
		colorMap.put(Level.SEVERE, Color.RED);
	}

	public DefaultLogRecordFormatter() {
	}

	@Override
	public String format(LogRecord rec) {
		StringBuffer buf = new StringBuffer();
		buf.append("<");
		buf.append(rec.getLevel().getName());
		buf.append(rec.getMessage());
		return buf.toString();
	}
}
