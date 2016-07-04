package com.fivetrue.db.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.fivetrue.db.DBMessage;
import com.fivetrue.db.DatabaseHelper;
import com.fivetrue.db.DatabaseObject;
import com.fivetrue.db.DatabaseObject.QueryBuilder;
import com.fivetrue.utils.TextUtils;

public abstract class DatabaseManagerImpl <T extends DatabaseObject> {

	protected DatabaseHelper<T> mDbHelper = null;

	protected DatabaseManagerImpl(String server, String dbName, String id, String password){
		mDbHelper = new DatabaseHelper<T>(server, dbName, 
				id, password);
		//		createDatabase(getDefaultData());
	}

	public DatabaseHelper<T> getDatabaseHelper(){
		return mDbHelper;
	}

	protected boolean hasWhere(String query){
		boolean has = false;
		if(query != null && query.length() > 0){
			has = query.contains("where");
			if(!has){
				has = query.contains("WHERE");
			}
		}
		return has;
	}

	protected StringBuilder appendWhere(StringBuilder sb, String key, String value, boolean isText){
		if(key != null && key.length() > 0 && value != null){
			boolean hasWhere = hasWhere(sb.toString());
			if(isText){
				sb.append(hasWhere ? key + "='" + value +"'" : " where " + key + "='" + value + "'");	
			}else{
				sb.append(hasWhere ? key + "=" + value : " where " + key + "=" + value);
			}
			sb.append(" ");
		}
		return sb;
	}

	public ArrayList<T> rawQuery(String query){
		System.out.println("ojkwon : rawQuery : query = " + query);
		ArrayList<T> datas = getDatabaseHelper().rawSelectQuery(query, getDatabaseObjectClass());
		return datas;
	}

	public DBMessage insertObject(DatabaseObject data){
		DBMessage msg = new DBMessage();
		Connection conn = getDatabaseHelper().connectDatabase();
		if(conn != null){
			if(data != null){
				PreparedStatement ps = null;
				String query = data.insertQuery();
				System.out.println("ojkwon insertObject : query = " + query);
				try {
					ps = conn.prepareStatement(query);
					msg.setRow(ps.executeUpdate());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					msg.setMessage(e1.getMessage());
				}
			}
			getDatabaseHelper().disconnectDatabase(conn);
		}
		return msg;
	}

	public DBMessage removeObject(DatabaseObject data){
		DBMessage msg = new DBMessage();
		Connection conn = getDatabaseHelper().connectDatabase();
		if(conn != null){
			if(data != null){
				PreparedStatement ps = null;
				String query = data.deleteQuery();
				System.out.println("ojkwon removeObject : query = " + query);
				try {
					ps = conn.prepareStatement(query);
					msg.setRow(ps.executeUpdate());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					msg.setMessage(e1.getMessage());
				}
			}
			getDatabaseHelper().disconnectDatabase(conn);
		}
		return msg;
	}

	public DBMessage updateObject(DatabaseObject data){
		DBMessage msg = new DBMessage();
		Connection conn = getDatabaseHelper().connectDatabase();
		if(conn != null){
			if(data != null){
				PreparedStatement ps = null;
				String query = data.updateQuery();
				System.out.println("ojkwon update : query = " + query);
				try {
					ps = conn.prepareStatement(query);
					msg.setRow(ps.executeUpdate());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					msg.setMessage(e1.getMessage());
				}
			}
			getDatabaseHelper().disconnectDatabase(conn);
		}
		return msg;
	}

	public DBMessage create(){

		DBMessage msg = new DBMessage();
		Connection conn = getDatabaseHelper().connectDatabase();
		if(conn != null){
			DatabaseObject object = null;
			try {
				object = getDatabaseObjectClass().newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(object != null){
				PreparedStatement ps = null;
				String query = object.createQuery(getDatabaseHelper().getDatabaseName());
				System.out.println("ojkwon update : create = " + query);
				try {
					ps = conn.prepareStatement(query);
					msg.setRow(ps.executeUpdate());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					msg.setMessage(e1.getMessage());
				}
			}
		}
		getDatabaseHelper().disconnectDatabase(conn);
		return msg;
	}

	public DBMessage drop(){

		DBMessage msg = new DBMessage();
		Connection conn = getDatabaseHelper().connectDatabase();
		if(conn != null){
			PreparedStatement ps = null;
			String query = "DROP TABLE " + getDatabaseHelper().getDatabaseName() 
					+ "." + getDatabaseObjectClass().getSimpleName().toLowerCase() +";";
			System.out.println("ojkwon update : drop = " + query);
			try {
				ps = conn.prepareStatement(query);
				msg.setRow(ps.executeUpdate());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				msg.setMessage(e1.getMessage());
			}
		}
		getDatabaseHelper().disconnectDatabase(conn);
		return msg;
	}

	public String getSelectQuery(String[] selection, String where){
		return QueryBuilder.newInstance().selectQuery(getDatabaseObjectClass(), selection, where);
	}

	//	public ArrayList<T> getSelectQueryData(String[] selection, String[] where){
	public ArrayList<T> getSelectQueryData(String[] selection, String where){
		String query = QueryBuilder.newInstance().selectQuery(getDatabaseObjectClass(), selection, where);
		return rawQuery(query);
	}
	
	public int getCountData(String where){
		String query = "SELECT COUNT(*) "
				+ " FROM " + getDatabaseObjectClass().getSimpleName().toLowerCase();
		if(!TextUtils.isEmpty(where)){
			query += " WHERE " + where;
		}
		query += ";";
					
		return getDatabaseHelper().rawCountQuery(query);
	}

	protected abstract Class <? extends T> getDatabaseObjectClass();

	public abstract T getDefaultData();

	//	public boolean isTableExists(){
	//		Connection conn = getDatabaseHelper().connectRootDatabase();
	//		int count = 0;
	//		if(conn != null){
	//			String query = "SELECT COUNT(*) "
	//					+ "FROM information_schema.tables "
	//					+ "WHERE table_schema='" + getDatabaseHelper().getDatabaseName() +"' AND table_name='"+ getDatabaseObjectClass().getSimpleName().toLowerCase() +"'";
	//			System.out.println("isTableExists = " + query);
	//			PreparedStatement pstmt = null;
	//			try {
	//				pstmt = conn.prepareStatement(query);
	//				ResultSet rs = pstmt.executeQuery();
	//				
	//				count = rs.getInt(0);
	//			} catch (SQLException e1) {
	//				// TODO Auto-generated catch block
	//				e1.printStackTrace();
	//			} finally{
	//				getDatabaseHelper().disconnectDatabase(conn);
	//			}
	//		}
	//	    return count > 0;
	//	}
	//	
}
