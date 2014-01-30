package FileHandler;

import java.io.File;
import java.util.HashMap;

import Controller.MyFilter;
import Controller.Nothingtosync;

public class Directory {
	private HashMap<String , Long> file;
	private String path;
	
	public Directory(String path){
		this.path = path;
		file = new HashMap<String, Long>();
	}
	public void setUp() throws Nothingtosync{
		file.clear();
		File f = new File(path);
		if(f.exists()){
			if(f.isDirectory()){
				for(File temp : f.listFiles(new MyFilter())){
					file.put(temp.getName(), temp.lastModified());
				}
			}else
				throw new Nothingtosync();
		}else
			throw new Nothingtosync();
		
	}
	public boolean newer(String name , Long last) throws Nothingtosync{
		boolean newer = true;
		Long l = file.get(name);
		if( l == null)
			throw new Nothingtosync();
		if(l <= last)
			newer = false;
		return newer;
	}
}
