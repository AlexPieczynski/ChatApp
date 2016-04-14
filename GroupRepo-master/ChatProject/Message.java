//An object meant to be sent by Client to the Server over a Socket

import java.util.Vector;
import java.io.Serializable;

public class Message implements Serializable
{
  private Vector<String> recipients;
  private String sender;
  private String msg;
  
  
  public Message(String msg, String sender, Vector<String> recipients)
  {
    this.msg = msg;
    this.sender = sender;
    this.recipients = recipients;
  }
  
  
  /******************** SERVER-CLIENT COMMINUCATION PROTOCOL *******************/
  
  //returns true if the message from server says that a userName is valid
  public boolean nameValid(){
    return (this.msg.equals("VALID_NAME"));
  }
  
  //returns true if the message from serve says that a userName is invalid
  public boolean nameInvalid(){
    return this.msg.equals("INVALID_NAME") && this.recipients == null;
  }
  
  //returns true if message from server indicates a full server (more than 6 users)
  public boolean serverFull(){
    return this.msg.equals("SERVER_FULL") && this.recipients == null;
  }
  
  //returns true if the message from server says a new user connected
  public boolean newUser(){
    return this.msg.equals("NEW_USER") && this.recipients == null;
  }
  
  //returns true if the message from the server says a user has disconnected
  public boolean userLeft(){
    return this.msg.equals("USER_LEFT") && this.recipients == null;
  }
  
  //SERVER FULL NOTIFICATION
  // looks like: Message m = new Message("SERVER_FULL", "SERVER", null);
  
  //NEW USER NOTIFICATION FROM SERVER TO CLIENT
  // looks like: Message m = new Message("NEW_USER", name, null);
  
  //USER LEFT NOTIFICATION FROM SERVER TO CLIENT, SAME AS MESSAGE
  //  THAT CLIENT SENDS SERVER WHEN IT DISCONNECTS
  // looks like: Message m = new Message("USER_LEFT", name, null);
  
  //USER NAME REQUEST FROM CLIENT
  // looks like: Message m = new Message("IS_VALID?", name, null);
  
  /**************** END PROTOCOL DEFINITION **************************/
  
  
  //GETTERS
  
  public String getMsg(){
    return this.msg;
  }
  
  public String getSender(){
    return this.sender;
  }
  
  public Vector<String> getRecipients(){
    return this.recipients;
  }
}