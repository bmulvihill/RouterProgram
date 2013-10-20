/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package routerprogram;
import java.net.*;  
import java.io.*; 
import java.util.*;
/**
 *
 * @author bmulvihill
 */
public class RouterProgram {
    public static int SEQNUM;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Config c = Config.getInstance();
        c.setConfig();
        RoutingServer rs = new RoutingServer();
        rs.start();
        Timer t = new Timer();
        RoutingUpdater ru = new RoutingUpdater();
        //schedule routing updater to execute every 30 seconds or whatever the tick_time is set to
        t.scheduleAtFixedRate(ru, 3 * (c.tick_time * 1000), c.tick_time * 1000);
    }
}

class RoutingUpdater extends TimerTask{
    @Override
    public void run() {
        Config c = Config.getInstance();
        RouterProgram.SEQNUM += 1;
        System.out.println("in timer task");
        for (Map.Entry<String, String> entry : c.routerNeighbors.entrySet())
        {   
            LinkStatePacket n = new LinkStatePacket();
            n.setOwnerIP(c.ROUTER);
            n.setSeqNum(RouterProgram.SEQNUM);
            n.setTTL(5);
            n.setNeighbors(c.routerNeighbors);
            forwardPacket(n, entry.getKey());
            Logger.log("Packet: " + + n.seqNum + " sent to Router " + entry.getKey());
        }
       
    }
    
    public void forwardPacket(LinkStatePacket p, String destIP){
        try{
           System.out.println("Sending Packet: " + + p.seqNum + " to Router " + destIP);
           Logger.log("Sending Packet: " + + p.seqNum + " to Router " + destIP);
           Socket s = new Socket(destIP, Config.getInstance().serverPort);  
           OutputStream os = s.getOutputStream();  
           ObjectOutputStream oos = new ObjectOutputStream(os);  
           oos.writeObject(p);  
           oos.close();  
           os.close();  
           s.close();  
        }
        catch(Exception e) {
            System.out.println("An error occurred while attempted to forward packet " + p.seqNum + " to " + destIP);
            Logger.log("An error occurred while attempted to forward packet " + p.seqNum + " to " + destIP);
        }
    }
}
