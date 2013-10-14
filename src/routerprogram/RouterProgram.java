/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package routerprogram;
import java.io.DataOutputStream;
import java.net.Socket;
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
        t.scheduleAtFixedRate(ru, 3 * (1 * 1000), 1 * 1000);
    }
}

class RoutingUpdater extends TimerTask{
    @Override
    public void run() {
        Config c = Config.getInstance();
        //PacketQueue not necessary?
        System.out.println("in timer task");
        //PacketQueue pq = PacketQueue.getInstance();
        Iterator it = c.routerNeighbors.entrySet().iterator();
        while(it.hasNext()){
            RouterProgram.SEQNUM += 1;
            LinkStatePacket n = new LinkStatePacket();
            n.setOwnerIP(c.ROUTER);
            n.setSeqNum(RouterProgram.SEQNUM);
            n.setTTL(5);
            n.setNeighbors(c.routerNeighbors);
            System.out.println(it.next());
            //forwardPacket(n, it.next());
            //pq.add(n);
            //broadcast packet
            
        }
    }
    
    public void forwardPacket(LinkStatePacket p, String destIP){
        try{
           Socket s = new Socket(destIP, Config.getInstance().serverPort);
           DataOutputStream output = new DataOutputStream( s.getOutputStream()); 
           Logger.log("Sending Packet: " + + p.seqNum + " to Router " + destIP);
           //output.writeInt((int)p.size + Packet.HEADERSIZE); 
           //output.write(p.getPacketWithHeader(), 0, p.size + Packet.HEADERSIZE);  
        }
        catch(Exception e) {
            
        }
    }
}
