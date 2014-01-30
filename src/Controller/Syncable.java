package Controller;

import java.io.IOException;
import java.io.Serializable;

import FileHandler.Directory;

public interface Syncable extends Serializable{
	public void setUp(Directory dir) throws Nothingtosync, IOException;
	public String syncClient(String path, Directory dir);
	public String syncServer(String path,Controller c);
}
