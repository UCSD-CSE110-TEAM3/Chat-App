package Commands;

public class BroadcastCommand extends Commands{
	private String message;

	public BroadcastCommand(String message){
		this.type = Commands.BROADCAST;
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
}
