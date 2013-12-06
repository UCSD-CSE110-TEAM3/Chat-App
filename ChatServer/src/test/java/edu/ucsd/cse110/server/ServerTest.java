package edu.ucsd.cse110.server;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests the class Server.
 *
 */
public class ServerTest {
	private Server server;
	
	@Before
	private void setUp(){
		server = new Server();
	}
	
	@After
	private void tearDown(){
		server = null;
	}
	
	@Test
	public void testAccountLoad() throws FileNotFoundException {
		try{
			FileReader f0 = new FileReader("nonexistingfile.txt");
			fail("file not found");
		}catch(FileNotFoundException e){}
	}
	
	@Test void testValidLogin(){
		assertFalse(server.valid_login(null, null));
	}

}
