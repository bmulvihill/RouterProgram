/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package routerprogram;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
/**
 *
 * @author bmulvihill
 */
public  class Logger {
    public void Logger(){}
    public synchronized static void log(String value){
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            FileWriter out = new FileWriter("log.txt", true);  
            BufferedWriter bufWriter = new BufferedWriter(out);
            bufWriter.append(dateFormat.format(date) + ": " + value);
            bufWriter.newLine();
            bufWriter.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
       
    }
}
