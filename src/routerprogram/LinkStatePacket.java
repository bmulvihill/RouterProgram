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
    protected String ownerIP;
    protected HashMap neighbors;
    protected int seqNum;
    private static int SEQ = 0;
    
    LinkStatePacket(int num, int TTL, String IP, HashMap n){
        this.TTL = TTL;
        this.ownerIP = IP;
        seqNum = num;
        neighbors = n;
    }
    
}
