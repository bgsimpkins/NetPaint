/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;

/**
 *
 * @author ben
 */
public class PopupWindow extends JDialog
{
    JColorChooser cc;
    PaintPanel pp;

    public PopupWindow(PaintPanel pp)
    {
        this.pp = pp;
        

        this.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        //c.weighty=.8;

        cc = new JColorChooser();
        setSize(500,340);
        this.getContentPane().add(cc,c);

       

        JPanel buttPanel = new JPanel();
        buttPanel.setLayout(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();

        JButton ok = new JButton("OK");
        ok.addActionListener(pp);
        ok.setPreferredSize(new Dimension(40,30));
        buttPanel.add(ok,c2);

        
        //c.weighty = .2;
        c.gridy=1;

        getContentPane().add(buttPanel,c);

        cc.setPreviewPanel(new JPanel());
        AbstractColorChooserPanel[] cPanels = cc.getChooserPanels();
        //cc.removeChooserPanel(cPanels[1]);
        //cc.removeChooserPanel(cPanels[2]);
        //setVisible(true);


        
    }

    public Color getColor()
    {
        return cc.getColor();
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.getContentPane().setLayout(null);
        frame.getContentPane().add(new PopupWindow(null));

        frame.setVisible(true);
    }
}
