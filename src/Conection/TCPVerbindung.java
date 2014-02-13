package Conection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import Controller.Controller;
import Controller.Nothingtosync;
import Controller.Syncable;
import FileHandler.Directory;
import FileHandler.FileSyncer;
/**
 * Verbindung zu Cleints bzw Server
 * @author Dominik Backhausen
 *
 */
public class TCPVerbindung implements Runnable{
	private int ID;
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Thread thread;
	private boolean run;
	private Controller c;
	/**
	 * Konstruktor
	 * @param c
	 * @param host
	 * @param port
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public TCPVerbindung(Controller c, String host, int port) throws UnknownHostException, IOException{
		this.c = c;
		this.socket = new Socket(host, port);
		thread = new Thread(this);
	}
	/**
	 * Konsturktor
	 * @param c
	 * @param socket
	 */
	public TCPVerbindung(Controller c,Socket socket){
		this.c = c;
		this.ID = socket.getPort();
		this.socket = socket;
		thread = new Thread(this);
	}
	/**
	 * Oeffnet die Verbindung
	 * @throws IOException
	 */
	public void openConection() throws IOException{
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		run = true;
		thread.start();
	}
	/**
	 * Sliesst die Verbindung
	 */
	public void closeConection(){
		try {
			run = false;
			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
			System.err.println("ERROR during process of 'close connection'");
			System.exit(0);
		}
	}
	/**
	 * Sendet Objekte
	 * @param o
	 * @throws IOException
	 */
	public void sendObject(Object o) throws IOException{
		out.writeObject(o);
		out.flush();
	}
	/**
	 * Gibt Zieladdresse Zurueck
	 * @return
	 */
	public String getAddressEnd(){
		return socket.getInetAddress().getHostAddress();
	}
	public String getAddress(){
		return socket.getLocalAddress().getHostAddress();
	}
	@Override
	public void run() {
		System.out.println("Starting Listening");
		try{
			while(run){
				Thread.sleep(10);
	    		Object o = in.readObject();
	    		if(o instanceof Syncable){
	    			Syncable s = (Syncable)o;
	    			if(this.c.getClient())
	    				s.syncClient(this.c.getPath(), this.c,this);
	    			else{
	    				s.syncServer(this.c.getPath(), this.c,this);
	    				Directory dir = s.getDir();
	    				if(dir != null){
		    				FileSyncer temp = this.c.setUpFileSync(dir);
		    				this.sendObject(temp);
	    				}else{
	    					//TODO DB
	    				}
	    			}
	    		}
	    		if(o instanceof String){
	    			this.c.getLog().info((String)o);
	    		}
			}

		} catch(IOException e){
			this.c.removeCleint(this);
		} catch (InterruptedException e) {
		} catch (ClassNotFoundException e) {
			System.err.println("Could not receave Object!");
		} catch (Nothingtosync e) {
		}
		this.closeConection();
	}

}
