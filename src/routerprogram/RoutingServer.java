/*
 * Routing Server accepts updates and passes them to Routing Updater
 */
package routerprogram;

import java.io.*;
import java.net.*;
import java.util.*;

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
        PacketList pl = PacketList.getInstance();
        
        
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
                        String originIP = clientSocket.getRemoteSocketAddress().toString().split(":")[0].substring(1);
                        LinkStatePacket lsp = (LinkStatePacket)ois.readObject();
                        //check if TTL = 0 and remove that from the PL and forward to neighbors
                        if(lsp.TTL == 0){
                            pl.remove(lsp);
                            RoutingUpdater ru = new RoutingUpdater(lsp);
                            Logger.log("Packet with TTL = 0 received, removed from PL: " + lsp.name + " | Seq Num :" + lsp.seqNum);
                            System.out.println("Packet with TTL = 0 received, removed from PL: " + lsp.name + " | Seq Num :" + lsp.seqNum);
                        }
                        else if (lsp!=null && !pl.exists(lsp)){
                            lsp.TTL = lsp.TTL -1;
                            pl.add(lsp);
                            Logger.log("Packet received: " + lsp.name + " | Seq Num :" + lsp.seqNum);
                            System.out.println("Packet received: " + lsp.name + " | Seq Num :" + lsp.seqNum);      
                            RoutingUpdater ru = new RoutingUpdater(lsp, originIP); 
                            Dijkstra d = new Dijkstra();
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

/**
 * Data Structure to hold all received packets
 * Move to Router Program? More to do with RouterUpdater
 * @author bmulvihill
 */
class PacketList{   
    private ArrayList<LinkStatePacket> received = new ArrayList();
    private static PacketList instance = null;
    protected HashMap<String, Vertex> topology = new HashMap();
    protected PacketList() {}
    
    public static PacketList getInstance() {
      if(instance == null) {
         instance = new PacketList();
      }
      return instance;
    }
    
    protected ArrayList<LinkStatePacket> getList(){
      return received;  
    }
    
    protected synchronized void add(LinkStatePacket lsp){
        for (Iterator<LinkStatePacket> it = received.iterator(); it.hasNext(); ) {
            LinkStatePacket l = it.next();
            if(l.name.equals(lsp.name)){
                it.remove();
            }
        }
        received.add(lsp);
        topology.put(lsp.name, new Vertex(lsp.name));
    }
    
    protected void remove(LinkStatePacket lsp){
        received.remove(lsp);
    }
    
    protected Boolean exists(LinkStatePacket lsp){
        for (Iterator<LinkStatePacket> it = received.iterator(); it.hasNext(); ) {
            LinkStatePacket l = it.next();
            if(l.name.equals(lsp.name) && l.seqNum == lsp.seqNum){
                return true;
            }
        }
        return false;
    }
}