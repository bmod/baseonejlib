package com.baseoneonline.java.test.media.library.items;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;

import com.baseoneonline.java.test.media.Attribute;

public abstract class MediaItem {

	public static String[] EXT_MUSIC = { ".mp3", ".wma", ".ogg", ".aac",
			".flac" };
	public static String[] EXT_VIDEO = { ".mov", ".avi", ".wmv", ".mp4" };
	public static String[] EXT_IMAGE = { ".jpg", ".gif", ".tga" };

	public static String[][] EXT_ALL = { EXT_MUSIC, EXT_IMAGE, EXT_VIDEO };

	public static String FIELD_FILE = "File", FIELD_ARTIST = "Artist",
			FIELD_ALBUM = "Album", FIELD_TITLE = "Title",
			FIELD_GENRE = "Genre", FIELD_MTIME = "lastModified",
			FIELD_IMAGE = "image";

	public static String[] FIELDS = { FIELD_FILE, FIELD_ARTIST, FIELD_ALBUM,
			FIELD_TITLE, FIELD_GENRE, FIELD_MTIME, FIELD_IMAGE };

	public Attribute<File> file = new Attribute<File>(FIELD_FILE);
	public Attribute<Long> mtime = new Attribute<Long>(FIELD_MTIME);
	public Attribute<String> artist = new Attribute<String>(FIELD_ARTIST);
	public Attribute<String> album = new Attribute<String>(FIELD_ALBUM);
	public Attribute<String> title = new Attribute<String>(FIELD_TITLE);
	public Attribute<String> genre = new Attribute<String>(FIELD_GENRE);
	public Attribute<BufferedImage> image = new Attribute<BufferedImage>(
			FIELD_IMAGE);

	public Attribute<?>[] attributes = { file, artist, album, title, genre };

	HashMap<String, Attribute<?>> map = new HashMap<String, Attribute<?>>();

	public MediaItem(final File f) {
		file.value = f;
		for (final Attribute<?> at : attributes) {
			map.put(at.name, at);
		}
	}

	public abstract String getDisplayName();

	@Override
	public String toString() {
		return getDisplayName();
	}

	public abstract void readMetadata();

	/**
	 * @param string
	 * @return
	 */
	public Attribute<?> getAttribute(final String string) {
		return map.get(string);
	}

	/**
	 * @param f
	 * @return
	 */
	public static MediaItem create(final File f) {
		for (final String ext : EXT_MUSIC) {
			if (f.getName().endsWith(ext)) { return new MusicItem(f); }
		}
		for (final String ext : EXT_IMAGE) {
			if (f.getName().endsWith(ext)) { return new ImageItem(f); }
		}
		for (final String ext : EXT_VIDEO) {
			if (f.getName().endsWith(ext)) { return new VideoItem(f); }
		}
		return new UnknownItem(f);
	}

	public static FileFilter FILE_FILTER = new FileFilter() {
		@Override
		public boolean accept(final File f) {
			if (f.isDirectory()) return true;
			for (final String[] exts : EXT_ALL) {
				for (final String x : exts) {
					if (f.getName().toLowerCase().endsWith(x)) return true;
				}
			}
			return false;
		}
	};
}
