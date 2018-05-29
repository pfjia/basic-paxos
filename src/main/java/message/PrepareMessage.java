package message;

import datastructure.ProposalNumber;
import datastructure.NodeAddress;

import java.net.SocketAddress;

public class PrepareMessage extends PaxosMessage {
	SocketAddress

	private ProposalNumber proposalNumber;
	
	public PrepareMessage(ProposalNumber proposalNumber, NodeAddress senderAddress, NodeAddress receiverAddress) {
		super(senderAddress, receiverAddress);
		this.proposalNumber = proposalNumber;
	}
	
	public ProposalNumber getProposalNumber() {
		return proposalNumber;
	}

	@Override
    public String messageBodyToString() {
		return String.format("Prepare(%d)", this.proposalNumber.getNumberValue());
	}
}
