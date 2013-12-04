package Commands;

public class LogoutCommand extends Commands{
	String user;
	
	public LogoutCommand(){
		super.type = Commands.LOGOUT;
		super.status = true;
	}

	public LogoutCommand(String user) {
		super.type = Commands.LOGOUT;
		super.status = true;
		this.user = user;
	}
	
	
}
