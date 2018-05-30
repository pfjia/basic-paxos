package message;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Date;

/**
 * @author pfjia
 */
public abstract class PaxosMessage implements Serializable {

    private InetSocketAddress senderAddress;
    private InetSocketAddress receiverAddress;

    public PaxosMessage() {

    }

    public PaxosMessage(InetSocketAddress senderAddress, InetSocketAddress receiverAddress) {
        this.senderAddress = senderAddress;
        this.receiverAddress = receiverAddress;
    }

    public InetSocketAddress getSenderAddress() {
        return senderAddress;
    }

    public InetSocketAddress getReceiverAddress() {
        return receiverAddress;
    }

    abstract public String messageBodyToString();


    @Override
    public String toString() {
        return String.format("\t%s\t%-20s\t%s\t", this.senderAddress.toString(), this.messageBodyToString(), this.receiverAddress.toString());
    }

    public String toSendString() {
        return String.format("%d\tSEND\t---- %s", (new Date()).getTime(), this.toString());
    }

    public String toReceiveString() {
        return String.format("%d\t\t%s ----\tRECEIVE", (new Date()).getTime(), this.toString());
    }
}
