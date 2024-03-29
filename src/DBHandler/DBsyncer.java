package DBHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import Conection.TCPVerbindung;
import Controller.Controller;
import Controller.Nothingtosync;
import Controller.Syncable;
import FileHandler.Directory;

public class DBsyncer implements Syncable{
	private ArrayList<DBSaver> save;
	public DBsyncer(){}
	@Override
	public void setUp(Controller c) throws Nothingtosync, IOException {
		save = c.getDB().readChanges();
	}

	@Override
	public String syncClient(String path, Controller c, TCPVerbindung tcp) {
		String msg = "";
		for(int i=0; i < save.size(); i++){
			try{
				if(save.get(i).wirte(c.getDB())){
					try {
						String send = save.get(i).getTabname() + " " + save.get(i).getType() + " " + tcp.getAddressEnd() +" -> " + tcp.getAddress() + " OKAY";
						tcp.sendObject(send);
						c.getLog().info(send);
					} catch (IOException e) {
						c.getLog().info(save.get(i).getTabname() + " " + save.get(i).getType() + " " + tcp.getAddressEnd() +" -> " + tcp.getAddress() + " OKAY MIT FEHLER");
					}
	//				c.getDB().dellog(save.get(i));
					
				}else{
					try{
						String send = save.get(i).getTabname() + " " + save.get(i).getType() + " " + tcp.getAddressEnd() +" -> " + tcp.getAddress() + " FEHLGESCHLAGEN";
						tcp.sendObject(send);
						c.getLog().info(send);
					}catch(IOException e){
						c.getLog().info(save.get(i).getTabname() + " " + save.get(i).getType() + " " + tcp.getAddressEnd() +" -> " + tcp.getAddress() + " FEHLGESCHLAGEN");
					}
	//				c.getDB().dellog(save.get(i));
				}
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
				}
			}catch(Nothingtosync e){return "";}
		}
		for(int i = 0; i < save.size();i++){
			c.getDB().dellog(save.get(i));
		}
		return msg;

	}

	@Override
	public String syncServer(String path, Controller c, TCPVerbindung tcp) {
		String msg = "";
		for(int i=0; i < save.size(); i++){
			try{
				if(save.get(i).wirte(c.getDB())){
					try {
						String send = save.get(i).getTabname() + " " + save.get(i).getType() + " " + tcp.getAddressEnd() +" -> " + tcp.getAddress() + " OKAY";
						tcp.sendObject(send);
						c.getLog().info(send);
					} catch (IOException e) {
						c.getLog().info(save.get(i).getTabname() + " " + save.get(i).getType() + " " + tcp.getAddressEnd() +" -> " + tcp.getAddress() + " OKAY MIT FEHLER");
					}
				}else{
					try{
						String send = save.get(i).getTabname() + " " + save.get(i).getType() + " " + tcp.getAddressEnd() +" -> " + tcp.getAddress() + " FEHLGESCHLAGEN";
						tcp.sendObject(send);
						c.getLog().info(send);
					}catch(IOException e){
						c.getLog().info(save.get(i).getTabname() + " " + save.get(i).getType() + " " + tcp.getAddressEnd() +" -> " + tcp.getAddress() + " FEHLGESCHLAGEN");
					}
				}
	//			c.getDB().dellog(save.get(i));
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
				}
			}catch(Nothingtosync e){return "";}
		}
		for(int i = 0; i < save.size();i++){
			c.getDB().dellog(save.get(i));
		}
		try {
			c.send(this);
		} catch (IOException e) {
			c.getLog().info("Weiterleiten FEHLGESCHLAGEN!");
		}
		return msg;
	}

	@Override
	public Directory getDir() {
		return null;
	}

}
