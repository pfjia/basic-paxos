package role;

import message.PaxosMessage;
import runtime.MessageReceiverThread;
import runtime.Quorum;
import runtime.SendMessageThread;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Node {
    private String name;
    private InetAddress host;
    private int port;
    private Set<Role> roleSet = new HashSet<>();
    private Quorum quorum;

    public Node(int port) throws UnknownHostException {
        this(InetAddress.getLocalHost(), port);
    }

    public Node(String host, int port) throws UnknownHostException {
        this(InetAddress.getByName(host), port);
    }

    public Node(InetAddress host, int port) {
        this.host = host;
        this.port = port;
    }


    public String getName() {
        return Objects.requireNonNullElse(name, host.toString() + port);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Quorum getQuorum() {
        return quorum;
    }

    public void setQuorum(Quorum quorum) {
        this.quorum = quorum;
    }

    public void setRole(Role role) {
        roleSet.add(role);
        role.setNode(this);
    }

    public boolean hasRole(Class<? extends Role> roleClass) {
        return roleSet.stream().anyMatch(roleClass::isInstance);
    }

    public void start() {
        new MessageReceiverThread(port, this).start();
    }

    public void handleMessage(PaxosMessage message) {
        for (Role role : roleSet) {
            role.handleMessage(message);
        }
    }

    public void sendMessage(PaxosMessage message) {
        (new SendMessageThread(message)).start();
    }

}
