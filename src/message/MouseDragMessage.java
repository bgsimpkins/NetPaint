/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package message;

import java.awt.Color;
import java.awt.Point;

/**
 *
 * @author bsimpkins
 */
public class MouseDragMessage implements PaintMessage{

    private Point point;
    private Color c;
    private int thick;
    public MouseDragMessage(Point p,Color c, int thick)
    {
        this.point = p;
        this.c = c;
        this.thick = thick;
    }

    public Point getPoint()
    {
        return point;
    }

    public Color getColor()
    {
        return c;
    }

    public int getThickness()
    {
        return thick;
    }
}
