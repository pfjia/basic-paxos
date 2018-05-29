package message;

import datastructure.NodeAddress;

import java.io.Serializable;
import java.util.Date;

public abstract class PaxosMessage implements Serializable {

	private NodeAddress senderAddress;
	private NodeAddress receiverAddress;
	
	private PaxosMessage() {
		
	}
	
	public PaxosMessage(NodeAddress senderAddress, NodeAddress receiverAddress) {
		this.senderAddress = senderAddress;
		this.receiverAddress = receiverAddress;
	}
	
	public NodeAddress getSenderAddress() {
		return senderAddress;
	}
	public NodeAddress getReceiverAddress() {
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
