package Connector;

public class TestConnector {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DBConnector db=new DBConnector( "jdbc:mysql://localhost:25565/test","root", "root");
		db.getTables();
		db.getContent("genre");
	}

}
