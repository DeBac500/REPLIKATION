package FileHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;


public class FileSaver implements Serializable{
	private String name;
	private byte[] bytes;
	private long lastmodi;
	
	public FileSaver(String name) {
		this.name = name;
	}
	
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
	
	public void write(String path) throws IOException{
		File f = new File(path + "/" + name);
		if(!f.exists()){
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(bytes);
			fos.close();
		}
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getLastmodi() {
		return lastmodi;
	}

	public void setLastmodi(long lastmodi) {
		this.lastmodi = lastmodi;
	}

	public static void main(String[] args){
		FileSaver fs = new FileSaver("test.pdf");
		try {
			fs.read();
			System.out.println("Read finished!");
			Thread.sleep(1000);
			fs.write("Rechnungen");
			System.out.println("Write finished!");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
