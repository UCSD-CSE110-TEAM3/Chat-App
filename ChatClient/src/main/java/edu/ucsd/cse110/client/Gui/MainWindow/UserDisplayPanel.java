package edu.ucsd.cse110.client.Gui.MainWindow;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class UserDisplayPanel extends JPanel{
   private HashMap<String, JButton> users =  new HashMap<String, JButton>(0);
   JLabel  heading = new JLabel("Users Online");
   public UserDisplayPanel(Set<String> usernames){
	   super();
	   
	   JButton tempButton;
	   String  tempuser;
	   Iterator<String> iterator = usernames.iterator();
	   add(heading);
	   this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	   while(iterator.hasNext()){
		   tempuser = iterator.next();
		   if(tempuser == null)
			   continue;
		   
		   tempButton = new JButton(tempuser);
		   tempButton.addActionListener(WhisperPanels.getInstance());
		   users.put(tempuser, tempButton);
		   System.out.println("Yes");
		   this.add(tempButton);
	   }
	   JButton quit = new JButton("End Chat");
	   quit.addActionListener(WhisperPanels.getInstance());
	   this.add(quit);
   }
   
   public UserDisplayPanel(){
	  super();
	  this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	  add(heading);
   }
   
   public void addUser(String user){
	   //add(heading, BorderLayout.NORTH);
	   if(users.containsValue(user))
		   return;
	   JButton tempButton = new JButton(user);
	   tempButton.addActionListener(WhisperPanels.getInstance());
	   users.put( user, tempButton);
		   
	   this.add(tempButton);
	   //repaint container
	   
	   repaint();
	   tempButton.repaint();
   }

   public void deleteUser(String user){
	   if(users.containsKey(user) == false)
		   return;
	   
	   
	   this.remove(users.get(user));
	   users.remove(user);
	   repaint();
   }
}
