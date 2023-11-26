/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author ben
 */
public abstract class ImageButton extends JButton
{
    private BufferedImage image;

    public ImageButton(int width, int height)
    {
        this.setSize(width,height);

        Dimension size = new Dimension(width,height);
        this.setPreferredSize(size);
        this.setMinimumSize(size);
        this.setMaximumSize(size);

        image = new BufferedImage(width,height,BufferedImage.TRANSLUCENT);
        paintImage(image.createGraphics());

        this.setIcon(new ImageIcon(image));
        //this.setBorderPainted(false);
    }


    public void repaintImage()
    {
        paintImage(image.createGraphics());
        //this.setIcon(new ImageIcon(image));
    }

    protected abstract void paintImage(Graphics g);

    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200,200);

        ImageButton ib = new ImageButton(100,100)
        {
            public void paintImage(Graphics g)
            {
                g.setColor(Color.RED);
                g.fillRect(10, 10, 80, 80);
            }
        };

        frame.getContentPane().add(ib);
        frame.setVisible(true);
    }
}
