package runtime;

import role.Acceptor;
import role.Node;
import role.Proposer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pfjia
 * @since 2018/5/29 22:21
 */
public class Quorum {
    List<Node> nodeList = new ArrayList<>();

    public void addNode(Node node) {
        nodeList.add(node);
        node.setQuorum(this);
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public List<Node> getProposerList() {
        return nodeList.stream().filter(node -> node.hasRole(Proposer.class)).collect(Collectors.toList());
    }

    public List<Node> getAcceptorList() {
        return nodeList.stream().filter(node -> node.hasRole(Acceptor.class)).collect(Collectors.toList());
    }

    public void start() {
        for (Node node : nodeList) {
            node.start();
        }
    }
}
