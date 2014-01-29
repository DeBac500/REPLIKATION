package FileHandler;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import Controller.Controller;
import Controller.MyFilter;
import Controller.Nothingtosync;
import Controller.Syncable;

public class FileSyncer implements Syncable{
	private ArrayList<FileSaver> files;
	private String path;
	
	public FileSyncer(String path){
		this.path = path;
		files = new ArrayList<FileSaver>();
	}
	@Override
	public String sync(String path) {
		String msg = "Not Implemented!";
		
		return msg;
	}
	@Override
	public void setUp() throws Nothingtosync, IOException{
		File dir = new File(this.path);
		boolean direx = false;
		
		if(dir.exists())
			direx = true;
		else
			if(dir.mkdirs())
				direx =  true;
		
		File[] todo = dir.listFiles(new MyFilter());
		if(todo.length <= 0){
			throw new Nothingtosync();
		}else{
			for(int i = 0; i < todo.length; i++){
				files.add(new FileSaver(todo[i].getName()));
				files.get(i).read();
			}
		}
	}
	public static void main(String[] args){
//		FileSyncer s = new FileSyncer("Rechnungen");
//		try {
//			s.setUp();
//		} catch (Nothingtosync e) {
//			System.out.println("Nothing to sync");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
