package edu.ucsd.cse110.client.Gui.MainWindow;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

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
	private class TabPanel extends JPanel{
		JButton exit;
		JLabel  title;
		
		public TabPanel(String title){
			this.title = new JLabel(title);
			this.title.setToolTipText("Send a Whisper to "+title);
			this.exit = new JButton("x");
			this.exit.setMargin(new Insets(0,0,0,0));
			this.exit.setVerticalAlignment(SwingConstants.CENTER);
			this.exit.setActionCommand("Quit: "+title);
			this.exit.addActionListener(WhisperPanels.getInstance());
			this.setFont(new Font(null, Font.PLAIN, 8));
			add(this.title);
			add(exit);
		}
	}
	/*
	 * This method will get replaced by action event listener when
	 * fully implemented
	 */
	public void newChat() {
		/* this line below is only here for test purpose */
		this.addTab("NewChat", new Whisper(""));

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
		if(e.getActionCommand().contains("Quit: ")){
			removeUserTab(e.getActionCommand().split(" ")[1]);
		}else if(receivers.contains(e.getActionCommand()) == false){
			addUserTab(e.getActionCommand());
		}
	}

	public void printWhisper(String from, String message) {
		int index = this.indexOfTab(from);
		Whisper temp;
		if(index < 0){
			temp = new Whisper(from);
			temp.updateHistory(message);
			addTab(from, temp);
			receivers.add(from);
		}
		else{
		temp = (Whisper)this.getComponentAt(index);
		temp.updateHistory(message);
		}
		temp.repaint();
		this.repaint();
	}
	
	public void removeUserTab(String user){
		int index = indexOfTab(user);
		if(index >= 0){
			removeTabAt(index);
			receivers.remove(user);
		}
	}
	
	public void addUserTab(String user){
		addTab(user, new Whisper(user));
		receivers.add(user);
		setTabComponentAt(indexOfTab(user), new TabPanel(user));
		repaint();
	}
	
	

}
