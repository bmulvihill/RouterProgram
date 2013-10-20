/*
 * Routing Server accepts updates and passes them to Routing Updater
 */
package routerprogram;

import java.io.*;
import java.net.*;

/**
 *
 * @author bmulvihill
 */
public class RoutingServer extends Thread {
    private int serverPort = Config.getInstance().serverPort;
    
    public RoutingServer(){
        Config config = Config.getInstance();
        serverPort = config.serverPort;
    }
    
    @Override
    public void run(){
        try{  
            ServerSocket welcomeSocket = new ServerSocket(serverPort);   
            System.out.println("IP Server Receiver listening on port " + serverPort + "... ... ...");
            while(true) { 
                    Socket clientSocket = welcomeSocket.accept(); 
                    Connection c = new Connection(clientSocket); 
            } 
	} 
	catch(IOException e) {
                Logger.log(e.getMessage());
		System.out.println("Listen :"+e.getMessage());
        } 
    }
}

class Connection extends Thread { 
	DataInputStream input; 
	DataOutputStream output; 
	Socket clientSocket; 
	Config config = Config.getInstance();
        PacketQueue pq = PacketQueue.getInstance();
        
	public Connection (Socket aClientSocket) { 
		try { 
                    clientSocket = aClientSocket; 
                    input = new DataInputStream( clientSocket.getInputStream()); 
                    output =new DataOutputStream( clientSocket.getOutputStream()); 
                    this.start(); 
                } 
                    catch(IOException e) {
                        Logger.log(e.getMessage());
                        System.out.println("Connection: "+e.getMessage());
		} 
	  } 

	  public void run() { 
		try { 		                                
                        ObjectInputStream ois = new ObjectInputStream(input);  
                        LinkStatePacket lsp = (LinkStatePacket)ois.readObject();  
                        if (lsp!=null){
                            Logger.log("Packet received from " + lsp.ownerIP + " | Seq Num :" + lsp.seqNum);
                            System.out.println("Packet received from " + lsp.ownerIP + " | Seq Num :" + lsp.seqNum);
                        }  
                        input.close();    
			} 
                        catch(ClassNotFoundException e){
                            Logger.log(e.getMessage());
                            System.out.println("Class Not Found:"+e.getMessage());  
                        }
			catch(EOFException e) {
                            Logger.log(e.getMessage());
                            System.out.println("EOF:"+e.getMessage()); 
                        } 
			catch(IOException e) {
                            Logger.log(e.getMessage());
                            System.out.println("IO:"+e.getMessage());
                        }  
			finally { 
			  try { 
                            clientSocket.close();
			  }
			  catch (IOException e){
                              Logger.log(e.getMessage());
                          }
			}
		}
}