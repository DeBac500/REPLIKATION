package Controller;

import java.util.Scanner;

public class UserInterface implements Runnable{
	private Controller controller;
	private Scanner in;
	private Thread thread;
	private boolean run;
	public UserInterface(Controller controller){
		this.controller = controller;
		in = new Scanner(System.in);
		thread = new Thread(this);
	}
	public void start(){
		run = true;
		if(thread != null) thread.start();
	}
	public void stop(){
		run = false;
	}
	@Override
	public void run() {
		while(run){
			this.handleInput(in.nextLine());
		}
	}
	public void handleInput(String in){
		System.out.println(in);
	}

}
