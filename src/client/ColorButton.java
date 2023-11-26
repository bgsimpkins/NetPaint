/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author ben
 */
public class ColorButton extends ImageButton{

    private Color color;

    public ColorButton()
    {
        super(30,30);
        this.setActionCommand("Color");
        color = Color.BLACK;
        this.repaintImage();
    }

    @Override
    protected void paintImage(Graphics g)
    {
        g.setColor(color);
        g.fillRect(5, 5,20, 20);
    }
    public void setColor(Color color)
    {
        this.color = color;
        repaintImage();
    }
}
