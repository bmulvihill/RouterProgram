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
    public static int HEADERSIZE = 82;
    protected int seqNum;
    protected int TTL;
    protected String ownerIP;
    protected HashMap neighbors;
    
    public void setTTL(int TTL){
        this.TTL = TTL;
    }
    
    public void setOwnerIP(String IP){
        this.ownerIP = IP;
    }
    
    public void setSeqNum(int num){
        seqNum = num;
    }
    
    public void setNeighbors(HashMap n){
        neighbors = n;
    }
    
    LinkStatePacket(){}
    /**
    *
    * Returns packet with header byte array
    */
    protected byte[] getPacket(){
        return packet;
    }
    
    private byte[] packet;
    
}
