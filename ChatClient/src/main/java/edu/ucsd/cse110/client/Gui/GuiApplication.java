package edu.ucsd.cse110.client.Gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import edu.ucsd.cse110.client.Gui.MainWindow.*; 

import javax.swing.JFrame;

import Commands.*;

public class GuiApplication implements CommandHandler{
	private LoginWindow loginWindow; // Login Window for User
	private MainWindow mainWindow; // Main Window for loggedOnUser
	public static GuiApplication instance = new GuiApplication();
	private Controller controller  = Controller.getInstance();
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
	    mainWindow.addWindowListener(new WindowAdapter(){
	    	public void windowClosing(WindowEvent e ){
	    		controller.sendCommand(new LogoutCommand());
	    	}
	    });
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
			if(loginWindow != null && mainWindow == null)
				attemptLogin();
			else
			  mainWindow.addUser(command.getUsername());
		}else{
		  if(loginWindow != null)
			loginWindow.notifyFailLogin(command.getLogMessage());
		}
	}
	public void receiveWhisper(WhisperCommand command) {
		if(mainWindow != null){
			mainWindow.redirectWhisper(command.getReceiver(), command.getMessage());
		}
		return;

	}

	public void receiveBroadcast(BroadcastCommand command) {
		if(mainWindow != null){
			mainWindow.displayBroadcast(command.getMessage());
		}

	}

	public void recieveRegisterResponse(Commands command) {
		// TODO Auto-generated method stub

	}

	public void receiveUsers(CheckUsersCommand command) {
		if(mainWindow != null)
			mainWindow.addUser(command.getUsers());

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
			this.receiveLogout((LogoutCommand)command);
			break;
		case Commands.WHISPER:
			this.receiveWhisper((WhisperCommand)command);
			break;
		case Commands.BROADCAST:
			this.receiveBroadcast((BroadcastCommand)command);
			break;
		case Commands.REGISTER:
			
			break;
		case Commands.CHECKUSERS:
			this.receiveUsers((CheckUsersCommand)command);
			break;
		}
		
	}

	private void receiveLogout(LogoutCommand command) {
		if(command.getStatus()){
			this.logOut();
		}
		
	}

}
