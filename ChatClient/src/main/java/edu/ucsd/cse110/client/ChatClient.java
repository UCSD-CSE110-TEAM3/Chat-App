/**
 * Name1: Nhu-Quynh Liu
 * PID1: A10937319
 * Email1: n1liu@ucsd.edu
 */
package edu.ucsd.cse110.client;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

public class ChatClient {
	private MessageProducer producer;
	private Session session;
	private String user;
	
	public ChatClient(MessageProducer producer, Session session, String user) {
		super();
		this.user = user;
		this.producer = producer;
		this.session = session;
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
}
