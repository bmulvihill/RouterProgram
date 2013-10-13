/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package routerprogram;
import java.util.*;
/**
 *
 * @author bmulvihill
 */
public class RouterProgram {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Config c = Config.getInstance();
        c.setConfig();
        RoutingServer rs = new RoutingServer();
        Timer t = new Timer();
        RoutingUpdater ru = new RoutingUpdater();
        //schedule routing updater to execute every 30 seconds or whatever the tick_time is set to
        t.scheduleAtFixedRate(ru, 3 * c.tick_time * 1000, c.tick_time * 1000);
    }
}

class RoutingUpdater extends TimerTask{
    public RoutingUpdater(){}
    @Override
    public void run() {
    }
}
