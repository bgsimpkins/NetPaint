/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package message;

import java.awt.Point;

/**
 *
 * @author bsimpkins
 */
public class MouseReleaseMessage implements PaintMessage
{
    private Point mousePoint;
    public MouseReleaseMessage(Point point)
    {
        this.mousePoint = point;
    }

    public Point getPoint()
    {
        return mousePoint;
    }
}
