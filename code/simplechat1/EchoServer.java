// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient (Object msg, ConnectionToClient client)
  {
    String message = (String) msg;
    

    // Only commands from the server UI can reach the server, Commands for Client will have been taken care of by clientUI
    // Command to quit server
    if (message.equals("#quit")){
      try 
      {
        this.close();
      }
      catch(Exception EX){}

    }
    // Command to stop listening to new client
    if (message.equals("#stop")){
      this.stopListening();
    }
    // Command to change port to new port
    if (message.substring(0, message.indexOf(' ')).equals("#setport")){
      if(!this.isListening()){
        int port = Integer.parseInt(message.substring(message.indexOf('<'), message.indexOf('>')));
        this.setPort(port);
      }
      else{
        System.out.println("Server is still Listening cannot change port");
      }
    }
    // Command to start listening again
    if (message.equals("#start")){
      if(!this.isListening()){
        try {
          this.listen();
        }
        catch (Exception EX){}
      }
      else{
        System.out.println("Server is already started and listening for new clients.");
      }
    }
    // Command to set login info
    if (message.substring(0, message.indexOf(' ')).equals("#login")){
      // check if loginID has not been assigned already
      if (client.getInfo("loginID").equals(null)){
        client.setInfo("loginID", message.substring(message.indexOf('<'), message.indexOf('>')));
      }
      else{
        try{
          client.close();
        }
        catch (Exception Ex){}
        
      }
      
    }

    // Command to get port and display it
    if (message.equals("#getport")){
      System.out.println(this.getPort());
    }


    System.out.println("Message received: " + msg + " from " + client);
    // Set the login ID before sending a msg
    this.sendToAllClients(client.getInfo("loginID") + ": " + msg);
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening BLAH for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when tclient connects.
   */
  protected void clientConnected(ConnectionToClient client) {
    System.out.println("WELCOME! NEW CLIENT HAS CONNECTED.");
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when client disconnects.
   */
  synchronized protected void clientDisconnected( ConnectionToClient client) {
    System.out.println("AU REVOIR! A CLIENT HAS DISCONNECTED.");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
