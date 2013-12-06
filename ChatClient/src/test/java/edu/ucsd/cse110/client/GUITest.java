package edu.ucsd.cse110.client;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.ucsd.cse110.client.Gui.Controller;
import edu.ucsd.cse110.client.Gui.GuiApplication;

public class GUITest {
	private GuiApplication application;
	private Controller controller;
	
	@Before
	private void setUp(){
		application = GuiApplication.getInstance();
		controller = Controller.getInstance();
	}
	
	@After
	private void tearDown(){
		application = null;
		controller = null;
	}
	@Test
	public void testApplication() {
		if(application == null)
			fail("Application is null");
	}
	
	@Test
	public void testController(){
		if(controller == null)
			fail("controller is null");
		controller.prepareController();
		if(controller.ingoingHandler == null)
			fail("ingoing null after prep");
			
	}

}
