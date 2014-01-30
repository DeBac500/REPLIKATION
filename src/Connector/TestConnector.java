package Connector;

import java.sql.SQLException;

public class TestConnector {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DBConnector db = new DBConnector("jdbc:mysql://localhost:25565/test",
				"root", "root");
		try {
			System.out.println(db.showTables().getString(1));
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		try {
			System.out.println(db.showContent("genre").getString(1));
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
}
