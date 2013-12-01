package edu.ucsd.cse110.client.Gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

public class LoginWindow extends JFrame {
	LoginPanel loginPanel = new LoginPanel();;


	public LoginWindow() {
		super("WhisperMe Login");

		
		loginPanel.setBackground(Color.WHITE);
		add(loginPanel, BorderLayout.CENTER);
	}
	
	public void notifyFailLogin(String message){
		loginPanel.loginMessage(message);
		
	}

}
