package Controller;

import java.io.IOException;
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
			if(this.controller.getClient())
				this.handleCInput(in.nextLine());
			else
				this.handleSInput(in.nextLine());
		}
	}
	public void handleCInput(String in){
		if(in.equalsIgnoreCase("sync")){
			try {
				this.controller.send(this.controller.setUpFileSync());
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
			System.out.println("Invalide Command \nType sync or exit");
	}
	public void handleSInput(String in){
		if(in.equalsIgnoreCase("exit")){
			this.controller.shutdown();
		}else
			System.out.println("Invalide Command \nType exit");
	}

}
