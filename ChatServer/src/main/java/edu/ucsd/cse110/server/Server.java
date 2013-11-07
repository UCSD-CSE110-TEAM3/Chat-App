package edu.ucsd.cse110.server;

import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Destination;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class Server{
	JmsTemplate template;
	HashMap<String, Destination> online;
	
	public Server() {
		ActiveMQConnectionFactory factory= new ActiveMQConnectionFactory(Constants.ACTIVEMQ_URL);
		template = new JmsTemplate(factory);
		online = new HashMap<String, Destination>();
	}
	
	public void receive(Message msg) throws JMSException {
		// if login
		if ( msg.getJMSType() != null && msg.getJMSType().equals("login") ) {
			String user = ((TextMessage)msg).getText();
			Destination dest = msg.getJMSReplyTo();
			this.login( user, dest );
			template.convertAndSend( dest, "Logged onto server ");
			this.broadcastAll( user + " has logged on" );
			return;
		}
		// if broadcast msg
		else if ( msg.getJMSType() != null && msg.getJMSType().equals("all") ) {
			String allMsg = ((TextMessage)msg).getText();
			String username = msg.getStringProperty("username");
			this.broadcastAll( username + ": "+ allMsg);
			return;
		}
		// if whisper
		else if ( msg.getJMSType() != null && msg.getJMSType().equals("wMsg") ) {
			String wMsg = ((TextMessage)msg).getText();
			String username = msg.getStringProperty("username");
			String toUser = msg.getStringProperty("toUser");
			Destination fromUser = msg.getJMSReplyTo();
			this.sendOne(">whisper from " + username + ": " +wMsg, toUser, fromUser);
			return;
		}
		// if logout
		else if ( msg.getJMSType() != null && msg.getJMSType().equals("logout") ) {
			String user = ((TextMessage)msg).getText();
			this.logout( user );
			Destination dest = msg.getJMSReplyTo();
			template.convertAndSend( dest, "Logged out of server ");
			this.broadcastAll( user + " has logged out" );
			return;
		}
		try {
			System.out.println(((TextMessage)msg).getText());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		processMessage( msg );
		
	}
	
	private void login( String user, Destination dest ) {
		online.put(user, dest);
	}
	
	private void logout( String user ) {
		online.remove(user);
	}
	
	private void processMessage( Message msg ) {
		try {
			template.convertAndSend( msg.getJMSReplyTo(), ((TextMessage)msg).getText() );
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void broadcastAll( String message ) {
		for (String user:online.keySet()) {
			Destination dest = online.get(user);
			template.convertAndSend(dest, message);
		}
		return;
	}
	
	public void sendOne( String message, String toUser, Destination fromUser ) {
		if (!online.containsKey(toUser)) {
			template.convertAndSend( fromUser, "User not found :(");
		}
		for (String user:online.keySet()) {
			if ( toUser.equals(user) ) {
				Destination dest = online.get(user);
				template.convertAndSend(dest, message);
				return;
			}
		}
		return;
	}
}
