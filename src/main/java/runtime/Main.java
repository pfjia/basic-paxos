package runtime;

import datastructure.PaxosValue;
import datastructure.RoleAddress;
import role.Acceptor;
import role.Learner;
import role.Proposer;

import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException {
        GlobalConfig.INSTANCE.init(Integer.parseInt(args[0]), ConnectionProtocol.TCP_CONNECTION);
        Proposer proposer = new Proposer(GlobalConfig.INSTANCE.getCurrentRoleAddressByRoleClass(Proposer.class));
        Acceptor acceptor = new Acceptor(GlobalConfig.INSTANCE.getCurrentRoleAddressByRoleClass(Acceptor.class));
        Learner learner = new Learner(GlobalConfig.INSTANCE.getCurrentRoleAddressByRoleClass(Learner.class));
        proposer.start();
        acceptor.start();
        learner.start();
        new Thread(new ClientRequestHandler(proposer)).start();
    }
}
