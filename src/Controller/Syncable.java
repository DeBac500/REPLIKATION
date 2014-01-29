package Controller;

import java.io.IOException;
import java.io.Serializable;

public interface Syncable extends Serializable{
	public void setUp() throws Nothingtosync, IOException;
	public String sync(String path);
}
