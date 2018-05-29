package runtime;

import com.google.gson.Gson;
import datastructure.RoleAddress;
import role.Acceptor;
import role.Learner;
import role.PaxosRole;
import role.Proposer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

public enum GlobalConfig {

    INSTANCE;

    private int currentNodeNumber;
    private ConnectionProtocol connectionProtocol;
    public InetAddress localIp;
    private InetAddress[] paxosHosts;

    public static final int CLIENT_PORT = 50007;
    public static final int PROPOSER_PORT = 50001;
    public static final int ACCEPTOR_PORT = 50002;
    public static final int LEARNER_PORT = 50003;
    private static final int MAXIMUM_NODE_NUMBER = 10;

    public void init(int currentNode, ConnectionProtocol connectionProtocol) {
        this.currentNodeNumber = currentNode;
        this.connectionProtocol = connectionProtocol;
        try {
            Gson gson = new Gson();
            BufferedReader bufferedReader = new BufferedReader(new FileReader("global_config.json"));
            String[] ipStrings = gson.fromJson(bufferedReader, String[].class);
            this.paxosHosts = new InetAddress[ipStrings.length];
            for (int i = 0; i < ipStrings.length; i++) {
                this.paxosHosts[i] = InetAddress.getByName(ipStrings[i]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        /*
        try {
        this.localIp = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
        e.printStackTrace();
        }
        */
        this.localIp = this.paxosHosts[this.currentNodeNumber];
    }

    public void init() {

    }

    public int getClientListenPortNumber() {
        return CLIENT_PORT;
    }

    public RoleAddress getCurrentRoleAddressByRoleClass(Class paxosRoleClass) {
        if (paxosRoleClass.equals(Proposer.class)) {
            return (new RoleAddress(this.localIp, GlobalConfig.PROPOSER_PORT));
        } else if (paxosRoleClass.equals(Acceptor.class)) {
            return (new RoleAddress(this.localIp, GlobalConfig.ACCEPTOR_PORT));
        } else if (paxosRoleClass.equals(Learner.class)) {
            return (new RoleAddress(this.localIp, GlobalConfig.LEARNER_PORT));
        } else {
            assert false:"Invalid PaxosRole class: " + paxosRoleClass;
            return null;
        }
    }

    public RoleAddress getCurrentRoleAddressByRole(PaxosRole paxosRole) {
        return this.getCurrentRoleAddressByRoleClass(paxosRole.getClass());
    }

    public RoleAddress[] getAllProposerAddresses() {
        int numberOfPaxosHosts = this.paxosHosts.length;
        RoleAddress[] allProposerAddresses = new RoleAddress[numberOfPaxosHosts];
        int i;
        for (i=0; i<numberOfPaxosHosts; i++) {
            allProposerAddresses[i] = new RoleAddress(this.paxosHosts[i], GlobalConfig.PROPOSER_PORT);
        }
        return allProposerAddresses;
    }

    public RoleAddress[] getAllAcceptorAddresses() {
        int numberOfPaxosHosts = this.paxosHosts.length;
        RoleAddress[] allAcceptorAddresses = new RoleAddress[numberOfPaxosHosts];
        int i;
        for (i=0; i<numberOfPaxosHosts; i++) {
            allAcceptorAddresses[i] = new RoleAddress(this.paxosHosts[i], GlobalConfig.ACCEPTOR_PORT);
        }
        return allAcceptorAddresses;
    }

    public RoleAddress[] getAllLearnerAddresses() {
        int numberOfPaxosHosts = this.paxosHosts.length;
        RoleAddress[] allLearnerAddresses = new RoleAddress[numberOfPaxosHosts];
        int i;
        for (i=0; i<numberOfPaxosHosts; i++) {
            allLearnerAddresses[i] = new RoleAddress(this.paxosHosts[i], GlobalConfig.LEARNER_PORT);
        }
        return allLearnerAddresses;
    }

    public int getNumberOfQuorum() {
        return this.paxosHosts.length/2 + 1;
    }

    public int whichNodeIsThisAddress(RoleAddress roleAddress) {
        int numberOfPaxosHosts = this.paxosHosts.length;
        int nodeNumber = -1;
        for (int i=0; i<numberOfPaxosHosts; i++) {
            if (this.paxosHosts[i].equals(roleAddress.getIp())) {
                nodeNumber=i;
            }
        }
        return nodeNumber;
    }

    public Class whichRoleIsThisAddress(RoleAddress roleAddress) {

        switch (roleAddress.getPortNumber()) {
            case GlobalConfig.PROPOSER_PORT:
                return Proposer.class;
            case GlobalConfig.ACCEPTOR_PORT:
                return Acceptor.class;
            case GlobalConfig.LEARNER_PORT:
                return Learner.class;
            default:
                assert false:"roleAddress is invalid:" + roleAddress;
                return null;
        }
    }

    public int getCurrentNodeNumber() {
        return currentNodeNumber;
    }

    public int getMaximumNodeNumber() {
        return MAXIMUM_NODE_NUMBER;
    }

    public ConnectionProtocol getConnectionProtocol() {
        return connectionProtocol;
    }
}


