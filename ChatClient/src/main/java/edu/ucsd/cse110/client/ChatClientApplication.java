package edu.ucsd.cse110.client;

import java.net.URISyntaxException;
import java.util.Scanner;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;

public class ChatClientApplication {
	private static Scanner scanner = new Scanner( System.in );
	/*
	 * This inner class is used to make sure we clean up when the client closes
	 */
	static private class CloseHook extends Thread {
		ActiveMQConnection connection;
		private CloseHook(ActiveMQConnection connection) {
			this.connection = connection;
		}
		
		public static Thread registerCloseHook(ActiveMQConnection connection) {
			Thread ret = new CloseHook(connection);
			Runtime.getRuntime().addShutdownHook(ret);
			return ret;
		}
		
		public void run() {
			try {
				System.out.println("Closing ActiveMQ connection");
				connection.close();
			} catch (JMSException e) {
				/* 
				 * This means that the connection was already closed or got 
				 * some error while closing. Given that we are closing the
				 * client we can safely ignore this.
				*/
			}
		}
	}

	/*
	 * This method wires the client class to the messaging platform
	 * Notice that ChatClient does not depend on ActiveMQ (the concrete 
	 * communication platform we use) but just in the standard JMS interface.
	 */
	private static ChatClient wireClient( String username, String password ) throws JMSException, URISyntaxException {
		ActiveMQConnection connection = 
				// make a connection to the server (localhost)
				ActiveMQConnection.makeConnection(
				/*Constants.USERNAME, Constants.PASSWORD,*/ Constants.ACTIVEMQ_URL);
        connection.start();
        CloseHook.registerCloseHook(connection);//ignore
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE); 
        // make a queue that server will listen to
        Queue destQueue = session.createQueue(Constants.DESTQUEUE);
        // producer sends messages to server
        MessageProducer producer = session.createProducer(destQueue);
        /*
        Queue oriQueue = oriQueue = session.createTemporaryQueue();
        MessageConsumer consumer = session.createConsumer(oriQueue);*/
        
        // make them log in to get their name to put in the constructor
        return new ChatClient(producer, session, username, password);
	}
	
	public static void main(String[] args) {
		try {
			
			/* 
			 * We have some other function wire type ChatClient 
			 * to the communication platform
			 */
	        System.out.println("Enter your username.");
	        System.out.print("Username: ");
	        String username = scanner.next();
	        System.out.println("Enter your password");
	        System.out.print("Password: ");
	        String password = scanner.next();
	        ChatClient client = wireClient( username, password );
			// Now we can happily send messages around
	        client.startBroadChat();

	        
			//client.sendMessageTo( "", "Hi" );
	        //client.logout();
	        client.logout();
	        //System.exit(0);
	        
		} catch (JMSException e) {
			e.printStackTrace();
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

}
