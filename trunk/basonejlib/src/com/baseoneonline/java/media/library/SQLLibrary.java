package com.baseoneonline.java.media.library;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SQLLibrary {

	private Connection con;

	private PreparedStatement prep;

	private final int flushInterval = 1000;
	private int flushCounter = 0;

	/**
	 * @param db
	 *            The name of the database file to use.
	 */
	public SQLLibrary(String db) {
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:" + db);
			con.setAutoCommit(false);
			Statement stat = con.createStatement();

			// stat.executeUpdate("DROP TABLE IF EXISTS media");

			stat.executeUpdate("CREATE TABLE IF NOT EXISTS media ("
				+ "file VARCHAR(255)," + "UNIQUE (file) ON CONFLICT REPLACE"
				+ ");");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
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
				prep = con.prepareStatement("INSERT INTO media VALUES (?);");
			}

			prep.setString(1, item.file.getAbsolutePath());

			prep.addBatch();
			if (flushCounter >= flushInterval) {
				flushCounter = 0;
				flush();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void remove(MediaItem m) {
		try {
			Statement stat = con.createStatement();
			stat.executeUpdate("DELETE FROM media WHERE file='"+m.file.getAbsolutePath()+"';");
			con.commit();
		} catch (SQLException e) {
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return All media items in the current database
	 */
	public List<MediaItem> getItems() {
		try {
			Statement stat = con.createStatement();

			con.setAutoCommit(true);
			ResultSet rs = stat.executeQuery("SELECT * FROM media;");
			con.setAutoCommit(false);

			List<MediaItem> list = new ArrayList<MediaItem>();
			while (rs.next()) {
				MediaItem item = new MediaItem(new File(rs.getString("file")));
				list.add(item);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
