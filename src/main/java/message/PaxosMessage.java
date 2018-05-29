package message;

import datastructure.RoleAddress;

import java.io.Serializable;
import java.util.Date;

public abstract class PaxosMessage implements Serializable {

	private RoleAddress senderAddress;
	private RoleAddress receiverAddress;
	
	private PaxosMessage() {
		
	}
	
	public PaxosMessage(RoleAddress senderAddress, RoleAddress receiverAddress) {
		this.senderAddress = senderAddress;
		this.receiverAddress = receiverAddress;
	}
	
	public RoleAddress getSenderAddress() {
		return senderAddress;
	}
	public RoleAddress getReceiverAddress() {
		return receiverAddress;
	}
	
	abstract public String messageBodyToString();
	
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
