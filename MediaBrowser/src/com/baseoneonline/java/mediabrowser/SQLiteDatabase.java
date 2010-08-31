package com.baseoneonline.java.mediabrowser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.sqlite.JDBC;

public class SQLiteDatabase implements Database {

	Connection con;

	public SQLiteDatabase(final String dbfile) {
		try {

			Class.forName(JDBC.class.getName());
			con = DriverManager.getConnection("jdbc:sqlite:" + dbfile);

			final Statement stat = con.createStatement();
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS media (file);");
			con.close();

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

}
