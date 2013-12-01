package edu.ucsd.cse110.client.Gui.MainWindow;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Whisper extends JPanel {
	JTextArea  chatHistory;
	JTextField output;
	JButton    quit; 
	String outputStream;
	String inputStream;
	
	

	
	public Whisper() {
		super(new BorderLayout());

		quit = new JButton("End Chat");
		quit.addActionListener(WhisperPanels.getInstance());
		
		chatHistory = new JTextArea(30, 30);
		chatHistory.setEditable(false);
		chatHistory.setFocusable(false);
		output = new JTextField(30);
		output.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputStream = output.getText();
				inputStream = outputStream;
				output.setText("");
				updateHistory();
			}
		});
		

	
		
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
