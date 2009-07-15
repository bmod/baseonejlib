package com.baseoneonline.java.tools;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public class StringUtils {

	/**
	 * Convert an array into a string, each element delimited by a specified
	 * delimiter <br>
	 * Example:<br>
	 * <br>
	 * <code>["One", "Two", "Three"]</code><br>
	 * <br>
	 * becomes<br>
	 * <br>
	 * "One, Two, Three"
	 * 
	 * @param array
	 *            The array to join
	 * @param delim
	 *            The delimiter to be used.
	 * @return A String containing all the elements of the array.
	 */
	public static String join(final String[] array, final String delim) {
		final StringBuffer sb = join(array, delim, new StringBuffer());
		return sb.toString();
	}

	/**
	 * Append an array to a {@link StringBuffer}, each element delimited by a
	 * specified delimiter <br>
	 * Example:<br>
	 * <br>
	 * <code>["One", "Two", "Three"]</code><br>
	 * <br>
	 * becomes<br>
	 * <br>
	 * "One, Two, Three"
	 * 
	 * @param array
	 *            The array to join
	 * @param delim
	 *            The delimiter to be used.
	 * @param sb
	 *            Append the elements to this {@link StringBuffer}
	 * @return The provided {@link StringBuffer} containing all the elements of
	 *         the array.
	 */
	public static StringBuffer join(final String[] array, final String delim,
			final StringBuffer sb) {
		for (int i = 0; i < array.length; i++) {
			if (i != 0) sb.append(delim);
			sb.append(array[i]);
		}
		return sb;
	}

	/**
	 * Convenience method to quickly decode url strings. Eliminates error
	 * throwing, but logs the error instead.
	 * 
	 * @param url
	 * @return
	 */
	public static String urlDecode(final String url) {
		try {
			return URLDecoder.decode(url, Charset.defaultCharset().name());
		} catch (final UnsupportedEncodingException e) {
			Logger.getLogger(StringUtils.class.getName()).warning(
					"Unsupported Encoding in URL: " + url);
		}
		return null;
	}

	public static String padFront(String number, final int length,
			final String chr) {
		while (number.length() < length) {
			number = chr + number;
		}
		return number;
	}

	public static String stripExtension(final String filename) {
		final int idx = filename.lastIndexOf(".");
		if (-1 != idx) { return filename.substring(0, idx); }
		return filename;
	}

	/**
	 * Replace or add extension to a file(name)
	 * 
	 * @param file
	 *            The source filename to use.
	 * @param newExtension
	 *            Don't provide a dot, it will be added.
	 * @return A new file with the provided extension.
	 */
	public static File replaceExtension(final File file,
			final String newExtension) {
		final String fname = stripExtension(file.getAbsolutePath());
		return new File(fname + "." + newExtension);
	}

}
