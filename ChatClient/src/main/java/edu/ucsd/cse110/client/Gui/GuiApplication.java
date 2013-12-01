package edu.ucsd.cse110.client.Gui;

import edu.ucsd.cse110.client.Gui.MainWindow.*; 

import javax.swing.JFrame;

import Commands.*;

public class GuiApplication implements CommandHandler{
	private LoginWindow loginWindow; // Login Window for User
	private MainWindow mainWindow; // Main Window for loggedOnUser
	public static GuiApplication instance = new GuiApplication();
	
	// We will import a list of Windows here for chatrooms

	private GuiApplication() {
	}

	public static GuiApplication getInstance() {
		if (instance == null) {
			instance = new GuiApplication();
		}
		return instance;
	}

	/*
	 * Only method accessible by an executable file
	 * 
	 * Run declares a new loginWindow. For the user to make
	 */
	public void run() {
		loginWindow = new LoginWindow();
		loginWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginWindow.setSize(300, 600);
		loginWindow.setVisible(true);
	}

	public void attemptLogin() {
		if (mainWindow == null)
	    	loadMainWindow();

	}

	private void loadMainWindow() {
		loginWindow.dispose();
		loginWindow = null;
		mainWindow = new MainWindow();
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setSize(600, 600);
		mainWindow.setVisible(true);
	}

	public void logOut() {
		mainWindow.dispose();
		mainWindow = null;
		loginWindow = new LoginWindow();
		loginWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginWindow.setSize(300, 600);
		loginWindow.setVisible(true);
	}
	
	public void receiveLogon(LoginCommand command){
		
		if(command.getStatus()){
			attemptLogin();
		}else{
		  if(loginWindow != null)
			loginWindow.notifyFailLogin(command.getLogMessage());
		}
	}
	public void receiveWhisper(Commands command) {
		// find a way to send to get message to window.
		return;

	}

	public void receiveBroadcast(Commands command) {
		// TODO Auto-generated method stub

	}

	public void recieveRegisterResponse(Commands command) {
		// TODO Auto-generated method stub

	}

	public void receiveUsers(Commands command) {
		// TODO Auto-generated method stub

	}

	public void sendCommand(Commands command) {
		// TODO Auto-generated method stub
		
	}

	public void recieveCommand(Commands command) {
		if (command == null) {
			return;
		}

		switch (command.getType()) {
		case Commands.LOGIN:
			receiveLogon((LoginCommand)command);
			break;
		case Commands.LOGOUT:
			
			break;
		case Commands.WHISPER:
			
			break;
		case Commands.BROADCAST:
			
			break;
		case Commands.REGISTER:
			
			break;
		case Commands.CHECKUSERS:

		}
		
	}

}
