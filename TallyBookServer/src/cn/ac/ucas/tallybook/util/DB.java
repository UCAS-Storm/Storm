package cn.ac.ucas.tallybook.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB {
	/* configure.properties */
    private static String DB_PATH = null;//指向要访问的数据库名
    private static String DB_USER = null;// MySQL配置时的用户名
    private static String DB_PASSWD = null; // MySQL配置时的密码
	
		static {
			Configuration config = new Configuration("configure.properties");
			DB_PATH = config.getValue("DB_PATH");
			DB_USER = config.getValue("DB_USER");
			DB_PASSWD = config.getValue("DB_PASSWD");
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		public static Connection getConnection() {
			Connection conn = null;
			try {
				conn = DriverManager.getConnection(DB_PATH, DB_USER, DB_PASSWD);

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return conn;
		}
		
		public static void close(Connection conn) {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		public static void close(PreparedStatement pstmt) {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		public static void close(ResultSet rs) {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
}
