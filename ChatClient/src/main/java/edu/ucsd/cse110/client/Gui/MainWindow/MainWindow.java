package edu.ucsd.cse110.client.Gui.MainWindow;

import edu.ucsd.cse110.client.Gui.MainWindow.Menu.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainWindow extends JFrame {
	
	WhisperPanels currentWhispers = WhisperPanels.getInstance();
	MainMenu menu = new MainMenu();
	UserDisplayPanel usersOnline = new UserDisplayPanel();
	BroadcastPanel broadcast = BroadcastPanel.getInstance();
	
	public MainWindow() {
		super("WhisperMe");
		currentWhispers.newChat();
		menu.prepareItems();
		broadcast.prepareComponents();
		usersOnline.addUser("test");
		usersOnline.addUser("test2");
		
		add(menu, BorderLayout.NORTH);
		add(usersOnline, BorderLayout.LINE_START);
		add(currentWhispers, BorderLayout.CENTER);
		add(broadcast, BorderLayout.LINE_END);
		
	}

}
