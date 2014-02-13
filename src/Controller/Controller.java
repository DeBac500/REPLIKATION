package Controller;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import Conection.TCPClientRegistration;
import Conection.TCPVerbindung;
import DBHandler.DBConnector;
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
	private boolean client;
	private Logger log;
	private String rechp;
	private Directory dir;
	private DBConnector db;
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
	public Controller(String dburl, String dbusr, String dbpwd, String server, int port,String rechp,String logp) throws UnknownHostException, IOException{
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
		
		try {
			System.out.println("Starting Client...");
			client = true;
			db = new DBConnector(dburl, dbusr, dbpwd);
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
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("DB ERROR:\n"+  e.getMessage() + "\nClosing Program!");
			System.exit(0);
		} catch(Nothingtosync e){}
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
		
		try{
			System.out.println("Starting Server..");
			client = false;
			db = new DBConnector(dburl, dbusr, dbpwd);
			conect = new ArrayList<TCPVerbindung>();
			tcpreg = new TCPClientRegistration(this, port);
			tot = new Totengraeber();
			this.rechp = rechp;
			this.checkPath();
			dir=null;
			ui = new UserInterface(this);
			ui.start();
			System.out.println("Server started!");
	} catch (ClassNotFoundException | SQLException e) {
		System.out.println("DB ERROR:\n"+  e.getMessage() + "\nClosing Program!");
		System.exit(0);
	}
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
			if(db != null)db.close();
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
		boolean wronginput = true;
		try{
			wronginput = false;
			if(!(args.length >= 1)){
				wronginput =true;
			}else{
				if(args[0].equalsIgnoreCase("dc")){
					new Controller("jdbc:mysql://127.0.0.1/rep_db", "test", "test","127.0.0.1", 4444, "Rechnungen","replication.log");
				}else if(args[0].equalsIgnoreCase("ds")){
					new Controller("jdbc:mysql://127.0.0.1/rep_db1", "test", "test", 4444, "Rechnungen1","replication1.log");
				}else if(args[0].equalsIgnoreCase("s")){
					new Controller("jdbc:mysql://" + args[4] + "/" + args[5], args[6], args[7], Integer.parseInt(args[1]), args[2],args[3]);
				}else if(args[0].equalsIgnoreCase("c")){
					new Controller("jdbc:mysql://" + args[5] + "/" + args[6], args[7], args[8], args[1],  Integer.parseInt(args[2]), args[3],args[4]);
				}else
					wronginput = true;
			}
			
		} catch(NumberFormatException e){
			wronginput = true;
		} catch (UnknownHostException e) {
			wronginput = true;
		} catch (IOException e) {
			wronginput = true;
		} catch (NullPointerException e){
			wronginput = true;
		}
		if(wronginput){
			System.out.println("Wrong Arguments!");
			System.out.println("Please enter one of the following options:");
			System.out.println("dc - Default Settings for Client");
			System.out.println("ds - Default Settings for Server");
			System.out.println("s <PORT> <FILEDIR> <LOGFILE> <DB-IP> <DB-NAME> <DB-USER> <DB-PASS>");
			System.out.println("c <SERVER-IP> <PORT> <FILEDIR> <LOGFILE> <DB-IP> <DB-NAME> <DB-USER> <DB-PASS>");
		}
	}
}
