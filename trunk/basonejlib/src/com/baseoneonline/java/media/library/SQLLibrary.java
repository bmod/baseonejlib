package com.baseoneonline.java.media.library;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.baseoneonline.java.media.library.items.MediaItem;
import com.baseoneonline.java.tools.StringUtils;

public class SQLLibrary {

	private Connection con;

	private PreparedStatement prep;

	private final int flushInterval = 1000;
	private int flushCounter = 0;

	/**
	 * @param db
	 *            The name of the database file to use.
	 */
	public SQLLibrary(final String db) {
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:" + db);
			con.setAutoCommit(false);
			final Statement stat = con.createStatement();

			stat.executeUpdate("CREATE TABLE IF NOT EXISTS media ("
					+ "file VARCHAR(255)," + "artist VARCHAR(255),"
					+ "album VARCHAR(255)," + "title VARCHAR(255)," + ""
					+ "UNIQUE (file) ON CONFLICT REPLACE" + ");");

		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Queue an item for storage in the database, will commit automatically
	 * after {@link SQLLibrary#flushInterval} items.
	 * </p>
	 * <p>
	 * <b> Warning:</b> Use {@link SQLLibrary#flush()} to ensure all data is
	 * stored.
	 * </p>
	 * 
	 * @param item
	 */
	public void add(final MediaItem item) {
		try {
			if (null == prep) {
				prep = con
						.prepareStatement("INSERT INTO media VALUES (?,?,?,?);");
			}

			prep.addBatch();
			if (flushCounter >= flushInterval) {
				flushCounter = 0;
				flush();
			}
		} catch (final SQLException e) {
			e.printStackTrace();

		}
	}

	public void remove(final MediaItem m) {
		try {
			final Statement stat = con.createStatement();

			stat.executeUpdate("DELETE FROM media WHERE file=\""
					+ m.file.value.toURI().getRawPath() + "\";");
			con.commit();
		} catch (final SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Commit pending changes to the database.
	 */
	public void flush() {
		Logger.getLogger(getClass().getName()).info("Flushing.");
		try {
			prep.executeBatch();
			con.commit();
			prep = null;
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	public List<MediaItem> getItems() {
		System.out.println("GetItems");
		return getItemsQuery("SELECT * FROM media;");
	}

	private final String[] filterFields = { MediaItem.FIELD_ALBUM,
			MediaItem.FIELD_ARTIST, MediaItem.FIELD_TITLE };

	/**
	 * @param filterText
	 * @return
	 */
	public List<MediaItem> getItemsFiltered(final String filterText) {
		final String keywords = StringUtils.join(filterText.replaceAll("'",
				"''").split(" "), "%");

		final StringBuffer buf = new StringBuffer("SELECT * FROM media WHERE ");
		// Filter these fields

		// buf.append(StringUtils.join(filterFields, " OR "));
		final String[] ffields = new String[filterFields.length];
		for (int i = 0; i < filterFields.length; i++) {
			final StringBuffer b = new StringBuffer("(" + filterFields[i] + " ");

			b.append(" LIKE '%");
			b.append(keywords);
			b.append("%')");
			ffields[i] = b.toString();
		}
		buf.append(StringUtils.join(ffields, " OR "));
		System.out.println(buf);
		return getItemsQuery(buf.toString());
		// return getItemsQuery("SELECT * FROM media WHERE "
		// + "artist OR album OR title LIKE '%red%';");
	}

	/**
	 * @return All media items in the current database
	 */
	public List<MediaItem> getItemsQuery(final String query) {
		try {
			final Statement stat = con.createStatement();

			con.setAutoCommit(true);
			final ResultSet rs = stat.executeQuery(query);
			con.setAutoCommit(false);

			final List<MediaItem> list = new ArrayList<MediaItem>();
			while (rs.next()) {
				final URI fname = new URI(rs.getString(1));

				final MediaItem item = MediaItem.create(new File(fname));
				item.artist.value = rs.getString(2);
				item.album.value = rs.getString(3);
				item.title.value = rs.getString(4);

				list.add(item);
			}
			return list;
		} catch (final SQLException e) {

			e.printStackTrace();
		} catch (final URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
