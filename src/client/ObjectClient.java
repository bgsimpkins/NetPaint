/*
 * ObjectServer.java
 *
 * Created on July 4, 2008, 10:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package client;

import java.io.*;
import java.net.Socket;



/**
 *
 * @author bsimpkins
 */
public class ObjectClient {
    
    private Socket sock;
    protected ObjectInputStream objectIn;
    protected ObjectOutputStream objectOut;
    
    /** Creates a new instance of ObjectServer */
    public ObjectClient(int port, String host) {
        makeContact(port,host);
        
    }
    
    private void makeContact(int port, String host){
        try {
            sock = new Socket(host, port);
            objectOut = new ObjectOutputStream( new DataOutputStream(sock.getOutputStream()));
            objectIn  = new ObjectInputStream( new BufferedInputStream(sock.getInputStream())) ;

        }
            catch(Exception e)
            {  
                System.err.print("Error in client making contact with server");
            }

    }
    
    public void closeLink()
    {
        try {
          //objectOut.println("bye");    // tell server that client is disconnecting
          sock.close();
          objectIn.close();
          objectOut.close();
        }
        catch(Exception e)
        {  
            System.err.println("Exception when attempting to close client link "+e);
        }
        System.exit( 0 ); 
    }
    
    public ObjectInputStream getInputStream(){
        return objectIn;
    }
    
    public ObjectOutputStream getOutputStream(){
        return objectOut;
    }
    
}
