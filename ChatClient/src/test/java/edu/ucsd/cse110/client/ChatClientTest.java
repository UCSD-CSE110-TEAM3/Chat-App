package edu.ucsd.cse110.client;

import static org.junit.Assert.*;

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
	private void setUp(){
		testClientSender = new ChatClient(null, null, "this", "thatis");
		testClientReceiver1 = new ChatClient(null, null, "receiver1", "test");
		testClientReceiver2 = new ChatClient(null, null, "receiver2", "test");
	}
	
	@After
	private void tearDown(){
		testClientSender = null;
		testClientReceiver1 = null;
		testClientReceiver2 = null;
	}

	@Test
	public void testSend() {
		try{
			testClientSender.logon();
		}
		catch(Exception e){
			System.err.println("could not log in");
		}
	}
	
	@Test 
	public void testRegister(){
		testClientReceiver1.register();
	}


}
