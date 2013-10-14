/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package routerprogram;

import java.io.*;
import java.util.*;
import java.net.Inet4Address;

/**
 *
 * @author bmulvihill
 */

public class Config {
    private static Config instance = null;
    protected static String ROUTER = "B";
    protected Config() {}
    
    public static Config getInstance() {
      if(instance == null) {
         instance = new Config();
      }
      return instance;
    }
    
    protected void setConfig(){
            try{
                BufferedReader inFromUser = new BufferedReader(new FileReader("/Users/bmulvihill/desktop/router_config.txt"));
                tick_time = Integer.parseInt(inFromUser.readLine().split("\\W+")[1]);
                System.out.println(tick_time);
                Logger.log("Tick time: " + tick_time);
                String line;
                while ((line = inFromUser.readLine()) != null) {
                    List<String> row = Arrays.asList(line.split("\\W+"));
                    if (row.contains(ROUTER)){
                        String n = "";
                        if(row.get(0).equals(ROUTER)){ 
                            n = row.get(1);
                        } else {
                            n = row.get(0);
                        }
                        routerNeighbors.put(n, row.get(2).toString());
                    } 
                }
                inFromUser.close();
                System.out.println(routerNeighbors);
                Logger.log("Router Neighbors: " + routerNeighbors);
                
            }
            catch (FileNotFoundException e){
                //..
            }
            catch (IOException e){
                //..
            }
    }
    
    protected int packetSize;
    protected int serverPort;
    protected int tick_time;
    protected HashMap<String, String> routerNeighbors = new HashMap();
}
