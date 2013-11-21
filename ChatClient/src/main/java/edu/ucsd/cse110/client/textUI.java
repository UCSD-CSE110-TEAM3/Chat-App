package edu.ucsd.cse110.client;

import java.util.Scanner;

public class textUI {
	private static Scanner scanner = new Scanner(System.in);
	private Scanner cmdScanner = new Scanner(System.in);
	private int cmd = 0;
	
	protected boolean registering = false;

	protected String username = "";
	protected String password = "";

	/**
	 * Constructor for initializing data of the UI
	 */
	public textUI() {
	}

	/**
	 * will run the UI, and prompt user with command menu
	 * 
	 */
	public void run() {
		boolean quitcmd = false;
		do {
			promptMenu();
			String input = cmdScanner.next();
			cmd = getCmd(input);
			if (validInput(cmd))
				switch (cmd) {
				case 0:
					promptRegister();
					registering = true;
					quitcmd = true;
					break;
				case 1:
					promptLogin();
					if(username != null && password != null)
						quitcmd = true;
					break;
				case 2:
					promptMenu();
					break;
				case 3:
					cmdScanner.close();
					System.exit(1);
					quitcmd = true;
					System.out.println("Quitting ...");
					break;
				default:
					break;
				}
			else
				System.out.println("Enter Valid Command\n/cmd for cmd menu");
		} while (!quitcmd);
	}

	private boolean validInput(int cmd) {
		if (cmd == 0 || cmd == 1 || cmd == 2 || cmd == 3)
			return true;
		return false;
	}

	private void promptLogin() {
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("LOGIN");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("Enter your username.");
		System.out.print("Username: ");
		username = scanner.next();
		System.out.println("Enter your password");
		System.out.print("Password: ");
		password = scanner.next();
		return;
	}

	private void promptRegister() {
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("REGISTER");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("Enter desired username.");
		System.out.print("Username: ");
		username = scanner.next();
		System.out.println("Enter desired password");
		System.out.print("Password: ");
		password = scanner.next();
		return;
	}

	private void promptMenu() {
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("MENU");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("register\t--\t/rg");
		System.out.println("login\t\t--\t/l");
		System.out.println("cmd menu\t--\t/cmd");
		System.out.println("quit\t\t--\t/q");
	}

	private int getCmd(String command) {
		try {
			if (command.equals("/rg")) {
				return 0;
			} else if (command.equals("/l")) {
				return 1;
			} else if (command.equals("/cmd")) {
				return 2;
			} else if (command.equals("/q"))
				return 3;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return 4;

	}
}
