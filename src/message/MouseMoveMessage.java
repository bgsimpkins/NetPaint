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
public class MouseMoveMessage implements PaintMessage{

    private Point point;

    public MouseMoveMessage(Point p)
    {
        this.point = p;

    }

    public Point getPoint()
    {
        return point;
    }
}
