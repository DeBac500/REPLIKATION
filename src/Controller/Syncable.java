package Controller;

import java.io.IOException;
import java.io.Serializable;

import Conection.TCPVerbindung;
import FileHandler.Directory;
/**
 * Interface von Syncable
 * @author Dominik Backhausen
 */
public interface Syncable extends Serializable{
	/**
	 * Initialisiert Syncable
	 * @param dir
	 * @throws Nothingtosync
	 * @throws IOException
	 */
	public void setUp(Controller c) throws Nothingtosync, IOException;
	/**
	 * Sync fuer Cleint
	 * @param path
	 * @param dir
	 * @return
	 */
	public String syncClient(String path, Controller c,TCPVerbindung tcp);
	/**
	 * Sync fuer Server
	 * @param path
	 * @param c
	 * @return
	 */
	public String syncServer(String path,Controller c,TCPVerbindung tcp);
	
	public Directory getDir();
}
