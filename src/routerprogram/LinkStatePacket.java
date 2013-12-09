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
    protected HashMap<String, String> neighbors = new HashMap();
    protected String name;
    protected int seqNum;
    protected Boolean link;
    
    LinkStatePacket(int num, int TTL, String IP, HashMap<String, String> n){
        this.TTL = TTL;
        seqNum = num;
        name = IP;
        neighbors = n;
        this.link = true;
    }
    
    LinkStatePacket(int num, int TTL, String IP, Boolean link){
        this.TTL = TTL;
        seqNum = num;
        name = IP;
        this.link = link;
    }
    
}
