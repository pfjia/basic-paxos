package runtime;

import role.Acceptor;
import role.Learner;
import role.Node;
import role.Proposer;

import java.io.IOException;
import java.net.UnknownHostException;

public class Main {
	
	public static void main(String[] args) throws UnknownHostException {
	    Quorum quorum=new Quorum();
	    Node a=new Node(50000);
	    a.setRole(new Proposer());
	    a.setRole(new Acceptor());
	    Node b=new Node(60000);
	    b.setRole(new Proposer());
	    b.setRole(new Acceptor());
	    Node c=new Node(70000);
	    c.setRole(new Proposer());
	    c.setRole(new Acceptor());
	    quorum.addNode(a);
	    quorum.addNode(b);
	    quorum.addNode(c);
	    quorum.start();

        new Thread(new ClientRequestHandler(proposer)).start();
    }
}
