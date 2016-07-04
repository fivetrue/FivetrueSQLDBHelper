package com.fivetrue.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.fivetrue.utils.TextUtils;

public class DatabaseHelper <T> {
	
	private static final String MYSQL_PORT = "3306";
	private static final String MYSQL_DRIVER_CLASS = "com.mysql.jdbc.Driver";
	private static final String MYSQL_PARAMS = "useEncoding=true&characterEncoding=UTF-8";
	
	public static interface ResultSetListener{
		void onReceived(ResultSet rs);
	}
	
	private String mDbServer = null;
	private String mDbName = null;
	private String mDbRootUrl = null;
	private String mDbUrl = null;
	private String mDbUserId = null;
	private String mDbUserPass = null;
	
	static{
		try {
			Class.forName(MYSQL_DRIVER_CLASS);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}                      
	}
	
	public DatabaseHelper(String dbServer, String dbName, String id, String pass){
		
		if(dbServer == null || dbName == null || id == null || pass == null){
			throw new IllegalArgumentException("Parameters must be not null");
		}
		mDbServer = dbServer;
		mDbName = dbName;
		mDbRootUrl = "jdbc:mysql://" + dbServer +  ":" + MYSQL_PORT + "/";
		mDbUrl = mDbRootUrl + mDbName;         
		mDbUserId = id;
		mDbUserPass = pass;
	}
	
	public int rawCountQuery (String query){
		int count = 0;
		Connection conn = connectDatabase();
		if(query != null && conn != null){
			Statement stmt = null;
			try {
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()){
					count = rs.getInt(1);
				}
				rs.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally{
				disconnectDatabase(conn);
			}
		}
		System.out.println(query + " = " + count);
		return count;
	}
	
	public ArrayList<T> rawSelectQuery(String query, Class <? extends DatabaseObject> cls){
		ArrayList<T> datas = new ArrayList<T>();
		Connection conn = connectDatabase();
		if(query != null && conn != null){
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(query);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()){
					DatabaseObject obj = null;
					try {
						obj = cls.newInstance();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					obj.setObject(rs);
					datas.add((T)obj);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally{
				disconnectDatabase(conn);
			}
		}
		return datas;
	}
	
	public String getDatabaseName(){
		return mDbName;
	}
	
	public String getDatabaseServer(){
		return mDbServer;
	}
	
	public Connection connectDatabase(){
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(mDbUrl + "?" + MYSQL_PARAMS, mDbUserId, mDbUserPass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	public void disconnectDatabase(Connection conn){
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Connection connectRootDatabase(){
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(mDbRootUrl + "?" + MYSQL_PARAMS, mDbUserId, mDbUserPass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
}
