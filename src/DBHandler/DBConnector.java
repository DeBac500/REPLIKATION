package DBHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class DBConnector {
	private String url;
	private String user;
	private String pass;
	private Connection conn = null;

	public DBConnector(String url, String user, String pass) throws ClassNotFoundException, SQLException {
		this.url = url;
		this.user = user;
		this.pass = pass;

		Class.forName("com.mysql.jdbc.Driver");

		System.out.println("Connecting to a selected database...");
		conn = DriverManager.getConnection(this.url, this.user, this.pass);
		System.out.println("Connected successfully...");

		setUP();
	}
	
	public void setUP() throws SQLException{
		String sql = "drop table if exists log_123";
		conn.createStatement().execute(sql);
		sql="CREATE TABLE log_123(act VARCHAR(255),tab VARCHAR(255),col VARCHAR(255),valNEW TEXT,valOLD TEXT,tim timestamp,PRIMARY KEY (tim))ENGINE=INNODB";
		conn.createStatement().execute(sql);
		
		ArrayList<String> tabn = showTables();
		for(int i = 0; i < tabn.size();i++){
			if(!tabn.get(i).equalsIgnoreCase("log_123")){
				this.triggerINS(tabn.get(i));
				this.triggerUPD(tabn.get(i));
				this.triggerDEL(tabn.get(i));
			}
		}
	}
	public void dellog(DBSaver d){
		try{
			String sql = "DELETE FROM log_123 WHERE tab=" +d.getTabname() + " AND act=" +d.getType();
			conn.createStatement().executeUpdate(sql);
		}catch(SQLException e){}
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
			System.err.println("SQL-ERROR: "+e.getMessage());
		} catch (NullPointerException e1){
			System.err.println("Error with database! Is the database running right now?");
		}
		return dat;
	}
	
	public int executeUpdate(String sql) throws SQLException {
		int re = conn.createStatement().executeUpdate(sql);
		return re;
	}
	
	public void triggerUPD(String tab){
		String sql;
		ResultSet rs = null;
		ArrayList<String> col=new ArrayList<String>();
		try {
			sql="show columns from "+tab;
			rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				col.add(rs.getString(1));
			}
			conn.createStatement().execute("DROP TRIGGER IF EXISTS logUPD_"+tab);
			//conn.createStatement().execute("DELIMITER | ");
			sql = ""
					+ ""
					+ "CREATE TRIGGER logUPD_"+tab+" AFTER UPDATE ON "+tab+" FOR EACH ROW "
					+ "BEGIN "
					+ "INSERT INTO log_123 (act,tab,col,valNEW,valOLD,tim) "
					+ "VALUES('UPDATE','"+tab+"','";
			for(int i=0;i<col.size();i++){
				if(i != col.size()-1){
					sql += col.get(i) + ",";
				}else{
					sql += col.get(i);
				}
			}
			sql += "',CONCAT(";
			for(int i=0;i<col.size();i++){
				if(i != col.size()-1){
					sql += "'\"',NEW." + col.get(i) + " , '\"' , ',' ,";
				}else{
					sql += "'\"',NEW." + col.get(i) + ",'\"' ";
				}			
			}
			sql += "),CONCAT(";
			for(int i=0;i<col.size();i++){
				if(i != col.size()-1){
					sql += "'\"',OLD." + col.get(i) + " , '\"' , ',' ,";
				}else{
					sql += "'\"',OLD." + col.get(i) + ",'\"' ";
				}			
			}
			sql +="),NOW());"
					+ " END "
					+ ""
					+ "";
			conn.createStatement().execute(sql);
			//conn.createStatement().execute("DELIMITER ; ");
		} catch (SQLException e) {
			System.err.println("Error with creating the UPDATE Triggers: "+e.getMessage());
		} catch (NullPointerException e1){
			System.err.println("Error with database! Is the database running right now?");
		}
		
	}
	
	public void triggerINS(String tab){
		String sql = "";
		ResultSet rs = null;
		ArrayList<String> col=new ArrayList<String>();
		try {
			sql="show columns from "+tab;
			rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				col.add(rs.getString(1));
			}
			conn.createStatement().execute("DROP TRIGGER IF EXISTS logINS_"+tab);
			//conn.createStatement().execute("DELIMITER | ");
			sql = ""
					+ ""
					+ "CREATE TRIGGER logINS_"+tab+" AFTER INSERT ON "+tab+" FOR EACH ROW "
					+ "BEGIN "
					+ "INSERT INTO log_123 (act,tab,col,valNEW,valOLD,tim) "
					+ "VALUES('INSERT','"+tab+"','";
			for(int i=0;i<col.size();i++){
				if(i != col.size()-1){
					sql += col.get(i) + ",";
				}else{
					sql += col.get(i);
				}
			}
			sql += "',CONCAT(";
			for(int i=0;i<col.size();i++){
				if(i != col.size()-1){
					sql += "'\"',NEW." + col.get(i) + " , '\"' , ',' ,";
				}else{
					sql += "'\"',NEW." + col.get(i) + ",'\"' ";
				}			
			}
			sql += "),NULL ";
			sql +=",NOW());"
					+ " END "
					+ ""
					+ "";
			conn.createStatement().execute(sql);
			//conn.createStatement().execute("DELIMITER ; ");
		} catch (SQLException e) {
			System.err.println("Error with creating the INSERT Triggers: "+e.getMessage());
		} catch (NullPointerException e1){
			System.err.println("Error with database! Is the database running right now?");
		}
		
	}
	
	public void triggerDEL(String tab){
		String sql = "";
		ResultSet rs = null;
		ArrayList<String> col=new ArrayList<String>();
		try {
			sql="show columns from "+tab;
			rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				col.add(rs.getString(1));
			}
			conn.createStatement().execute("DROP TRIGGER IF EXISTS logDEL_"+tab);
			//conn.createStatement().execute("DELIMITER | ");
			sql = ""
					+ ""
					+ "CREATE TRIGGER logDEL_"+tab+" BEFORE DELETE ON "+tab+" FOR EACH ROW "
					+ "BEGIN "
					+ "INSERT INTO log_123 (act,tab,col,valNEW,valOLD,tim) "
					+ "VALUES('DELETE','"+tab+"','";
			for(int i=0;i<col.size();i++){
				if(i != col.size()-1){
					sql += col.get(i) + ",";
				}else{
					sql += col.get(i);
				}
			}
			sql += "',NULL,CONCAT(";
			for(int i=0;i<col.size();i++){
				if(i != col.size()-1){
					sql += "'\"',OLD." + col.get(i) + " , '\"' , ',' ,";
				}else{
					sql += "'\"',OLD." + col.get(i) + ",'\"' ";
				}			
			}
			sql += ")";
			sql +=",NOW());"
					+ " END "
					+ ""
					+ "";
			conn.createStatement().execute(sql);
			//conn.createStatement().execute("DELIMITER ; ");
		} catch (SQLException e) {
			System.err.println("Error with creating the DELETE Triggers: "+e.getMessage());
		} catch (NullPointerException e1){
			System.err.println("Error with database! Is the database running right now?");
		} 
	}
	public ArrayList<DBSaver> readChanges(){
		String sql = "select * from log_123";
		ResultSet rs = null;
		ArrayList<DBSaver> dat=new ArrayList<DBSaver>();
		try {
			rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				dat.add(new DBSaver(rs.getString("act"), rs.getString("tab"), rs.getString("col"), rs.getString("valNEW"), rs.getString("valOLD")));
			}
			conn.createStatement().executeUpdate("DELETE FROM log_123");
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} catch (NullPointerException e1){
			System.err.println("Error with database! Is the database running right now?");
		}
		return dat;
	}
	public void close(){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}