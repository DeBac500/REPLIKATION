package Connector;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {
	private String url;
	private String user;
	private String pass;
	private Connection conn = null;

	public DBConnector(String url, String user, String pass) {
		this.url = url;
		this.user = user;
		this.pass = pass;
		try {

			Class.forName("com.mysql.jdbc.Driver");

			System.out.println("Connecting to a selected database...");
			setConn(DriverManager.getConnection(this.url, this.user, this.pass));
			System.out.println("Connected successfully...");

		} catch (SQLException se) {
			System.err.println(se.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		// finally {
		// // finally block used to close resources
		// try {
		// if (stmt != null)
		// conn.close();
		// } catch (SQLException se) {
		// }// do nothing
		// try {
		// if (conn != null)
		// conn.close();
		// } catch (SQLException se) {
		// se.printStackTrace();
		// }// end finally try
		// }// end try
	}

	public ResultSet showTables() {
		String sql = "show tables";
		ResultSet rs = null;
		try {
			rs = conn.createStatement().executeQuery(sql);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
//		DatabaseMetaData md = null;
//		try {
//			md = this.conn.getMetaData();
//			rs = md.getTables(null, null, "%", null);
//		} catch (SQLException e) {
//			System.err.println(e.getMessage());
//		}
		return rs;
	}

	public ResultSet showContent(String tablename) {
		String sql = "SELECT * FROM "+tablename;
		ResultSet rs = null;
		try {
			rs = conn.createStatement().executeQuery(sql);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return rs;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * @param pass
	 *            the pass to set
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}

	/**
	 * @return the conn
	 */
	public Connection getConn() {
		return conn;
	}

	/**
	 * @param conn
	 *            the conn to set
	 */
	public void setConn(Connection conn) {
		this.conn = conn;
	}
}
