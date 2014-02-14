package DBHandler;

import java.io.IOException;
import java.util.ArrayList;

import Conection.TCPVerbindung;
import Controller.Controller;
import Controller.Nothingtosync;
import Controller.Syncable;
import FileHandler.Directory;

public class DBsyncer implements Syncable{
	private ArrayList<DBSaver> save;

	@Override
	public void setUp(Controller c) throws Nothingtosync, IOException {
		save = c.getDB().readChanges();
	}

	@Override
	public String syncClient(String path, Controller c, TCPVerbindung tcp) {
		String msg = "";
		for(int i=0; i < save.size(); i++){
			if(save.get(i).wirte(c.getDB())){
				try {
					c.getDB().dellog(save.get(i));
					String send = save.get(i).getTabname() + " " + save.get(i).getType() + " " + tcp.getAddressEnd() +" -> " + tcp.getAddress() + " OKAY";
					tcp.sendObject(send);
					c.getLog().info(send);
				} catch (IOException e) {
					c.getLog().info(save.get(i).getTabname() + " " + save.get(i).getType() + " " + tcp.getAddressEnd() +" -> " + tcp.getAddress() + " OKAY MIT FEHLER");
				}
			}else{
				try{
					c.getDB().dellog(save.get(i));
					String send = save.get(i).getTabname() + " " + save.get(i).getType() + " " + tcp.getAddressEnd() +" -> " + tcp.getAddress() + " FEHLGESCHLAGEN";
					tcp.sendObject(send);
					c.getLog().info(send);
				}catch(IOException e){
					c.getLog().info(save.get(i).getTabname() + " " + save.get(i).getType() + " " + tcp.getAddressEnd() +" -> " + tcp.getAddress() + " FEHLGESCHLAGEN");
				}
			}
		}
		return msg;
	}

	@Override
	public String syncServer(String path, Controller c, TCPVerbindung tcp) {
		String msg = "";
		for(int i=0; i < save.size(); i++){
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
