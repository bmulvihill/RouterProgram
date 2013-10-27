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
public class LinkStatePacket implements Serializable  {
    //protected int seqNum;
    protected int TTL;
    protected ArrayList<Edge> neighbors = new ArrayList();
    protected Vertex node;
    protected int seqNum;
    private static int SEQ = 0;
    
    LinkStatePacket(int num, int TTL, String IP, HashMap<String, String> n){
        this.TTL = TTL;
        seqNum = num;
        node = new Vertex(IP);
        for (Map.Entry<String, String> entry : n.entrySet()){ 
            neighbors.add(new Edge(node, Integer.parseInt(entry.getValue())));
        }
    }
    
}
