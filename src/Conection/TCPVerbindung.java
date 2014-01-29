package Conection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import Controller.Controller;

public class TCPVerbindung implements Runnable{
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
		this.socket = socket;
		thread = new Thread(this);
	}
	
	public void openConection() throws IOException{
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		run = true;
		thread.start();
	}
	
	public void closeConection() throws IOException{
		run = false;
		out.close();
		in.close();
		socket.close();
	}
	public void sendObject(Object o) throws IOException{
		out.writeObject(o);
		out.flush();
	}
	@Override
	public void run() {
		try{
			while(run){
				
			}
			this.closeConection();
		}catch(IOException e){
			
		}
	}

}
