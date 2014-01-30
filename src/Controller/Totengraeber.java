package Controller;

import java.io.IOException;
import java.util.ArrayList;

import Conection.TCPVerbindung;

public class Totengraeber implements Runnable{
	private ArrayList<TCPVerbindung> tot;
	private Thread thread;
	private boolean run;
	
	public Totengraeber(){
		tot = new ArrayList<TCPVerbindung>();
		run = true;
		thread= new Thread(this);
	}
	public void addDead(TCPVerbindung newdead){
		tot.add(newdead);
	}
	public void stop(){
		run = false;
	}
	@Override
	public void run() {
		try{
			TCPVerbindung temp;
			while(run){
				if(tot.size() > 0){
					temp = tot.get(0);
					tot.remove(0);
					temp.closeConection();
					temp=null;
				}
				thread.sleep(1000);
			}
			while(tot.size() > 0){
				temp = tot.get(0);
				tot.remove(0);
				temp.closeConection();
				temp=null;
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

}
