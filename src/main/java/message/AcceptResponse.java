package message;

import datastructure.ProposalNumber;

/**
 * @author pfjia
 * @since 2018/5/30 14:05
 */
public class AcceptResponse extends PaxosMessage {

    /**
     * 唯一标识此acceptReponse是对哪个acceptRequest的回复,correspondingProposalNumber即为所回复的acceptRequest的proposalNumber
     */
    private ProposalNumber correspondingProposalNumber;
    private boolean accept;

    public AcceptResponse(ProposalNumber correspondingProposalNumber, boolean accept) {
        this.correspondingProposalNumber = correspondingProposalNumber;
        this.accept = accept;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    @Override
    public String messageBodyToString() {
        return null;
    }

    public ProposalNumber getCorrespondingProposalNumber() {
        return correspondingProposalNumber;
    }

    public void setCorrespondingProposalNumber(ProposalNumber correspondingProposalNumber) {
        this.correspondingProposalNumber = correspondingProposalNumber;
    }

}
