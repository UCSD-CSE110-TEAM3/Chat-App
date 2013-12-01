package Commands;

public class LoginCommand extends Commands{
	private String username;
	private String password;
	private String logMessage;
	
	public LoginCommand(String username){
		super.type =  Commands.LOGIN;
		super.status = true;
		this.username = username;
	}
	
	public LoginCommand(String username, String password){
		super.type =  Commands.LOGIN;
		super.status = true;
		this.username = username;
		this.password = password;
	}
	
	public LoginCommand(String username, String password, String logMessage){
		this.logMessage = logMessage;
		super.type =  Commands.LOGIN;
		super.status = true;
		this.username = username;
		this.password = password;
	}
	

	public String getUsername() {
		return username;
	}


	public String getPassword() {
		return password;
	}


	public String getLogMessage() {
		return logMessage;
	}


	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}
	
}
