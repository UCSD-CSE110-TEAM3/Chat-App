package edu.ucsd.cse110.client;

import static org.junit.Assert.*;

import java.net.URISyntaxException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



/**
 * Tests the ChatClient class.
 *
 */
public class ChatClientTest {
	
	private ChatClient testClientSender;
	private ChatClient testClientReceiver1;
	private ChatClient testClientReceiver2;

	@Before
	public void setUp(){
		
		ActiveMQConnection connection1;
		ActiveMQConnection connection2;
		ActiveMQConnection connection3;
		
		try {
			
			
			
			connection1 = ActiveMQConnection.makeConnection(	Constants.ACTIVEMQ_URL);
			connection1.start();
			Session session1 = connection1.createSession(false,Session.AUTO_ACKNOWLEDGE);
			Queue destQueue1 = session1.createQueue(Constants.DESTQUEUE);
			MessageProducer producer1 = session1.createProducer(destQueue1);
			
			
			connection2 = ActiveMQConnection.makeConnection(	Constants.ACTIVEMQ_URL);
			connection2.start();
			Session session2 = connection2.createSession(false,Session.AUTO_ACKNOWLEDGE);
			Queue destQueue2 = session2.createQueue(Constants.DESTQUEUE);
			MessageProducer producer2 = session2.createProducer(destQueue2);
			
			connection3 = ActiveMQConnection.makeConnection(	Constants.ACTIVEMQ_URL);
			connection3.start();
			Session session3 = connection3.createSession(false,Session.AUTO_ACKNOWLEDGE);
			Queue destQueue3 = session3.createQueue(Constants.DESTQUEUE);
			MessageProducer producer3 = session3.createProducer(destQueue3);
			
			
			//Since wireclient is static and private, Can not directly create client through
			//it - bryant
	
			
			testClientSender = new ChatClient(producer1, session1, "sender_1", "passsword1");
			testClientReceiver1 = new ChatClient(producer2, session2, "receiver_1", "passsword2");
			testClientReceiver2 = new ChatClient(producer3, session3, "receiver_2", "passsword3");
			
			//logout to clear sessions for re-testing
			//you "can" logout without logging in
			testClientSender.logout();
		    testClientReceiver1.logout();
		    testClientReceiver2.logout();
			
		    
			testClientSender.logon();
		    testClientReceiver1.logon();
		    testClientReceiver2.logon();
		    
				
		} catch (JMSException e) {
			fail("Can not create Client");
			e.printStackTrace();
		} catch (URISyntaxException e) {
			fail("Can not create Client");
			e.printStackTrace();
		}
		
		


			
				
		
	}
	
	@After
	public void tearDown(){
		testClientSender = null;
		testClientReceiver1 = null;
		testClientReceiver2 = null;
	}
	
	@Test 
	public void init_test(){
		setUp();
	}
	
	

	
	@Test 
	public void testRegister_logon(){
		
		testClientSender.register();
		testClientReceiver1.register();
		testClientReceiver2.register();
		
	    testClientSender.logon();
	    testClientReceiver1.logon();
	    testClientReceiver2.logon();
		
	}
	@Test
	public void test_relog(){
		
		testClientSender.logout();
	    testClientReceiver1.logout();
	    testClientReceiver2.logout();
		
	    
		testClientSender.logon();
	    testClientReceiver1.logon();
	    testClientReceiver2.logon();
		
	    
	}
	
	public void test_onlines()
	{
		testClientSender.getUsersOnline(); //is void
		testClientReceiver2.logout();
		testClientSender.getUsersOnline(); //is void
		testClientReceiver2.logon();
		testClientSender.getUsersOnline(); //is void
	}
	@Test
	public void chat_send1(){
		
		//testClientSender.startBroadChat();
		testClientSender.whisper("reciever1 hi");

	}
	@Test
	public void test_send2(){

		//Difficult to test methods that can hold be stopped by itself. namely
		//testClientSender.
		//someone help debug these
		
		
		//You can see it in console at least.
		testClientSender.createChatroom(" room1");
		testClientReceiver1.tryJoinChatroom("room1");
		testClientReceiver2.tryJoinChatroom("room1");
		testClientReceiver1.leaveChatroom();
		




		

	}


}


