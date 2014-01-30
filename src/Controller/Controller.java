package Controller;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import Conection.TCPClientRegistration;
import Conection.TCPVerbindung;
import FileHandler.Directory;
import FileHandler.FileSyncer;

public class Controller {
	private TCPClientRegistration tcpreg;
	private ArrayList<TCPVerbindung> conect;
	private Totengraeber tot;
	private UserInterface ui;
	private String dburl, dbusr, dbpwd;
	private boolean client;
	private Logger log;
	private String rechp;
	private Directory dir;
	
	public Controller(String dburl, String dbusr, String dbpwd, String server, int port,String rechp) throws UnknownHostException, IOException, Nothingtosync{
		log = Logger.getLogger("REPLIKATION");
		log.setLevel(Level.INFO);
		log.info("Starting Client...");
		client = true;
		this.dburl = dburl;
		this.dbusr =dbusr;
		this.dbpwd = dbpwd;
		conect = new ArrayList<TCPVerbindung>();
		conect.add(new TCPVerbindung(this,server, port));
		this.rechp = rechp;
		this.dir = new Directory(rechp);
		dir.setUp();
		log.info("Client started");
	}
	public Controller(String dburl, String dbusr, String dbpwd, int port,String rechp){
		log = Logger.getLogger("REPLIKATION");
		log.setLevel(Level.INFO);
		log.info("Starting Server..");
		client = false;
		this.dburl = dburl;
		this.dbusr =dbusr;
		this.dbpwd = dbpwd;
		conect = new ArrayList<TCPVerbindung>();
		tcpreg = new TCPClientRegistration(this, port);
		tot = new Totengraeber();
		this.rechp = rechp;
		dir=null;
		log.info("Server started!");
	}
	
	public void addClient(Socket socket) throws IOException{
		log.info("New Client conected: " + socket.getInetAddress().getHostAddress());
		conect.add(new TCPVerbindung(this, socket));
		conect.get(conect.size()-1).openConection();
		try{
		conect.get(conect.size()-1).sendObject(this.setUpFileSync());
		}catch(Nothingtosync e){}
	}
	public void removeCleint(TCPVerbindung tcp){
		log.info("Client Disconected: " + tcp.getAddress());
		conect.remove(tcp);
		tot.addDead(tcp);
	}
	public void shutdown(){
		log.info("Programm closing...");
		try {
			if(ui != null)ui.stop();
			if(tcpreg != null)tcpreg.stop();
			if(conect != null)
				while(conect.size() >0)
					this.removeCleint(conect.get(0));
			if(tot!=null)tot.stop();
			Thread.sleep(1000);
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public FileSyncer setUpFileSync() throws Nothingtosync, IOException{
		FileSyncer sync = new FileSyncer(this.rechp);
		sync.setUp();
		return sync;
	}
	public Logger getLog(){
		return this.log;
	}
	public String getPath(){
		return this.rechp;
	}
}
