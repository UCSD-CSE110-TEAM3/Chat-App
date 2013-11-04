/**
 * Name1: Nhu-Quynh Liu
 * PID1: A10937319
 * Email1: n1liu@ucsd.edu
 */
package edu.ucsd.cse110.client;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class ChatClient implements MessageListener{
	private MessageProducer producer; // makes messages
	private MessageConsumer consumer; // receives messages
	private Session session;
	private String user;
	
	public ChatClient(MessageProducer producer, MessageConsumer consumer, Session session, String user) {
		super();
		this.user = user;
		this.producer = producer;
		this.session = session;
		this.consumer = consumer;
		try {
			consumer.setMessageListener( this );
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	public void send(String msg) throws JMSException {
		producer.send(session.createTextMessage(msg));
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
		toSend.setStringProperty( "from", "user" );
		
		producer.send( session.createTextMessage( message ) );
	}
	
	public void onMessage( Message msg ) {
		System.out.println( "Hi" );
		//deal with message from server
		try {
			System.out.println(((TextMessage)msg).getText() + "Received");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
