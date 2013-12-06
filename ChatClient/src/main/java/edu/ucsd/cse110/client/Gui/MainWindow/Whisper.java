package edu.ucsd.cse110.client.Gui.MainWindow;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Commands.WhisperCommand;
import edu.ucsd.cse110.client.Gui.Controller;

public class Whisper extends JPanel {
	JTextArea  chatHistory;
	JTextField output;
	JButton    quit; 
	String outputStream;
	String inputStream;
	Controller controller = Controller.getInstance();
	
	public Whisper(String name ) {
		super(new BorderLayout());
		this.setName(name);
		
		chatHistory = new JTextArea(30, 30);
		chatHistory.setEditable(false);
		chatHistory.setFocusable(false);
		output = new JTextField(30);
		output.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputStream = output.getText();
				inputStream = outputStream;
				output.setText("");
				sendWhisper();
				updateHistory();
			}

			
		});
		add(chatHistory, BorderLayout.CENTER);
		add(output, BorderLayout.PAGE_END);
		
	}
    
	public void sendWhisper() {
		if(outputStream != null){
			controller.sendCommand(new WhisperCommand(this.getName(), outputStream));
		}
				
	}
	
	public void updateHistory(String message){
		chatHistory.append(message + "\n");
	}
	public void updateHistory() {
		if (inputStream != null) {
			chatHistory.append(inputStream + "\n");
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//add(quit, BorderLayout.LINE_END);
		add(chatHistory, BorderLayout.CENTER);
		add(output, BorderLayout.PAGE_END);
	}
}
