package com.baseoneonline.java.mediabrowser.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import javax.swing.Timer;

import org.sqlite.JDBC;

import com.baseoneonline.java.mediabrowser.util.Util;

public class Database {

	private final String dbFile = "media.db";

	private static Database instance;

	private Connection con;

	private ArrayList<FileType> fileTypes;
	private ArrayList<File> mediaSources;

	private final int flushSettingsDelay = 300;

	private Database() {
		initConnection();
		initTables();
		readSettings();
		settingsTimer.setRepeats(false);

	}

	private final Timer settingsTimer = new Timer(flushSettingsDelay,
			new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					flushSettings();
				}
			});

	private void initConnection() {
		try {
			Class.forName(JDBC.class.getName());
			con = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void initTables() {
		sql("CREATE TABLE IF NOT EXISTS media (file PRIMARY KEY, type INTEGER, lastmodified LONG);");
		sql("CREATE TABLE IF NOT EXISTS filetypes ("
				+ "uid INTEGER PRIMARY KEY, " + "name, " + "extensions);");
		sql("CREATE TABLE IF NOT EXISTS mediasources (directory PRIMARY KEY);");
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
			final PreparedStatement prep = con
					.prepareStatement("REPLACE INTO media VALUES(?, ?, ?);");
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

	public ArrayList<FileType> getFileTypes() {
		return fileTypes;
	}

	public void setFileTypes(final ArrayList<FileType> fileTypes) {
		this.fileTypes = fileTypes;
		settingsTimer.restart();
	}

	public ArrayList<File> getMediaSources() {
		return mediaSources;
	}

	public void setMediaSources(final ArrayList<File> mediaSources) {
		this.mediaSources = mediaSources;
		settingsTimer.restart();
	}

	private void readSettings() {
		ResultSet rs;
		try {

			// Read media sources

			mediaSources = new ArrayList<File>();
			rs = con.createStatement().executeQuery(
					"SELECT * FROM mediasources;");
			while (rs.next()) {
				final File f = new File(rs.getString(1));
				mediaSources.add(f);
			}

			// Read filetypes

			fileTypes = new ArrayList<FileType>();
			rs = con.createStatement().executeQuery("SELECT * FROM filetypes;");
			while (rs.next()) {

				final FileType type = new FileType();
				type.uid = rs.getInt(1);
				type.name = rs.getString(2);
				type.extensions = Util.split(rs.getString(3), ",");
				fileTypes.add(type);

			}

		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void flushSettings() {
		Logger.getLogger(getClass().getName()).info("Flushing settings...");

		PreparedStatement prep;
		try {

			// Store media sources

			con.createStatement().executeUpdate("DELETE FROM mediasources;");
			prep = con.prepareStatement("REPLACE INTO mediasources VALUES(?);");
			for (final File f : mediaSources) {
				prep.setString(1, f.getAbsolutePath());
				prep.addBatch();
			}
			con.setAutoCommit(false);
			prep.executeBatch();
			con.commit();
			con.setAutoCommit(true);

			// Store file types

			con.createStatement().executeUpdate("DELETE FROM filetypes;");
			prep = con.prepareStatement("INSERT INTO filetypes VALUES(?,?,?);");
			for (final FileType t : fileTypes) {
				prep.setString(2, t.name);
				prep.setString(3, Util.join(t.extensions, ","));
				prep.addBatch();
			}
			con.setAutoCommit(false);
			prep.executeBatch();
			con.commit();
			con.setAutoCommit(true);

		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		readSettings();
	}

	public static Database get() {
		if (null == instance)
			instance = new Database();
		return instance;
	}

}
