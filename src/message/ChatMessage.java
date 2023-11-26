/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package message;

/**
 *
 * @author bsimpkins
 */
public class ChatMessage implements PaintMessage
{
    private String sender, recipient, message;

    public ChatMessage(String sender, String recipient, String message)
    {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }

    /**
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * @return the recipient
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
