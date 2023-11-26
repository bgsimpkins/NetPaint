/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package message;

import java.awt.Color;
import java.awt.Point;

/**
 *
 * @author ben
 */
public class FillMessage implements PaintMessage{
    private Point point;
    private Color color;

    public FillMessage(Point point, Color color)
    {
        this.point = point;
        this.color = color;
    }

    /**
     * @return the point
     */
    public Point getPoint() {
        return point;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

}
