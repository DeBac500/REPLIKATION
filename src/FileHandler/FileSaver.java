package FileHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;


public class FileSaver implements Serializable{
	private String name;
	private byte[] bytes;
	
	public FileSaver(String name) {
		this.name = name;
	}
	
	public void read()throws IOException{
		File f = new File("Rechnungen/"+name);
		if(f.isFile()){
			FileInputStream fis = new FileInputStream(f);
			bytes = new byte[(int)f.length()];
			fis.read(bytes);
			fis.close();
		}
	}
	
	public void write() throws IOException{
		File f = new File("6*/testneu.pdf");
		if(!f.exists()){
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(bytes);
			fos.close();
		}
	}
	
	public static void main(String[] args){
		FileSaver fs = new FileSaver("test.pdf");
		try {
			fs.read();
			System.out.println("Read Fertig!");
			Thread.sleep(1000);
			fs.write();
			System.out.println("write Fertig!");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
