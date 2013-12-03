package Commands;

import java.util.HashSet;
import java.util.Set;

public class CheckUsersCommand extends Commands{
	private Set<String> usersOnline;
	
	public CheckUsersCommand(){
		super.type = Commands.CHECKUSERS;
		super.status = true;
		this.usersOnline = new HashSet<String>(0);
	}
	
	public int getSize(){
		return usersOnline.size();
	}
	
	public Set<String> getUsers(){
		return usersOnline;
	}
	
	public void addUser(String user){
		usersOnline.add(user);
	}
}
