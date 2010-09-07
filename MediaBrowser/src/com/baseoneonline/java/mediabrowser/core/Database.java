package com.baseoneonline.java.mediabrowser.core;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.sqlite.JDBC;

import com.baseoneonline.java.mediabrowser.util.Util;

public class Database {

	private final String dbFile = "media.db";

	private static Database instance;

	private Connection con;

	private ArrayList<FileType> fileTypes;

	private Database() {
		try {

			Class.forName(JDBC.class.getName());
			con = DriverManager.getConnection("jdbc:sqlite:" + dbFile);

			sql("CREATE TABLE IF NOT EXISTS media (file PRIMARY KEY, type INTEGER, lastmodified LONG);");
			sql("CREATE TABLE IF NOT EXISTS filetypes ("
					+ "uid INTEGER PRIMARY KEY AUTOINCREMENT, " + "name, "
					+ "extensions);");
			sql("CREATE TABLE IF NOT EXISTS mediasources (directory PRIMARY KEY);");

		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setFileTypes(final ArrayList<FileType> fileTypes) {

	}

	public ArrayList<FileType> getFileTypes() {
		try {

			final ArrayList<FileType> fileTypes = new ArrayList<FileType>();
			final ResultSet rs =
					Database.get().query("SELECT * FROM filetypes;");
			while (rs.next()) {

				final FileType type = new FileType();
				type.uid = rs.getInt(1);
				type.name = rs.getString(2);
				type.extensions = Util.split(rs.getString(3), ",");
				fileTypes.add(type);

			}
			return fileTypes;

		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}

	}

	private void sql(final String statement) {
		Statement stat;
		try {
			stat = con.createStatement();
			stat.executeUpdate(statement);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public ResultSet query(final String sql) {
		try {
			final Statement stat = con.createStatement();
			return stat.executeQuery(sql);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

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

	public void storeMediaFiles(final List<MediaFile> chunks) {
		try {
			final PreparedStatement prep =
					con.prepareStatement("REPLACE INTO media VALUES(?, ?, ?);");
			for (final MediaFile mf : chunks) {

				prep.setString(1, mf.file);
				prep.setInt(2, mf.type.uid);
				prep.setLong(3, mf.lastModified);

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

	public static Database get() {
		if (null == instance)
			instance = new Database();
		return instance;
	}

	public ArrayList<File> getMediaSources() {
		try {
			final ArrayList<File> dirs = new ArrayList<File>();
			final ResultSet rs = query("SELECT * FROM mediasources;");
			while (rs.next()) {
				final File f = new File(rs.getString(1));
				dirs.add(f);
			}
			return dirs;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setMediaSources(final ArrayList<File> directories) {
		try {
			con.createStatement().executeUpdate("DELETE FROM mediasources;");
			final PreparedStatement prep =
					con.prepareStatement("REPLACE INTO mediasources VALUES(?);");
			for (final File f : directories) {
				prep.setString(1, f.getAbsolutePath());
				prep.addBatch();
			}
			con.setAutoCommit(false);
			prep.executeBatch();
			con.commit();
			con.setAutoCommit(true);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

}
