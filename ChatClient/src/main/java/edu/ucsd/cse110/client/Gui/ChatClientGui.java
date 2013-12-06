package edu.ucsd.cse110.client.Gui;


import edu.ucsd.cse110.client.*;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import Commands.*;

public class ChatClientGui extends ChatClient implements MessageListener, CommandHandler{
	private MessageProducer producer; // makes messages
	private MessageConsumer consumer; // receives messages
	private MessageFactory msgFactory;
	
	private Session session;
	private User user;
	private Queue originQueue;
	
	private Controller controller = Controller.getInstance();
	
	public ChatClientGui(MessageProducer producer, Session session){
		this.producer = producer;
		this.session  = session;
		try {
			this.originQueue = session.createTemporaryQueue();
			this.msgFactory = new MessageFactory( this );
			this.consumer = session.createConsumer(originQueue);
			consumer.setMessageListener( this );   // consumer takes msgs from Queue
		
		} catch (JMSException e) {
			System.out.println( "ERROR: Failed to construct ChatClient.");
			System.exit(1);
		}
	}
	
	private void setUser(String username, String password){
		this.user  = new User(username, password);
	}
	private void setUserStatus(boolean status){
		if(user != null)
			user.setOnlineStatus(status);
	}
	private void register(RegisterCommand command){
		Message registerMsg;
		setUser(command.getUser(), command.getPassword());
		try {
			registerMsg = msgFactory.createMessage("register", command.getUser()+":"+command.getPassword());
			producer.send(registerMsg);
			
		} catch (JMSException e) {
			//send failure message
			System.out.println( "ERROR: Registration on unsuccessful. Check if Server is running.");
		}
		return;
	}
	private void logon(LoginCommand command){
		Message logonMsg;
		setUser(command.getUsername(), command.getPassword());
		
		try {
			logonMsg = msgFactory.createMessage("login", command.getUsername()+":"+command.getPassword());
			producer.send(logonMsg);
			
		} catch (JMSException e) {
			System.out.println( "ERROR: Logging on unsuccessful. Check if Server is running.");
		}

		return;
	}
	
	public void logout(){
		Message logoutMsg;
		try {
			logoutMsg = msgFactory.createMessage("logout");
			producer.send(logoutMsg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		return;
	}
    private void broadcast(BroadcastCommand command){
    	Message allMsg;
		try {
			allMsg = msgFactory.createMessage("all", command.getMessage());
			producer.send(allMsg);
		} catch (JMSException e) {
			System.out.println( "ERROR: Failed to broadcast message.");
		}
		return;
    }
	private void whisper(WhisperCommand command){
		if(command.getReceiver() == null){
			return;
		}
		System.out.println(command.getReceiver() + command.getMessage());
		Message wMsg;
		try {
			wMsg = msgFactory.createMessage("wMsg", command.getMessage(), command.getReceiver());
			producer.send(wMsg);
		} catch (JMSException e) {
			System.out.println( "Failed to whisper to another user.");
		}
		return;
	}
    private void getUsers(CheckUsersCommand command) {
		
		Message Msg;
		try {
			Msg = msgFactory.createMessage("getUsersOnline");
			producer.send(Msg);
		} catch (JMSException e) {
			System.out.println( "Failed to get users.");
		}
		return;
		
	}
	public void recieveCommand(Commands command) {
		if (command == null) {
			return;
		}

		switch (command.getType()) {
		case Commands.LOGIN:
			logon((LoginCommand)command);
			break;
		case Commands.LOGOUT:
			logout();
			break;
		case Commands.WHISPER:
			whisper((WhisperCommand)command);
			break;
		case Commands.BROADCAST:
			broadcast((BroadcastCommand)command);
			break;
		case Commands.REGISTER:
			register((RegisterCommand)command);
			break;
		case Commands.CHECKUSERS:
			getUsers((CheckUsersCommand)command);
			break;
		}
		
	}
	/*
	public ChatClientGui(MessageProducer producer, Session session, String username, String password) {
		super();
		this.user = new User(username, password);
		this.producer = producer;
		this.session = session;		
		this.replyTo = "";
		try {
			this.originQueue = session.createTemporaryQueue();
			//this.msgFactory = new MessageFactory( this );
			// oriQueue = address of this client 
			this.consumer = session.createConsumer(originQueue);
			// consumer takes msgs from Queue
			consumer.setMessageListener( this );
		} catch (JMSException e) {
			System.out.println( "ERROR: Failed to construct ChatClient.");
			System.exit(1);
		}
	}
	*/

	

	public void onMessage(Message msg) {
		// deal with message from server
		try {
			msg.acknowledge(); // acknowledge to know that it is already
								// received
			String received = ((TextMessage) msg).getText();
			
			if (!user.online()) {
				LoginCommand command = new LoginCommand(user.toString(),
						user.getPassword());
				if (received.equals("!loginFailed")) {
					command.setStatus(false);
					command.setLogMessage("Username or Password is invalid");
					sendCommand(command);
				} else {
					setUserStatus(true);
					sendCommand(command);
				}
			} else if (received.contains("Users Online:")) {
				String[] users = received.split("\n");
				CheckUsersCommand command = new CheckUsersCommand();

				for (int i = 1; i < users.length; ++i) {
					command.addUser(users[i]);
				}
				sendCommand(command);
			} else if (received.contains("has logged on")) {
				if (!received.split(" ")[0].equals(user.toString()))
					sendCommand(new LoginCommand(received.split(" ")[0],
							received));
			} else if (received.contains("has logged out")) {
				Commands command = new LogoutCommand(received.split(" ")[0]);
				command.setStatus(false);
				sendCommand(command);
			} else if (received.contentEquals("You have been logged out of the server")) {
				sendCommand(new LogoutCommand());
				setUserStatus(false);
			} else if (received.contains(">whisper from ")) {
				String modifiedMess = received.replace(">whisper from ", "");
				sendCommand(new WhisperCommand(modifiedMess.split(":")[0],
						modifiedMess));
			} else {

				sendCommand(new BroadcastCommand(received));
			}

		} catch (JMSException e) {
			e.printStackTrace();
		}
		return;
	}
	

	public Session getSession() {
		return session;
	}
    public Queue getQueue() {
		return originQueue;
	}
    public void sendCommand(Commands command) {
		controller.recieveCommand(command);
	}
    
    public User getUser(){
    	return this.user;
    }

}
