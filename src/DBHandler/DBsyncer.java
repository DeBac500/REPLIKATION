package DBHandler;

import java.io.IOException;

import Conection.TCPVerbindung;
import Controller.Controller;
import Controller.Nothingtosync;
import Controller.Syncable;
import FileHandler.Directory;

public class DBsyncer implements Syncable{

	@Override
	public void setUp(Controller c) throws Nothingtosync, IOException {
				
	}

	@Override
	public String syncClient(String path, Controller c, TCPVerbindung tcp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String syncServer(String path, Controller c, TCPVerbindung tcp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Directory getDir() {
		return null;
	}

}
