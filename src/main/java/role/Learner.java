package role;

import message.AbstractPaxosMessage;

/**
 * @author pfjia
 * @since 2018/5/30 14:05
 */
public class Learner implements Role {

    @Override
    public Node getNode() {
        return null;
    }

    @Override
    public void setNode(Node node) {

    }

    @Override
    public void start() {

    }

    @Override
    public void sendMessage(AbstractPaxosMessage message) {

    }

    @Override
    public void init() {

    }
}
