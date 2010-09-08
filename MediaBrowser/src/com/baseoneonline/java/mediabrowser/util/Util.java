package com.baseoneonline.java.mediabrowser.util;

import java.util.ArrayList;

public class Util {

	private Util() {

	}

	public static String[] trim(final String[] a) {
		final String[] b = new String[a.length];
		for (int i = 0; i < a.length; i++) {
			b[i] = a[i].trim();
		}
		return b;
	}

	public static String join(final String[] extensions, final String separator) {
		final StringBuffer buf = new StringBuffer();
		for (int i = 0; i < extensions.length; i++) {
			buf.append(extensions[i]);
			if (i < extensions.length - 1)
				buf.append(separator);
		}
		return buf.toString();
	}

	public static String[] split(final String n, final String separator) {
		final String[] array = n.split(separator);
		final ArrayList<String> out = new ArrayList<String>();
		for (final String s : array) {
			final String ss = s.trim();
			if (ss.length() > 0) {
				out.add(ss);
			}
		}
		return out.toArray(new String[out.size()]);
	}

	public static boolean contains(final String[] arr, final String n) {
		for (final String a : arr) {
			if (n.equalsIgnoreCase(a))
				return true;
		}
		return false;
	}

	public static String extension(final String filename) {
		final int idx = filename.lastIndexOf('.');
		if (-1 == idx)
			return "";
		return filename.substring(idx+1);
	}

}
