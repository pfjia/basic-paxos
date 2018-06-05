package message;

import datastructure.PaxosValue;
import datastructure.ProposalNumber;
import role.Node;

import java.util.Optional;

/**
 * @author pfjia
 * @since 2018/5/30 14:05
 */
public class AcceptRequest extends AbstractPaxosMessage {

    private ProposalNumber proposalNumber;
    private PaxosValue paxosValue;

    public AcceptRequest(Node senderNode, Node receiverNode, ProposalNumber proposalNumber, PaxosValue paxosValue) {
        super(senderNode,receiverNode);
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
        return String.format("AcceptRequest(%d,%s)", Optional.ofNullable(proposalNumber).map(ProposalNumber::getNumber).orElse(null), Optional.ofNullable(paxosValue).map(PaxosValue::getValue).orElse(null));
    }
}
