package com.baseoneonline.java.houGeo;

class UnexpectedTokenException extends RuntimeException {
	public UnexpectedTokenException(final String expected, final String got,
			final int line) {
		super(String.format("Expected '%s' but got '%s' on line %s'", expected,
				got, line));
	}
}