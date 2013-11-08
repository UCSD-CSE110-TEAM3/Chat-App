package edu.ucsd.cse110.client;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

/* 
 * Class to make messages. It sucks right now.... What pattern to use?
 */
public class MessageFactory {
	Session session;
	Queue queue;
	ChatClient client;
	
	public MessageFactory( ChatClient client ) {
		this.session = client.getSession();
		this.queue = client.getQueue();
		this.client = client;
	}
	
	public TextMessage createMessage( String type ) {
		try {
			TextMessage newMessage = session.createTextMessage();
			newMessage.setJMSType(type);
			newMessage.setJMSReplyTo(queue);
			newMessage.setStringProperty("username", client.getUser().toString());
			return newMessage;
		} catch ( JMSException e ) {
			System.out.println( "ERROR: Failed to create message of type " + type + "." );
			return null;
		}
	}
	
	public TextMessage createMessage( String type, String message ) throws JMSException {
		TextMessage newMessage = createMessage( type );
		newMessage.setText(message);
		return newMessage;
	}
	
	public TextMessage createMessage( String type, String message, String toUser ) throws JMSException {
		TextMessage newMessage = createMessage( type, message );
		newMessage.setStringProperty("toUser", toUser );
		return newMessage;
	}
}
