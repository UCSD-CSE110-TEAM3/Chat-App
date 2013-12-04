package edu.ucsd.cse110.client.Gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Insets;

import javax.swing.*;

import Commands.CommandHandler;
import Commands.Commands;
import Commands.LoginCommand;
import Commands.RegisterCommand;

public class LoginPanel extends JPanel {
	private GridBagConstraints grid = new GridBagConstraints();
	private CommandHandler commandHandler = Controller.getInstance();
	private Commands panelCommand;
	private Cursor mouse = new Cursor(Cursor.DEFAULT_CURSOR);
	
	private  JLabel user = new JLabel("Username");
	private  JLabel pass = new JLabel("Password");
	private  JLabel log  = new JLabel("");
	private  JTextField username = new JTextField("Enter Username", 10);
	private  JPasswordField password = new JPasswordField(10);
	private  JButton login = new JButton("Login In");
	private  JButton register = new JButton("Register");

	public LoginPanel() {
		super();
		setLayout(new GridBagLayout());
		grid.insets = new Insets(0, 10, 5, 10);
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(username.getText()!=null && password.getPassword()!= null){
					commandHandler.sendCommand(new LoginCommand(username
						.getText(), new String(password.getPassword())));
					waitForRespond();
				}
				
			}
		});
		register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(username.getText()!=null && password.getPassword()!= null){
					commandHandler.sendCommand(new RegisterCommand(username
						.getText(), new String(password.getPassword())));
					waitForRespond();
				}
				
			}
		});
		
		
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		grid.gridy = 0;
		grid.gridx = 0;
		grid.gridwidth = 2;
		add(log, grid);
		grid.gridwidth = 1;
		grid.gridy = 1;
		add(user, grid);
		grid.gridx = 1;
		add(username, grid);
		grid.gridx = 0;
		grid.gridy = 2;
		add(pass, grid);
		grid.gridx = 1;
		add(password, grid);
		grid.gridx = 0;
		grid.gridy = 3;
		add(register, grid);
		grid.gridx = 1;
		add(login, grid);
	
	}

	public Dimension getPreferredSize() {
		return new Dimension(200, 700);
	}

	public Dimension getMinimumSize() {
		return this.getPreferredSize();
	}

	public void waitForRespond() {
		username.setEditable(false);
		password.setEditable(false);
		
	}
	
	public void loginMessage(String message) {
		username.setEditable(true);
		password.setEditable(true);
		log.setText(message);
		repaint();
	}


}