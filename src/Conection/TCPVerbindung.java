package Conection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import Controller.Controller;
import Controller.Syncable;
import FileHandler.FileSyncer;

public class TCPVerbindung implements Runnable{
	private int ID;
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Thread thread;
	private boolean run;
	private Controller c;
	
	public TCPVerbindung(Controller c, String host, int port) throws UnknownHostException, IOException{
		this.c = c;
		this.socket = new Socket(host, port);
		thread = new Thread(this);
	}
	public TCPVerbindung(Controller c,Socket socket){
		this.c = c;
		this.ID = socket.getPort();
		this.socket = socket;
		thread = new Thread(this);
	}
	
	public void openConection() throws IOException{
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		run = true;
		thread.start();
	}
	
	public void closeConection(){
		try {
			run = false;
			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
			this.c.getLog().error("ERROR duaring closing conection");
			System.exit(0);
		}
	}
	public void sendObject(Object o) throws IOException{
		out.writeObject(o);
		out.flush();
	}
	public String getAddress(){
		return socket.getInetAddress().getHostAddress();
	}
	@Override
	public void run() {
		this.c.getLog().info("Starting Listening");
		try{
			while(run){
				Thread.sleep(10);
	    		Object o = in.readObject();
	    		if(o instanceof Syncable){
	    			Syncable s = (Syncable)o;
	    			if(this.c.getClient())
	    				s.syncClient(this.c.getPath(), this.c.getDir());
	    			else
	    				s.syncServer(this.c.getPath(), this.c);
	    		}
			}

		} catch(IOException e){
			this.c.getLog().error("ERROR 404!!");
		} catch (InterruptedException e) {
		} catch (ClassNotFoundException e) {
			this.c.getLog().error("Could not receave Objekt!!");
		}
		this.closeConection();
	}

}
