package role;

import message.AbstractPaxosMessage;

/**
 * @author pfjia
 * @since 2018/5/29 22:35
 */
public interface Role {

    void sendMessage(AbstractPaxosMessage message);

    Node getNode();

    void setNode(Node node);

    /**
     * 开始paxos算法,集群达到一致后停止
     */
    void start();

}
