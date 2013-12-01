package edu.ucsd.cse110.client.Gui.MainWindow.Menu;

import edu.ucsd.cse110.client.Gui.Controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import Commands.LogoutCommand;

public final class MainMenu extends JMenuBar {
	private Controller controller = Controller.getInstance();
	private AccountMenu menu;

	public MainMenu() {
		menu = new AccountMenu();
	}

	private final class AccountMenu extends JMenu {
		private JMenuItem logOut = new JMenuItem("LogOut");

		public AccountMenu() {
			super("Account");
		}

		public void prepareItems() {
			add(logOut);
			logOut.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.recieveCommand(new LogoutCommand());
				}
			});
		}
	}

	public void prepareItems() {
		menu.prepareItems();
		add(menu);
	}

}
