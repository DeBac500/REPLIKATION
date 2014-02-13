package DBHandler;

import java.io.Serializable;

public class DBSaver implements Serializable {
	public String type, tabname, colname, colvalue;
	
	
	public DBSaver(){
	}
	
	public boolean read(String type, String tabname, String colname, String colvalue){
		this.type=type;
		this.tabname = tabname;
		this.colname = colname;
		this.colvalue = colvalue;
		return false;	
	}
	public boolean wirte(DBConnector db){
		String sql = this.generateSQL();
		return false;
	}
	private String generateSQL(){
		String sql = "";
		if(this.type.equalsIgnoreCase("INSERT"))
			sql = this.generateInsert();
		if(this.type.equalsIgnoreCase("UPDATE"))
			sql = this.generateUpdate();
		if(this.type.equalsIgnoreCase("DELEATE"))
			sql = this.generateDeleate();
		return sql;
	}
	private String generateInsert(){
		String sql = "INSERT INTO " + this.tabname + "("+this.colname+") VALUES ("+ this.colvalue + ")";
		return sql;
	}
	private String generateUpdate(){
		String sql="UPDATE " + this.tabname + " SET ";
		String[] temp1 = this.colname.split(",");
		String[] temp2 = this.colvalue.split(",");
		for(int i = 0; i < temp1.length;i++){
			if(1 != temp1.length-1){
				sql += temp1[i] + "=" + temp2[i] + ",";
			}else{
				sql +=  temp1[i] + "=" + temp2[i] + " ";
			}
		}
		//TODO Where-Bedingung
		return sql;
	}
	private String generateDeleate(){
		String sql = "DELETE FROM " + this.tabname + "WHERE";
		//TODO Where-Bedingung
		return sql;
	}
}
