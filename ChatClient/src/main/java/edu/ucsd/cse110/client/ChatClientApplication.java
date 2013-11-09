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
	private static Scanner scanner = new Scanner(System.in);
	private static String username;
	private static String password;
	private static String command;

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
	private static ChatClient wireClient(String username, String password)
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
		return new ChatClient(producer, session, username, password);
	}

	private static void promptLogin() {
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("LOGIN");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("Enter your username.");
		System.out.print("Username: ");
		username = scanner.next();
		System.out.println("Enter your password");
		System.out.print("Password: ");
		password = scanner.next();
	}

	private static void promptCommands() {
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("COMMANDS");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("register\t--\t/register");
		System.out.println("login\t\t--\t/login");
		System.out.println("quit\t\t--\t/quit");
		command = scanner.next();
	}
	
	private static void promptRegister(){
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("REGISTER");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("Enter desired username.");
		System.out.print("Username: ");
		username = scanner.next();
		System.out.println("Enter your password(Min. 6 characters)");
		System.out.print("Password: ");
		password = scanner.next();
	}

	public static void main(String[] args) {
		System.out.println("\t\tWHISPER ME");
		ChatClient client;
		try {
			// prompt for command
			promptCommands();
			if (command.equals("/quit")) {
				System.out.println("Quitting...");
				System.exit(1);
			} else if (command.equals("/login")) {
				promptLogin();
				do {
					client = wireClient(username, password);
					listenForLoginStatus(client);
				} while (!client.isLogOn());
				client.startBroadChat();
			} else if (command.equals("/register")) {
				promptRegister();
				client = wireClient(username, password);
				client.register();
			} else {
				System.out.println("Enter a valid command");
				promptCommands();
			}

		} catch (JMSException e) {
			System.out
					.println("ERROR: Failed to wire client. Server might be offline. ");
			System.exit(1);
		} catch (URISyntaxException e) {
			System.out
					.println("ERROR: Failed to wire client. Check if URI is correct.");
			System.exit(1);
		}

	}

	/*
	 * This method was designed to allow the client to properly update its
	 * status before main() checks it in the condition loop
	 * 
	 * - We might also want to add a loginTimeOutError in case logging in is
	 * taking too long. i.e there is an infinite loop.
	 */
	private static void listenForLoginStatus(ChatClient client) {
		long clock = 0; // Start a clock time
		System.out.print("Checking account");
		while (client.loginInProgress() == true) { // Exit loop after attempt
													// login
			++clock;
			if (clock % 100000 == 0) { // Print "." at every iteration
				System.out.print(".");
			}
		}

		return;

	}

}
