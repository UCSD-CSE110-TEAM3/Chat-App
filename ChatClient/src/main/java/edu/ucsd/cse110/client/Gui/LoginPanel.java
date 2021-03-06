package edu.ucsd.cse110.client.Gui;

import java.awt.*;
import java.awt.event.*;
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
	private  JButton login = new JButton("Login");
	private  JButton register = new JButton("Register");
	
	private  ImageIcon icon = new ImageIcon("wm.png", "Whisper Me Logo");
	private  JLabel  logo = new JLabel(icon);
	private  boolean clicked;

	public LoginPanel() {
		super();
		
		setLayout(new GridBagLayout());
		clicked = false;
	    username.addMouseListener(new MouseAdapter(){
	    	
	        public void mouseClicked( MouseEvent e ){
	        	if ( !clicked ) {
	        		clicked = true;
	        		username.setText("");
	        		return;
	        	}
        		username.removeMouseListener(this);
	        }
	        
	    });
	    
	    username.addKeyListener(new KeyListener(){
	    	
	        public void keyTyped( KeyEvent e ){
	        	if ( !clicked ) {
	        		clicked = true;
	        		username.setText("");
	        		return;
	        	}
        		username.removeKeyListener(this);
	        }

			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
	        
	    });
	    
	    password.addKeyListener(new KeyListener(){
	    	
	        public void keyTyped( KeyEvent e ){
	        }

			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void keyPressed(KeyEvent arg0) {
				if( arg0.getKeyCode()==KeyEvent.VK_ENTER) {
					login();
				}
			}
	        
	    });	    
	    
		grid.insets = new Insets(0, 10, 75, 10);
		grid.gridy = 0;
		grid.gridx = 0;
		grid.gridwidth = 2;
		add(logo,  grid);
		grid.insets = new Insets(0, 10, 5, 10);
		grid.gridy = 1;
		grid.gridx = 0;
		grid.gridwidth = 2;
		add(log, grid);
		grid.gridwidth = 1;
		grid.gridy = 2;
		add(user, grid);
		grid.gridx = 1;
		add(username, grid);
		grid.gridx = 0;
		grid.gridy = 3;
		add(pass, grid);
		grid.gridx = 1;
		add(password, grid);
		grid.gridx = 0;
		grid.gridy = 4;
		add(register, grid);
		grid.gridx = 1;
		add(login, grid);
		
		clicked = false;
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				login();
			}
		});
		register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String user = username.getText().trim();
				String pass = new String(password.getPassword()).trim();
				
				if(user.length()>0 && pass.length() >0){
					commandHandler.sendCommand(new RegisterCommand(user, pass));
					waitForRespond();
				}else{
					loginMessage("Password or Username not specified");
				}
				
				
			}
		});
		
		username.requestFocusInWindow();
		
	}

	public void paintComponent(Graphics g) {
		/*super.paintComponent(g);
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
	*/
	}

	public Dimension getPreferredSize() {
		return new Dimension(200, 700);
	}

	public Dimension getMinimumSize() {
		return this.getPreferredSize();
	}

	public void login() {
		String user = username.getText().trim();
		String pass = new String(password.getPassword()).trim();
		
		if(user.length()>0 && pass.length() >0){
			commandHandler.sendCommand(new LoginCommand(user, pass));
			waitForRespond();
		}else{
			loginMessage("Password or Username not specified");
		}
	}
	
	public void waitForRespond() {
		username.setText("");
		password.setText("");
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
