package edu.ucsd.cse110.server;


import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader; 
import java.io.FileWriter;
import java.io.IOException;

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
	
	private JmsTemplate template;
	private HashMap<String, Destination> online;
	private HashMap<String, String> accounts; //the accounts that is loaded to memory for now.
	
	
	public Server( ) {
		online = new HashMap<String, Destination>();
		accounts = new HashMap<String, String>();
		
		try {
			load_accounts();
		} catch (IOException e) {
			System.out.println("accounts not loaded");
		}

	}
	
	//loads account info from disk to hashmap
	public void load_accounts() throws IOException{
		
		FileReader f0 = null;
		f0 = new FileReader("user_accounts.txt");
		
		BufferedReader br = new BufferedReader(f0);
		String line;
		while ((line = br.readLine()) != null) {
			List<String> pair = Arrays.asList(line.split(":"));
			accounts.put(pair.get(0), pair.get(1));
		}
		br.close();
		f0.close();
			 

	}
	
	/*
	 * This private method writes username and password into file.
	 * It will write even if null. 
	 * 
	 * Throw IOException if file could not be open
	 */
	private void save_accounts(String user, String password) throws IOException {
		 FileWriter f0 = new FileWriter("user_accounts.txt", true);
		 f0.write( user+":"+password);
		 f0.close();
		 	 
	}


	/*
	 * This private method checks if username is in our current hashmap
	 */
	private boolean user_exists(String input) {
		if (this.accounts.get(input)!=null) {
			return true;	
		} 	
		return false;	
		
	}

	/*
	 * This private method will check if username and password are valid 
	 * for registration. If they are it will call method to write to file.
	 * 
	 * Check code for reasons of failure
	 */
	private void registerUser(String user, String password) throws RegistrationException{
		if(user == null || password == null){
			throw new RegistrationException("Username or password was inValid.");
		}
		if(user_exists(user)){
			throw new RegistrationException("Username already exist.");
		}
		if(password.length() < 6){
			throw new RegistrationException("Password must be at least 6 characters long.");
		}
		
		try {
			save_accounts(user, password);
		} catch (IOException e) {
			throw new RegistrationException("Server was not able to save your account.");
		}
	
		accounts.put(user, password);
		
		return;

	}
	
	
	
	//logs in by checking if account exsists and password matches in hashmap
	public boolean valid_login(String user, String password) {
		
		if ((password == null)||(user==null)) {
			return false;
		}
		if (this.accounts.get(user)!=null) {
			if (this.accounts.get(user).equals(password)) {
				return true;
			}
		}
		return false; 
		
	}
	
	public void setSender( JmsTemplate jmsTemplate ) {
		this.template = jmsTemplate;
		
	}
	
	
	public void receive(Message msg) throws JMSException {
		// if registering user
		if ( msg.getJMSType() != null && msg.getJMSType().equals("register") ) {
			String[] userData = ((TextMessage)msg).getText().split(":");
			try{
				registerUser(userData[0], userData[1]);
				template.convertAndSend(msg.getJMSReplyTo(), "Your account has been registered.\nYou can now logon and whisper.");
			}catch(Exception e){
				template.convertAndSend(msg.getJMSReplyTo(), e.getCause() );
			}

			return;
		}else
		// if login
		if ( msg.getJMSType() != null && msg.getJMSType().equals("login") ) {
			String[] userData = ((TextMessage)msg).getText().split(":");
			if(this.valid_login(userData[0], userData[1])){
				template.convertAndSend(msg.getJMSReplyTo(), "\nLogging In...");
				Destination dest = msg.getJMSReplyTo();
				this.login(userData[0], dest );
				template.convertAndSend( dest, "Logged onto server ");
				this.broadcastAll( userData[0] + " has logged on" );
				template.convertAndSend( dest, "Enter '/help' for chat commands.");

			}else{
				template.convertAndSend(msg.getJMSReplyTo(), "!loginFailed");
			}
			
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
			String user = msg.getStringProperty("username");
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
		//processMessage( msg );
		
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
