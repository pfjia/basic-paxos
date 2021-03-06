package role;

import message.AbstractPaxosMessage;

import java.io.Serializable;

/**
 * @author pfjia
 * @since 2018/5/29 22:35
 */
public interface Role  extends Serializable {

    void sendMessage(AbstractPaxosMessage message);

    Node getNode();

    void setNode(Node node);

    /**
     * 初始化接受线程
     */
    void init();

    /**
     * 开始paxos算法,集群达到一致后停止
     */
    void start();

}
