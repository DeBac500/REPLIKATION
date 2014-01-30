package Controller;
/**
 * Error wenn nichts zu syncen ist
 * @author Dominik Backhausen
 *
 */
public class Nothingtosync extends Exception {
	public Nothingtosync (){
		super ();
    }
	public Nothingtosync (String message){
		super (message);
    }
}
