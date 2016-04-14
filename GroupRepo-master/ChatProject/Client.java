//class to hold information for each Client in the Chat Application
//A new process is created for each client connected to the server
//connects to Server on port 9001 by default

import javax.swing.JOptionPane;
import java.io.*;
import java.net.*;
import java.util.Vector;


public class Client implements Runnable
{
  private ClientGUI gui; //reference to the GUI class to update when messages are received
  private String userName; //string to indentify the client
  private boolean connected;
  private Socket sock; //connection to server
  private Vector<String> peers; //list of others connected to server
  private ObjectOutputStream out = null; //sends data to server over sock
  private ObjectInputStream in = null;   //receives data from server over sock
  private Vector<Message> received;  //list of all received messages since instantiation
  
  
  //instantiates a new client
  //should be called when the ClientGUI is instantiated 
  //next need to connect to a server with valid username
  public Client(ClientGUI gui)
  {
    this.gui = gui;
    connected = false;
    peers = new Vector<String>();
    received = new Vector<Message>();
  }
  
  
  //gets ipAddess from popup in GUI, binds socket to that ip
  //returns a boolean to GUI indicating whether or not the connection was successful
  //if true, GUI asks for username
  //if false, GUI asks for another IP, i.e. calls this function again
  public boolean connectToServer(String ipAddress)
  {
    try {
      this.sock = new Socket(ipAddress, 9001);
      this.out = new ObjectOutputStream(sock.getOutputStream());
      this.in = new ObjectInputStream(sock.getInputStream());
      this.connected = true;
      return true;
    }
    
    catch (UnknownHostException e) {
      System.err.println("Error connecting to host");
      this.connected = false;
      return false;
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for connection");
      System.exit(1);
    }
    return true;
  }
  
  
  //GUI popup a text input to get user name
  //this functions sends a message to server asking to use the name
  //returns bool to GUI telling whether or not it must reprompt for username
  //After userName is set, run() method is called and Client begins processing
  //  messages from the server in a new thread
  public boolean setUsername(String name)
  {
    //define protocol for how client asks server is name is valid
    //server, by protocol, must check if each msg is "ISVALID?"
    // and if recipients is null. if so, server returns msg
    Message m = new Message("IS_VALID?", name, null);
    
    try{
      //send request
      out.writeObject(m);
      out.flush();
      
      //check server's response
      m = (Message) in.readObject();
      if (m.nameValid()){
        this.userName = name;
        this.peers = m.getRecipients();
        gui.updatePeers(this.peers);
        new Thread(this).start();
        return true;
      }
      else if (m.nameInvalid())
        return false;
      else if (m.serverFull()){
        JOptionPane.showMessageDialog(null, "This server is full. The program will now exit.",
                                    "Server Connection Error", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
      }
      else{
        System.out.println("ERROR: invalid response from server: "+ m.getMsg());
        return false;
      }
    }
    catch(IOException ioe){
      System.out.println("IO EXCEPTION in setUsername");
    }
    catch(ClassNotFoundException cnfe){
      System.out.println("Message class not found!");
      System.exit(-1);
    }
    return false;
  }
  
  
  //called by send button of GUI with textbox contents as parameter
  //constructs and sends a Message object to the server over the socket
  public void sendMessage(String words, Vector<String> recipients)
  {
    //don't send anything if client is not connected to a server
    if (!connected){
      JOptionPane.showMessageDialog(null, "You must connect to a server before sending a message",
                                    "Message Send Failed", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    Message m = new Message(words, this.userName, recipients);
    try{
      out.writeObject(m);
      out.flush();
      out.reset();
    }
    catch(IOException ioe){
      System.out.println("IO Exception: message could not be sent");
    }
  }
  
  
  //informs server that client is disconnecting
  //sets all data members to indicate disconnection
  //calls on JFrame close and perhaps also a disconnect button
  public void disconnect()
  {
    if (!connected || (userName == null))
      return;
    
    sendMessage("USER_LEFT", null);
    userName = null;
    connected = false;
    sock = null;
    peers = new Vector<String>();
    received = new Vector<Message>();
    out = null;
    in = null;
  }
  
  
  //main event loop
  //check for incoming messages from server, updates GUI
  //interupted by action listener for SEND button
  public void run()
  {
    while (connected)
    {
      try{
        Message m = (Message) in.readObject();
        
        if (m.newUser()){ //server notifying clients a new user has connected
          if (!m.getSender().equals(this.userName))
            this.peers.add(m.getSender());
          this.gui.updatePeers(this.peers);
        }
        if (m.userLeft()){ //user has left
          this.peers.remove(m.getSender());
          this.gui.updatePeers(this.peers);
        }
        else{ //regular message from some other Client
          this.received.add(m);
          this.gui.updateMessages(m); //make sure to update GUI
        }
      } 
      
      catch(IOException ioe){
        //System.out.println("IOE: could not read object from server");
      } catch(ClassNotFoundException cnfe){
        System.out.println("CNFE: could not find class Message");
      }
    }
  }
  
  //test code
  public static void main (String[] args)
  {
    //Client c = new Client();
  }
}