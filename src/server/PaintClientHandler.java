/*
 * ClientHandler.java
 *
 * Created on June 29, 2008, 12:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package server;
import java.io.IOException;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;


import java.awt.Point;
import java.util.StringTokenizer;
import message.PaintMessage;
/**
 *
 * @author Ben
 */
public class PaintClientHandler extends Thread {
    private Socket clientSock;
    private String cliAddr;
    private PaintServer paintServer;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    private int clientNum;
    
    /** Creates a new instance of ClientHandler */
    public PaintClientHandler(Socket s, PaintServer ps,int clientNum) {
        clientSock = s;
        paintServer = ps;
        cliAddr = s.getInetAddress().getHostAddress();
        this.clientNum = clientNum;
        
        start();
    }
    
    public void setPaintServer(PaintServer ps){
        this.paintServer = ps;
    }
    
    @Override
    public void run(){
        try {
            System.out.println("Client connection from " + 
				clientSock.getInetAddress().getHostAddress() );
              // Get I/O streams from the socket
            out = new ObjectOutputStream(new DataOutputStream(clientSock.getOutputStream()));           
            in  = new ObjectInputStream(new BufferedInputStream(clientSock.getInputStream()));      
            
            processClient();
            
            // Close client connection
            clientSock.close();
            paintServer.removeClient(clientNum);    
            System.out.println("Client connection closed\n");
        } catch (IOException ex) {}

    }
    
    private void processClient(){
        String line;
        Object object;
        boolean done = false;
        try {
            while (!done) {
                /*if((line = in.readLine()) == null)
                    done = true;
                else {
                    doRequest(line);
                }*/
                if ((object = in.readObject()) == null){
                    done = true;
                    System.out.println("Done!");
                }
                else {
                    if (object instanceof PaintMessage){
                        paintServer.distributeMessage((PaintMessage)object,clientNum);
                        //Point point = (Point)object;
                        //System.out.println("Server processing mouseUpdate: x: "+point.x+" y: "+point.y);
                    }
                }
            }
        }catch(IOException e) {
            System.err.println("IO Exeption in processClient()");
        }catch(ClassNotFoundException e){}
    }
    
    
    private void doRequest(String line){

        if ((line.length() >= 12) && (line.substring(0,12).toLowerCase().equals("mouse_update"))) {
            //System.out.println("Processing mouse update");
            Point mousePoint,oldMousePoint;
            
            String mouseVals = line.substring(13);    // cut the keyword + a space
            
            StringTokenizer st = new StringTokenizer(mouseVals," ");
            int mouseX = Integer.parseInt(st.nextToken());
            int mouseY = Integer.parseInt(st.nextToken());
            mousePoint = new Point(mouseX,mouseY);
        
            paintServer.mouseUpdate(mousePoint,clientNum);
        }
        
        //if mouse has been released, send null value to server for mousePoint
        else if ((line.length() >= 14) && (line.substring(0,14).toLowerCase().equals("mouse_released"))){
            paintServer.mouseUpdate(null,clientNum);
        }
        
    }
    
    public void sendMouseUpdateToClient(Point mousePoint){
        //System.out.println("Sending mouse update to client");
        if (mousePoint != null){
            try {
                //out.println("mouse_update "+mousePoint.x+ " "+mousePoint.y);
                out.writeObject(mousePoint);
                //System.out.println("Mouse going out! X: "+mousePoint.x+ " Y: "+mousePoint.y);
                out.reset();
            } catch (IOException ex) {
                System.err.println("IO Exception when sending update from server to client");
            }
        }
        else {
            //out.println("mouse_released");
        }
        
    }

    public void sendMessageToClient(PaintMessage message)
    {
        try {
                //out.println("mouse_update "+mousePoint.x+ " "+mousePoint.y);
                out.writeObject(message);
                //System.out.println("Mouse going out! X: "+mousePoint.x+ " Y: "+mousePoint.y);
                out.reset();
            } catch (IOException ex) {
                System.err.println("IO Exception when sending update from server to client");
            }
    }
}
