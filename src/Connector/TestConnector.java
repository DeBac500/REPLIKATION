package Connector;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestConnector {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DBConnector db = new DBConnector("jdbc:mysql://localhost:25565/test",
				"root", "root");
		ResultSet rs=db.showTables();
		ResultSet rs2=db.showContent("genre");
		try {
			while(rs.next()){
				System.out.println(rs.getString(1));
			}
			System.out.println();
			while(rs2.next()){
				System.out.println(rs2.getString(1));
			}
		} catch (SQLException e1) {
			System.err.println(e1.getMessage());
		}
	}
}
