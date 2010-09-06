package com.baseoneonline.java.mediabrowser.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.sqlite.JDBC;

public class SQLiteDatabase implements Database {

	private Connection con;

	public SQLiteDatabase(final String dbFile) {
		try {

			Class.forName(JDBC.class.getName());
			con = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
			final Statement stat = con.createStatement();
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS media (file);");

		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<MediaFile> getMediaFiles() {
		try {
			final ArrayList<MediaFile> files = new ArrayList<MediaFile>();
			final Statement stat = con.createStatement();
			final ResultSet rs = stat.executeQuery("SELECT * FROM media");
			while (rs.next()) {
				final MediaFile mf = new MediaFile(rs.getString(1));
				files.add(mf);
			}
			rs.close();
			con.close();
			return files;
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void storeMediaFiles(final List<MediaFile> chunks) {
		try {
			final PreparedStatement prep = con.prepareStatement("INSERT INTO media VALUES(?, ?, ?);");
			for (final MediaFile mf : chunks) {
				prepareMediaFile(prep, mf);
				prep.addBatch();
			}
			con.setAutoCommit(false);
			prep.executeBatch();
			con.commit();
			con.setAutoCommit(true);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void prepareMediaFile(final PreparedStatement prep, final MediaFile mf) throws SQLException {
		int i=1;
		prep.setString(i++, mf.file);
		prep.setLong(i++, mf.lastModified);
		prep.setString(i++, mf.type.uid);
	}

}
