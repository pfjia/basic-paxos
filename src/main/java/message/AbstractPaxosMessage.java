package message;

import role.Node;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author pfjia
 * @since 2018/5/30 14:05
 */
public abstract class AbstractPaxosMessage implements Serializable {

    private Node sender;
    private Node receiver;


    public AbstractPaxosMessage(Node sender, Node receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public Node getSender() {
        return sender;
    }

    public Node getReceiver() {
        return receiver;
    }

    abstract public String messageBodyToString();


    @Override
    public String toString() {
        return String.format("\t%s\t%-20s\t%s\t", sender.getAddress(),  this.messageBodyToString(), receiver.getAddress());
    }

    public String toSendString() {
        return String.format("%d\tSEND\t---- %s", (new Date()).getTime(), this.toString());
    }

    public String toReceiveString() {
        return String.format("%d\t\t%s ----\tRECEIVE", (new Date()).getTime(), this.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractPaxosMessage that = (AbstractPaxosMessage) o;
        return Objects.equals(sender, that.sender) &&
                Objects.equals(receiver, that.receiver);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sender, receiver);
    }
}
