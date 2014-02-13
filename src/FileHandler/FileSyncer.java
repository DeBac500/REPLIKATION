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

import Conection.TCPVerbindung;
import Controller.Controller;
import Controller.MyFilter;
import Controller.Nothingtosync;
import Controller.Syncable;
/**
 * Syncronisiert die Files
 * @author Dominik Backhausen, Alexander Rieppel
 */
public class FileSyncer implements Syncable{
	private HashMap<String,FileSaver> files;
	private String path;
	private Directory d;
	/**
	 * Konstruktor
	 * @param path
	 */
	public FileSyncer(String path){
		this.path = path;
		files = new HashMap<String,FileSaver>();
	}
	
	@Override
	public void setUp(Controller c) throws  IOException{
		File dir = new File(this.path);
		boolean direx = false;
		this.d=c.getDir();
		if(dir.exists())
			direx = true;
		else
			if(dir.mkdirs())
				direx =  true;
		
		if(direx){
			for( File temp : dir.listFiles(new MyFilter())){
				try{
					if(d != null){
						if(d.newer(temp.getName(), temp.lastModified())){
							load(temp);
						}
					}else{
						load(temp);
					}
				}catch(Nothingtosync e){load(temp);}
			}
		}
		
	}
	
	public void load(File temp){
		FileSaver s = new FileSaver(temp.getName());
		s.read(this.path);
		files.put(temp.getName(), s);
		System.out.println("Loading... " + temp.getName());
	}

	@Override
	public String syncClient(String path, Controller c,TCPVerbindung tcp) {
		String msg = "";
		File f = new File(path);
		if(!f.exists())
			f.mkdirs();
		if(f.isDirectory()){
			for(File temp : f.listFiles(new MyFilter())){
				if(files.containsKey(temp.getName())){
					FileSaver s = files.get(temp.getName());
					try {
						if(c.getDir().newer(s.getName(),s.getLastmodi())){
							if(c.getDir().newer(temp.getName(), temp.lastModified()))
								this.copyFile(temp, path);
						}else{
							this.copyFile(temp, path);
						}
						temp.delete();
						if(s.write(path)){
				        	String send = s.getName() + ": "+ tcp.getAddressEnd() + " -> " + tcp.getAddress() + " OKAY";
							tcp.sendObject(send);
							c.getLog().info(send);
						}else{
							String send = s.getName() + ": "+ tcp.getAddressEnd() + " -> " + tcp.getAddress() + " FEHLGESCHLAGEN";
							tcp.sendObject(send);
							c.getLog().info(send);
						}
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
			    		Map.Entry pairs = (Map.Entry)it.next();
			    		FileSaver s = (FileSaver) pairs.getValue();
				        new File(path + "/" + s.getName()).delete();
				        if(s.write(path)){
				        	String send = s.getName() + ": "+ tcp.getAddressEnd() + " -> " + tcp.getAddress() + " OKAY";
							tcp.sendObject(send);
							c.getLog().info(send);
						}else{
							String send = s.getName() + ": "+ tcp.getAddressEnd() + " -> " + tcp.getAddress() + " FEHLGESCHLAGEN";
							tcp.sendObject(send);
							c.getLog().info(send);
						}
			    	}catch(IOException e){e.printStackTrace();}
			    }
			}
			try {
				c.getDir().setUp();
			} catch (Nothingtosync e) {
			}
			msg = "Sync finished";
		}else
			msg = "No Directory";
		return msg;
	}

	@Override
	public String syncServer(String path,Controller c,TCPVerbindung tcp) {
		System.out.println("Test");
		String msg ="";
		Iterator it = files.entrySet().iterator();
	    while (it.hasNext()) {
	    	try{
	    		Map.Entry pairs = (Map.Entry)it.next();
		        FileSaver s = (FileSaver) pairs.getValue();
		        new File(path + "/" + s.getName()).delete();
		        if(s.write(path)){
		        	String send = s.getName() + ": "+ tcp.getAddressEnd() + " -> " + tcp.getAddress() + " OKAY";
					tcp.sendObject(send);
					c.getLog().info(send);
				}else{
					String send = s.getName() + ": "+ tcp.getAddressEnd() + " -> " + tcp.getAddress() + " FEHLGESCHLAGEN";
					tcp.sendObject(send);
					c.getLog().info(send);
				}
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
	/**
	 * Kopiert Files
	 * @param from
	 * @param path
	 * @throws IOException
	 */
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
	
	public Directory getDir(){
		return this.d;
	}
}
