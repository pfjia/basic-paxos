package runtime;

import com.jfinal.log.LogManager;
import log.Slf4jLogFactory;
import role.Acceptor;
import role.Node;
import role.Proposer;

import java.net.UnknownHostException;

/**
 * @author pfjia
 * @since 2018/5/30 14:05
 */
public class Main {
    public static void main(String[] args) throws UnknownHostException {
        LogManager.me().setDefaultLogFactory(new Slf4jLogFactory());
        Cluster cluster = new Cluster();
        Node a = new Node(5000);
        a.setRole(new Proposer());
        a.setRole(new Acceptor());
        Node b = new Node(6001);
        b.setRole(new Proposer());
        b.setRole(new Acceptor());
        Node c = new Node(7000);
        c.setRole(new Proposer());
        c.setRole(new Acceptor());
        cluster.addNode(a);
        cluster.addNode(b);
        cluster.addNode(c);
        cluster.start();
    }
}
