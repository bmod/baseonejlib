package com.baseoneonline.java.coretext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Logger;



public class CoreElement {
	
	
	private Reader reader;
	
	private char charReadTooMuch;
	public void readFromFile(String filename) {
		File f = new File(filename); 
		try {
			reader = new FileReader(f);
			int c;
			StringBuffer buf = new StringBuffer();
			while (-1 != (c = reader.read())) {
				buf.append((char)c);
			}
			parse(buf);
		} catch (FileNotFoundException e) {
			Logger.getLogger(getClass().getName()).warning(
				"File not found: " + f.getAbsolutePath());
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).warning(
				"IO Exception while reading: " + f.getAbsolutePath());
		}
	}
	
	private void parse(StringBuffer str) {
		
		
		
	}
	protected char scanWhitespace() throws Exception {
		for (;;) {
			final char ch = this.readChar();
			switch (ch) {
			case ' ':
			case '\t':
			case '\n':
			case '\r':
				break;
			default:
				return ch;
			}
		}
	}
	
	/**
	 * Reads a character from a reader.
	 */
	protected char readChar() throws Exception {
		if (this.charReadTooMuch != '\0') {
			final char ch = this.charReadTooMuch;
			this.charReadTooMuch = '\0';
			return ch;
		} else {
			final int i = this.reader.read();
			if (i < 0) {
				throw this.unexpectedEndOfData();
			} else if (i == 10) {
				return '\n';
			} else {
				return (char) i;
			}
		}
	}
	protected void unreadChar(final char ch) {
		this.charReadTooMuch = ch;
	}
	protected Exception unknownEntity(final String name) {
		final String msg = "Unknown or invalid entity: &" + name + ";";
		return new Exception(msg);
	}
	protected Exception unexpectedEndOfData() {
		final String msg = "Unexpected end of data reached";
		return new Exception(msg);
	}
}
