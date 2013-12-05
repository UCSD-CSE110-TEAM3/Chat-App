package edu.ucsd.cse110.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;


/* OBSERVER PATTERN
 * OBSERVABLE: Chatroom
 * OBSERVERS: Users/members
 */
public class Chatroom {

    private String roomID; // name of chatroom
    private HashMap<String, String> members; //structure to access room members
    private int amount;
    private static int LIMIT = 10; //limit members per chatroom
    
    // make a chatroom with a random id
    public Chatroom() {
   	 this.roomID = generateRandomID();
   	 members = new HashMap<String, String>();
   	 amount = 0;
    }
    
    // make a chatroom with an id
    public Chatroom(String roomID) {
   	 this.roomID = roomID;
   	 members = new HashMap<String, String>();
   	 amount = 0;
    }
    
    public String generateRandomID() {
   	 Random rnd = new Random();
   	 int number = rnd.nextInt();
   	 number = Math.abs(number);
   	 number = number % 10000; //want a >= 4 digit number
   	 return Integer.toString(number);
    }
    
    public String getRoomID() {
   	 return roomID;
    }
    
    public int getCapacity() {
   	 return amount;
    }
    
    // add an observer (registerObserver)
    public void addMember(String member) {
   	 members.put(member, member);
   	 amount++;
    }
    
    // remove an observer (removeObserver)
    public void removeMember(String member) {
   	 members.remove(member);
   	 amount--;
    }
    
    public Set<String> getMembers() {
   	 return members.keySet();
    }
}


