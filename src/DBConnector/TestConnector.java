package DBConnector;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestConnector {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DBConnector db = new DBConnector("jdbc:mysql://localhost:25565/new",
				"root", "root");
		db.triggerINS("genre2");
//		db.triggerUPD("genre2");
//		db.triggerDEL("genre2");
	}
}
