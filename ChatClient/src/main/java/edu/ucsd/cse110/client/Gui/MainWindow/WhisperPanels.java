package edu.ucsd.cse110.client.Gui.MainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.HashSet;

import java.util.Set;

import javax.swing.JTabbedPane;

public class WhisperPanels extends JTabbedPane implements ActionListener{
	protected static WhisperPanels instance = new WhisperPanels();
	private Set<String> receivers = new HashSet(0);
	private int size = 0;
	
	private WhisperPanels() {
		super();
	}
    
	public static WhisperPanels getInstance(){
		if(instance == null){
			instance = new WhisperPanels();
		}
		return instance;
	}
	/*
	 * This method will get replaced by action event listener when
	 * fully implemented
	 */
	public void newChat() {
		/* this line below is only here for test purpose */
		this.addTab("NewChat", new Whisper());

	}


	/*
	 * This is called from events that want to request a new 
	 * chat tab. It must be able to specify the user.
	 * 
	 * Note: For now it will not check if the user exists or not
	 * that is why we only call it from events that know if user
	 * exists
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("End Chat")){
	        //int index = this.indexOfTabComponent((Whisper)e.getSource());
			
	       // if (index != -1) 
			 //  remove(index);
			   		
		}
		if(receivers.contains(e.getActionCommand()) == false){
			addTab(e.getActionCommand(), new Whisper());
			receivers.add(e.getActionCommand());
			
		}
	}
	
	
}
