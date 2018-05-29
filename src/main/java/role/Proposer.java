package role;

import datastructure.NodeAddress;
import datastructure.PaxosValue;
import datastructure.ProposalNumber;
import message.*;
import runtime.GlobalConfig;

import java.util.ArrayList;

public class Proposer implements Role{

    //NOTICE: this.reset() does not reset iAmLeader
    private Node node;
    private boolean iAmLeader = false;

    private ProposalNumber currentProposalNumber = null;
    private PaxosValue requestedValue = null;
    private int numberOfPromises = 0;
    private ProposalNumber highestAcceptedProposalNumber = null;
    private PaxosValue paxosValueOfHighestAcceptedProposalNumber = null;
    private ArrayList<NodeAddress> quorum = null;
    private boolean ignoreNewPromiseMessage = false;


    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public void setNode(Node node) {
        this.node = node;
    }

    public void reset() {
        this.currentProposalNumber = null;
        this.requestedValue = null;
        this.numberOfPromises = 0;
        this.highestAcceptedProposalNumber = null;
        this.paxosValueOfHighestAcceptedProposalNumber = null;
        this.quorum = null;
        this.ignoreNewPromiseMessage = false;
    }

    public void initiatePaxos(PaxosValue paxosValue) {
        this.reset();
        System.out.println("Paxos is initiated with Paxos Value: " + paxosValue);
        this.requestedValue = paxosValue;
        sendPrepareMessage();
    }

    private void sendPrepareMessage() {
        this.currentProposalNumber = new ProposalNumber();
        for (Node node1 : getNode().getQuorum().getAcceptorList()) {

        }
        NodeAddress[] allAcceptorAddresses = GlobalConfig.INSTANCE.getAllAcceptorAddresses();

        for (NodeAddress acceptorReceiverAddress : allAcceptorAddresses) {
            this.sendMessage(new PrepareMessage(this.currentProposalNumber, this.address, acceptorReceiverAddress));
        }
    }

    private void resendPrepareMessage() {
        NodeAddress[] allAcceptorAddresses = GlobalConfig.INSTANCE.getAllAcceptorAddresses();
        ProposalNumber tempProposalNumber = this.highestAcceptedProposalNumber;
        PaxosValue tempPaxosValue = this.requestedValue;
        this.reset();
        this.currentProposalNumber = new ProposalNumber(tempProposalNumber.getNumberValue() + 1);
        this.requestedValue = tempPaxosValue;

        for (NodeAddress acceptorReceiverAddress : allAcceptorAddresses) {
            this.sendMessage(new PrepareMessage(this.currentProposalNumber, this.address, acceptorReceiverAddress));
        }
    }

    private void sendAcceptRequestMessage(ProposalNumber proposalNumber, PaxosValue paxosValue, NodeAddress senderAddress, ArrayList<NodeAddress> receiverAddresses) {
        for (NodeAddress receiverAddress : receiverAddresses) {
            this.sendMessage(new AcceptRequestMessage(proposalNumber, paxosValue, senderAddress, receiverAddress));
        }
    }

    @Override
    public void handleMessage(PaxosMessage message) {
        if (message instanceof PromiseMessage) {
            if (this.ignoreNewPromiseMessage == false) {

                boolean isPromised = ((PromiseMessage) message).isPromised();
                ProposalNumber receivedNumber = ((PromiseMessage) message).getProposalNumber();
                PaxosValue receivedValue = ((PromiseMessage) message).getPaxosValue();

                if (isPromised && this.currentProposalNumber != null && this.currentProposalNumber.equals(receivedNumber)) {
                    (this.numberOfPromises)++;
                } else {

                }

                if (quorum == null) {
                    quorum = new ArrayList<NodeAddress>();
                } else {

                }

                quorum.add(message.getSenderAddress());

                if (receivedNumber != null && this.currentProposalNumber != null && receivedNumber.compareTo(this.currentProposalNumber) > 0 && (this.highestAcceptedProposalNumber == null || receivedNumber != null && this.highestAcceptedProposalNumber != null && receivedNumber.compareTo(this.highestAcceptedProposalNumber) > 0)) {
                    if (this.iAmLeader == true) {
                        System.out.println("LEADER RESEND!!");
                        this.resendPrepareMessage();
                    } else {
                        this.highestAcceptedProposalNumber = receivedNumber;
                        this.paxosValueOfHighestAcceptedProposalNumber = receivedValue;
                    }
                } else {

                }

                if (this.numberOfPromises >= GlobalConfig.INSTANCE.getNumberOfQuorum()) {    // Needs to set a value
                    /*
                     * I didn't store currentProposalNumber in highestAcceptedProposalNumber because they have different meaning, even their types are same.
                     */
                    if (this.highestAcceptedProposalNumber == null) {
                        this.sendAcceptRequestMessage(this.currentProposalNumber, this.requestedValue, message.getReceiverAddress(), this.quorum);
                    } else {
                        this.sendAcceptRequestMessage(this.highestAcceptedProposalNumber, this.paxosValueOfHighestAcceptedProposalNumber, message.getReceiverAddress(), this.quorum);
                    }
                    this.ignoreNewPromiseMessage = true;
                } else {
                    // wait more promise
                }

            } else {

            }

        } else if (message instanceof AcceptedMessage && this.currentProposalNumber != null && ((AcceptedMessage) message).getProposalNumber().compareTo(this.currentProposalNumber) >= 0) {
            this.reset();
        } else {
            // Something wrong, what kind of exception to throw?
        }
    }

    public boolean isiAmLeader() {
        return iAmLeader;
    }
}
