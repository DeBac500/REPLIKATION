package Controller;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import Conection.TCPClientRegistration;
import Conection.TCPVerbindung;
import FileHandler.Directory;
import FileHandler.FileSyncer;
/**
 * Controller des Programms
 * @author Dominik Backhausen, Alexander Rieppel
 *
 */
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
	/**
	 * Konstruktor
	 * @param dburl
	 * @param dbusr
	 * @param dbpwd
	 * @param server
	 * @param port
	 * @param rechp
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws Nothingtosync
	 */
	public Controller(String dburl, String dbusr, String dbpwd, String server, int port,String rechp,String logp) throws UnknownHostException, IOException, Nothingtosync{
		log = Logger.getRootLogger();
		
		PatternLayout layout1 = new PatternLayout("%m%n");
		PatternLayout layout = new PatternLayout( "%d{dd-MM-yyyy HH:mm:ss} REP %m%n" );
		FileAppender fileAppender = new FileAppender( layout1, logp, true );
		log.addAppender(fileAppender);
		log.setLevel(Level.ALL);
		log.info("------------------------------------------------------------------------------------------------");
		log.removeAppender(fileAppender);
		fileAppender = new FileAppender( layout, logp, true );
		ConsoleAppender consoleAppender = new ConsoleAppender( layout );
		log.addAppender(consoleAppender);
		log.addAppender(fileAppender);
		
		
		System.out.println("Starting Client...");
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
		System.out.println("Client started");
	}
	/**
	 * Konstruktor
	 * @param dburl
	 * @param dbusr
	 * @param dbpwd
	 * @param port
	 * @param rechp
	 * @throws IOException 
	 */
	public Controller(String dburl, String dbusr, String dbpwd, int port,String rechp,String logp) throws IOException{
		log = Logger.getRootLogger();
		
		PatternLayout layout1 = new PatternLayout("%m%n");
		PatternLayout layout = new PatternLayout( "%d{dd-MM-yyyy HH:mm:ss} REP %m%n" );
		FileAppender fileAppender = new FileAppender( layout1, logp, true );
		log.addAppender(fileAppender);
		log.setLevel(Level.ALL);
		log.info("------------------------------------------------------------------------------------------------");
		log.removeAppender(fileAppender);
		fileAppender = new FileAppender( layout, logp, true );
		ConsoleAppender consoleAppender = new ConsoleAppender( layout );
		log.addAppender(consoleAppender);
		log.addAppender(fileAppender);
		
		System.out.println("Starting Server..");
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
		System.out.println("Server started!");
	}
	/**
	 * Überprueft den File Pfad
	 */
	public void checkPath(){
		File p = new File(this.rechp);
		if(p.exists()){
			if(!p.isDirectory())
				if(p.isFile()){
					System.err.println("Path is a File!\n Stopping Programm!");
					this.shutdown();
				}
		}else{
			p.mkdirs();
			System.out.println("Path: " +this.rechp +"created");
		}
	}
	/**
	 * Fuegt Clients hinzu
	 * @param socket
	 * @throws IOException
	 */
	public void addClient(Socket socket) throws IOException{
		System.out.println("New Client connected: " + socket.getInetAddress().getHostAddress());
		conect.add(new TCPVerbindung(this, socket));
		conect.get(conect.size()-1).openConection();
	}
	/**
	 * Loescht Clients
	 * @param tcp
	 */
	public void removeCleint(TCPVerbindung tcp){
		System.out.println("Client Disconnected: " + tcp.getAddress());
		conect.remove(tcp);
		if(tot!= null)
			tot.addDead(tcp);
		else{
			tcp.closeConection();
		}
	}
	/**
	 * Sendet an alle Clients
	 * @param o
	 * @throws IOException
	 */
	public void send(Object o) throws IOException{
		for(TCPVerbindung temp : conect){
			temp.sendObject(o);
		}
	}
	/**
	 * Beendet den Server
	 */
	public void shutdown(){
		System.out.println("Shutting down...");
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
	/**
	 * Erstellt ein FileSync
	 * @return
	 * @throws Nothingtosync
	 * @throws IOException
	 */
	public FileSyncer setUpFileSync() throws Nothingtosync, IOException{
		FileSyncer sync = new FileSyncer(this.rechp);
		sync.setUp(this);
		return sync;
	}
	public FileSyncer setUpFileSync(Directory d) throws Nothingtosync, IOException{
		FileSyncer sync = new FileSyncer(this.rechp);
		this.dir =d;
		sync.setUp(this);
		return sync;
	}
	/**
	 * Gibt Logger zurueck
	 * @return
	 */
	public Logger getLog(){
		return this.log;
	}
	/**
	 * Gibt Pfad zurueck
	 * @return
	 */
	public String getPath(){
		return this.rechp;
	}
	/**
	 * Gibt directory zurueck
	 * @return
	 */
	public Directory getDir(){
		return this.dir;
	}
	/**
	 * Gibt An ob client oder Server
	 * @return
	 */
	public boolean getClient(){return this.client;}
	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args){
		try{
			boolean wronginput = false;
			if(!(args.length >= 1)){
				wronginput =true;
			}else{
				if(args[0].equalsIgnoreCase("dc")){
					new Controller("", "", "","127.0.0.1", 4444, "Rechnungen","replication.log");
				}else if(args[0].equalsIgnoreCase("ds")){
					new Controller("", "", "", 4444, "Rechnungen1","replication1.log");
				}else if(args[0].equalsIgnoreCase("s")){
					new Controller("", "", "", Integer.parseInt(args[1]), args[2],args[3]);
				}else if(args[0].equalsIgnoreCase("c")){
					new Controller("", "", "", args[1],  Integer.parseInt(args[2]), args[3],args[4]);
				}else
					wronginput = true;
			}
			if(wronginput){
				//TODO anpassen
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
