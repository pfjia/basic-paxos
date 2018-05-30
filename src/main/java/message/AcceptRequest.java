package message;

import datastructure.PaxosValue;
import datastructure.ProposalNumber;

import java.net.InetSocketAddress;

public class AcceptRequest extends PaxosMessage {

	private ProposalNumber proposalNumber;
	private PaxosValue paxosValue;

	public AcceptRequest(ProposalNumber proposalNumber, PaxosValue value, InetSocketAddress senderAddress, InetSocketAddress receiverAddress) {
		super(senderAddress, receiverAddress);
		this.proposalNumber = proposalNumber;
		this.paxosValue = value;
	}

    public AcceptRequest(ProposalNumber proposalNumber, PaxosValue paxosValue) {
        this.proposalNumber = proposalNumber;
        this.paxosValue = paxosValue;
    }

    public ProposalNumber getProposalNumber() {
		return proposalNumber;
	}
	
	public PaxosValue getValue() {
		return paxosValue;
	}

	@Override
    public String messageBodyToString() {
		return String.format("AcceptRequest(%d,%s)", this.proposalNumber.getNumber(), this.paxosValue.getValue());
	}
}
