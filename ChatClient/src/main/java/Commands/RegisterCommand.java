package Commands;

public class RegisterCommand extends Commands{
	String user;
	String password;
	String log;
	
	public RegisterCommand(String user, String password){
		this.type = Commands.REGISTER;
		this.user = user;
		this.password = password;
		this.status = true;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
}
