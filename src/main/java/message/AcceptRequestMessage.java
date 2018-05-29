package message;

import datastructure.PaxosValue;
import datastructure.ProposalNumber;
import datastructure.RoleAddress;

public class AcceptRequestMessage extends PaxosMessage {

	private ProposalNumber proposalNumber;
	private PaxosValue paxosValue;

	public AcceptRequestMessage(ProposalNumber proposalNumber, PaxosValue value, RoleAddress senderAddress, RoleAddress receiverAddress) {
		super(senderAddress, receiverAddress);
		this.proposalNumber = proposalNumber;
		this.paxosValue = value;
	}
	
	public ProposalNumber getProposalNumber() {
		return proposalNumber;
	}
	
	public PaxosValue getValue() {
		return paxosValue;
	}

	public String messageBodyToString() {
		return String.format("AcceptRequest(%d,%s)", this.proposalNumber.getNumberValue(), this.paxosValue.getValue());
	}
}
