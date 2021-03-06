package edu.ucsd.cse110.client.Gui.MainWindow;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Commands.BroadcastCommand;
import edu.ucsd.cse110.client.Gui.Controller;

public class BroadcastPanel extends JPanel{
	private static BroadcastPanel instance = new BroadcastPanel();
	private Controller controller  = Controller.getInstance();
	
	private JTextArea broadcasts;
	private JTextField message;
    private JLabel heading;
    
	private BroadcastPanel(){
		super(new BorderLayout());
		
	}
	
	public static BroadcastPanel getInstance(){
		if(instance == null){
			instance = new BroadcastPanel();
		}
		
		return instance;
	}
	
	public void prepareComponents(){
		
		broadcasts = new JTextArea(JTextArea.HEIGHT, 25);
		broadcasts.setEditable(false);
		heading = new JLabel("Broadcasts");
		message = new JTextField(25);
		message.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(message.getText()!=""){
					controller.sendCommand(new BroadcastCommand(message.getText()));
				}
				message.setText("");
		
			}
		});
		add(heading, BorderLayout.NORTH);
		add(broadcasts, BorderLayout.CENTER);
		add(message, BorderLayout.SOUTH);
	}
	

	
	public void updateHistory(String username, String message) {
		if (message != null) {
			broadcasts.append(username+": "+message + "\n");
		}
	}

	public void displayBroadcast(String message) {
		broadcasts.append(message +"\n");
		
	}
	
}
