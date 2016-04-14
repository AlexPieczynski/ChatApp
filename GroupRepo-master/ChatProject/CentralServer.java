/*  A class to act as a central server for a network chat project. 
 * This class accepts "clients" that can communicate to each other through the
 * server.
 * Create for CS-342 Spring 2016 class at UIC.
 * Project # 4
 */
import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import java.net.*;

public class CentralServer implements Runnable{
  //A thread-safe hash-map for storing Clients and their Object writers.
  private static ConcurrentHashMap<String, ObjectOutputStream> clientList = new ConcurrentHashMap<String, ObjectOutputStream>();
  //A String Vector of list of Clients.
  private static Vector<String> vectorClientList = new Vector<String>();
  
  
    //We'll use 9001 as the port to host the chat server.
    private final static int serverPort = 9001;
    private ServerGUI servGUI;
    
  //Constructor code simply "listens" on the port and waits for clients to join.
  //Whenever new clients join, a new ClientHandler thread is created for them.
    public CentralServer(ServerGUI servGUI){
      this.servGUI = servGUI;
      new Thread(this).start();
    }
    
    public void run(){
      ServerSocket chatListener = null;
      try{
        chatListener = new ServerSocket(serverPort);
        while(true){
        new ClientHandler(chatListener.accept(), this, servGUI).start();
        }
      }
      
       catch(Exception e){
          System.out.println("Exception when creating ServerSocket");
        }
      
     finally{
       try{
         chatListener.close();
       }
       catch(Exception e){
         System.out.println("Exception when closing ServerSocket");
       }
     }
    }

    
   //Helper class to handle each new client.
    public static class ClientHandler extends Thread{
         
        //A boolean to indicate when a thread should be running. Will be set to false to terminate.
        private boolean running = true;
        
        CentralServer server;
        ServerGUI servGUI;
        
        private String clientName;
        private Socket clientSocket;
        private ObjectInputStream input;
        private ObjectOutputStream output;
        //To check if a client was successfully added to clientList.
        private boolean attempt = false;
        private String msgString = null;
        private Message message = null; 
        
        public ClientHandler(Socket clientSocket, CentralServer server, ServerGUI servGUI){
          this.clientSocket = clientSocket;
          this.server = server;
          this.servGUI = servGUI;
        }
        
        
        public void run(){
          try{
           //Initialize the input stream and output stream readers.
            this.input = new ObjectInputStream(clientSocket.getInputStream());
            this.output = new ObjectOutputStream(clientSocket.getOutputStream());
            
            while (running) {
              
              //This assumes that the client GUI will handle the prompt for username input.

              //Read an object from the input stream.
              try{
              message = (Message)input.readObject(); //Cast the object as a message.
              msgString = message.getMsg();
              }
              
              catch(Exception e){
               e.printStackTrace();
              }
              
              //First check if this is a request to add a client to the server.
              if(msgString.equals("IS_VALID?")){
                
                if(server.getVectorClientList().size() < 6){ //Only add client if there's less than 6 people on server.
                  
               attempt = addToClientList(message.getSender(), output);
               
               if(attempt){

                 //Let the Client know that connection attempt was successful.
                 Vector<String> vectorList = server.getVectorClientList();
                 output.writeObject( new Message("VALID_NAME", message.getSender(), vectorList));
                 
                 //Also let other Client objects on the server know that a new user connected.
                 ConcurrentHashMap<String, ObjectOutputStream> currentList = null;
                 currentList = server.getClientList(); //Get the current list of clients
                 
                 for (ObjectOutputStream out : currentList.values()) {
                   out.writeObject( new Message("NEW_USER", message.getSender(), null));
                  }
                 
               }
               
               else  //The name was not valid. Let the Client object know.
                 output.writeObject( new Message("INVALID_NAME", "SERVER", null));
              }
              else  //If we reach here, the server was full. Let the client know.
                output.writeObject(new Message("SERVER_FULL", "SERVER", null));
            }
              
              else if( msgString.equals("USER_LEFT") ) {//Attempt to disconnect a user. 
                String sender = message.getSender(); //Make sure we know who sent it.
                attempt = removeFromClientList(sender);
                
                if(attempt){  //The request to disconnect was successful. Let the other Client objects know.
                  
                  ConcurrentHashMap<String, ObjectOutputStream> currentList = null;
                  currentList = server.getClientList(); //Get the current list of clients
                  
                  for (ObjectOutputStream out : currentList.values()) {
                    out.writeObject(message);  //Send the "USER_LEFT" to the other users.
                  }
                  
                }
              }
              
              else{ //If we reached here, assume the Message object is a valid message.
                Vector<String> recipients = message.getRecipients();
                ObjectOutputStream out = null;
                ConcurrentHashMap<String, ObjectOutputStream> currentList = null;
                currentList = server.getClientList();
                
                servGUI.displayMessage(message);
                
                for(int i = 0; i < recipients.size(); i++){
                  //Get the ObjectOutputStream for this client.
                    out = currentList.get(recipients.get(i));  
                    out.writeObject(message);
                  }
              }
              
            }
          }
          catch(IOException e){//Catch possible input stream exception.
            e.printStackTrace();
          }
          
          finally {
            ConcurrentHashMap<String, ObjectOutputStream> currentList = null;
            currentList = server.getClientList();
            
            Vector<String> vectorList = server.getVectorClientList();
            
                // We're all done so close the socket and remove it's name, reader, and writer from our lists.
            if (clientName != null) {
              currentList.remove(clientName);
              vectorList.remove(clientName);
            }
            
            try{
              clientSocket.close();
            }
                catch (IOException e) {
                 e.printStackTrace();
                }
          }
          
        }//End of run method.
        
        //Method that attempts to add client to the server.
        
        public boolean addToClientList(String clientName, ObjectOutputStream output){
        //If the name already exists in clientList.
          ConcurrentHashMap<String, ObjectOutputStream> currentList;
          currentList = server.getClientList();
          
          if(currentList.containsKey(clientName))
            return false;
        //The name does not exist in clientList, so we'll add it and add the ouput stream variable.
          currentList.put(clientName, output);
          
          Vector<String> vectorList = server.getVectorClientList();
          vectorList.add(clientName);
          
         //Update ServerGUI to display that someone connected.
          servGUI.displayMessage(new Message("CONNECTED", message.getSender(), null));
          
          return true;
        }
        
        public boolean removeFromClientList(String clientName){
          //First check if the clientName actually exists in clientList
          ConcurrentHashMap<String, ObjectOutputStream> currentList;
          currentList = server.getClientList();
          
          if(currentList.containsKey(clientName)){
            
            //Also update the server GUI to show that someone disconnected.
            servGUI.displayMessage(new Message("DISCONNECTED", clientName, null));
            
            currentList.remove(clientName);
            
            Vector vectorList = server.getVectorClientList();
            vectorList.remove(clientName);
            
            running = false;
            
            return true;
          }
          //The clientName was not found in clientList, so the disconnect request failed. 
          return false;
        }
        
    }
    //Return the current list of clients.
    public ConcurrentHashMap<String, ObjectOutputStream> getClientList(){
      return clientList;
    }
    
    //Return the list of Clients as a Vector of Strings.
    public Vector<String> getVectorClientList(){
      return vectorClientList;
    }
}
