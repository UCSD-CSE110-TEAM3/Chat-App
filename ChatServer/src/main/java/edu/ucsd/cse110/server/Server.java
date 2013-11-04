/**
 * Name1: Nhu-Quynh Liu
 * PID1: A10937319
 * Email1: n1liu@ucsd.edu
 */
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
		if ( msg.getJMSType() != null && msg.getJMSType().equals("login") ) {
			System.out.println( "Logging in" );
			String user = ((TextMessage)msg).getText();
			Destination dest = msg.getJMSReplyTo();
			this.login( user, dest );
			template.convertAndSend( dest, "Logged onto server ");
			this.broadcastAll( user + " has logged on" );
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
	}
}
