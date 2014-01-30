package Conection;

import java.io.IOException;
import java.net.ServerSocket;

import Controller.Controller;

/**
 * This Class waits for Clients which wont to Connect to this server
 * @author Dominik Backhausen
 *
 */
public class TCPClientRegistration implements Runnable {
	private ServerSocket server = null;
	private Controller controller;
	private boolean run;
	private Thread t;
	/**
	 *  Construktor to Create a Instance of this Class 
	 *  @param sc the ServerController
	 *  @param port the port on which this Class will listen
	 */
	public TCPClientRegistration(Controller sc, int port) {
		this.controller = sc;
		try{  
			this.controller.getLog().info("Binding to port " + port + ", please wait  ...");
			server = new ServerSocket(port);  
			this.controller.getLog().info("Server started: " + server);
			run = true;
			t = new Thread(this);
			this.start();
		}catch(IOException ioe){  
			this.controller.getLog().severe("Can't bind to port: " + port); 
			System.exit(0);
		}
	}

	/**
	 * The Run-Methode of a Thread which is waiting for new Clients
	 */
	@Override
	public void run() {
		while (run){
			try{
				this.controller.getLog().info("Waiting for a client ..."); 
				this.controller.addClient(server.accept()); 
			}catch(IOException ioe){
				this.controller.getLog().severe("Server accept error: \n" + ioe.getMessage());
			}
		}
	}
	/**
	 * This start the Thread of this Class
	 */
	public void start(){
		if(t != null)
			t.start();
	}
	/**
	 * This stops the Thread of this Class
	 * @throws IOException
	 */
	public void stop() throws IOException{
		run = false;
		if(server != null)server.close();
	}
}
