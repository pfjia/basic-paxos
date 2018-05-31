package message;

import datastructure.PaxosValue;
import datastructure.ProposalNumber;

import java.net.InetSocketAddress;
/**
 * @author pfjia
 * @since 2018/5/30 14:05
 */
public class AcceptRequest extends AbstractPaxosMessage {

	private ProposalNumber proposalNumber;
	private PaxosValue paxosValue;

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
