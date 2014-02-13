package FileHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import Controller.Nothingtosync;

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
	 * @throws Nothingtosync 
	 */
	public boolean read(String path){
		try{
			File f = new File(path + "/"+name);
			if(f.isFile()){
				lastmodi = f.lastModified();
				FileInputStream fis = new FileInputStream(f);
				bytes = new byte[(int)f.length()];
				fis.read(bytes);
				//System.out.println("ByteLänge: " + bytes.length);
				fis.close();
				return true;
			}else{
				return false;
			}
		}catch(IOException e){
			return false;
		}
	}
	/**
	 * Schreiben
	 * @param path
	 * @throws IOException
	 */
	public boolean write(String path){
		try{
			File f = new File(path + "/" + name);
			if(!f.exists()){
				f.createNewFile();
				FileOutputStream fos = new FileOutputStream(f);
				//System.out.println("ByteLänge: " + bytes.length);
				fos.write(bytes);
				fos.close();
				return true;
			}else
				return false;
		}catch(IOException e){
			return false;
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
