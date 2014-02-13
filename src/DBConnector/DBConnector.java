package DBConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
	}

	public ArrayList<String> showTables() {
		String sql = "show tables";
		ResultSet rs = null;
		ArrayList<String> dat=new ArrayList<String>();
		try {
			rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				dat.add(rs.getString(1));
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} catch (NullPointerException e1){
			System.err.println("Error with database! Is the database running right now?");
		}
		return dat;
	}

	public ArrayList<String> showContent(String tab) {
		String sql = "SELECT * FROM " + tab;
		ResultSet rs = null;
		ArrayList<String> dat=new ArrayList<String>();
		try {
			rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				dat.add(rs.getString(1));
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} catch (NullPointerException e1){
			System.err.println("Error with database! Is the database running right now?");
		}
		return dat;
	}
	
	public void triggerINS(String tab){
		String sql = "select count(*) FROM "+tab;
		ResultSet rs = null;
		int tabcount=-1;
		ArrayList<String> col=new ArrayList<String>();
		try {
			rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				tabcount=Integer.parseInt(rs.getString(1));
			}
			sql="show columns from "+tab;
			rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				col.add(rs.getString(1));
			}
			sql = "DROP TRIGGER IF EXISTS logINS_"+tab;
			conn.createStatement().execute(sql);
			sql = ""
					+ "DELIMITER |"
					+ "CREATE TRIGGER logINS_"+tab+" AFTER INSERT ON "+tab+" FOR EACH ROW"
					+ "BEGIN"
					+ "INSERT INTO log_123 (act,tab,col,val,tim)"
					+ "VALUES('INSERT','"+tab+"','";
//			for(int i=0;i<col.size();i++){
//				if(i!=col.size())
//					sql+=col.get(i)+",";
//				else
//					sql+=col.get(i);
//				
//			}
//			sql += "',";
//			for(int i=0;i<col.size();i++){
//				if(i!=col.size())
//					sql+="New."+col.get(i)+",";
//				else
//					sql+="New."+col.get(i);
//				
//			}
//			sql +=",NOW());"
//					+ "END"
//					+ "|"
//					+ "DELIMITER ;";
			conn.createStatement().executeQuery(sql);
		} catch (SQLException e) {
			System.err.println("Error with creating the INSERT Triggers: "+e.getMessage());
		} catch (NullPointerException e1){
			System.err.println("Error with database! Is the database running right now?");
		}
	}
	
	public void triggerUPD(String tab){
		String sql = "select count(*) FROM "+tab;
		ResultSet rs = null;
		int tabcount=-1;
		ArrayList<String> col=new ArrayList<String>();
		try {
			rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				tabcount=Integer.parseInt(rs.getString(1));
			}
			sql="show columns from "+tab;
			rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				col.add(rs.getString(1));
			}
			for(int i=0;i<col.size();i++){
				sql = "DROP TRIGGER IF EXISTS logUPD_"+col.get(i);
				conn.createStatement().executeQuery(sql);
				sql = ""
					+ "DELIMITER |"
					+ "CREATE TRIGGER logUPD_"+col.get(i)+" AFTER UPDATE ON "+tab+" FOR EACH ROW "
					+ "BEGIN "
					+ "INSERT INTO log_123 (act,tab,col,val,tim)"
					+ "VALUES('UPDATE','"+tab+"','"+col.get(i)+"',NEW."+col.get(i)+",NOW());"
					+ "END"
					+ "|"
					+ "DELIMITER ;";
				conn.createStatement().executeQuery(sql);
			}
			
		} catch (SQLException e) {
			System.err.println("Error with creating the UPDATE Triggers: "+e.getMessage());
		} catch (NullPointerException e1){
			System.err.println("Error with database! Is the database running right now?");
		}
	}
	
	public void triggerDEL(String tab){
		String sql = "select count(*) FROM "+tab;
		ResultSet rs = null;
		int tabcount=-1;
		ArrayList<String> col=new ArrayList<String>();
		try {
			rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				tabcount=Integer.parseInt(rs.getString(1));
			}
			sql="show columns from "+tab;
			rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				col.add(rs.getString(1));
			}
			for(int i=0;i<col.size();i++){
				sql = "DROP TRIGGER IF EXISTS logDEL_"+col.get(i);
				conn.createStatement().executeQuery(sql);
				sql = ""
					+ "DELIMITER |"
					+ "CREATE TRIGGER logDEL_"+col.get(i)+" BEFORE DELETE ON "+tab+" FOR EACH ROW "
					+ "BEGIN "
					+ "INSERT INTO log_123 (act,tab,col,val,tim)"
					+ "VALUES('DELETE','"+tab+"','"+col.get(i)+"',NEW."+col.get(i)+",NOW());"
					+ "END"
					+ "|"
					+ "DELIMITER ;";	
				conn.createStatement().executeQuery(sql);
			}
			
		} catch (SQLException e) {
			System.err.println("Error with creating the DELETE Triggers: "+e.getMessage());
		} catch (NullPointerException e1){
			System.err.println("Error with database! Is the database running right now?");
		}
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
