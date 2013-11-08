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
	
	JmsTemplate template;
	private HashMap<String, Destination> online;
	private HashMap<String, String> accounts; //the accounts that is loaded to memory for now.
	
	public Server( ) {
		ActiveMQConnectionFactory factory= new ActiveMQConnectionFactory(Constants.ACTIVEMQ_URL);
		template = new JmsTemplate(factory);
		//template = jmsTemplate;
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
	//dumps pairs in hashmap to disk
	public void save_accounts() throws IOException {
		
		 FileWriter f0 = null;
		 f0 = new FileWriter("user_accounts.txt");
		 
		 String nl = System.getProperty("line.separator"); // '/n' not very portable
		 
		 //Dumping all key, values into file , line by line
		 //dumps it in this format
			 // user01:password01
			 // user02:password02
			 
			 //System.out.println("key : " + key)
			 //System.out.println("value : " + accounts.get(key));
			 
		 for (String key: accounts.keySet()) {
			 f0.write( key +":"+ accounts.get(key)+nl);
		 }
		 
         	f0.close();
		 
		 
	}
	//checks from hashmap
	public boolean user_exists(String input) {
		
		if (this.accounts.get(input)!=null) {
			return true;	
		} 	
		return false;	
		
	}
	
	//puts in hashmap and writes to disk
	public boolean make_account(String user, String password){
		
		if (!user_exists(user)){
			accounts.put(user, password);
			try {
			save_accounts();
			} catch (IOException e) {
			System.out.println("accounts not saved");
		}
			return true;
		}
		
		return false;
	}
	
	
	
	
	
	public void setSender( JmsTemplate jmsTemplate ) {
		this.template = jmsTemplate;
	}
	
	//logs in by checking if account exsists and password matches in hashmap
	public boolean valid_login(String user, String password) {
		
		if (password == null) {
			return false;
		}
		if (this.accounts.get(user)==password) {
			return true;
		}
		return false; 
		
	}
	
	//logs in by checking if account exsists and password matches in hashmap
	public boolean valid_login(String user, String password) {
		
		if (password == null) {
			return false;
		}
		if (this.accounts.get(user)==password) {
			return true;
		}
		return false; 
		
	}
	
	public void receive(Message msg) throws JMSException {
		// if login
		if ( msg.getJMSType() != null && msg.getJMSType().equals("login") ) {
			String user = ((TextMessage)msg).getText();
			Destination dest = msg.getJMSReplyTo();
			this.login( user, dest );
			template.convertAndSend( dest, "Logged onto server ");
			this.broadcastAll( user + " has logged on" );
			template.convertAndSend( dest, "Enter '/help' for chat commands.");
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
			String user = ((TextMessage)msg).getText();
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
		processMessage( msg );
		
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
