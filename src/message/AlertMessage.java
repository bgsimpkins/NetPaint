/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

/**
 *
 * @author bsimpkins
 */
public class AlertMessage implements PaintMessage
{
    private String id;
    private String message;
    public AlertMessage(String id, String mess)
    {
        this.id = id;
        this.message = mess;
    }
    
    public String getId()
    {
        return id;
    }
    
    public String getMessage()
    {
        return message;
    }
}
