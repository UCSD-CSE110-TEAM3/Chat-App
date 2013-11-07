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
	private Session session;
	private User user;
	private Queue oriQueue;
	
	public ChatClient(MessageProducer producer, Session session, String username, String password) {
		super();
		this.user = new User(username, password);
		this.producer = producer;
		this.session = session;		
		try {
			this.oriQueue = session.createTemporaryQueue();
			// oriQueue = address of this client 
			this.consumer = session.createConsumer(oriQueue);
			// consumer takes msgs from Queue
			consumer.setMessageListener( this );
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.logon();
	} 

	/* Send a TextMessage to the server of type login with the user's name */
	public void logon() {
		TextMessage logon;
		try {
			logon = session.createTextMessage(this.user.toString());
			logon.setJMSType("login");
			logon.setJMSReplyTo(oriQueue);
			producer.send(logon);
			System.out.println( "logged on" );
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
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
			else if (userMsg.length() >= 5 &&
					 userMsg.substring(0, 5).equals("/help")) {
				this.help();
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
		TextMessage allMsg;
		try {
			allMsg = session.createTextMessage(userMsg);
			allMsg.setJMSType("all");
			allMsg.setStringProperty("username", this.user.toString());
			producer.send(allMsg);
		} catch (JMSException e) {
			e.printStackTrace();
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
		String toUser = userMsg.substring(0, userMsg.indexOf(" "));
		userMsg = userMsg.substring(userMsg.indexOf(" ") + 1);
		TextMessage wMsg;
		try {
			wMsg = session.createTextMessage(userMsg);
			wMsg.setJMSType("wMsg");
			wMsg.setStringProperty("username", this.user.toString());
			wMsg.setStringProperty("toUser", toUser);
			wMsg.setJMSReplyTo(oriQueue);
			producer.send(wMsg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return;
	}
	
	// prints our all commands
	public void help() {
		System.out.println("Here is a list of commands:");
		System.out.println("To logout: '/logout'");
		System.out.println("To whisper: '/w username message'");
	}
	
	/* Sends a TextMessage to the server of type logout with the user's name */
	public void logout() {
		TextMessage logout;
		try {
			logout = session.createTextMessage(this.user.toString());
			logout.setJMSType("logout");
			logout.setJMSReplyTo(oriQueue);
			producer.send(logout);
			System.out.println( "logging out" );
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void send(String msg) throws JMSException {
		producer.send(session.createTextMessage(msg));
		return;
	}
	
	public boolean equals( ChatClient c ) {
		if ( c.user == this.user ) {
			return true;
		}
		return false;
	}
	
	public void sendMessageTo( String receipients, String message ) throws JMSException {
		
		TextMessage toSend = session.createTextMessage( message );
		toSend.setObjectProperty( "receipients", receipients );
		toSend.setJMSReplyTo( oriQueue );
		
		producer.send( toSend );
	}
	
	public void onMessage( Message msg ) {
		//deal with message from server
		try {
			msg.acknowledge(); // acknowledge to know that it is already received
			System.out.println(((TextMessage)msg).getText());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
}
