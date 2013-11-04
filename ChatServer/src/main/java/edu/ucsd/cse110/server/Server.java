/**
 * Name1: Nhu-Quynh Liu
 * PID1: A10937319
 * Email1: n1liu@ucsd.edu
 */
package edu.ucsd.cse110.server;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class Server{
	JmsTemplate template;
	
	public Server( JmsTemplate jmsTemplate ) {
		ActiveMQConnectionFactory factory= new ActiveMQConnectionFactory(Constants.ACTIVEMQ_URL);
		template = new JmsTemplate( factory) ;
		
	}
	
	public void receive(Message msg) {
		try {
			System.out.println( "Hi");
			System.out.println(((TextMessage)msg).getText());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		processMessage( msg );
		
	}
	
	private void processMessage( Message msg ) {
		try {
			String person = (String) msg.getObjectProperty( "receipients" );
			MessageCreator messageCreator = new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage("Ping!");
				}
	        }; 
			template.send( msg.getJMSReplyTo(), messageCreator );
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void sendMessage() {
		
	}
}
