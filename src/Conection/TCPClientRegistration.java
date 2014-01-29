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
			System.out.println("Binding to port " + port + ", please wait  ...");
			server = new ServerSocket(port);  
			System.out.println("Server started: " + server);
			run = true;
			t = new Thread(this);
			this.start();
		}catch(IOException ioe){  
			System.out.println("Can not bind to port: " + port); 
		}
	}

	/**
	 * The Run-Methode of a Thread which is waiting for new Clients
	 */
	@Override
	public void run() {
		while (run){
			try{
				System.out.println("Waiting for a client ..."); 
				this.controller.addClient(server.accept()); 
				System.out.println("Client Verbunden!!");
			}catch(IOException ioe){
				System.out.println("Server accept error: " + ioe);
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
