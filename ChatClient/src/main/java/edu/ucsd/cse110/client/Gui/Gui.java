package edu.ucsd.cse110.client.Gui;

public class Gui {
	public static void main(String[] args) {
		GuiApplication application = GuiApplication.getInstance();
		Controller controller = Controller.getInstance();
		controller.prepareController();
		if(controller.ingoingHandler == null || controller.outgoingHandler == null ){
			System.out.println("Failed to connect to server.");
			return;
		}
		application.run();	
	}

}
