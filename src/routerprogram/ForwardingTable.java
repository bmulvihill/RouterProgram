/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package routerprogram;
import java.io.Serializable;
import java.util.PriorityQueue;
import java.util.List;
import java.util.*;

/**
 *
 * @author bmulvihill
 */
public class ForwardingTable {
    private static ForwardingTable instance = null;
    protected ForwardingTable() {}
    
    public static ForwardingTable getInstance() {
      if(instance == null) {
         instance = new ForwardingTable();
      }
      return instance;
    }
    
    public void setTable(Vertex dest, Vertex next){
            table.put(dest.name, next.name);    
    }
    
    public String getNextHop(String dest){
        return table.get(dest);
    }
    
    HashMap<String, String> table = new HashMap();
}
