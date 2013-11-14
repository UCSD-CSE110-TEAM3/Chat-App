package edu.ucsd.cse110.client;

import java.util.Scanner;

public class textUI {
	private static Scanner scanner = new Scanner(System.in);
	private Scanner cmdScanner = new Scanner(System.in);
	private int cmd = 0;

	public textUI() {
	}

	public void run() {
		boolean quitcmd = false;

		System.out.println("\t\tWHISPER ME");
		promptMenu();
		do {
			String input = cmdScanner.next();
			cmd = getCmd(input);
			if (validInput(cmd))
				switch (cmd) {
				case 0:
					promptRegister();
					break;
				case 1:
					promptLogin();
					break;
				case 2:
					promptMenu();
					break;
				case 3:
					cmdScanner.close();
					System.exit(1);
					break;
				default:
					break;
				}
			else
				System.out.println("Enter Valid Command\n/cmd for main menu");
		} while (!quitcmd);
		System.out.println("Quitting ...");
	}

	private boolean validInput(int cmd) {
		if (cmd == 0 || cmd == 1 || cmd == 2 || cmd == 3)
			return true;
		return false;
	}

	private static void promptLogin() {
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("LOGIN");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("Enter your username.");
		System.out.print("Username: ");
		String username = scanner.next();
		System.out.println("Enter your password");
		System.out.print("Password: ");
		String password = scanner.next();
	}

	private static void promptRegister() {
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("REGISTER");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("Enter your username.");
		System.out.print("Username: ");
		String username = scanner.next();
		System.out.println("Enter your password");
		System.out.print("Password: ");
		String password = scanner.next();
	}

	private void promptMenu() {
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("MENU");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("register\t--\t/rg");
		System.out.println("login\t\t--\t/l");
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
