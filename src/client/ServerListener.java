/*
 * ServerListener.java
 *
 * Created on June 29, 2008, 3:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package client;
import java.awt.Point;
import java.io.BufferedReader;

/**
 *
 * @author Ben
 */
public interface ServerListener {
    public void externalMousePressed(Point mousePoint);
    public void externalMouseReleased();
}
