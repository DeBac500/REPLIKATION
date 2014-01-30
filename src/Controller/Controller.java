package Controller;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


import java.util.logging.Level;
import java.util.logging.Logger;

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
		log = Logger.getLogger("REPLI");
		log.setLevel(Level.INFO);
		log.info("Starting Client...");
		client = true;
		this.dburl = dburl;
		this.dbusr =dbusr;
		this.dbpwd = dbpwd;
		conect = new ArrayList<TCPVerbindung>();
		conect.add(new TCPVerbindung(this,server, port));
		conect.get(0).openConection();
		this.rechp = rechp;
		this.checkPath();
		this.dir = new Directory(rechp);
		dir.setUp();
		ui = new UserInterface(this);
		ui.start();
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
		this.checkPath();
		dir=null;
		ui = new UserInterface(this);
		ui.start();
		log.info("Server started!");
	}
	public void checkPath(){
		File p = new File(this.rechp);
		if(p.exists()){
			if(!p.isDirectory())
				if(p.isFile()){
					log.severe("Path is a File!\n Stopping Programm!");
					this.shutdown();
				}
		}else{
			p.mkdirs();
			log.info("Path created");
		}
	}
	
	public void addClient(Socket socket) throws IOException{
		log.info("New Client connected: " + socket.getInetAddress().getHostAddress());
		conect.add(new TCPVerbindung(this, socket));
		conect.get(conect.size()-1).openConection();
	}
	public void removeCleint(TCPVerbindung tcp){
		log.info("Client Disconnected: " + tcp.getAddress());
		conect.remove(tcp);
		if(tot!= null)
			tot.addDead(tcp);
		else{
			tcp.closeConection();
		}
	}
	public void send(Object o) throws IOException{
		for(TCPVerbindung temp : conect){
			temp.sendObject(o);
		}
	}
	public void shutdown(){
		log.info("Shutting down...");
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
		sync.setUp(this.dir);
		return sync;
	}
	public Logger getLog(){
		return this.log;
	}
	public String getPath(){
		return this.rechp;
	}
	public Directory getDir(){
		return this.dir;
	}
	public boolean getClient(){return this.client;}
	
	public static void main(String[] args){
		try{
			boolean wronginput = false;
			if(!(args.length >= 1)){
				wronginput =true;
			}else{
				if(args[0].equalsIgnoreCase("dc")){
					new Controller("", "", "","127.0.0.1", 4444, "Rechnungen");
				}else if(args[0].equalsIgnoreCase("ds")){
					new Controller("", "", "", 4444, "Rechnungen1");
				}else if(args[0].equalsIgnoreCase("s")){
					new Controller("", "", "", Integer.parseInt(args[1]), args[2]);
				}else if(args[0].equalsIgnoreCase("c")){
					new Controller("", "", "", args[1],  Integer.parseInt(args[2]), args[3]);
				}else
					wronginput = true;
			}
			if(wronginput){
				System.out.println("Please enter one of the following options:");
				System.out.println("The default options with 50 Tests(N) and 5000 ArraySize");
				System.out.println("<d> Testing both algorithm");
				System.out.println("<fd> Testing the fact algorithm");
				System.out.println("<sd> Testing the sort algorithm");
				System.out.println("The custom options with custom arraysizes");
				System.out.println("<b> <N> <ArraySize> Testing both algorithm ");
				System.out.println("<f> <N> <ArraySize> Testing the fact algorithm");
				System.out.println("<s> <N> <ArraySize> Testing the sort algorithm");
			}
		} catch(NumberFormatException e){
			System.err.println("Bitte richtige Zahlen eingaben!");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Nothingtosync e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
