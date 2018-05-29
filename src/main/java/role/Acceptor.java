package role;

import datastructure.NodeAddress;
import datastructure.PaxosValue;
import datastructure.ProposalNumber;
import message.*;
import runtime.GlobalConfig;

public class Acceptor implements Role {

    private ProposalNumber highestAcceptedProposalNumber = null;
    private PaxosValue paxosValueOfhighestAcceptedProposalNumber = null;
    private ProposalNumber theNumberN = null;
    private boolean iPromiseIgnoreAllFutureProposalsHavingANumberLessThanN;


    private void sendPromiseMessage(boolean iPromiseIgnoreAllFutureProposalsHavingANumberLessThanN, ProposalNumber previousHighestProposalNumber, PaxosValue valueOfPreviousHighestProposalNumber, NodeAddress senderAddress, NodeAddress receiverAddress) {
        this.sendMessage(new PromiseMessage(iPromiseIgnoreAllFutureProposalsHavingANumberLessThanN, previousHighestProposalNumber, valueOfPreviousHighestProposalNumber, senderAddress, receiverAddress));
    }

    private void sendAcceptedMessage(ProposalNumber number, PaxosValue value, NodeAddress senderAddress, NodeAddress proposerAddress, NodeAddress[] learnerAddresses) {
        this.sendMessage(new AcceptedMessage(number, value, senderAddress, proposerAddress));
        for (NodeAddress learnerAddress : learnerAddresses) {
            this.sendMessage(new AcceptedMessage(number, value, senderAddress, learnerAddress));
        }
    }

    @Override
    public void handleMessage(PaxosMessage message) {
        super.handleMessage(message);

        if (message instanceof PrepareMessage) {
            ProposalNumber receivedNumber = ((PrepareMessage) message).getProposalNumber();
            if (!(this.iPromiseIgnoreAllFutureProposalsHavingANumberLessThanN)
                    || this.iPromiseIgnoreAllFutureProposalsHavingANumberLessThanN && this.theNumberN.compareTo(receivedNumber) < 0) {
                if (this.highestAcceptedProposalNumber == null || receivedNumber.compareTo(this.highestAcceptedProposalNumber) > 0) {    //Promise
                    this.sendPromiseMessage(true, receivedNumber, null, message.getReceiverAddress(), message.getSenderAddress());
                    this.theNumberN = receivedNumber;
                    this.iPromiseIgnoreAllFutureProposalsHavingANumberLessThanN = true;
                } else {
                    this.sendPromiseMessage(false, this.highestAcceptedProposalNumber, this.paxosValueOfhighestAcceptedProposalNumber, message.getReceiverAddress(), message.getSenderAddress());
                }
                // Notice: Do not set highestAcceptedProposalNumber and paxosValueOfhighestAcceptedProposalNumber in Phrase 1b (promise). This should happen in Phrase 2b (accepted).
            }
        } else if (message instanceof AcceptRequestMessage) {
            ProposalNumber receivedNumber = ((AcceptRequestMessage) message).getProposalNumber();
            PaxosValue receivedValue = ((AcceptRequestMessage) message).getValue();
            if (this.iPromiseIgnoreAllFutureProposalsHavingANumberLessThanN && this.theNumberN != null && receivedNumber.equals(this.theNumberN)) {
                this.sendAcceptedMessage(receivedNumber, receivedValue, message.getReceiverAddress(), message.getSenderAddress(), GlobalConfig.INSTANCE.getAllLearnerAddresses());
                this.highestAcceptedProposalNumber = receivedNumber;
                this.paxosValueOfhighestAcceptedProposalNumber = receivedValue;
                this.theNumberN = null;
                this.iPromiseIgnoreAllFutureProposalsHavingANumberLessThanN = false;
            } else {

            }
        } else {
            // Something wrong, what kind of exception to throw?
        }
    }
}
