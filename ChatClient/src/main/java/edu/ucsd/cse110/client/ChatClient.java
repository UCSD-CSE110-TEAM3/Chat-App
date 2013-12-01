package edu.ucsd.cse110.client;

import java.util.Scanner;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class ChatClient implements MessageListener{
	private MessageProducer producer; // makes messages
	private MessageConsumer consumer; // receives messages
	private MessageFactory msgFactory;
	
	private Session session;
	private User user;
	private Queue originQueue;
	private String replyTo;
	
	private boolean loginInProgress = false;
	private boolean registerInProgress = false;
	
	public ChatClient(){};
	
	public ChatClient(MessageProducer producer, Session session, String username, String password) {
		super();
		this.user = new User(username, password);
		this.producer = producer;
		this.session = session;		
		this.replyTo = "";
		try {
			this.originQueue = session.createTemporaryQueue();
			this.msgFactory = new MessageFactory( this );
			// oriQueue = address of this client 
			this.consumer = session.createConsumer(originQueue);
			// consumer takes msgs from Queue
			consumer.setMessageListener( this );
		} catch (JMSException e) {
			System.out.println( "ERROR: Failed to construct ChatClient.");
			System.exit(1);
		}
	}
	
	public void register() {
		Message registerMsg;
		this.registerInProgress = true;
		try {
			registerMsg = msgFactory.createMessage("register", user.toString()+":"+user.getPassword());
			producer.send(registerMsg);
			
		} catch (JMSException e) {
			System.out.println( "ERROR: Registration on unsuccessful. Check if Server is running.");
		}
		return;
	}

	/* Send a TextMessage to the server of type login with the user's name */
	public void logon() {
		Message logonMsg;
		this.loginInProgress = true;
		try {
			logonMsg = msgFactory.createMessage("login", user.toString()+":"+user.getPassword());
			producer.send(logonMsg);
			
		} catch (JMSException e) {
			System.out.println( "ERROR: Logging on unsuccessful. Check if Server is running.");
		}
		return;
	}
	
	/* Sends a TextMessage to the server of type logout with the user's name */
	public void logout() {
		Message logoutMsg;
		try {
			logoutMsg = msgFactory.createMessage("logout");
			producer.send(logoutMsg);
		} catch (JMSException e) {
			e.printStackTrace();
		}		
	}
	
	//start public chat!
	public void startBroadChat() {
		String userMsg = "";
		Scanner scanner = new Scanner(System.in);
		System.out.println("Send a broadcast message!");
		while(!userMsg.equals("/logout")) {
			userMsg = scanner.nextLine();
			if (userMsg.length() > 2 &&
				userMsg.substring(0, 3).equals("/w ")) {
					userMsg = userMsg.substring(3);
					this.whisper(userMsg);
			}
			else if (!this.replyTo.equals("") &&
					userMsg.substring(0, 3).equals("/r ")){
				this.whisper(userMsg);
			}
			else if (userMsg.length() >= 5 &&
					 userMsg.substring(0, 5).equals("/help")) {
				this.help();
			}
			else if ( userMsg.equals("/logout") ){
				scanner.close();
				this.logout();
				return;
			}
			else {
				this.allMsg(userMsg);
			}
		}
		//close the scanner
		scanner.close();
		//end chat broadcasting
		return;
	}
	
	//send a TextMessage to server of type "all" and user's name
	public void allMsg(String userMsg) {
		Message allMsg;
		try {
			allMsg = msgFactory.createMessage("all", userMsg);
			producer.send(allMsg);
		} catch (JMSException e) {
			System.out.println( "ERROR: Failed to broadcast message.");
		}
		return;
	}
	
	// Sends TextMessage to server to send to one particular client
	public void whisper(String userMsg){
		if (userMsg.indexOf(" ") == -1) {
			System.out.println("Improper use of '/w'");
			System.out.println("Please use '/help' for a list of commands");
			return;
		}
		if (userMsg.substring(0, 3).equals("/r ")) {
			String toUser = this.replyTo;
			userMsg = userMsg.substring(userMsg.indexOf(" ") + 1);
			Message wMsg;
			try {
				wMsg = msgFactory.createMessage("wMsg", userMsg, toUser);
				producer.send(wMsg);
			} catch (JMSException e) {
				System.out.println( "Failed to whisper to another user.");
			}
			return;
		}
		String toUser = userMsg.substring(0, userMsg.indexOf(" "));
		userMsg = userMsg.substring(userMsg.indexOf(" ") + 1);
		Message wMsg;
		try {
			wMsg = msgFactory.createMessage("wMsg", userMsg, toUser);
			producer.send(wMsg);
		} catch (JMSException e) {
			System.out.println( "Failed to whisper to another user.");
		}
		return;
	}
	
	// prints our all commands
	public void help() {
		System.out.println("Here is a list of commands:");
		System.out.println("To logout: '/logout'");
		System.out.println("To whisper: '/w username message'");
		System.out.println("To reply to recent whisper: '/r message'");
	}
	
	public void onMessage( Message msg ) {
		//deal with message from server
		try {
			msg.acknowledge(); // acknowledge to know that it is already received
			String received = ((TextMessage)msg).getText();
			
			
			if(received.equals("!loginFailed")){
				this.loginInProgress = false;
				System.out.println("\n!Login Failed: Username or password is incorrect. ");
				return;
			}else{
				this.user.setOnlineStatus(true);
				this.loginInProgress = false;
							
			}
			
			System.out.println(received);
			if (received.substring(0, 1).equals(">")) {
				this.replyTo = received.substring(received.indexOf(" ", 11 ) + 1,
						received.indexOf(":"));
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public boolean loginInProgress(){
		return this.loginInProgress;
	}
	public boolean isLogOn(){
		return this.user.online();
	}
	
	public User getUser() {
		return user;
	}
	
	public Session getSession() {
		return session;
	}
	
	public Queue getQueue() {
		return originQueue;
	}
}
