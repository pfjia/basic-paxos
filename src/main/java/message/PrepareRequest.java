package message;

import datastructure.ProposalNumber;
import role.Node;

import java.util.Optional;

/**
 * @author pfjia
 * @since 2018/5/30 14:04
 */
public class  PrepareRequest extends AbstractPaxosMessage {
    /**
     *
     */
    private ProposalNumber proposalNumber;

    public PrepareRequest(Node sender, Node receiver, ProposalNumber proposalNumber) {
        super(sender,receiver);
        this.proposalNumber = proposalNumber;
    }

    public ProposalNumber getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(ProposalNumber proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    @Override
    public String messageBodyToString() {
        return String.format("Prepare(%d)", Optional.ofNullable(proposalNumber).map(ProposalNumber::getNumber).orElse(Integer.MIN_VALUE));
    }

}
