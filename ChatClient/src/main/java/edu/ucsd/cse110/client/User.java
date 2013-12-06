package edu.ucsd.cse110.client;

public class User {
	private String username;
	private String password;
	private boolean online;
	private boolean register;
	private String chatroomid;
	
	public User(String username, String password){
		this.username = username;
		this.password = password;
		online = false;
		chatroomid = ""; // not in a chatroom
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public String getPassword(){
		return password;
	}
	
	public boolean online(){
		return online;
	}
	
	public boolean register(){
		return register;
	}
	
	public void setOnlineStatus(boolean status){
		online = status;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	public void setChatroomID(String chatroomid){
		this.chatroomid = chatroomid;
		return;
	}
	
	public String getChatroomID() {
		return this.chatroomid;
	}
	
	public void removeChatroomID() {
		this.chatroomid = "";
		return;
	}

}
