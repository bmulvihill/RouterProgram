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
    /**
     * @param args the command line arguments
     */
    public static int SEQNUM = 0;
    public static void main(String[] args) {
        Config c = Config.getInstance();
        c.setConfig();
        RoutingServer rs = new RoutingServer();
        rs.start();
        Timer t = new Timer();
        RoutingTick rt = new RoutingTick();
        //schedule routing updater to execute every 30 seconds or whatever the tick_time is set to
        t.scheduleAtFixedRate(rt, 3 * (c.tick_time * 1000), c.tick_time * 1000);
        UserCommand uc = new UserCommand();
        uc.start();
    }
}

class UserCommand extends Thread{
    public void run(){
        while (true){
            try{
                Config c = Config.getInstance();
                RoutingUpdater ru = new RoutingUpdater();
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String[] args = br.readLine().split(" ");
                if((args[2].toUpperCase().contains("DOWN"))){
                    c.routerNeighbors.remove(args[1]);
                    LinkStatePacket lsp = new LinkStatePacket(RouterProgram.SEQNUM+=1, 0, c.ROUTER, false);
                    ru.forwardPacket(lsp, args[1]);
                    System.out.println("Down");
                } else if((args[2].toUpperCase().contains("UP"))){
                    System.out.println("Up");              
                } else if((args[2].toUpperCase().contains("CHANGE"))){
                    System.out.println("Change");              
                } else {
                   System.out.println("Unrecognized Command"); 
                }
            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println(e.getMessage());
                continue;
            }
            catch (IOException e){
                System.out.println(e.getMessage());
                continue;
            }
        }
    }
}

//Time Task that executes every tick
class RoutingTick extends TimerTask{
    @Override
    public void run() {
        PacketList pl = PacketList.getInstance();
        RoutingUpdater ru = new RoutingUpdater(pl);
        decrement(pl.getList());
        ArrayList<LinkStatePacket> list = pl.getList();
        System.out.println("OwnerIP | TTL | SeqNum");
        for (Iterator<LinkStatePacket> it = list.iterator(); it.hasNext(); ) {
            LinkStatePacket l = it.next();
            System.out.println(l.name + " | " + l.TTL + " | " + l.seqNum );
        }
    }
    
    protected void decrement(ArrayList received){
        for (Iterator<LinkStatePacket> it = received.iterator(); it.hasNext(); ) {
            LinkStatePacket lsp = it.next();
            lsp.TTL = lsp.TTL - 1;
            Logger.log("Time to Live for Packet: " + lsp.seqNum + " from " + lsp.name + " | " + lsp.TTL);
            if (lsp.TTL == 0) {
                Logger.log("TTL Expired for Packet: " + lsp.seqNum + " from " + lsp.name);
                RoutingUpdater ru = new RoutingUpdater(lsp);
                it.remove();
            }
        }
    }
}

//Routing Updater - creates and send LSPs to be forwarded to router neighbors
class RoutingUpdater {
    Config c = Config.getInstance();
    public RoutingUpdater(){}
    //simply forwards a given packet to all neighbors
    public RoutingUpdater(LinkStatePacket lsp){
        for (Map.Entry<String, String> entry : c.routerNeighbors.entrySet()){
            forwardPacket(lsp, entry.getKey());
        }
    }
    
    //constructor for updates received from neighboring routers
    public RoutingUpdater(LinkStatePacket lsp, String originIP){
       for (Map.Entry<String, String> entry : c.routerNeighbors.entrySet()){
           if(!entry.getKey().equals(lsp.name) && !entry.getKey().equals(originIP)){
            forwardPacket(lsp, entry.getKey());
           }
        }
    }
    
    //constructor for Updates that occur every tick
    public RoutingUpdater(PacketList pl) { 
        System.out.println("in timer task");
        LinkStatePacket lsp = new LinkStatePacket(RouterProgram.SEQNUM += 1, 6, c.ROUTER, c.routerNeighbors);
        pl.add(lsp);
        for (Map.Entry<String, String> entry : c.routerNeighbors.entrySet())
        {   
            forwardPacket(lsp, entry.getKey());
            Logger.log("Packet: " + + lsp.seqNum + " sent to Router " + entry.getKey());
        }
       
    }
    
    //forwards LSPs to router's neighbors
    protected void forwardPacket(LinkStatePacket p, String destIP){
        try{
           System.out.println("Forwarding Packet: " + p.name + " | " + p.seqNum + " to Router " + destIP);
           Logger.log("Forwarding Packet: " + p.name + " | " + p.seqNum + " to Router " + destIP);
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
