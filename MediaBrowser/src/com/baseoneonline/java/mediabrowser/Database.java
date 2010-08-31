package com.baseoneonline.java.mediabrowser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.sqlite.JDBC;

public class Database {

	private static Database instance;
	
	private final String dbfile = "media.db";
	
	Connection con;

	private Database() {
		try {
			
			Class.forName(JDBC.class.getName());
			con = DriverManager.getConnection("jdbc:sqlite:"+dbfile);
			
			final Statement stat = con.createStatement();
			stat.executeUpdate("create table media (file);");
			con.close();
			
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	

	public static Database get() {
		if (null == instance)
			instance = new Database();
		return instance;
	}

}
