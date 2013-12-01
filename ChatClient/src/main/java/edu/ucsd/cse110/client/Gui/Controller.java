package edu.ucsd.cse110.client.Gui;

import java.net.URISyntaxException;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;

import edu.ucsd.cse110.client.ChatClient;
import edu.ucsd.cse110.client.Constants;
import Commands.CommandHandler;
import Commands.Commands;

public class Controller implements CommandHandler {
	public GuiApplication ingoingHandler;
	public ChatClientGui outgoingHandler;
	
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
				connection.close();
			} catch (JMSException e) {
				/*
				 * This means that the connection was already closed or got some
				 * error while closing. Given that we are closing the client we
				 * can safely ignore this.
				 */
			}
		}
	}

	/*
	 * This method wires the client class to the messaging platform Notice that
	 * ChatClient does not depend on ActiveMQ (the concrete communication
	 * platform we use) but just in the standard JMS interface.
	 */
	private static ChatClientGui wireClient()
			throws JMSException, URISyntaxException {
		ActiveMQConnection connection =
		// make a connection to the server (localhost)
		ActiveMQConnection.makeConnection(
		/* Constants.USERNAME, Constants.PASSWORD, */Constants.ACTIVEMQ_URL);
		connection.start();
		CloseHook.registerCloseHook(connection);
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		// make a queue that server will listen to
		Queue destQueue = session.createQueue(Constants.DESTQUEUE);
		// producer sends messages to server
		MessageProducer producer = session.createProducer(destQueue);

		// make them log in to get their name to put in the constructor
		return new ChatClientGui(producer, session);
	}
	private static Controller instance = new Controller();

	private Controller() {
	}

	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}
	
	public void prepareController(){
		try{
			outgoingHandler = wireClient();
		}catch(Exception e){
			System.out.print("Error");
		}
		ingoingHandler = GuiApplication.getInstance();
	}
	public void sendCommand(Commands command) {
		outgoingHandler.recieveCommand(command);
	}
	
	public void recieveCommand(Commands command) {
		ingoingHandler.recieveCommand(command);	
		
	}

}
