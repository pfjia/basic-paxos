package message;

import datastructure.ProposalNumber;
import datastructure.RoleAddress;

public class PrepareMessage extends PaxosMessage {

	private ProposalNumber proposalNumber;
	
	public PrepareMessage(ProposalNumber proposalNumber, RoleAddress senderAddress, RoleAddress receiverAddress) {
		super(senderAddress, receiverAddress);
		this.proposalNumber = proposalNumber;
	}
	
	public ProposalNumber getProposalNumber() {
		return proposalNumber;
	}

	public String messageBodyToString() {
		return String.format("Prepare(%d)", this.proposalNumber.getNumberValue());
	}
}
