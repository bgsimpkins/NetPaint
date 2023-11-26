/*
 * ServerListenerManager.java
 *
 * Created on June 29, 2008, 3:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package client;
import java.io.*;
import java.awt.Point;
import java.util.StringTokenizer;

/**
 *
 * @author Ben
 */
public class ServerListenerManager extends Thread {
    private ServerListener listener;
    private BufferedReader in;
 
    /** Creates a new instance of ServerListenerManager */
    public ServerListenerManager(ServerListener listener, BufferedReader in){
        this.listener = listener;
        this.in = in;
    }
    
    public void run(){
        while(true){
            //System.out.println("ServerListenerManager Running!");
            try {
                String line = in.readLine();
                
                if ((line.length() >= 12) && (line.substring(0,12).toLowerCase().equals("mouse_update"))) {
                    //System.out.println("ServerListener: New data from server");
                    Point mousePoint;
            
                    String mouseVals = line.substring(13);    // cut the keyword + a space

                    StringTokenizer st = new StringTokenizer(mouseVals," ");
                    int mouseX = Integer.parseInt(st.nextToken());
                    int mouseY = Integer.parseInt(st.nextToken());
                    mousePoint = new Point(mouseX,mouseY);

                    listener.externalMousePressed(mousePoint);
                }
                else if ((line.length() >= 14) && (line.substring(0,14).toLowerCase().equals("mouse_released"))){
                    listener.externalMouseReleased();
                }
                
            } catch (IOException ex) {}
        }
    }
    
}
