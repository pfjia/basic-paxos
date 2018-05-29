package message;

import datastructure.NodeAddress;
import datastructure.PaxosValue;
import datastructure.ProposalNumber;

public class PromiseMessage extends PaxosMessage {

	private boolean promised;
	private ProposalNumber proposalNumber;
	private PaxosValue paxosValue;
	
	public PromiseMessage(boolean promised, ProposalNumber proposalNumber, PaxosValue paxosValue, NodeAddress senderAddress, NodeAddress receiverAddress) {
		super(senderAddress, receiverAddress);
		this.promised = promised;
		this.proposalNumber = proposalNumber;
		this.paxosValue = paxosValue;
	}
	
	public boolean isPromised() {
		return promised;
	}

	public ProposalNumber getProposalNumber() {
		return proposalNumber;
	}

	public PaxosValue getPaxosValue() {
		return paxosValue;
	}
	
	@Override
	public String messageBodyToString() {
		return String.format("Promise(%s,%d,%s)", this.promised?"YES":"NO", this.proposalNumber.getNumberValue(), this.paxosValue!=null?this.paxosValue.getValue():"NULL");
	}
}
