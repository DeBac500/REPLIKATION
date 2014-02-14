package DBHandler;

import java.io.Serializable;
import java.sql.SQLException;

public class DBSaver implements Serializable {
	public String type, tabname, colname, valNEW, valOLD;
	
	
	public DBSaver(String type, String tabname, String colname, String valNEW, String valOLD){
		this.type=type;
		this.tabname = tabname;
		this.colname = colname;
		this.valNEW = valNEW;
		this.valOLD = valOLD;
	}
	
	public boolean read(String type, String tabname, String colname, String valNEW, String valOLD){
		this.type=type;
		this.tabname = tabname;
		this.colname = colname;
		this.valNEW = valNEW;
		this.valOLD = valOLD;
		return true;	
	}
	public boolean wirte(DBConnector db){
		try{
			String sql = this.generateSQL();
			int ch = db.executeUpdate(sql);
			if(ch >= 1){
				return true;
			}else
				return false;
		}catch(SQLException e){
			return false;
		}
	}
	private String generateSQL(){
		String sql = "";
		if(this.type.equalsIgnoreCase("INSERT"))
			sql = this.generateInsert();
		if(this.type.equalsIgnoreCase("UPDATE"))
			sql = this.generateUpdate();
		if(this.type.equalsIgnoreCase("DELEATE"))
			sql = this.generateDelete();
		return sql;
	}
	private String generateInsert(){
		String sql = "INSERT INTO " + this.tabname + "("+this.colname+") VALUES ("+ this.valNEW + ")";
		return sql;
	}
	private String generateUpdate(){
		String sql="UPDATE " + this.tabname + " SET ";
		String[] temp1 = this.colname.split(",");
		String[] temp2 = this.valNEW.split(",");
		String[] temp3 = this.valOLD.split(",");
		for(int i = 0; i < temp1.length;i++){  
			if(1 != temp1.length-1){
				sql += temp1[i] + "=" + temp2[i] + ",";
			}else{
				sql +=  temp1[i] + "=" + temp2[i] + " ";
			}
		}
		sql += "WHERE ";
		for(int i = 0; i < temp1.length;i++){
			if(1 != temp1.length-1){
				sql += temp1[i] + "=" + temp3[i] + " AND ";
			}else{
				sql +=  temp1[i] + "=" + temp3[i] + " ";
			}
		}
		return sql;
	}
	private String generateDelete(){
		String sql = "DELETE FROM " + this.tabname + " WHERE ";
		String[] temp1 = this.colname.split(",");
		String[] temp3 = this.valOLD.split(",");
		for(int i = 0; i < temp1.length;i++){
			if(1 != temp1.length-1){
				sql += temp1[i] + "=" + temp3[i] + " AND ";
			}else{
				sql +=  temp1[i] + "=" + temp3[i] + " ";
			}
		}
		return sql;
	}

	public String getType() {
		return type;
	}
	public String getTabname() {
		return tabname;
	}
}
