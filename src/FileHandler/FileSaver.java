package FileHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Speicher Files
 * @author Dominik Backhausen
 */
public class FileSaver implements Serializable{
	private String name;
	private byte[] bytes;
	private long lastmodi;
	/**
	 * Konstruktor
	 * @param name
	 */
	public FileSaver(String name) {
		this.name = name;
	}
	/**
	 * Speichern
	 * @throws IOException
	 */
	public void read()throws IOException{
		File f = new File("Rechnungen/"+name);
		lastmodi = f.lastModified();
		if(f.isFile()){
			FileInputStream fis = new FileInputStream(f);
			bytes = new byte[(int)f.length()];
			fis.read(bytes);
			fis.close();
		}
	}
	/**
	 * Schreiben
	 * @param path
	 * @throws IOException
	 */
	public void write(String path) throws IOException{
		File f = new File(path + "/" + name);
		if(!f.exists()){
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(bytes);
			fos.close();
		}
	}
	/**
	 * naem zurueck
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * Letzte aenderung zurueck
	 * @return
	 */
	public long getLastmodi() {
		return lastmodi;
	}
}
