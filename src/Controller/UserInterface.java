package Controller;

import java.io.IOException;
import java.util.Scanner;

import FileHandler.FileSyncer;
/**
 * UserInterface fuer Usereingaben
 * @author Dominik Backhausen, Alexander Rieppel
 */
public class UserInterface implements Runnable{
	private Controller controller;
	private Scanner in;
	private Thread thread;
	private boolean run;
	/**
	 * Konstruktor
	 * @param controller
	 */
	public UserInterface(Controller controller){
		this.controller = controller;
		in = new Scanner(System.in);
		thread = new Thread(this);
	}
	/**
	 * Started thread
	 */
	public void start(){
		run = true;
		if(thread != null) thread.start();
	}
	/**
	 * stopt Thread
	 */
	public void stop(){
		run = false;
	}
	@Override
	public void run() {
		while(run){
			if(this.controller.getClient())
				this.handleCInput(in.nextLine());
			else
				this.handleSInput(in.nextLine());
		}
	}
	/**
	 * Input verarbietung fuer Client
	 * @param in
	 */
	public void handleCInput(String in){
		if(in.equalsIgnoreCase("sync")){
			try {
				FileSyncer s = this.controller.setUpFileSync();
				this.controller.send(s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Nothingtosync e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(in.equalsIgnoreCase("exit")){
			this.controller.shutdown();
		}else
			System.out.println("Invalid command \nType sync or exit");
	}
	/**
	 * Input verarbietung fuer server
	 * @param in
	 */
	public void handleSInput(String in){
		if(in.equalsIgnoreCase("exit")){
			this.controller.shutdown();
		}else
			System.out.println("Invalid command \nType exit");
	}

}
