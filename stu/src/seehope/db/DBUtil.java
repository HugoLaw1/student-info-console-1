package seehope.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

	private static DBUtil db = new DBUtil();
	private static Connection conn = null;

	private DBUtil() {
		super();
	}

	public static DBUtil getInstance() {
		return db;
	}

	public void disConnection() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Connection getConnection() {
		if (conn == null) {
			synchronized (DBUtil.class) {
				if (conn == null) {
					connect();
				}
			}
		}
		return conn;
	}

	private void connect() {
		try {
			// 1.加载驱动类
			Class.forName("com.mysql.jdbc.Driver");
			// 2.获取连接
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/score?useUnicode=true&characterEncoding=utf-8","root", "mysql");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close(ResultSet rs, Statement stmt) {
		closeResultSet(rs);
		closeStatement(stmt);
	}

	public void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void closeStatement(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.cancel();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
