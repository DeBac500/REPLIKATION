package FileHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Controller.Controller;
import Controller.MyFilter;
import Controller.Nothingtosync;
import Controller.Syncable;

public class FileSyncer implements Syncable{
	private HashMap<String,FileSaver> files;
	private String path;
	
	public FileSyncer(String path){
		this.path = path;
		files = new HashMap<String,FileSaver>();
	}
	
	@Override
	public void setUp(Directory d) throws  IOException{
		File dir = new File(this.path);
		boolean direx = false;
		
		if(dir.exists())
			direx = true;
		else
			if(dir.mkdirs())
				direx =  true;
		
		if(direx){
			for( File temp : dir.listFiles(new MyFilter())){
				try{
					if(d.newer(temp.getName(), temp.lastModified())){
						FileSaver s = new FileSaver(temp.getName());
						s.read();
						files.put(temp.getName(), s);
						System.out.println("Laden.. " + temp.getName());
					}
				}catch(Nothingtosync e){}
			}
		}
	}

	@Override
	public String syncClient(String path, Directory dir) {
		String msg = "";
		File f = new File(path);
		if(!f.exists())
			f.mkdirs();
		if(f.isDirectory()){
			for(File temp : f.listFiles(new MyFilter())){
				if(files.containsKey(temp.getName())){
					FileSaver s = files.get(temp.getName());
					try {
						if(dir.newer(s.getName(),s.getLastmodi())){
							if(dir.newer(temp.getName(), temp.lastModified()))
								this.copyFile(temp, path);
						}else{
							this.copyFile(temp, path);
						}
						temp.delete();
						s.write(path);
					} catch (Nothingtosync e) {
					} 
					catch (IOException e) {
						e.printStackTrace();
					}
					files.remove(temp.getName());
				}
			}
			if(files.size() > 0){
				Iterator it = files.entrySet().iterator();
			    while (it.hasNext()) {
			    	try{
				        FileSaver s = (FileSaver) it.next();
				        s.write(path);
			    	}catch(IOException e){e.printStackTrace();}
			    }
			}
			try {
				dir.setUp();
			} catch (Nothingtosync e) {
			}
			msg = "Sync finished";
		}else
			msg = "No Directory";
		return msg;
	}

	@Override
	public String syncServer(String path,Controller c) {
		String msg ="";
		Iterator it = files.entrySet().iterator();
	    while (it.hasNext()) {
	    	try{
	    		Map.Entry pairs = (Map.Entry)it.next();
		        FileSaver s = (FileSaver) pairs.getValue();
		        new File(path + "/" + s.getName()).delete();
		        s.write(path);
	    	}catch(IOException e){e.printStackTrace();}
	    }
	    try {
			c.send(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    msg = "Sync finished";
		return msg;
	}
	public void copyFile(File from, String path) throws IOException{
		FileInputStream in  = new FileInputStream(from);
		File to = new File(path + "/CONFLICTED");
		if(!to.exists())
			to.mkdirs();
		else{
			to = new File(path + "/CONFLICTED/" + from.getName());
			if(!to.exists()){
				to.createNewFile();
				FileOutputStream out = new FileOutputStream(to);
				byte[] temp = new byte[(int)from.length()];
				in.read(temp);
				out.write(temp);
				out.close();
			}
		}
		in.close();
	}
}
