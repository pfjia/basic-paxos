package role;

import message.AcceptedMessage;
import message.PaxosMessage;
import runtime.GlobalConfig;

public class Learner implements Role {

    @Override
    public void handleMessage(PaxosMessage message) {
        super.handleMessage(message);
        assert (message instanceof AcceptedMessage);
        synchronized (System.out) {
            System.out.println("Learner " + GlobalConfig.INSTANCE.getCurrentNodeNumber() + " get the Paxos value: " + ((AcceptedMessage) message).getPaxosValue().getValue());
            System.out.flush();
        }
    }

    @Override
    public void sendMessage(PaxosMessage message) {

    }

}
