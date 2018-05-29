package role;

import datastructure.PaxosValue;
import datastructure.ProposalNumber;
import datastructure.RoleAddress;
import message.*;
import runtime.GlobalConfig;

public class Acceptor extends PaxosRole {

	private ProposalNumber highestAcceptedProposalNumber = null;
	private PaxosValue paxosValueOfhighestAcceptedProposalNumber = null;
	private ProposalNumber theNumberN = null;
	private boolean iPromiseIgnoreAllFutureProposalsHavingANumberLessThanN;

    public Acceptor(RoleAddress myAddress) {

        super(myAddress);
    }

	private void sendPromiseMessage(boolean iPromiseIgnoreAllFutureProposalsHavingANumberLessThanN, ProposalNumber previousHighestProposalNumber, PaxosValue valueOfPreviousHighestProposalNumber, RoleAddress senderAddress, RoleAddress receiverAddress) {
		this.sendMessage(new PromiseMessage(iPromiseIgnoreAllFutureProposalsHavingANumberLessThanN, previousHighestProposalNumber, valueOfPreviousHighestProposalNumber, senderAddress, receiverAddress));
	}
	
	private void sendAcceptedMessage(ProposalNumber number, PaxosValue value, RoleAddress senderAddress, RoleAddress proposerAddress, RoleAddress[] learnerAddresses) {
		this.sendMessage(new AcceptedMessage(number, value, senderAddress, proposerAddress));
		for (RoleAddress learnerAddress : learnerAddresses) {
			this.sendMessage(new AcceptedMessage(number, value, senderAddress, learnerAddress));
		}
	}
	
	@Override
    public void handleMessage(PaxosMessage message) {
		super.handleMessage(message);
				
		if (message instanceof PrepareMessage) {
			ProposalNumber receivedNumber = ((PrepareMessage)message).getProposalNumber();
			if (!(this.iPromiseIgnoreAllFutureProposalsHavingANumberLessThanN) 
					|| this.iPromiseIgnoreAllFutureProposalsHavingANumberLessThanN && this.theNumberN.compareTo(receivedNumber)<0) {
				if (this.highestAcceptedProposalNumber == null || receivedNumber.compareTo(this.highestAcceptedProposalNumber) > 0) {	//Promise
					this.sendPromiseMessage(true, receivedNumber, null, message.getReceiverAddress(), message.getSenderAddress());
					this.theNumberN = receivedNumber;
					this.iPromiseIgnoreAllFutureProposalsHavingANumberLessThanN = true;
				} else {
					this.sendPromiseMessage(false, this.highestAcceptedProposalNumber, this.paxosValueOfhighestAcceptedProposalNumber, message.getReceiverAddress(), message.getSenderAddress());
				}
				// Notice: Do not set highestAcceptedProposalNumber and paxosValueOfhighestAcceptedProposalNumber in Phrase 1b (promise). This should happen in Phrase 2b (accepted).
			}
		} else if (message instanceof AcceptRequestMessage) {
			ProposalNumber receivedNumber = ((AcceptRequestMessage)message).getProposalNumber();
			PaxosValue receivedValue = ((AcceptRequestMessage)message).getValue();
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
