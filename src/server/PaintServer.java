/*
 * PaintServer.java
 *
 * Created on June 29, 2008, 12:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


package server;

import java.net.Socket;
import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import message.AlertMessage;
import message.PaintMessage;
/**
 *
 * @author Ben
 */
public class PaintServer extends ObjectServer {
    
    //protected ArrayList<PaintClientHandler> clientHandlers = new ArrayList();

    protected HashMap<Integer,PaintClientHandler> clientHandlers = new HashMap();
    /** Creates a new instance of PaintServer */
    public PaintServer(int port) {
        super(port);
    }
    
    
    
    public void mouseUpdate(Point mousePoint, int clientNum){        
        for (int i = 0; i < clientHandlers.size();i++){
            PaintClientHandler handler = clientHandlers.get(i);

            //if client is not the one making changes, update it
            if (clientNum != i) handler.sendMouseUpdateToClient(mousePoint);
        }
        
    }

    public void distributeMessage(PaintMessage message, int clientNum)
    {
        Iterator iter = clientHandlers.entrySet().iterator();

        while (iter.hasNext())
        {
            Map.Entry me = (Map.Entry)iter.next();
            int i = (Integer)me.getKey();
            PaintClientHandler handler = (PaintClientHandler)me.getValue();
            if (clientNum != i) handler.sendMessageToClient(message);
        }
       
    }
    
    public void acceptClient(Socket clientSock, int clientNum){
        PaintClientHandler pch = new PaintClientHandler(clientSock,this,clientNum);
        //pch.setPaintServer(this);
        
        if (clientHandlers.size() < 2)
        {
            String add = clientSock.getInetAddress().getHostAddress();
            clientHandlers.put(clientNum, pch);
            
            if (clientHandlers.size()==2) distributeMessage(new AlertMessage("Info","Another painter is here: "+add),-1);
        }
        else
        {
            pch.sendMessageToClient(new AlertMessage("Capacity Reached","Two painters are already using NetPaint. We currently don't support more than 2 painters. Sorry!"));
            pch = null;
        }
        
    }

    protected void deleteClient(int clientNum){
        
        PaintClientHandler pch = clientHandlers.remove(clientNum);
        pch = null;
    }
    
    public static void main(String[] args){
        if (args.length == 1){
            System.out.println("Port: "+args[0]);
            PaintServer ps = new PaintServer(Integer.parseInt(args[0]));
            ps.listenForClients();
        }
        else {
            System.out.print("Beginning with Default: \nPort: 1234 \n" +
                    "Else, restart with usage: server.PaintServer <Port No.>\n");
            PaintServer ps = new PaintServer(1234);
            ps.listenForClients();
            
        }
        
    }
}
