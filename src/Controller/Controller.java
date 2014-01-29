package Controller;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import Conection.TCPClientRegistration;
import Conection.TCPVerbindung;

public class Controller {
	private TCPClientRegistration tcpreg;
	private ArrayList<TCPVerbindung> conect;
	private Totengraeber tot;
	private UserInterface ui;
	private String dburl, dbusr, dbpwd;
	private boolean client;
	
	public Controller(String dburl, String dbusr, String dbpwd, String server, int port) throws UnknownHostException, IOException{
		client = true;
		this.dburl = dburl;
		this.dbusr =dbusr;
		this.dbpwd = dbpwd;
		conect = new ArrayList<TCPVerbindung>();
		conect.add(new TCPVerbindung(this,server, port));
	}
	public Controller(String dburl, String dbusr, String dbpwd, int port){
		client = false;
		this.dburl = dburl;
		this.dbusr =dbusr;
		this.dbpwd = dbpwd;
		conect = new ArrayList<TCPVerbindung>();
		tcpreg = new TCPClientRegistration(this, port);
		tot = new Totengraeber();
	}
	
	public void addClient(Socket socket) throws IOException{
		conect.add(new TCPVerbindung(this, socket));
		conect.get(conect.size()-1).openConection();
	}
	public void removeCleint(TCPVerbindung tcp){
		conect.remove(tcp);
		tot.addDead(tcp);
	}
	public void shutdown(){
		try {
			if(ui != null)ui.stop();
			if(tcpreg != null)tcpreg.stop();
			if(conect != null)
				while(conect.size() >0)
					this.removeCleint(conect.get(0));
			if(tot!=null)tot.stop();
			Thread.sleep(1000);
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
