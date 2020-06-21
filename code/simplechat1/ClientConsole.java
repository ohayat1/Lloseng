// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port, String loginID) 
  {
    try 
    {
      client= new ChatClient(host, port, this, loginID);
      if (client.getLoginID().equals(null) || client.getLoginID().equals("")){
        client.quit();
      }
      client.handleMessageFromClientUI("#login <" + client.getLoginID() + ">");
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message;

        
      while (true) 
      {
        message = fromConsole.readLine();
        // Case where connection needs to be terminated
        if(message.equals("#quit")){
          client.quit();
        }
        if(message.equals("#logoff")){
          client.closeConnection();
        }
        // Command to change host to new host
        if(message.substring(0, message.indexOf(' ')).equals("#sethost")){
          if (!client.isConnected()){
            String host = message.substring(message.indexOf('<'), message.indexOf('>'));
            client.setHost(host);
          }
          else{
            System.out.println("Client is already connected, cannot change host");
          }
         
        }
        // Command to change port to new port
        if (message.substring(0, message.indexOf(' ')).equals("#setport")){
          if(!client.isConnected()){
            int port = Integer.parseInt(message.substring(message.indexOf('<'), message.indexOf('>')));
            client.setPort(port);
          }
          else{
            System.out.println("Client is already connected cannot change port");
          }
        }
        // Command to login with new port and host specified, only allowed if not already connected
        if (message.equals("#login")){
          if (!client.isConnected()){
            client.openConnection();
          }
          else{
            System.out.println("Cannot make new connection. Client is already connected.");
          }
          
        }
        // Command to display host name
        if (message.equals("#getHost")){
          System.out.println(client.getHost());
        }
        // Commant to display port 
        if (message.equals("#getPort")){
          System.out.println(client.getPort());
        }
        client.handleMessageFromClientUI(message);
      }

    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
  //Class methods ***************************************************
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String loginID = "";
    String host = "";
    int port = 0;  //The port number
    BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));

    try {
      loginID = fromConsole.readLine();
    }
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
    
    

    try
    {
      port = Integer.parseInt(args[0]);
      host = "localhost";
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      port = DEFAULT_PORT;
      host = "localhost";
    }
    ClientConsole chat= new ClientConsole(host, port, loginID);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
