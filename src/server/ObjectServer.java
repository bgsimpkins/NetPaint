/*
 * ObjectServer.java
 *
 * Created on July 4, 2008, 10:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package server;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 *
 * @author bsimpkins
 */
public abstract class ObjectServer {    
    
    protected int clientNum = 0;
    protected ServerSocket serverSock;
    protected Socket clientSock;
    
    /** Creates a new instance of ObjectServer */
    public ObjectServer(int port) {
        try {
            serverSock = new ServerSocket(port);            
                      
        } catch (IOException ex) {}
        
    }
    
    public void listenForClients(){
        try {
            while(true){
                    System.out.println("Waiting for a client...");
                    clientSock = serverSock.accept();
                    //clientHandlers.add(new PaintClientHandler(clientSock,this,clientNum));
                    acceptClient(clientSock,clientNum);
                    
                    clientNum++;
            }
        } catch (IOException ex) {}
    }
    
    public abstract void acceptClient(Socket clientSock,int clientNum);
    
    public void removeClient(int clientNum){
        
        deleteClient(clientNum);
        clientNum--;
    }
    
    protected abstract void deleteClient(int clientNum);   
    
    public int getClientNumber(){
        return clientNum;
    }
    
    
}
