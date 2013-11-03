/**
 * Name1: Nhu-Quynh Liu
 * PID1: A10937319
 * Email1: n1liu@ucsd.edu
 */
package edu.ucsd.cse110.client;

import static org.junit.Assert.*;

import org.junit.Test;


/**
 * Tests the ChatClient classs.
 *
 */
public class ChatClientTest {

	@Test
	public void testSend() {
		try {
			ChatClient myClient = new ChatClient( null, null );
		} catch (Exception e ) {
			fail( "Constructor threw an exception ");
		}		
	}

}
