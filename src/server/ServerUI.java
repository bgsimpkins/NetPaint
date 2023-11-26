/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author bsimpkins
 */
public class ServerUI extends JPanel
{
    private JTable clientTable;
    public ServerUI()
    {
        String[] colNames = new String[]{"IP","Name","c_index","Time"};

        clientTable = new JTable();
        

    }
}
