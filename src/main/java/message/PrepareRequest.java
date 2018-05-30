package message;

import datastructure.ProposalNumber;

/**
 * @author pfjia
 * @since 2018/5/30 14:04
 */
public class PrepareRequest extends PaxosMessage {
    private ProposalNumber proposalNumber;

    public PrepareRequest(ProposalNumber proposalNumber) {
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
        return String.format("Prepare(%d)", this.proposalNumber.getNumber());
    }

}
