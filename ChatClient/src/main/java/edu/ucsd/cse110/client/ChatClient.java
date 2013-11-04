/**
 * Name1: Nhu-Quynh Liu
 * PID1: A10937319
 * Email1: n1liu@ucsd.edu
 */
package edu.ucsd.cse110.client;

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
	private String user;
	private Queue oriQueue;
	
	public ChatClient(MessageProducer producer, Session session, String user) {
		super();
		this.user = user;
		this.producer = producer;
		this.session = session;		
		try {
			this.oriQueue = session.createTemporaryQueue();
			this.consumer = session.createConsumer(oriQueue);
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
		toSend.setJMSReplyTo( oriQueue );
		
		producer.send( toSend );
	}
	
	public void onMessage( Message msg ) {
		//deal with message from server
		try {
			System.out.println(((TextMessage)msg).getText() + "Received");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
