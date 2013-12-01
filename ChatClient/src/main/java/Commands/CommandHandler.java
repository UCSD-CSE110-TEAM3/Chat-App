package Commands;

/*
 * Classes that extract data from a command must implement CommandHandler
 * or redirect commands.
 * 
 */
public interface CommandHandler {
	public void sendCommand(Commands command);
	public void recieveCommand(Commands command);
}
