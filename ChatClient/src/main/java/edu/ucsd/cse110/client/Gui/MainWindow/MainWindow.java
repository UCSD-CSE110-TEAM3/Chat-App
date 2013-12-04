package edu.ucsd.cse110.client.Gui.MainWindow;

import edu.ucsd.cse110.client.Gui.Controller;
import edu.ucsd.cse110.client.Gui.MainWindow.Menu.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import Commands.CheckUsersCommand;


public class MainWindow extends JFrame {
	
	WhisperPanels currentWhispers = WhisperPanels.getInstance();
	MainMenu menu = new MainMenu();
	UserDisplayPanel usersOnline = new UserDisplayPanel();
	BroadcastPanel broadcast = BroadcastPanel.getInstance();
	Controller controller = Controller.getInstance();
	public MainWindow() {
		super("WhisperMe");
		currentWhispers.newChat();
		menu.prepareItems();
		broadcast.prepareComponents();
		controller.sendCommand(new CheckUsersCommand());
		
		add(menu, BorderLayout.NORTH);
		add(usersOnline, BorderLayout.LINE_START);
		add(currentWhispers, BorderLayout.CENTER);
		add(broadcast, BorderLayout.LINE_END);
		
	}
	
	public void addUser(String username){
		usersOnline.addUser(username);
	}

	public void addUser(Set<String> users) {
		Iterator<String> it = users.iterator();
		while(it.hasNext())
			usersOnline.addUser(it.next());
	}

	public void displayBroadcast(String message) {
		broadcast.displayBroadcast(message);
		
	}

	public void redirectWhisper(String from, String message) {
		currentWhispers.printWhisper(from, message);
		
	}
	

}
