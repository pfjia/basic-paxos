package role;

import message.PaxosMessage;

/**
 * @author pfjia
 * @since 2018/5/29 22:35
 */
public interface Role {

    void sendMessage(PaxosMessage message);


    Node getNode();

    void setNode(Node node);

    void start();

}
