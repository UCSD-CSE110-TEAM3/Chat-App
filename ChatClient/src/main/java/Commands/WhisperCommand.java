package Commands;

public class WhisperCommand extends Commands{
	String from;
	String receiver;
	String message;
	String log;
	
	public WhisperCommand(String from, String message){
		super.type    = Commands.WHISPER;
		super.status  = true;
		this.from     = from;
		this.message  = message;
	}
	public WhisperCommand(String from, String receiver, String message){
		super.type    = Commands.WHISPER;
		super.status  = true;
		this.from     = from;
		this.receiver = receiver;
		this.message  = message;
	}
	
	public void appendMessage(String message){
		this.message += message;
	}

	public String getFrom() {
		return from;
	}

	public String getMessage() {
		return message;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
	
	public String getReceiver(){
		return receiver;
	}
	
}
