package Commands;

public abstract class Commands {
	public static final int REGISTER = 0;
	public static final int LOGIN = 1;
	public static final int LOGOUT = 2;
	public static final int BROADCAST = 3;
	public static final int WHISPER = 4;
	public static final int CHECKUSERS = 5;
	
    protected int type;
    protected boolean status;
    
    
	public int getType(){
		return type;
	}
	
	public boolean getStatus(){
		return status;
	}
	
	
	public void setStatus(boolean status){
		this.status = status;
	}
}
